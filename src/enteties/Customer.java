package enteties;

import java.io.Serializable;

/**
 * A class that represents a Customer in the system.
 * 
 * @author nadiaelhaddaoui
 *
 */

public class Customer implements Serializable, EntityInterface {

	private static final long serialVersionUID = 1L;
	private int customerId;
	private String name;
	private String address;
	private String zipCode;
	private String city;
	private String phoneNumber;
	private String email;
	private String vatNumber;
	private int creditLimit;
	private int operation;
	private final String[] COLUMNNAMES = { "customerId", "name", "address", "zipCode", "city", "phoneNumber", "email",
			"vatNumber", "creditLimit" };
	private final String TABLENAME = "customer";

	/**
	 * Customers constructor.
	 * 
	 * @param customerId A unique Id
	 * @param name The name of the customer
	 * @param adress The address of the customer
	 * @param zipCode The zipcode of the customer
	 * @param city The town in which the customer lives in
	 * @param phoneNumber The customers phonenumber
	 * @param email The customers email
	 * @param vatNumber The organizationnumber for the customer
	 */

	// Krav på namn, adress, zip, city, tele.
	public Customer(int customerId, String name, String address, String zipCode, String city, String phoneNumber,
			String email, String vatNumber, int creditLimit) {
		this.customerId = customerId;
		this.name = name;
		this.address = address;
		this.zipCode = zipCode;
		this.city = city;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.vatNumber = vatNumber;
		this.creditLimit = creditLimit;

	}

	public Customer(int customerId) {
		this.customerId = customerId;
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

	public String getAddress() {

		return address;
	}

	public void setAddress(String address) {

		this.address = address;
	}

	public String getZipCode() {

		return zipCode;
	}

	public void setZipCode(String zipCode) {

		this.zipCode = zipCode;
	}

	public String getCity() {

		return city;
	}

	public void setCity(String city) {

		this.city = city;
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

	public String getVatNumber() {

		return vatNumber;
	}

	public void setVatNumber(String vatNumber) {

		this.vatNumber = vatNumber;
	}

	public int getCreditLimit() {

		return creditLimit;
	}

	public void setCreditLimit(int creditLimit) {

		this.creditLimit = creditLimit;
	}

	@Override
	public void setOperation(int operation) {

		this.operation = operation;
	}

	@Override
	public int getOperation() {

		return operation;
	}

	@Override
	public Object[] getAllInObjects() {

		Object[] obj = { customerId, name, address, zipCode, city, phoneNumber, email, vatNumber, creditLimit };
		return obj;
	}

	@Override
	public String[] getColumnNames() {

		return COLUMNNAMES;
	}

	@Override
	public String getTableName() {

		return TABLENAME;
	}

}
