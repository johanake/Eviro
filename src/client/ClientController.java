package client;

import enteties.Customer;

/**
 * Creates and sends objects to the client. 
 * @author Robin Overgaard
 * @version 1.0
 */
public class ClientController {

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
	public void createCustomer(int customerId, String name, String adress, int zipCode, String town, String phoneNumber, String email, int vatNumber) {
		send(1,new Customer(customerId,name,adress,zipCode,town,phoneNumber,email,vatNumber));
	}
	
//	public void createInvoice() {
//		send(new Invoice());
//	}
	
//	public void createProduct() {
//		send(new Product());
//	}
	
	/**
	 * Sends an operation and entity to the client. 
	 * @param operation the operation to be executed by the client
	 * @param obj the entity that the client will execute the operation on
	 */
	private void send(int operation, Object obj) {
		
		client.sendMessage(new Object[]{operation, obj});
		
	}
}
