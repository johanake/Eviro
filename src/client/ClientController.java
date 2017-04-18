package client;

import java.util.HashMap;

import enteties.Customer;
import server.Database;

/**
 * Builds querys thats is passed to the client.
 * @author Robin Overgaard
 * @version 1.0
 */
public class ClientController {

	private Client client;

	/**
	 * Constructs the ClientController.
	 * 
	 * @param client
	 *            the client for the system
	 */
	public ClientController(Client client) {
		this.client = client;
	}

	public void updateCustomer(int id, String name, String adress, int zip, String city, String phone,
			String mail, int vat) {

		Customer customer = new Customer(id, name, adress, zip, city, phone, mail, vat);
		customer.setOperation(Database.INSERT);
		customer.setQuery("UPDATE customer SET name = '" + customer.getName() + "', adress = '" + customer.getAdress()
				+ "', zipCode = '" + customer.getZipCode() + "', city = '" + customer.getTown() + "', phoneNumber = '"
				+ customer.getPhoneNumber() + "', email = '" + customer.getEmail() + "', organisationNumber = '"
				+ customer.getVatNumber() + "' WHERE customerId = " + customer.getCustomerId());
		client.sendObject(customer);
		
	}

	public HashMap<String, String> createCustomer() {

		Customer customer = new Customer();
		customer.setOperation(Database.INSERT);
		customer.setQuery("INSERT INTO customer (customerId) VALUES (NULL)");
		client.sendObject(customer);
		return selectCustomer(Integer.parseInt((String) client.waitForResponse()));

	}

	public HashMap<String, String> selectCustomer(int customerId) {
		Customer customer = new Customer(customerId);
		customer.setOperation(Database.SELECT);
		customer.setQuery("SELECT * FROM customer WHERE customerId = " + customer.getCustomerId());
		client.sendObject(customer);
		return (HashMap<String, String>) client.waitForResponse();
	}

}
