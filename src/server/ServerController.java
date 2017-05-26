package server;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

import enteties.Comment;
import enteties.Customer;
import enteties.Entity;
import enteties.ForumMessage;
import enteties.Invoice;
import enteties.Product;
import enteties.Transaction;
import enteties.User;
import shared.Eviro;

/**
 * Controller class for the server system. Handles most of the logic between the server and the database. Also logs traffic to and from database.
 * @author Mattias Sundquist,
 * @author Peter Sjögren
 * @author Robin Overgaard
 * @author nadiaelhaddaoui
 */
public class ServerController {

	private Server server;
	private ConnectDB connectDB;
	private ServerGUI serverGUI;
	private Logger log = Logger.getLogger("log");
	private FileHandler fhLog;
	private SimpleFormatter sfLog = new SimpleFormatter();
	private FileReader reader;
	private Properties properties = new Properties();
	private BasicTextEncryptor textCryptor = new BasicTextEncryptor();
	private StrongPasswordEncryptor passCryptor = new StrongPasswordEncryptor();

	/**
	 * Constructor that loads information from the config file for later use by the system. Starts the ServerGUI class and starts the logger.
	 * @param server
	 */
	public ServerController(Server server) {

		this.server = server;

		try {
			properties.load(reader = new FileReader("properties/serverConfig.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		serverGUI = new ServerGUI(this, server);
		setUpLogger();

	}

	/**
	 * Login method that checks if the password is OK, starts the ConnectDB class and tells the Server class to start the ClientListener
	 */
	protected boolean login(String pass) {

		if (passCryptor.checkPassword(pass, properties.getProperty("admin"))) {

			textCryptor.setPassword(pass);

			connectDB = new ConnectDB(this);
			server.connect();

			return true;
		}
		return false;
	}

	/**
	 * Method for decrypting properties from the serverConfig.dat file.
	 * @param property Name of the property to be decrypted
	 * @return String the value of the decrypted property.
	 */
	public String decrypt(String property) {

		return textCryptor.decrypt(getProperty(property));
	}

	/**
	 * Method for finding properties from the serverConfig.dat file
	 * @param property Name of the property
	 * @return value of the property, in most cases an encrypted value.
	 */
	public String getProperty(String property) {

		return properties.getProperty(property);
	}

	/**
	 * Method to change properties that are stored in the serverConfig.dat file.
	 * @param property Name of the property
	 * @param value New value to be stored
	 */
	public boolean setProperty(String property, String value) {

		properties.setProperty(property, value);
		try {
			properties.store(new FileWriter("properties/serverConfig.properties"), "Changed: " + property + " (old = " + value + ")");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Sets up the logger
	 */
	private void setUpLogger() {

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

	/**
	 * Adds a new info message in the logger.
	 * @param msg the message to insert into the logger
	 */
	public void logAppend(String msg) {

		log.info(msg);
		serverGUI.append(msg + "\n");
	}

	/**
	 * Handles Entity-objects coming from the server. Finds out what operation to perform based on the getOperation method in the Entity Interface, builds a query and sends the query to the database to execute.
	 * @param ei The Entity Interface coming from the server.
	 */
	public ArrayList<Entity> operationHandler(Entity ei) {

		ArrayList<Entity> returnObject = null;

		switch (ei.getOperation()) {

		case Eviro.DB_ADD:
			connectDB.executeInsertOrDeleteQuery(buildInsertQuery(ei));
			returnObject = createList(connectDB.executeGetQuery(buildSearchQuery(ei)));
			break;
		case Eviro.DB_SEARCH:
			returnObject = createList(connectDB.executeGetQuery(buildSearchQuery(ei)));
			break;
		case Eviro.DB_UPDATE:
			connectDB.executeUpdateQuery(buildUpdateQuery(ei));
			returnObject = createList(connectDB.executeGetQuery(buildSearchQuery(ei)));
			break;
		case Eviro.DB_DELETE:
			connectDB.executeInsertOrDeleteQuery(buildDeleteQuery(ei));
			break;
		case Eviro.DB_GETALL:
			returnObject = createList(connectDB.executeGetQuery(buildGetAllQuery(ei)));
			break;
		case Eviro.DB_SEARCH_COMMENT:
			returnObject = createList(connectDB.executeGetQuery(buildGetCommentQuery(ei)));
			break;
		case Eviro.DB_ADD_COMMENT:
			buildAndExecuteInsertCommentQuery(ei);
			break;
		}
		return returnObject;
	}

	/**
	 * Checks which Object the Entity Interface is an instance of and returns that objects table name.
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
		else if (ei instanceof Comment)
			tableName = "comment";

		return tableName;
	}

	/**
	 * Asks the database for the tables column names with the MySql command "DESCRIBE table_name". The name of the table itself is provided by the getTableName method.
	 * @param ei The Entity Interface you want the column names from.
	 * @param tableName The name of the Entity Interface table
	 * @return A String array with all the column names of that table.
	 */
	private String[] getColNames(Entity ei, String tableName) {

		String query = "DESCRIBE " + tableName;
		ResultSet rs = connectDB.executeGetQuery(query);
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
	 * Builds a "get all" for the Entity Interface given in the parameter.
	 * @param ei The Entity Interface to build the get all query around.
	 * @return
	 */
	private String buildGetAllQuery(Entity ei) {

		String tableName = getTableName(ei);
		String query = "SELECT * FROM " + tableName;

		logAppend(query);
		return query;
	}

	/**
	 * @param ei The EntityInterface to build the search-query around.
	 */
	private void buildAndExecuteInsertCommentQuery(Entity ei) {

		Object[] info = ei.getData();

		String entityNo = (String) info[0];
		String entity = (String) info[1];

		String commentTableQuery = "INSERT INTO comment (comment, date) VALUES ('" + info[2] + "', '" + info[3] + "');";
		String commentId = connectDB.executeUpdateQueryAndReturnGeneratedId(commentTableQuery);
		logAppend(commentTableQuery);

		String entityCommentTableQuery = "INSERT INTO " + entity + "comment (commentId, " + entity + "Id) VALUES ('"
				+ commentId + "', " + entityNo + ");";
		connectDB.executeInsertOrDeleteQuery(entityCommentTableQuery);
		logAppend(entityCommentTableQuery);

	}

	/**
	 * @param ei The EntityInterface to build the search-query around.
	 * @return A String-query ready to be executed by the database.
	 */
	private String buildGetCommentQuery(Entity ei) {

		Object[] info = ei.getData();

		String tableName = getTableName(ei);
		String entity = (String) info[1];
		String entityNo = (String) info[0];

		String query = "SELECT " + entity + "Id, comment, date FROM " + tableName + " INNER JOIN " + entity + tableName
				+ " ON " + tableName + ".commentId = " + entity + tableName + ".commentId WHERE " + entity + "Id = '"
				+ entityNo + "';";

		logAppend(query);
		return query;
	}

	/**
	 * Builds a search-query based on information in the Entity Interface given in the parameter.
	 * @param ei The Entity Interface to build the search query around.
	 * @return A query ready to be executed by the database.
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
		return query;
	}

	/**
	 * Builds an insert query based on information in the Entity Interface given in the parameter.
	 * @param ei The Entity Interface to build the insert query around.
	 * @return A query ready to be executed by the database.
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
		return query;
	}

	/**
	 * Builds an update query based on information in the Entity Interface given in the parameter. Expects that the EntityInterface id is specified.
	 * @param ei The Entity Interface to build the update query around.
	 * @return A query ready to be executed by the database.
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
		return query;
	}

	/**
	 * Build a delete query based on the information in the Entity Interface given in the parameter. Expects that the EntityInterface id is specified.
	 * @param ei The Entity Interface to build the update query around.
	 * @return A query ready to be executed by the database.
	 */
	private String buildDeleteQuery(Entity ei) {

		Object[] info = ei.getData();
		String tableName = getTableName(ei);
		String[] colNames = getColNames(ei, tableName);
		String query = "DELETE FROM " + tableName + " WHERE " + colNames[0] + " = " + info[0];

		logAppend(query);
		return query;
	}

	/**
	 * Builds an Arraylist of Entity Interfaces based on the resultset given in the parameter.
	 * @param rs The resultset to build the Arraylist on.
	 * @return An ArrayList of Entity Interfaces containing the Entity Interfaces from the resultset.
	 */
	private ArrayList<Entity> createList(ResultSet rs) {

		ArrayList<Entity> ei = new ArrayList<Entity>();
		try {
			while (rs.next()) {

				if (rs.getMetaData().toString().contains("tableName=comment")) {
					ei.add(new Comment(new Object[] { rs.getString(1), rs.getString(2), rs.getString(3) }));

				} else if (rs.getMetaData().toString().contains("tableName=customer")) {
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
							rs.getString(7),
							rs.getString(8) }));

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
					ei.add(new ForumMessage(
							new Object[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4) }));

				} else if (rs.getMetaData().toString().contains("tableName=user")) {
					ei.add(new ForumMessage(new Object[] { rs.getString(1), rs.getString(2), rs.getString(3) }));

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ei;
	}
}
