package com.capgemini.jdbc.addressbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.capgemini.jdbc.addressbook.service.AddressBookService;

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
		assertEquals(5, contactList.size());
	}
	
	@Test 
	public void givenContactDetails_WhenUpdated_ShouldSyncWithDB() {
		addressBookService.readData();
		boolean isUpdated = addressBookService.updateContact("Bill","Smith","123456789","BillSmith@email.com");
		addressBookService.readData();
		boolean result = addressBookService.checkEmployeePayrollInSyncWithDB("Bill","Smith");
		assertTrue(result&&isUpdated);
	}
	
	@Test
	public void givenDateRange_ShouldRetrieveContactsInThatRange() {
		addressBookService.readData();
		List<Contact> contactList = addressBookService.getContactInDateRange("2019-01-19","2020-01-19");
		assertEquals(2,contactList.size());
	}
}
