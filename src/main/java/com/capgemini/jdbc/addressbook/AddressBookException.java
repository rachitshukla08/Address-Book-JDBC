package com.capgemini.jdbc.addressbook;

public class AddressBookException extends Exception {
	public enum ExceptionType{
		
	}
	ExceptionType type;
	public AddressBookException(ExceptionType type,String message) {
		super(message);
		this.type = type;
	}
}
