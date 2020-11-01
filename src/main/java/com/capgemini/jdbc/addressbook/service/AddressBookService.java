package com.capgemini.jdbc.addressbook.service;

import java.util.List;

import com.capgemini.jdbc.addressbook.Contact;

public class AddressBookService {
	
	List<Contact> addressBookList;
	AddressBookDBService addressBookDBService = AddressBookDBService.getInstance();
	
	public List<Contact> readData() {
		addressBookList = addressBookDBService.readData();
		return addressBookList;
	}

	public boolean updateContact(String firstName, String lastName, String phone, String email) {
		int rows = addressBookDBService.updateContact(firstName,lastName,phone,email);
		if(rows>0)
			return true;
		return false;
	}
	
	public boolean checkEmployeePayrollInSyncWithDB(String firstName,String lastName) {
		 List<Contact> checkList = addressBookDBService.getContactDetailsDB(firstName, lastName);
		return checkList.get(0).equals(getContactDetails(firstName, lastName));
		
	}
	
	private Contact getContactDetails(String firstName,String lastName) {
		Contact contactData = this.addressBookList.stream()
				.filter(employee->employee.getFirstName().equals(firstName)&&employee.getLastName().equals(lastName))
				.findFirst()
				.orElse(null);
		return contactData;
	}

	public List<Contact> getContactInDateRange(String start, String end) {
		return addressBookDBService.getContactInDateRange(start,end);
	}

}
