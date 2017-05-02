package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import enteties.Customer;
import enteties.Invoice;

/**
 * Creates and sends objects to the client.
 * @author Robin Overgaard, Johan Åkesson
 * @version 1.0
 */
public class ClientController {
	public static final int ADDCUSTOMER = 1;
	// public static final int GETCUSTOMER = 3;
	public static final int SEARCHCUSTOMER = 2;
	public static final int UPDATECUSTOMER = 3;
	public static final int DELETECUSTOMER = 4;

	public static final int INVOICEADD = 10;
	public static final int INVOICEUPDATE = 11;

	private Client client;

	private HashMap<String, String> txtList = new HashMap<String, String>();

	/**
	 * Constructs the ClientController.
	 * @param client the client for the system
	 */
	public ClientController(Client client) {
		this.client = client;
	}

	/**
	 * Creates a new customer.
	 * @param name customer name
	 * @param adress customer adress
	 * @param zipCode customer zip code
	 * @param town customer town
	 * @param phoneNumber customer phone number
	 * @param email customer email
	 * @param vatNumber customer vat number
	 */
	public void createCustomer(String name, String adress, String zipCode, String town, String phoneNumber, String email, String vatNumber, int creditLimit) {
		Customer c = new Customer(name, adress, zipCode, town, phoneNumber, email, vatNumber, creditLimit);
		c.setOperation(ADDCUSTOMER);
		client.sendObject(c);
	}

	public String checkFields(String name, String address, String zipCode, String city, String phoneNbr, String email, String vatNbr) {
		txtList.put("Name", name);
		txtList.put("Address", address);
		txtList.put("Zip Code", zipCode);
		txtList.put("City", city);
		txtList.put("Phone number", phoneNbr);
		txtList.put("Email", email);
		txtList.put("VAT number", vatNbr);

		for (Map.Entry<String, String> entry : txtList.entrySet()) {
			if (entry.getValue().trim().length() <= 0) {
				return "Please check following data: " + entry.getKey();
			}
		}
		createCustomer(name, address, zipCode, city, phoneNbr, email, vatNbr, 0);
		return "Customer added";

	}

	// public void getCustomer(int customerId){
	// Customer c = new Customer(customerId);
	// c.setOperation(GETCUSTOMER);
	// ArrayList<Customer> cList = (ArrayList<Customer>) client.sendObject(c);
	//
	// System.out.println(cList.get(0).getAddress());
	// }

//	public ArrayList<Customer> searchCustomer(String customerId, String name, String adress, String zipCode, String town, String phoneNumber, String email, String vatNumber, int creditLimit) {
//		Customer c = new Customer(customerId, name, adress, zipCode, town, phoneNumber, email, vatNumber, creditLimit);
//		c.setOperation(SEARCHCUSTOMER);
//		// client.sendObject(c);
//		ArrayList<Customer> customerList;
//		customerList = (ArrayList<Customer>) client.sendObject(c);
//
//		return customerList;
//	}
	
	public ArrayList<Customer> searchCustomer(Object[] obj, int creditLimit) {
		Customer c = new Customer(obj, creditLimit);
		c.setOperation(SEARCHCUSTOMER);
		// client.sendObject(c);
		ArrayList<Customer> customerList;
		customerList = (ArrayList<Customer>) client.sendObject(c);

		return customerList;
	}

	public void updateCustomer(String customerId, String name, String adress, String zipCode, String town, String phoneNumber, String email, String vatNumber, int creditLimit) {
		Customer c = new Customer(customerId, name, adress, zipCode, town, phoneNumber, email, vatNumber, creditLimit);
		c.setOperation(UPDATECUSTOMER);
		client.sendObject(c);

	}

	public void deleteCustomer(String customerId) {
		Customer c = new Customer(customerId);
		c.setOperation(DELETECUSTOMER);
		String temp = (String) client.sendObject(c);
		System.out.println(temp);
	}
	// public void createInvoice() {
	// send(new Invoice());
	// }

	public void createInvoice(String[] data) {
		Invoice i = new Invoice(data);
		i.setOperation(Eviro.DB_ADD);
		client.sendObject(i);
	}

	// public void createProduct() {
	// send(new Product());
	// }

}
