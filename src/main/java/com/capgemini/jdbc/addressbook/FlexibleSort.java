package com.capgemini.jdbc.addressbook;

import java.util.Comparator;

import com.capgemini.jdbc.addressbook.model.Contact;

public class FlexibleSort implements Comparator<Contact> {
	public enum Order{NAME,CITY,STATE,ZIP}
	
	public Order sortingBy;
	
	public FlexibleSort(Order sortingBy) {
		this.sortingBy = sortingBy;
	}
	
	@Override
	public int compare(Contact o1,Contact o2) {
		switch(sortingBy) {
		case NAME: 
			return (o1.getFirstName()+o1.getLastName()).compareTo(o2.getFirstName()+o2.getLastName());
		case CITY: 
			return (o1.getCity().compareTo(o2.getCity()));
		case STATE: 
			return (o1.getState().compareTo(o2.getState()));
		case ZIP: 
			return (o1.getZip().compareTo(o2.getZip()));
		default: System.out.println("Invalid choice");
		}
		return 0;
	}
}
