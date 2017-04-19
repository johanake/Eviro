package enteties;

import java.io.Serializable;

import server.Queryable;

/**
 * A class that represents a Customer in the system.
 * 
 * @author nadiaelhaddaoui
 * @autor Robin Overgaard
 *
 */

public class Customer implements Serializable, Queryable {

	private int customerId;
	private String name;
	private String address;
	private int zipCode;
	private String town;
	private String phoneNumber;
	private String email;
	private int vatNumber;
	private int operation;
	private String query;

	/**
	 * Customers constructor.
	 * 
	 * @param customerId
	 *            A unique Id
	 * @param name
	 *            The name of the customer
	 * @param adress
	 *            The address of the customer
	 * @param zipCode
	 *            The zipcode of the customer
	 * @param town
	 *            The town in which the customer lives in
	 * @param phoneNumber
	 *            The customers phonenumber
	 * @param email
	 *            The customers email
	 * @param vatNumber
	 *            The organizationnumber for the customer
	 */
	public Customer(int customerId, String name, String adress, int zipCode, String town, String phoneNumber,
		String email, int vatNumber) {
		this.customerId = customerId;
		this.name = name;
		this.address = adress;
		this.zipCode = zipCode;
		this.town = town;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.vatNumber = vatNumber;
	}

	public Customer(int id) {
		this.customerId = id;
		//TODO Set default values for not null columns in db.
	}
	
//	public Customer(int operation, int customerId) {
//		this.operation = operation;
//		this.customerId = customerId;
//	}
	
	public Customer() {
		//TODO Set default values for not null columns in db.
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdress() {
		return address;
	}

	public void setAdress(String adress) {
		this.address = adress;
	}

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getVatNumber() {
		return vatNumber;
	}

	public void setVatNumber(int vatNumber) {
		this.vatNumber = vatNumber;
	}
	
	@Override
	public int getOperation() {
		return this.operation;
	}
	
	@Override
	public void setOperation(int operation) {
		this.operation = operation;
	}

	@Override
	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public String getQuery() {
		return this.query;
	}
}
