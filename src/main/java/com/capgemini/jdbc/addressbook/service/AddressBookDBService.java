package com.capgemini.jdbc.addressbook.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.capgemini.jdbc.addressbook.Contact;


public class AddressBookDBService {
	
	private static AddressBookDBService addressBookDBService;
	private PreparedStatement addressBookPreparedStatement;
	
	private AddressBookDBService() {
	}
	
	public enum CountType{
		CITY,STATE
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
			contactList = this.getDataUsingResultSet(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}
	
	/**
	 * @param firstName
	 * @param lastName
	 * @param new phone number
	 * @param new email 
	 * @return number of rows affected
	 */
	public int updateContact(String firstName, String lastName, String phone, String email) {
		int rowsAffected=0;
		String sql = String.format("UPDATE address_book SET phone = '%s',email = '%s' "
				+ "WHERE first_name = '%s' AND last_name = '%s';",phone,email,firstName,lastName);
		try(Connection connection = this.getConnection()){
			Statement statement = connection.createStatement();
			rowsAffected = statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rowsAffected;
	}
	

	/**
	 * @param start
	 * @param end
	 * @return contact list in given date range
	 */
	public List<Contact> getContactInDateRange(String start, String end) {
		List<Contact> contactList = new ArrayList<Contact>();
		String sql = String.format("SELECT * FROM address_book where date_added BETWEEN '%s' AND '%s'",start,end);
		try(Connection connection = this.getConnection()){
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			contactList = this.getDataUsingResultSet(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}
	
	/**
	 * @param type
	 * @return Number of contacts in a given city or state
	 */
	public Map<String,Integer> getCountByCityState(CountType type) {
		String sql = null;
		Map<String,Integer> countMap = new HashMap<String,Integer>();
		if(type.equals(CountType.CITY))
			sql = "SELECT COUNT(email),city from address_book GROUP BY city;";
		else if(type.equals(CountType.STATE))
			sql = "SELECT COUNT(email),state from address_book GROUP BY state;";
		try(Connection connection = this.getConnection()){
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while(resultSet.next()) {
				String entry = null;
				int count = resultSet.getInt("COUNT(email)");
				if(type.equals(CountType.CITY)) {
					entry = resultSet.getString("city");
				}
				else if(type.equals(CountType.STATE)) {
					entry = resultSet.getString("state"); 
				}
				countMap.put(entry,count);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return countMap;
	}
	
	/**
	 * @param firstName
	 * @param lastName
	 * @return ContactList for a particular name
	 */
	public List<Contact> getContactDetailsDB(String firstName,String lastName){
		List<Contact> contactList = new ArrayList<Contact>();
		try {
			Connection connection = this.getConnection();
			String sql = "Select * from address_book WHERE first_name = ? AND last_name = ?;";
			addressBookPreparedStatement = connection.prepareStatement(sql);
			addressBookPreparedStatement.setString(1, firstName);
			addressBookPreparedStatement.setString(2, lastName);
			ResultSet resultSet = addressBookPreparedStatement.executeQuery();
			contactList = this.getDataUsingResultSet(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}

	/**
	 * @param resultSet
	 * @return contact list 
	 */
	private List<Contact> getDataUsingResultSet(ResultSet resultSet){
		List<Contact> contactList = new ArrayList<Contact>();
		try {
			while(resultSet.next()) {
				String first_Name = resultSet.getString("first_name");
				String last_Name = resultSet.getString("last_name");
				String address = resultSet.getString("address");
				String city = resultSet.getString("city");
				String state = resultSet.getString("state");
				String zip = resultSet.getString("zip");
				String phoneNo = resultSet.getString("phone");
				String email = resultSet.getString("email");
				Contact contact = new Contact(first_Name, last_Name, address, city, state, zip, phoneNo, email);
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
