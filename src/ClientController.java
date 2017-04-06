
/**
 * Creates and sends objects to the client. 
 * @author Robin Overgaard
 * @version 1.0
 */

public class ClientController {

	private Client client;
	
	public ClientController(Client client) {
		this.client = client;
	}
	
	public void createCustomer(int customerId, String name, String adress, int zipCode, String town, String phoneNumber, String email, int vatNumber) {
		send(new Customer(customerId,name,adress,zipCode,town,phoneNumber,email,vatNumber));
	}
	
//	public void createInvoice() {
//		send(new Invoice());
//	}
	
//	public void createProduct() {
//		send(new Product());
//	}
	
	private void send(Object obj) {
		
		client.sendMessage(new Object[]{1,obj});
		
//		Customer c = (Customer)obj; 
//		System.out.println(c.getAdress());
	}

}
