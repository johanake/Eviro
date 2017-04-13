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
	public void commandHandler(Object obj) {

		Object[] data = (Object[]) obj;
		switch ((int) data[0]) {
			case 1:
				addCustomer(data);
				break;
		}
	}

	/**
	 * Objects are sent here by the commandHandler method if the first element in the object array represents adding a
	 * new customer to the database. The data in the Object is transformed into Strings which is then sent to the
	 * database.
	 * 
	 * @param data The data which is to be transformed and sent to the database.
	 */
	private void addCustomer(Object[] data) {

		Customer c;
		if (data[1] instanceof Customer) {
			c = (Customer) data[1];

			String commandstring = c.toString();
			database.executeInsertQuery(commandstring);
		}

	}

}
