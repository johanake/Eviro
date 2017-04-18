package enteties;
import java.io.Serializable;

/**
 * A class that represents a Customer in the system.
 * @author nadiaelhaddaoui
 *
 */

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
	
	/**
	 * Customers constructor.
	 * @param customerId A unique Id
	 * @param name The name of the customer
	 * @param adress The address of the customer
	 * @param zipCode The zipcode of the customer
	 * @param town The town in which the customer lives in
	 * @param phoneNumber The customers phonenumber
	 * @param email The customers email
	 * @param vatNumber The organizationnumber for the customer
	 */
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
	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public String toString() {

			return null;
		}

}
