package enteties;

import java.io.Serializable;
import java.util.HashMap;

/**
 * A class that represents a Customer in the system.
 * 
 * @author nadiaelhaddaoui
 *
 */

public class Customer implements Serializable, EntityInterface {

	private static final long serialVersionUID = 1L;
	private String customerId;
	private String name;
	private String address;
	private String zipCode;
	private String city;
	private String phoneNumber;
	private String email;
	private String vatNumber;
	private int creditLimit;
	private int operation;
	
	private Object[] data = new Object[]{customerId, name, address, zipCode, city, phoneNumber, email, vatNumber, creditLimit};
	
	public Customer(Object[] data, int creditLimit){
		this.data = data;
		this.creditLimit = creditLimit;
		
	}

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

	public Customer(String customerId, String name, String address, String zipCode, String city, String phoneNumber,
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
	
	/**
	 * 
	 * @param name
	 * @param address
	 * @param zipCode
	 * @param city
	 * @param phoneNumber
	 * @param email
	 * @param vatNumber
	 * @param creditLimit
	 */
	public Customer(String name, String address, String zipCode, String city, String phoneNumber,String email, String vatNumber, int creditLimit) {
		this.name = name;
		this.address = address;
		this.zipCode = zipCode;
		this.city = city;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.vatNumber = vatNumber;
		this.creditLimit = creditLimit;

	}

	public Customer(String customerId) {
		this.customerId = customerId;
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

}
