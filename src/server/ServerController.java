package server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import enteties.Customer;
import enteties.EntityInterface;
import enteties.Invoice;
import enteties.Product;

/**
 * Handles most of the logic between the server and the database.
 * 
 * @author Mattias Sundquist, Peter Folke
 */
public class ServerController {

	private final int ADD = 1;
	private final int SEARCH = 2;
	private final int UPDATE = 3;
	private final int DELETE = 4;

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
	 * Checks which Object the EntityInterface is an instance of and returns that objects table name.
	 * @param ei The EntityInterface to check instance of.
	 * @return A String with the Objects table name.
	 */
	private String getTableName(EntityInterface ei) {
		String tableName = "";
		if (ei instanceof Customer)
			tableName = "customer";
		else if (ei instanceof Invoice)
			tableName = "invoice";
		else if (ei instanceof Product)
			tableName = "product";
		
		return tableName;
	}

	/**
	 * Asks the database for the tables column names with the MySql command "DESCRIBE table_name". The name of the table
	 * itself is provided by an EntityInterface.
	 * 
	 * @param ei An EntityInterface with a method to read the table name.
	 * @return A String array with all the table names.
	 */
	private String[] getColNames(EntityInterface ei, String tableName) {

		String query = "DESCRIBE " + tableName;
		ResultSet rs = database.executeGetQuery(query);
		try {
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(tableName);
			while (rs.next()) {
				temp.add(rs.getString(1));
			}
			String[] colNames = new String[temp.size()-1];
			for (int i = 0; i < colNames.length; i++) {
				colNames[i] = temp.get(i+1).toString();
			}
			return colNames;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Builds a search-query based on information in the EntityInterface given in the parameter.
	 * 
	 * @param ei The EntityInterface to build the search-query around.
	 * @return A String-query ready to be executed by the database.
	 */
	private String buildSearchQuery(EntityInterface ei) {

		Object[] info = ei.getAllInObjects();
		String tableName = getTableName(ei);
		String[] colNames = getColNames(ei, tableName);
		String query = "SELECT * FROM " + tableName + " WHERE ";
		String and = "";
		for (int i = 0; i < colNames.length; i++) {
			if (info[i].toString().trim().length() > 0) {
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

		System.out.println(query);
		return query;
	}

	/**
	 * Builds an update-query based on information in the EntityInterface given in the parameter. Expects that the
	 * EntityInterface id is specified.
	 * 
	 * @param ei The EntityInterface to build the update-query around.
	 * @return A String-query ready to be executed by the database.
	 */
	private String buildUpdateQuery(EntityInterface ei) {

		Object[] info = ei.getAllInObjects();
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
		return query;
	}

	/**
	 * Build a delete-query based on the information in the ENtityInterface given in the parameter. Expects that the
	 * EntityInterface id is specified.
	 * 
	 * @param ei The EntityInterface to build the update-query around.
	 * @return A String-query ready to be executed by the database.
	 */
	private String buildDeleteQuery(EntityInterface ei) {

		Object[] info = ei.getAllInObjects();
		String tableName = getTableName(ei);
		String[] colNames = getColNames(ei, tableName);
		String query = "DELETE FROM " + tableName + " WHERE " + colNames[0] + " = " + info[0];

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
					ei.add(new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
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
}
