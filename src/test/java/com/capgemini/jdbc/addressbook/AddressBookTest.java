package com.capgemini.jdbc.addressbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.capgemini.jdbc.addressbook.model.Contact;
import com.capgemini.jdbc.addressbook.service.AddressBookService;
import com.capgemini.jdbc.addressbook.service.AddressBookDBService.CountType;
import com.google.gson.Gson;

import com.capgemini.jdbc.addressbook.service.AddressBookService.IOService;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddressBookTest {

	AddressBookService addressBookService;

	@Before
	public void init() {
		addressBookService = new AddressBookService();
	}

	@Test
	public void givenAddressBookData_WhenRetreived_ShouldRetrieveAllContacts() {
		List<Contact> contactList = addressBookService.readData();
		System.out.println(contactList);
		assertEquals(9, contactList.size());
	}

	@Test
	public void givenContactDetails_WhenUpdated_ShouldSyncWithDB() throws AddressBookException {
		addressBookService.readData();
		boolean isUpdated = addressBookService.updateContact("Bill", "Smith", "123456789", "BillSmith@email.com",IOService.DB_IO);
		boolean result = addressBookService.checkAddressBookInSyncWithDB("Bill", "Smith");
		assertTrue(result && isUpdated);
	}
	
	@Test
	public void givenContactDetails_WhenNotPresent_ShouldThrowException() {
		addressBookService.readData();
		try {
			addressBookService.updateContact("ABC", "XYZ", "123456789", "BillSmith@email.com",IOService.DB_IO);
		} catch (AddressBookException e) {
			assertEquals(AddressBookException.ExceptionType.UPDATE_FAILED, e.type);
		}
	}

	@Test
	public void givenDateRange_ShouldRetrieveContactsInThatRange() throws AddressBookException {
		addressBookService.readData();
		List<Contact> contactList = addressBookService.getContactInDateRange("2019-01-19", "2020-01-19");
		assertEquals(2, contactList.size());
	}

	@Test
	public void givenStateOrCity_ShouldRetrieveCountOfContactsInThatCityOrState() {
		addressBookService.readData();
		Map<String, Integer> cityMap = addressBookService.getCountByCityState(CountType.CITY);
		Map<String, Integer> stateMap = addressBookService.getCountByCityState(CountType.STATE);
		System.out.println(cityMap);
		System.out.println(stateMap);
		int cityCount = cityMap.get("City 2");
		int stateCount = stateMap.get("California");
		boolean isValid = cityCount == 3 && stateCount == 4;
		assertTrue(isValid);
	}

	@Test
	public void givenAContact_WhenAdded_ShouldSyncWithDatabase() {
		addressBookService.readData();
		addressBookService.addContact("Rachit", "Shukla", "Street 190", "Bhopal", "MP", "456789", "9191919191",
				"rachit@email.com",LocalDate.now(),"name","Family");
		boolean result = addressBookService.checkAddressBookInSyncWithDB("Rachit", "Shukla");
		assertTrue(result);
	}
	
	@Test
	public void given3Contacts_WhenAddedToDatabase_ShouldMatchContactEntries() {
		addressBookService.readData();
		Contact[] contacts = {
				new Contact("Mark", "Zuckerberg", "Street 200", "NY", "New York", "456781", "9292929292",
				"mark@email.com",LocalDate.now(),"name","Friend"),
				new Contact("Bill", "Gates", "Street 250", "Medina", "Washington", "666781", "8892929291",
						"mark@email.com",LocalDate.now(),"name","Friend"),
				new Contact("Jeff", "Bezos", "Street 200", "City 8", "Washington", "456781", "7292929292",
						"jeff@email.com",LocalDate.now(),"name","Family")
				};
		addressBookService.addContacts(Arrays.asList(contacts));
		List<Contact> newList = addressBookService.readData();
		assertEquals(9, newList.size());
	}
	
	//REST API TESTS:
	
	/**
	 * Initial setup of baseURI and port of RestAssured
	 */
	@Before
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}
	
	/**
	 * @return Array of contacts
	 */
	public Contact[] getContactList() {
		Response response = RestAssured.get("/contacts");
		System.out.println("Employee entries in JSON SERVER : "+response.asString());
		Contact[] arrayOfEmps = new Gson().fromJson(response.asString(), Contact[].class);
		return arrayOfEmps;
	}
	
	@Test
	public void givenContactsInJSONServer_WhenRetrieved_ShouldMatchTheCount() {
		Contact[] arrayOfContacts = getContactList();
		AddressBookService addressBookService;
		addressBookService = new AddressBookService(Arrays.asList(arrayOfContacts));
		long entries = addressBookService.countEntries();
		assertEquals(4, entries);
	}
	
	@Test
	public void givenNewContact_WhenAdded_ShouldMatch201ResponseAndCount() {
		Contact[] arrayOfEmps = getContactList();
		AddressBookService addressBookService = new AddressBookService(Arrays.asList(arrayOfEmps));
		Contact contact = new Contact(0,"Mark", "Zuckerberg", "Street 190", "Bhopal", "MP", "444444", "7777777777",
				"Mark@email.com", LocalDate.now(), "Address Book 2", "Family");
		Response response  = addContactToJSONServer(contact);
		int statusCode = response.getStatusCode();
		assertEquals(201, statusCode);
		contact = new Gson().fromJson(response.asString(), Contact.class);
		addressBookService.addContact(contact,IOService.REST_IO);
		assertEquals(4, addressBookService.countEntries());
	}

	/**
	 * @param contact
	 * @return response
	 */
	private synchronized Response addContactToJSONServer(Contact contact) {
		System.out.println(contact);
		String contactJson = new Gson().toJson(contact);
		System.out.println(contactJson);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type","application/json");
		request.body(contactJson);
		return request.post("/contacts");
	}
	
	// UC23 REST
	@Test
	public void given3Contacts_WhenAdded_ShouldMatchCount() {
		Contact[] arrayOfContacts = getContactList();
		AddressBookService addressBookService = new AddressBookService(Arrays.asList(arrayOfContacts));
		Contact[] arrayOfContactToAdd = {
				new Contact(0, "Bill", "Gates", "Street 225", "Medina", "Washington", "434343", "8888877777",
						"Bill@email.com", LocalDate.now(), "Address Book 2", "Family"),
				new Contact(0, "Jeff", "Bezos", "Street 219", "XYZ", "New York", "545455", "7799997777",
						"Jeff@email.com", LocalDate.now(), "Address Book 2", "Family"),
				new Contact(0, "Anil", "Ambani", "Street 112", "Mumbai", "Maharashtra", "312351", "9976673371",
						"Anil@email.com", LocalDate.now(), "Address Book 3", "Friend") };
		addContactsToJSONWithThreads(Arrays.asList(arrayOfContactToAdd));
		arrayOfContacts = getContactList();
		addressBookService = new AddressBookService(Arrays.asList(arrayOfContacts));
		assertEquals(Arrays.asList(arrayOfContacts), addressBookService.countEntries());
	}

	public void addContactsToJSONWithThreads(List<Contact> contactList) {
		Map<String, Boolean> contactAdditionStatus = new HashMap<String, Boolean>();
		contactList.forEach(contact -> {
			Runnable task = () -> {
				contactAdditionStatus.put(contact.getFirstName(), false);
				System.out.println("Contact being added:(threads) " + Thread.currentThread().getName());
				this.addContactToJSONServer(contact);
				contactAdditionStatus.put(contact.getFirstName(), true);
				System.out.println("Contact added: (threads)" + Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, contact.getFirstName());
			thread.start();
		});
		while (contactAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// UC24
	@Test
	public void givenNewContactData_WhenUpdated_ShouldMatch200Response() throws AddressBookException {
		Contact[] arrayOfContacts = getContactList();
		AddressBookService addressBookService = new AddressBookService(Arrays.asList(arrayOfContacts));
		addressBookService.updateContact("Anil","Ambani","9876543210","anilambani@email.com",IOService.REST_IO);
		Contact contactData = addressBookService.getContactDetails("Anil","Ambani");
		String contactJson = new Gson().toJson(contactData);
		System.out.println(contactJson);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(contactJson);
		Response response = request.put("/contacts/" + contactData.getId());
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);
	}
	
	// UC25
	@Test
	public void givenContactToDelete_WhenDeleted_ShouldMatch200ResponseAndCount() {
		Contact[] arrayOfContacts = getContactList();
		AddressBookService addressBookService = new AddressBookService(Arrays.asList(arrayOfContacts));
		
		Contact contactData = addressBookService.getContactDetails("Anil","Ambani");
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type","application/json");
		Response response = request.delete("/contacts/"+contactData.getId());
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);
		
		addressBookService.deleteContactJSONServer(contactData.getFirstName(),contactData.getLastName(),IOService.REST_IO);
		long entries = addressBookService.countEntries();
		assertEquals(6, entries);
	}
}
