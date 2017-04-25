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

	private final int ADD = 1;
	private final int SEARCH = 2;
	private final int UPDATE = 3;
	private final int DELETE = 4;
	// public static final int ADDCUSTOMER = 1;
	// public static final int GETCUSTOMER = 2;
	// public static final int SEARCHCUSTOMER = 3;
	// public static final int UPDATECUSTOMER = 4;
	// public static final int DELETECUSTOMER = 5;
	// public static final int ADDINVOICE = 6;
	// public static final int GETINVOICE = 7;
	// public static final int SEARCHINVOICE = 8;
	// public static final int UPDATEINVOICE = 9;
	// public static final int DELETEINVOICE = 10;
	// public static final int ADDPRODUCT = 11;
	// public static final int GETPRODUCT = 12;
	// public static final int SEARCHPRODUCT = 13;
	// public static final int UPDATEPRODUCT = 14;
	// public static final int DELETEPRODUCT = 15;

	private ConnectDB database;

	/**
	 * Gets an instance of the ConnectDB class
	 */
	public ServerController() {
		database = new ConnectDB();
	}

	/**
	 * Handles objects coming from the server. Finds out what operation to perform based on the getOperation method in
	 * the EntityInterface
	 * 
	 * @param ei The object coming from the server.
	 */
	public ArrayList<EntityInterface> operationHandler(EntityInterface ei) {

		ArrayList<EntityInterface> returnObject = null;

		switch (ei.getOperation()) {
			case ADD:
				database.executeInsertOrDeleteQuery(buildInsertQuery(ei));
				returnObject = createList(database.executeGetQuery(buildSearchQuery(ei)));
				break;
			case SEARCH:
				returnObject = createList(database.executeGetQuery(buildSearchQuery(ei)));
				break;
			case UPDATE:
				database.executeUpdateQuery(buildUpdateQuery(ei));
				returnObject = createList(database.executeGetQuery(buildSearchQuery(ei)));
				break;
			case DELETE:
				database.executeInsertOrDeleteQuery(buildDeleteQuery(ei));
				break;
		}
		return returnObject;
	}

	/**
	 * Builds a search-query based on information in the EntityInterface given in the parameter. Expects that objects
	 * that is not specified has been given the value -1. If the EntityInterface id has been specified then only the id
	 * will be searched.
	 * 
	 * @param ei The EntityInterface to build the search-query around.
	 * @return A String-query ready to be executed by the database.
	 */
	public String buildSearchQuery(EntityInterface ei) {

		Object[] info = ei.getAllInObjects();
		String[] colNames = ei.getColumnNames();
		String query = "SELECT * FROM " + ei.getTableName() + " WHERE ";
		String and = "";
		for (int i = 0; i < colNames.length; i++) {
			if (i == 0 && (int) info[i] != -1) {
				query += colNames[i] + " = " + info[i];
				return query;
			}
			if (!info[i].toString().equals("-1")) {
				query += and + colNames[i] + " LIKE '%" + info[i] + "%'";
				and = " AND ";
			}
		}
		return query;
	}

	/**
	 * Builds an insert-query based on information in the EntityInterface given in the parameter.
	 * 
	 * @param ei The EntityInterface to build the insert-query around.
	 * @return A String-query ready to be executed by the database.
	 */
	private String buildInsertQuery(EntityInterface ei) {

		Object[] info = ei.getAllInObjects();
		String[] colNames = ei.getColumnNames();
		String query = "INSERT INTO " + ei.getTableName() + " (";

		for (int i = 1; i < colNames.length; i++) {
			if (i == colNames.length - 1)
				query += colNames[i] + ") VALUES (";
			else
				query += colNames[i] + ", ";
		}
		for (int i = 1; i < info.length; i++) {
			if (i == info.length - 1)
				query += "'" + info[i] + "') ";
			else
				query += "'" + info[i] + "', ";
		}
		return query;
	}

	/**
	 * Builds an update-query based on information in the EntityInterface given in the parameter. Expects that the
	 * EntityInterface id is specified.
	 * 
	 * @param ei The EntityInterface to build the update-query around.
	 * @return A String-query ready to be executed by the database.
	 */
	public String buildUpdateQuery(EntityInterface ei) {

		Object[] info = ei.getAllInObjects();
		String[] colNames = ei.getColumnNames();
		String query = "UPDATE " + ei.getTableName() + " SET ";
		for (int i = 1; i < colNames.length; i++) {
			if (i == colNames.length - 1)
				query += colNames[i] + " = '" + info[i] + "'";
			else
				query += colNames[i] + " = '" + info[i] + "', ";
		}
		query += " WHERE " + colNames[0] + " = " + info[0];
		return query;
	}

	/**
	 * Build a delete-query based on the information in the ENtityInterface given in the parameter. Expects that the
	 * EntityInterface id is specified.
	 * 
	 * @param ei The EntityInterface to build the update-query around.
	 * @return A String-query ready to be executed by the database.
	 */
	public String buildDeleteQuery(EntityInterface ei) {

		Object[] info = ei.getAllInObjects();
		String[] colNames = ei.getColumnNames();
		String query = "DELETE FROM " + ei.getTableName() + " WHERE " + colNames[0] + " = " + info[0];

		return query;
	}

	/**
	 * Builds an Arraylist of EntityInterfaces based on the resultset given in the parameter.
	 * 
	 * @param rs The resultset to build the Arraylist on.
	 * @return An ArrayList of EntityInterfaces containing the EntityInterfaces from the resultset.
	 */
	private ArrayList<EntityInterface> createList(ResultSet rs) {

		ArrayList<EntityInterface> ei = new ArrayList<EntityInterface>();
		try {
			while (rs.next()) {
				if (rs.getMetaData().toString().contains("tableName=customer")) {
					ei.add(new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9)));
				} else if (rs.getMetaData().toString().contains("tableName=invoice")) {

				} else if (rs.getMetaData().toString().contains("tableName=product")) {

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ei;
	}

	// /**
	// * Handles objects coming from the server. Finds out what operation to
	// * perform based on the first element in the object array.
	// *
	// * @param obj
	// * The object coming from the server.
	// */
	// public Object operationHandler(EntityInterface obj) {
	// System.out.println("operation: " + obj.getOperation());
	// Object returnObject = null;
	//
	// switch (obj.getOperation()) {
	// case ADDCUSTOMER:
	// returnObject = addCustomer((Customer) obj);
	// break;
	// case GETCUSTOMER:
	// returnObject = getCustomer((Customer) obj);
	// break;
	// case SEARCHCUSTOMER:
	// returnObject = searchCustomer((Customer) obj);
	// break;
	// case UPDATECUSTOMER:
	// returnObject = updateCustomer((Customer) obj);
	// break;
	// case DELETECUSTOMER:
	// returnObject = deleteCustomer((Customer) obj);
	// break;
	// case ADDINVOICE:
	// returnObject = null;
	// break;
	// }
	// return returnObject;
	// }
	//
	// /**
	// * Objects are sent here by the commandHandler method if the first element
	// * in the object array represents adding a new customer to the database. The
	// * data in the Object is transformed into Strings which is then sent to the
	// * database.
	// *
	// * @param data
	// * The data which is to be transformed and sent to the database.
	// */
	// private ArrayList<Customer> addCustomer(Customer customer) {
	// String query = "INSERT INTO customer (name, address, zipCode, city, phoneNumber, email, vatNumber, creditLimit) "
	// + "VALUES (\"" + customer.getName() + "\",\"" + customer.getAddress() + "\",\"" + customer.getZipCode()
	// + "\",\"" + customer.getCity() + "\",\"" + customer.getPhoneNumber() + "\",\"" + customer.getEmail()
	// + "\",\"" + customer.getVatNumber() + "\"," + customer.getCreditLimit() + ")";
	// database.executeInsertOrDeleteQuery(query);
	//
	// return searchCustomer(customer);
	// }
	//
	// /**
	// * Builds a SELECT-query with the customerId from the incoming
	// * customer-object, executes the query and sends the returning ResultSet to
	// * createCustomerList which returns an ArrayList<Customer> with a single
	// * Customer-object that is created from the ResultSet
	// *
	// * @param customer incoming Customer-object wich only has a customerId
	// * @return ArrayList with a single Customer-object.
	// */
	// private ArrayList<Customer> getCustomer(Customer customer) {
	// String query = "SELECT * FROM customer WHERE customerId = " + customer.getCustomerId();
	//
	// return createCustomerList(database.executeGetQuery(query));
	// }
	//
	// /**
	// * Builds a SELECT-query from all current information in the incoming
	// * customer-object, executes the query and sends the returning ResultSet to
	// * createCustomerList which returns an ArrayList<Customer> with all
	// * Customer-objects that are created from the ResultSet
	// *
	// * @param customer incomimg Customer-object with different kinds of data
	// * @return ArrayList with all Customers that corresponds with the data in the incoming Customer-object.
	// */
	// private ArrayList<Customer> searchCustomer(Customer customer) {
	// String query = "SELECT * FROM customer WHERE ";
	// String and = "";
	// if (customer.getCustomerId() != 0) {
	// query += and + "customerId LIKE '%" + customer.getCustomerId() + "%'";
	// and = " AND ";
	// }
	// if (customer.getName().length() > 0) {
	// query += and + "name LIKE '%" + customer.getName() + "%'";
	// and = " AND ";
	// }
	// if (customer.getAddress().length() > 0) {
	// query += and + "address LIKE '%" + customer.getAddress() + "%'";
	// and = " AND ";
	// }
	// if (customer.getZipCode().length() > 0) {
	// query += and + "zipCode LIKE '%" + customer.getZipCode() + "%'";
	// and = " AND ";
	// }
	// if (customer.getCity().length() > 0) {
	// query += and + "city LIKE '%" + customer.getCity() + "%'";
	// and = " AND ";
	// }
	// if (customer.getPhoneNumber().length() > 0) {
	// query += and + "phoneNumber LIKE '%" + customer.getPhoneNumber() + "%'";
	// and = " AND ";
	// }
	// if (customer.getEmail().length() > 0) {
	// query += and + "email LIKE '%" + customer.getEmail() + "%'";
	// and = " AND ";
	// }
	// if (customer.getVatNumber().length() > 0) {
	// query += and + "vatNumber LIKE '%" + customer.getVatNumber() + "%'";
	// and = " AND ";
	// }
	// // if (customer.getCreditLimit() != 0) {
	// // query += and + "creditLimit LIKE '%" + customer.getCreditLimit() +
	// // "%'";
	// // }
	// System.out.print(query);
	//
	// return createCustomerList(database.executeGetQuery(query));
	// }
	//
	// /**
	// *
	// * @param customer
	// * @return
	// */
	// public Object updateCustomer(Customer customer) {
	// String returnString = "error";
	// if (getCustomer(customer).size() != 0) {
	// String query = "UPDATE customer SET name = '" + customer.getName() + "', address = '"
	// + customer.getAddress() + "', zipCode = '" + customer.getZipCode() + "', city = '"
	// + customer.getCity() + "', phoneNumber = '" + customer.getPhoneNumber() + "', vatNumber = '"
	// + customer.getVatNumber() + "', creditLimit = " + customer.getCreditLimit() + " WHERE customerId = "
	// + customer.getCustomerId();
	// database.executeUpdateQuery(query);
	// returnString = "ok";
	// }
	// return returnString;
	// }
	//
	// /**
	// *
	// * @param customer
	// * @return
	// */
	// private Object deleteCustomer(Customer customer) {
	// String returnString = "error";
	// if (getCustomer(customer).size() != 0) {
	// String query = "DELETE FROM customer WHERE customerId = " + customer.getCustomerId();
	// database.executeInsertOrDeleteQuery(query);
	// returnString = "ok";
	// }
	// return returnString;
	// }
	//
	// /**
	// *
	// * @param rs
	// * @return
	// */
	// private ArrayList<Customer> createCustomerList(ResultSet rs) {
	// ArrayList<Customer> customerList = new ArrayList<Customer>();
	// try {
	// while (rs.next()) {
	// customerList.add(new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
	// rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9)));
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return customerList;
	// }

}
