package server;

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

/**
 * Handles most of the logic between the server and the database. Also logs traffic to and from database.
 * @author Mattias Sundquist, Peter Folke
 */
public class ServerController {

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
		}
	}

	/**
	 * Builds a "get all" query based on the information in the Entity given in the parameter.
	 * @param ei
	 * @return
	 */
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
		}

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

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ei;
	}
}
