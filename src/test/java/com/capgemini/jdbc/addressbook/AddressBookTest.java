package com.capgemini.jdbc.addressbook;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.capgemini.jdbc.addressbook.service.AddressBookService;

public class AddressBookTest {

	@Test
	public void givenAddressBookData_WhenRetreived_ShouldRetrieveAllContacts() {
		AddressBookService addressBookService = new AddressBookService();
		List<Contact> contactList = addressBookService.readData();
		System.out.println(contactList);
		assertEquals(6, contactList.size());
	}
}
