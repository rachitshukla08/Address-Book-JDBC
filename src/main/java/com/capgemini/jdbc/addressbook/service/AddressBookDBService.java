package com.capgemini.jdbc.addressbook.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.jdbc.addressbook.Contact;


public class AddressBookDBService {
	
	private static AddressBookDBService addressBookDBService;
	
	private AddressBookDBService() {
	}
	
	public static AddressBookDBService getInstance() {
		if (addressBookDBService == null)
			addressBookDBService = new AddressBookDBService();
		return addressBookDBService;
	}
	
	public List<Contact> readData() {
		List<Contact> contactList = new ArrayList<Contact>();
		String sql = "SELECT * FROM address_book;";
		try(Connection connection = this.getConnection()){
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while(resultSet.next()) {
				String firstName = resultSet.getString("first_name");
				String lastName = resultSet.getString("last_name");
				String address = resultSet.getString("address");
				String city = resultSet.getString("city");
				String state = resultSet.getString("state");
				String zip = resultSet.getString("zip");
				String phoneNo = resultSet.getString("phone");
				String email = resultSet.getString("email");
				Contact contact = new Contact(firstName, lastName, address, city, state, zip, phoneNo, email);
				contactList.add(contact);
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}
	
	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service?useSSL=false";
		String userName = "root";
		String password = "root";
		Connection connection;
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		System.out.println("Connection established: "+connection);
		return connection;
	}
}
