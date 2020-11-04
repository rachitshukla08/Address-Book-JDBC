package com.capgemini.jdbc.addressbook;

public class AddressBookException extends Exception {
	public enum ExceptionType{
		UPDATE_FAILED, NO_DATA_AVAILABLE, WRONG_TYPE
	}
	
	public ExceptionType type;
	public AddressBookException(ExceptionType type,String message) {
		super(message);
		this.type = type;
	}
}
