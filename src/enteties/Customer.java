package enteties;

import java.io.Serializable;

/**
 * Customer represents a customer entity in the system.
 * @author nadiaelhaddaoui
 * @author Robin Overgaard
 */

<<<<<<< HEAD
public class Customer implements Serializable, Entity {

	private int operation;
	private Object[] data;

=======
public class Customer implements Serializable {
	private int command;
	private int customerId;
	private String name;
	private String adress;
	private int zipCode;
	private String town;
	private String phoneNumber;
	private String email;
	private int vatNumber;
	
>>>>>>> origin/peterbranch
	/**
	 * Creates a empty customer entity object.
	 */
<<<<<<< HEAD
	public Customer() {
=======
	public Customer(int customerId, String name, String adress, int zipCode, String town, String phoneNumber, String email, int vatNumber){
		this.customerId=customerId;
		this.name=name;
		this.adress=adress;
		this.zipCode=zipCode;
		this.town=town;
		this.phoneNumber=phoneNumber;
		this.email=email;
		this.vatNumber=vatNumber;
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
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
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
>>>>>>> origin/peterbranch
	}

	/**
	 * Creates a customer entity object and populates it's data variable.
	 * @param data customer related data
	 */
	public Customer(Object[] data) {
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * @see enteties.EntityInterface#getOperation()
	 */
	@Override
	public int getOperation() {
		return operation;
	}

	/*
	 * (non-Javadoc)
	 * @see enteties.EntityInterface#setOperation(int)
	 */
	@Override
	public void setOperation(int operation) {
		this.operation = operation;
	}

	/*
	 * (non-Javadoc)
	 * @see enteties.EntityInterface#getData()
	 */
	@Override
	public Object[] getData() {
		return data;
	}

	/*
	 * (non-Javadoc)
	 * @see enteties.EntityInterface#setData(java.lang.Object[])
	 */
	@Override
	public void setData(Object[] data) {
		this.data = data;

<<<<<<< HEAD
=======
	public void setVatNumber(int vatNumber) {
		this.vatNumber = vatNumber;
	}
	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
>>>>>>> origin/peterbranch
	}

	public String toString() {

			return null;
		}

}
