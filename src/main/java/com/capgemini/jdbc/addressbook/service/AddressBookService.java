package com.capgemini.jdbc.addressbook.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

import com.capgemini.jdbc.addressbook.model.Contact;
import com.capgemini.jdbc.addressbook.service.AddressBookDBService.CountType;

public class AddressBookService {
	
	public enum IOService{
		DB_IO,REST_IO
	}
	List<Contact> addressBookList;
	AddressBookDBService addressBookDBService = AddressBookDBService.getInstance();
	
	public AddressBookService() {
	}
	
	public AddressBookService(List<Contact> contactList) {
		this.addressBookList = new ArrayList<>(contactList);
	}

	public List<Contact> readData() {
		addressBookList = addressBookDBService.readData();
		return addressBookList;
	}

	/**
	 * @param firstName
	 * @param lastName
	 * @param phone
	 * @param email
	 * @return true if contact is successfully updated
	 */
	public boolean updateContact(String firstName, String lastName, String phone, String email,IOService ioService) {
		if(ioService.equals(IOService.DB_IO)) {
			int rows = addressBookDBService.updateContact(firstName,lastName,phone,email);
			if(rows>0)
				return true;
		}
		else if(ioService.equals(IOService.REST_IO)) {
			Contact contactData = this.getContactDetails(firstName, lastName);
			if(contactData!=null) {
				contactData.setEmail(email);
				contactData.setPhoneNo(phone);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param firstName
	 * @param lastName
	 * @return if contact is present
	 */
	public boolean checkAddressBookInSyncWithDB(String firstName,String lastName) {
		 List<Contact> checkList = addressBookDBService.getContactDetailsDB(firstName, lastName);
		return checkList.get(0).equals(getContactDetails(firstName, lastName));
	}
	
	/**
	 * @param firstName
	 * @param lastName
	 * @return contact details of a given contact
	 */
	public Contact getContactDetails(String firstName,String lastName) {
		Contact contactData = this.addressBookList.stream()
				.filter(employee->employee.getFirstName().equals(firstName)&&employee.getLastName().equals(lastName))
				.findFirst()
				.orElse(null);
		return contactData;
	}

	/**
	 * @param start
	 * @param end
	 * @return List of contacts in a given date range
	 */
	public List<Contact> getContactInDateRange(String start, String end) {
		return addressBookDBService.getContactInDateRange(start,end);
	}

	/**
	 * @param type
	 * @return number of contacts by city or state
	 */
	public Map<String,Integer> getCountByCityState(CountType type) {
		return addressBookDBService.getCountByCityState(type);
	}

	/**
	 * Add a new contact to DB
	 * @param firstName
	 * @param lastName
	 * @param address
	 * @param city
	 * @param state
	 * @param zip
	 * @param phone
	 * @param email
	 * @param date
	 * @param name
	 * @param type
	 */
	public void addContact(String firstName, String lastName, String address, String city, String state,
			String zip, String phone, String email,LocalDate date,String name,String type) {
		addressBookList.add(addressBookDBService.addContact(firstName,lastName,address,city,state,zip,phone,email,date,name,type));
	}

	/**
	 * Using MultiThreading to add contact
	 * @param asList
	 */
	public void addContacts(List<Contact> addContactList) {
		Map<Integer,Boolean> additionStatus = new HashMap<Integer, Boolean>();
		addContactList.forEach(contact -> {
			Runnable task = () -> {
				additionStatus.put(contact.hashCode(), false);
				System.out.println("Contact being added:(threads) "+Thread.currentThread().getName());
				this.addContact(contact.getFirstName(),contact.getLastName(),contact.getAddress(),contact.getCity(),contact.getState(),
						contact.getZip(),contact.getPhoneNo(),contact.getEmail(),contact.getDate(),contact.getName(),contact.getType());
				additionStatus.put(contact.hashCode(), true);
				System.out.println("Contact added: (threads)"+Thread.currentThread().getName());
			};
			Thread thread = new Thread(task,contact.getFirstName());
			thread.start();
		});
		while(additionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(addressBookList);
	}

	/**
	 * @return size of address book list
	 */
	public long countEntries() {
		return addressBookList.size();
	}

	public void addContact(Contact contact, IOService ioService) {
		if(ioService.equals(IOService.DB_IO)) {
			this.addContact(contact.getFirstName(), contact.getLastName(), contact.getAddress(), contact.getCity()
					, contact.getState(), contact.getZip(), contact.getPhoneNo(), contact.getEmail(), 
					contact.getDate(), contact.getName(), contact.getType());
		}
		else if(ioService.equals(IOService.REST_IO)) {
			addressBookList.add(contact);
		}
			
	}
}
