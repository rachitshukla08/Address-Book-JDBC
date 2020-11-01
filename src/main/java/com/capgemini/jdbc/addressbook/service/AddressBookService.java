package com.capgemini.jdbc.addressbook.service;

import java.util.List;

import com.capgemini.jdbc.addressbook.Contact;

public class AddressBookService {
	
	AddressBookDBService addressBookDBService = AddressBookDBService.getInstance();
	
	public List<Contact> readData() {
		return addressBookDBService.readData();
	}

}
