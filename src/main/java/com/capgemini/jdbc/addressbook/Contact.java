package com.capgemini.jdbc.addressbook;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Rachit
 *
 */
public class Contact {
	
	private int id;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String phoneNo;
	private String email;
	private LocalDate date;
	private String name;
	private String type;
	
	public Contact() {
	}
	
	public Contact(String firstName, String lastName, String address, String city, String state, String zip,
			String phoneNo, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNo = phoneNo;
		this.email = email;
	}

	public Contact(String firstName,String lastName,String address,String city,
			String state,String zip,String phone,String email,LocalDate date,String name,String type) {
		this(firstName,lastName,address,city,state,zip,phone,email);
		this.date = date;
		this.name = name;
		this.type = type;
	}
	
	public Contact(int id,String firstName,String lastName,String address,String city,
			String state,String zip,String phone,String email,LocalDate date,String name,String type) {
		this(firstName,lastName,address,city,state,zip,phone,email,date,name,type);
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(firstName,lastName);
	}
	
	@Override
	public String toString() {
		return "First name="+firstName + ",Last name=" + lastName + ",Address=" + address + ",City=" + city + ",State=" + state + ",Zip=" + zip + ",Phone No="
				+ phoneNo + ",Email=" + email + "\n";
	}
	
	@Override
	public boolean equals(Object o) {
		Contact contact = (Contact) o;
		if ((this.firstName).equals(contact.firstName) && (this.lastName.equals(contact.lastName)))
			return true;
		else
			return false;
	}
}
