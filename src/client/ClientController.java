package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import enteties.Customer;
import server.Database;

/**
 * Builds querys thats is passed to the client.
 * 
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

	public void updateCustomer(int id, String name, String adress, int zip, String city, String phone, String mail,
			int vat) {

		Customer customer = new Customer(id, name, adress, zip, city, phone, mail, vat);
		customer.setOperation(Database.INSERT);
		customer.setQuery("UPDATE customer SET name = '" + customer.getName() + "', adress = '" + customer.getAdress()
				+ "', zipCode = '" + customer.getZipCode() + "', city = '" + customer.getTown() + "', phoneNumber = '"
				+ customer.getPhoneNumber() + "', email = '" + customer.getEmail() + "', organisationNumber = '"
				+ customer.getVatNumber() + "' WHERE customerId = " + customer.getCustomerId());
		client.sendObject(customer);

	}

	public ArrayList<HashMap> createCustomer() {

		Customer customer = new Customer();
		customer.setOperation(Database.INSERT);
		customer.setQuery("INSERT INTO customer (customerId) VALUES (NULL)");
		client.sendObject(customer);
		return selectCustomers(Integer.parseInt((String) client.waitForResponse()));

	}

	public ArrayList<HashMap> selectCustomers(int id) {
		Customer customer = new Customer(id);
		customer.setOperation(Database.SELECT);
		customer.setQuery("SELECT * FROM customer WHERE customerId = " + customer.getCustomerId());

		client.sendObject(customer);
		return (ArrayList<HashMap>) client.waitForResponse();
	}

	public ArrayList<HashMap> selectCustomers(HashMap<String, String> args) {

		args.values().removeIf(s -> s.length() == 0);

		String query = "SELECT * FROM customer ";

		int i = 0;

		for (Map.Entry<String, String> entry : args.entrySet()) {

			if (i++ == 0) {
				query += "WHERE " +  entry.getKey() + " = '" + entry.getValue() + "'";
			}
			
			else if (i++ <= args.entrySet().size()) {
				query += " AND " + entry.getKey() + " = '" + entry.getValue() + "'";
			}
		}

		System.out.println(query);

		Customer customer = new Customer();
		customer.setOperation(Database.SELECT);
		customer.setQuery(query);

		client.sendObject(customer);
		return (ArrayList<HashMap>) client.waitForResponse();
	}

}
