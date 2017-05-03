package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import enteties.Customer;
import enteties.Entity;
import enteties.Invoice;
import enteties.Product;
import enteties.Transaction;

/**
 * Creates and sends objects to the client.
 * @author Robin Overgaard, Johan Åkesson
 * @version 1.0
 */
public class ClientController {
	public static final int ADDCUSTOMER = 1;
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
	public void createCustomer(Object[] obj) {
		Customer c = new Customer(obj);
		c.setOperation(ADDCUSTOMER);
		client.sendObject(c);
	}

	public String checkFields(Object[] obj) {
		txtList.put("Name", obj[1].toString());
		txtList.put("Address", obj[2].toString());
		txtList.put("Zip Code", obj[3].toString());
		txtList.put("City", obj[4].toString());
		txtList.put("Phone number", obj[5].toString());
		txtList.put("Email", obj[6].toString());
		txtList.put("VAT number", obj[7].toString());
		txtList.put("Credit Limit", obj[8].toString());

		for (Map.Entry<String, String> entry : txtList.entrySet()) {

			if (entry.getValue().trim().length() <= 0) {
				return "Please check following data: " + entry.getKey();
			}

		}
		createCustomer(obj);
		return "Customer added";

	}

	public void updateCustomer(Object[] obj) {
		Customer c = new Customer(obj);
		c.setOperation(UPDATECUSTOMER);
		client.sendObject(c);

	}

	// public void deleteCustomer(String customerId) {
	// Customer c = new Customer(customerId);
	// c.setOperation(DELETECUSTOMER);
	// String temp = (String) client.sendObject(c);
	// System.out.println(temp);
	// }
	// public void createInvoice() {
	// send(new Invoice());
	// }

	public String createInvoice(String[] data) {
		Invoice i = new Invoice(data);
		i.setOperation(Eviro.DB_ADD);

		ArrayList<Invoice> response = (ArrayList<Invoice>) client.sendObject(i);
		System.out.println(response.size());

		return response.get(0).getInvoiceNbr();
	}

	public void createTransactions(String[] data) {
		Transaction t = new Transaction(data);
		t.setOperation(Eviro.DB_ADD);
		client.sendObject(t);
	}

	// Ersätts med Nadjas metod sen!
	// public Object[] searchArticle(String[] data) {
	// Product p = new Product(data);
	// p.setOperation(Eviro.DB_SEARCH);
	// ArrayList<Product> response;
	// response = (ArrayList<Product>) client.sendObject(p);
	// return response.get(0).getAllInObjects();
	//
	// }

	// De två nedanstående metoderna
	public void addProduct(String[] data) {
		Product p = new Product(data);
		p.setOperation(Eviro.DB_ADD);
		System.out.println(p.getData()[1]);
		client.sendObject(p);
	}

	public ArrayList<Entity> search(Object[] data, int entityType) {

		// Create entity, type by entityType
		Entity object = createEntityByType(entityType);

		// Populate
		object.setData(data);
		object.setOperation(Eviro.DB_SEARCH);

		// Get and return response
		ArrayList<Entity> response = null;
		response = (ArrayList<Entity>) client.sendObject(object);
		return response;

	}

	private Entity createEntityByType(int entityType) {

		if (entityType == Eviro.ENTITY_CUSTOMER) {
			return new Customer();
		}

		else if (entityType == Eviro.ENTITY_INVOICE) {
			return new Invoice();
		}

		else if (entityType == Eviro.ENTITY_PRODUCT) {
			return new Product();
		}

		else if (entityType == Eviro.ENTITY_TRANSACTION) {
			return new Transaction();
		}

		return null;

	}

}
