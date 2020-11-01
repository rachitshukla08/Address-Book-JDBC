package com.capgemini.jdbc.addressbook;

public interface ReadWriteService {
	public void readDataFromAddressBook();
	public boolean addAddressBook(String bookName);
	public void writeContactToAddressBook(Contact contactObj, String addressBookName);
	public void print();
}
