package client;

import enteties.Customer;

/**
 * Creates and sends objects to the client. 
 * @author Robin Overgaard
 * @version 1.0
 */
public class ClientController {
	public static final int ADDCUSTOMER = 1;
	public static final int GetCustomer = 2;
	public static final int GetAllCustomers = 3;
	public static final int UpdateCustomer = 4;
	private Client client;
	
	/**
	 * Constructs the ClientController. 
	 * @param client the client for the system
	 */
	public ClientController(Client client) {
		this.client = client;
	}
	
	/**
	 * Creates a new customer. 
	 * @param customerId customer id
	 * @param name customer name
	 * @param adress customer adress
	 * @param zipCode customer zip code
	 * @param town customer town
	 * @param phoneNumber customer phone number
	 * @param email customer email
	 * @param vatNumber customer vat number
	 */
	public void createCustomer(int customerId, String name, String adress, String zipCode, String town, String phoneNumber, String email, String vatNumber, int creditLimit) {
		Customer c = new Customer(customerId,name,adress,zipCode,town,phoneNumber,email,vatNumber, creditLimit);
		c.setOperation(ADDCUSTOMER);
		client.sendObject(c);
	}
	
//	public void createInvoice() {
//		send(new Invoice());
//	}
	
//	public void createProduct() {
//		send(new Product());
//	}
	
}
