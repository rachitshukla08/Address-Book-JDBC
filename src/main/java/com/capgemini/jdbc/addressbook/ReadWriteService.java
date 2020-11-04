package com.capgemini.jdbc.addressbook;

import com.capgemini.jdbc.addressbook.model.Contact;

public interface ReadWriteService {
	public void readDataFromAddressBook();
	public boolean addAddressBook(String bookName);
	public void writeContactToAddressBook(Contact contactObj, String addressBookName);
	public void print();
}
