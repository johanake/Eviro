package server;

<<<<<<< HEAD
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import enteties.Customer;
import enteties.Entity;
import enteties.ForumMessage;
import enteties.Invoice;
import enteties.Product;
import enteties.Transaction;
import enteties.User;
import shared.Eviro;
=======
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import enteties.Customer;
import enteties.Invoice;
import enteties.Product;
>>>>>>> origin/peterbranch

/**
 * Handles most of the logic between the server and the database. Also logs traffic to and from database.
 * @author Mattias Sundquist, Peter Folke
 */
public class ServerController {
	public static final int AddCustomer = 1;
	public static final int GetCustomer = 2;
	public static final int GetAllCustomers = 3;
	public static final int UpdateCustomer = 4;

	private Logger log = Logger.getLogger("log");
	private FileHandler fhLog;
	private SimpleFormatter sfLog = new SimpleFormatter();
	private ConnectDB database;
	private ServerGUI serverGUI;

	/**
	 * Gets an instance of the ConnectDB class
	 */
	public ServerController() {
		database = new ConnectDB();
		try {
			File logDir = new File("logs");
			logDir.mkdir();
			fhLog = new FileHandler("logs/log" + new SimpleDateFormat("ddMMYY").format(new Date()) + ".log", true);
			fhLog.setFormatter(sfLog);
			log.addHandler(fhLog);
			log.setUseParentHandlers(false);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	public void setServerGUI(ServerGUI serverGUI) {

		this.serverGUI = serverGUI;
	}

	/**
	 * Adds a new info message in the logger.
	 * @param msg
	 */
	public void logAppend(String msg) {

		log.info(msg);
		serverGUI.append(msg + "\n");
	}

	/**
	 * Handles objects coming from the server. Finds out what operation to perform based on the getOperation method in the EntityInterface
	 * @param ei The object coming from the server.
	 */
	public ArrayList<Entity> operationHandler(Entity ei) {

		ArrayList<Entity> returnObject = null;

		switch (ei.getOperation()) {
		case Eviro.DB_ADD:
			database.executeInsertOrDeleteQuery(buildInsertQuery(ei));
			returnObject = createList(database.executeGetQuery(buildSearchQuery(ei)));
			break;
		case Eviro.DB_SEARCH:
			returnObject = createList(database.executeGetQuery(buildSearchQuery(ei)));
			break;
		case Eviro.DB_UPDATE:
			database.executeUpdateQuery(buildUpdateQuery(ei));
			returnObject = createList(database.executeGetQuery(buildSearchQuery(ei)));
			break;
		case Eviro.DB_DELETE:
			database.executeInsertOrDeleteQuery(buildDeleteQuery(ei));
			break;
		case Eviro.DB_GETALL:
			returnObject = createList(database.executeGetQuery(buildGetAllQuery(ei)));
			break;
		}
		return returnObject;
	}

	/**
	 * Checks which Object the EntityInterface is an instance of and returns that objects table name.
	 * @param ei The EntityInterface to check instance of.
	 * @return A String with the Objects table name.
	 */
	private String getTableName(Entity ei) {

		String tableName = "";
		if (ei instanceof Customer)
			tableName = "customer";
		else if (ei instanceof Invoice)
			tableName = "invoice";
		else if (ei instanceof Product)
			tableName = "product";
		else if (ei instanceof Transaction)
			tableName = "transaction";
		else if (ei instanceof ForumMessage)
			tableName = "forummessage";
		else if (ei instanceof User)
			tableName = "user";

		return tableName;
	}

	/**
	 * Asks the database for the tables column names with the MySql command "DESCRIBE table_name". The name of the table itself is provided by an EntityInterface.
	 * @param ei An EntityInterface with a method to read the table name.
	 * @return A String array with all the table names.
	 */
<<<<<<< HEAD
	private String[] getColNames(Entity ei, String tableName) {

		String query = "DESCRIBE " + tableName;
		ResultSet rs = database.executeGetQuery(query);
		try {
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(tableName);
			while (rs.next()) {
				temp.add(rs.getString(1));
			}
			String[] colNames = new String[temp.size() - 1];
			for (int i = 0; i < colNames.length; i++) {
				colNames[i] = temp.get(i + 1).toString();
			}
			return colNames;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
=======
	public Object commandHandler(Object obj) {
		Customer customer = null;
		Invoice invoice = null;
		Product product = null;
		int command = 0;
		Object returnObj = null;
		
		//kontrollerar vilken typ av objekt som tas emot, castar objekt till rätt variabel och hämtar sedan command från det
		if (Customer.class.isInstance((Customer) obj)){
			customer = (Customer) obj;
			command = customer.getCommand();
		} else if (Invoice.class.isInstance((Invoice) obj)){
			invoice = (Invoice) obj;
//			com = invoice.getCom();
		} else if (Product.class.isInstance((Product) obj)){
			product = (Product) obj;
//			com = product.getCom();
		}
		
		//switchsatsen kollar vad kommandot är, skickar sedan objektet till rätt metod som returnerar det som skall
		//skickas tillbak till klienten
		switch ((int) command) {
			case AddCustomer:
				returnObj = addCustomer(customer);
				break;
			
			case GetCustomer:	
				returnObj = getCustomer(customer);
				break;
			
			case GetAllCustomers:
				returnObj = getAllCustomers();
				break;
			
			case UpdateCustomer:
				returnObj = updateCustomer(customer);
>>>>>>> origin/peterbranch
		}
		
		return returnObj;				
	}

	/**
	 * Builds a "get all" query based on the information in the Entity given in the parameter.
	 * @param ei
	 * @return
	 */
<<<<<<< HEAD
	private String buildGetAllQuery(Entity ei) {

		String tableName = getTableName(ei);
		String query = "SELECT * FROM " + tableName;

		logAppend(query);
		System.out.println("buildGetAllQuery: " + query);
		return query;
	}

	/**
	 * Builds a search-query based on information in the EntityInterface given in the parameter.
	 * @param ei The EntityInterface to build the search-query around.
	 * @return A String-query ready to be executed by the database.
	 */
	private String buildSearchQuery(Entity ei) {

		Object[] info = ei.getData();
		String tableName = getTableName(ei);

		String[] colNames = getColNames(ei, tableName);
		String query = "SELECT * FROM " + tableName + " WHERE ";
		String and = "";

		for (int i = 0; i < colNames.length; i++) {
			if (info[i] != null && info[i].toString().trim().length() > 0) {
				query += and + colNames[i] + " LIKE '%" + info[i] + "%'";
				and = " AND ";
			}
=======
	private Customer addCustomer(Customer c) {
		String query = "INSERT INTO customer (name, adress, zipCode, city, phoneNumber, email, organisationNumber, creditLimit) "
					+ "VALUES (\"" + c.getName() + "\",\"" + c.getAdress() + "\",\"" + c.getZipCode() + "\",\""
					+ c.getTown() + "\",\"" + c.getPhoneNumber() + "\",\"" + c.getEmail() + "\",\"" + c.getVatNumber()
					+ "\"," + 0 + ")";
		database.executeInsertDeleteQuery(query);
		return c;
	}


	//exempelmetod för att hämta en eller flera kunder ur databasen med hjälp av valfritt fält
	private ArrayList<Customer> getCustomer(Customer c){
		String query = "SELECT * FROM customer WHERE ";
		if (c.getCustomerId() > 0){
			query += "customerId = " + c.getCustomerId();
		}	
		else if (c.getName() != null){
			query += "name = '" + c.getName() + "'";
		}	
		else if (c.getVatNumber() != 0){
			query += "organisationNumber = " + c.getVatNumber();
		}
		else if (c.getPhoneNumber() != null){
			query += "phoneNumber = '" + c.getPhoneNumber() + "'";
		}
		else if (c.getEmail() != null){
			query += "email = '" + c.getEmail() + "'";
>>>>>>> origin/peterbranch
		}
		return createCustomerList(database.executeSelectQuery(query));
	}


	// hämtar alla kunder returnerar dem i en HashMap med customerId som nyckel
	private HashMap<Integer, Customer> getAllCustomers(){
		HashMap<Integer, Customer> customerMap = new HashMap<Integer, Customer>();
		String query = "SELECT * FROM customer";
		ResultSet rs = database.executeSelectQuery(query);
		Customer c;
		
		try {
			while(rs.next()){
				c = createCustomerObject(rs);
				customerMap.put(c.getCustomerId(), c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customerMap;
	}

<<<<<<< HEAD
		logAppend(query);
		System.out.println("buildSearchQuery: " + query);
		return query;
	}

	/**
	 * Builds an insert-query based on information in the EntityInterface given in the parameter.
	 * @param ei The EntityInterface to build the insert-query around.
	 * @return A String-query ready to be executed by the database.
	 */
	private String buildInsertQuery(Entity ei) {

		Object[] info = ei.getData();
		String tableName = getTableName(ei);
		String[] colNames = getColNames(ei, tableName);
		String query = "INSERT INTO " + tableName + " (";

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

		logAppend(query);
		System.out.println("buildInsertQuery: " + query);
		return query;
	}

	/**
	 * Builds an update-query based on information in the EntityInterface given in the parameter. Expects that the EntityInterface id is specified.
	 * @param ei The EntityInterface to build the update-query around.
	 * @return A String-query ready to be executed by the database.
	 */
	private String buildUpdateQuery(Entity ei) {

		Object[] info = ei.getData();
		String tableName = getTableName(ei);
		String[] colNames = getColNames(ei, tableName);
		String query = "UPDATE " + tableName + " SET ";
		for (int i = 1; i < colNames.length; i++) {
			if (i == colNames.length - 1)
				query += colNames[i] + " = '" + info[i] + "'";
			else
				query += colNames[i] + " = '" + info[i] + "', ";
		}
		query += " WHERE " + colNames[0] + " = " + info[0];

		logAppend(query);
		System.out.println("buildUpdateQuery: " + query);
		return query;
	}

	/**
	 * Build a delete-query based on the information in the ENtityInterface given in the parameter. Expects that the EntityInterface id is specified.
	 * @param ei The EntityInterface to build the update-query around.
	 * @return A String-query ready to be executed by the database.
	 */
	private String buildDeleteQuery(Entity ei) {

		Object[] info = ei.getData();
		String tableName = getTableName(ei);
		String[] colNames = getColNames(ei, tableName);
		String query = "DELETE FROM " + tableName + " WHERE " + colNames[0] + " = " + info[0];

		logAppend(query);
		System.out.println("buildDeleteQuery: " + query);
		return query;
	}

	/**
	 * Builds an Arraylist of EntityInterfaces based on the resultset given in the parameter.
	 * @param rs The resultset to build the Arraylist on.
	 * @return An ArrayList of EntityInterfaces containing the EntityInterfaces from the resultset.
	 */
	private ArrayList<Entity> createList(ResultSet rs) {

		ArrayList<Entity> ei = new ArrayList<Entity>();
		try {
			while (rs.next()) {

				if (rs.getMetaData().toString().contains("tableName=customer")) {
					ei.add(new Customer(new Object[] {
							rs.getString(1),
							rs.getString(2),
							rs.getString(3),
							rs.getString(4),
							rs.getString(5),
							rs.getString(6),
							rs.getString(7),
							rs.getString(8),
							rs.getInt(9) }));
				} else if (rs.getMetaData().toString().contains("tableName=invoice")) {
					ei.add(new Invoice(new Object[] {
							rs.getString(1),
							rs.getString(2),
							rs.getString(3),
							rs.getString(4),
							rs.getString(5),
							rs.getString(6),
							rs.getString(7) }));
				} else if (rs.getMetaData().toString().contains("tableName=product")) {
					ei.add(new Product(new Object[] {
							rs.getString(1),
							rs.getString(2),
							rs.getString(3),
							rs.getString(4),
							rs.getString(5),
							rs.getString(6),
							rs.getString(7),
							rs.getString(8),
							rs.getInt(9) }));
				} else if (rs.getMetaData().toString().contains("tableName=transaction")) {
					ei.add(new Transaction(new Object[] {
							rs.getString(1),
							rs.getString(2),
							rs.getString(3),
							rs.getString(4),
							rs.getString(5) }));
				} else if (rs.getMetaData().toString().contains("tableName=forummessage")) {
					ei.add(new ForumMessage(new Object[] {
							rs.getString(1),
							rs.getString(2),
							rs.getString(3),
							rs.getString(4) }));

				} else if (rs.getMetaData().toString().contains("tableName=user")) {
					ei.add(new ForumMessage(new Object[] {
							rs.getString(1),
							rs.getString(2),
							rs.getString(3), }));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ei;
	}
=======
	//test och exempel på en updatemetod som avnvänder PreparedStatement
	private Customer updateCustomer(Customer c){ 
		String query = "UPDATE customer SET name = ?, adress = ?, zipCode = ?, city = ?, phoneNumber = ?, email = ?, organisationNumber = ? WHERE customerId = ?";
		PreparedStatement ps = database.executeUpdateQuery(query);
		try {
			ps.setString(1, c.getName());
			ps.setString(2, c.getAdress());
			ps.setInt(3, c.getZipCode());
			ps.setString(4, c.getTown());
			ps.setString(5, c.getPhoneNumber());
			ps.setString(6, c.getEmail());
			ps.setInt(7, c.getVatNumber());
			ps.setInt(8, c.getCustomerId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return c;
	}

	//genererar ett customer-objekt från den första raden i en ResultSet
	private Customer createCustomerObject(ResultSet rs){
		Customer c = null;
		try {
			if (rs.isBeforeFirst()){
				rs.next();
			}
			c = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), 
					rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8));
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return c;
	}	
	
	//genererar en lista med alla customer-objekt som finns i ResultSet 
	private ArrayList<Customer> createCustomerList(ResultSet rs){
		ArrayList<Customer> customerList = new ArrayList<Customer>();
		try {
			while (rs.next()) {
				customerList.add(new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), 
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8)));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return customerList;
	}	

>>>>>>> origin/peterbranch
}
