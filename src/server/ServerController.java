package server;

import enteties.Customer;

/**
 * Handles most of the logic between the server and the database.
 * 
 * @author Mattias Sundquist, Peter Folke
 *
 */
public class ServerController {

	private ConnectDB database;

	/**
	 * Gets an instance of the ConnectDB class
	 */
	public ServerController() {
		database = new ConnectDB();
	}

	/**
	 * Handles objects coming from the server. Finds out what operation to perform based on the first element in the
	 * object array.
	 * 
	 * @param obj The object coming from the server.
	 */
	public Object operationHandler(Object obj) {
		Object returnObject = null;
		int operation = 0;
		
		switch ((int) operation) {
			case 1:
				addCustomer(null);
				break;
		}
		return returnObject;
	}

	/**
	 * Objects are sent here by the commandHandler method if the first element in the object array represents adding a
	 * new customer to the database. The data in the Object is transformed into Strings which is then sent to the
	 * database.
	 * 
	 * @param data The data which is to be transformed and sent to the database.
	 */
	private Customer addCustomer(Customer c) {
			String query = "INSERT INTO customer (name, address, zipCode, city, phoneNumber, email, vatNumber, creditLimit) "
					+ "VALUES (\"" + c.getName() + "\",\"" + c.getAddress() + "\",\"" + c.getZipCode() + "\",\""
					+ c.getCity() + "\",\"" + c.getPhoneNumber() + "\",\"" + c.getEmail() + "\",\"" + c.getVatNumber() + "\"," +
					c.getCreditLimit() + ")";
			database.executeInsertQuery(query);
			
			return null;

	}

}
