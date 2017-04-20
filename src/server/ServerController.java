package server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import enteties.Customer;
import enteties.EntityInterface;

/**
 * Handles most of the logic between the server and the database.
 * 
 * @author Mattias Sundquist, Peter Folke
 *
 */
public class ServerController {
	public static final int ADDCUSTOMER = 1;
	public static final int GETCUSTOMER = 2;
	public static final int SEARCHCUSTOMER = 3;
	public static final int UPDATECUSTOMER = 4;
	public static final int DELETECUSTOMER = 5;

	private ConnectDB database;

	/**
	 * Gets an instance of the ConnectDB class
	 */
	public ServerController() {
		database = new ConnectDB();
	}

	/**
	 * Handles objects coming from the server. Finds out what operation to
	 * perform based on the first element in the object array.
	 * 
	 * @param obj
	 *            The object coming from the server.
	 */
	public Object operationHandler(EntityInterface obj) {
		System.out.println("operation: " + obj.getOperation());
		Object returnObject = null;

		switch (obj.getOperation()) {
		case ADDCUSTOMER:
			returnObject = addCustomer((Customer) obj);
			break;
		case GETCUSTOMER:
			returnObject = getCustomer((Customer) obj);
			break;
		case SEARCHCUSTOMER:
			returnObject = searchCustomer((Customer) obj);
			break;
		case UPDATECUSTOMER:
			returnObject = updateCustomer((Customer) obj);
			break;
		case DELETECUSTOMER:
			returnObject = deleteCustomer((Customer) obj);
			break;
		}

		return returnObject;
	}

	/**
	 * Objects are sent here by the commandHandler method if the first element
	 * in the object array represents adding a new customer to the database. The
	 * data in the Object is transformed into Strings which is then sent to the
	 * database.
	 * 
	 * @param data
	 *            The data which is to be transformed and sent to the database.
	 */
	private Customer addCustomer(Customer customer) {
		String query = "INSERT INTO customer (name, address, zipCode, city, phoneNumber, email, vatNumber, creditLimit) "
				+ "VALUES (\"" + customer.getName() + "\",\"" + customer.getAddress() + "\",\"" + customer.getZipCode()
				+ "\",\"" + customer.getCity() + "\",\"" + customer.getPhoneNumber() + "\",\"" + customer.getEmail()
				+ "\",\"" + customer.getVatNumber() + "\"," + customer.getCreditLimit() + ")";
		database.executeInsertOrDeleteQuery(query);

		return customer;

	}
	
	/**
	 * 
	 * @param customer
	 * @return
	 */
	private ArrayList<Customer> getCustomer(Customer customer) {
		String query = "SELECT * FROM customer WHERE customerId = " + customer.getCustomerId();
		return createCustomerList(database.executeGetQuery(query));
	}

	/**
	 * 
	 * @param customer
	 * @return
	 */
	private ArrayList<Customer> searchCustomer(Customer customer) {
		String query = "SELECT * FROM customer WHERE ";
		String and = "";
		if (customer.getCustomerId() != 0) {
			query += and + "customerId LIKE '%" + customer.getCustomerId() + "%'";
			and = " AND ";
		}
		if (customer.getName().length() > 0) {
			query += and + "name LIKE '%" + customer.getName() + "%'";
			and = " AND ";
		}
		if (customer.getAddress().length() > 0) {
			query += and + "address LIKE '%" + customer.getAddress() + "%'";
			and = " AND ";
		}
		if (customer.getZipCode().length() > 0) {
			query += and + "zipCode LIKE '%" + customer.getZipCode() + "%'";
			and = " AND ";
		}
		if (customer.getCity().length() > 0) {
			query += and + "city LIKE '%" + customer.getCity() + "%'";
			and = " AND ";
		}
		if (customer.getPhoneNumber().length() > 0) {
			query += and + "phoneNumber LIKE '%" + customer.getPhoneNumber() + "%'";
			and = " AND ";
		}
		if (customer.getEmail().length() > 0) {
			query += and + "email LIKE '%" + customer.getEmail() + "%'";
			and = " AND ";
		}
		if (customer.getVatNumber().length() > 0) {
			query += and + "vatNumber LIKE '%" + customer.getVatNumber() + "%'";
			and = " AND ";
		}
		// if (customer.getCreditLimit() != 0) {
		// query += and + "creditLimit LIKE '%" + customer.getCreditLimit() +
		// "%'";
		// }
		System.out.print(query);

		return createCustomerList(database.executeGetQuery(query));
	}

	/**
	 * 
	 * @param customer
	 * @return
	 */
	public Object updateCustomer(Customer customer) {
		String query = "UPDATE customer SET name = '" + customer.getName() + "', address = '" + customer.getAddress()
				+ "', zipCode = '" + customer.getZipCode() + "', city = '" + customer.getCity() + "', phoneNumber = '"
				+ customer.getPhoneNumber() + "', vatNumber = '" + customer.getVatNumber() + "', creditLimit = "
				+ customer.getCreditLimit() + " WHERE customerId = " + customer.getCustomerId();
		database.executeUpdateQuery(query);

		return "Update done";
	}
	
	/**
	 * 
	 * @param customer
	 * @return
	 */
	private Object deleteCustomer(Customer customer) {
		String query = "DELETE FROM customer WHERE customerId = " + customer.getCustomerId();
		database.executeInsertOrDeleteQuery(query);
		return "Delete done";
	}
	
	/**
	 * 
	 * @param rs
	 * @return
	 */
	private ArrayList<Customer> createCustomerList(ResultSet rs) {
		ArrayList<Customer> customerList = new ArrayList<Customer>();
		try {
			while (rs.next()) {
				customerList.add(new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customerList;
	}

}
