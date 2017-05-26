package client;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.jasypt.util.password.StrongPasswordEncryptor;

import enteties.Comment;
import enteties.Customer;
import enteties.Entity;
import enteties.ForumMessage;
import enteties.Invoice;
import enteties.Product;
import enteties.Transaction;
import enteties.User;
import gui.GUIController;
import gui.Tool;
import shared.Eviro;

/**
 * Controller class for the client, handles communication within the system and
 * also creates and sends objects to the server.
 * 
 * @author Robin Overgaard
 * @author Johan Åkesson
 * @author Peter Sjögren
 * @version 1.1
 */
public class ClientController extends Thread {

	private StrongPasswordEncryptor passCryptor = new StrongPasswordEncryptor();
	private FileReader reader;
	private Properties properties = new Properties();
	private User activeUser = null;
	private Client client;

	/**
	 * Constructor that loads the properties that are stored in the
	 * clientConfig.dat file.
	 */
	public ClientController() {
		try {
			reader = new FileReader("config/clientConfig.dat");
			properties.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Run method that starts the Client class that handles connection with the
	 * server and also the GUI controller that handles graphics in the system
	 * 
	 */
	public void run() {

		client = new Client(this);
		new GUIController(this);

	}

	/**
	 * Creates and sends a "update operation" object to the server.
	 * 
	 * @param data
	 *            data to use when updating
	 * @param entityType
	 *            the type of entity to update
	 * @return whether the update was successfull or not
	 */
	public boolean update(String[] data, int entityType) {
		return update(null, data, entityType, false);
	}

	/**
	 * Creates and sends a "update operation" object to the server.
	 * 
	 * @param tool
	 * @param data
	 *            data to use when updating
	 * @param entityType
	 *            the type of entity to update
	 * @return whether the update was successfull or not
	 */
	public boolean update(Tool tool, String[] data, int entityType) {
		return update(tool, data, entityType, false);
	}

	/**
	 * Creates and sends a "update operation" object to the server.
	 * 
	 * @param tool
	 *            frame to display popup relative to
	 * @param data
	 *            data to use when updating
	 * @param entityType
	 *            the type of entity to update
	 */
	public boolean update(Tool tool, String[] data, int entityType, boolean isSilent) {

		if (!checkData(data))
			return false;

		String updates = "";

		// Create object to use in search for old data in database.
		Object[] oldData = new Object[data.length];
		oldData[0] = data[0]; // Set customerId;

		// Search for old data in database.
		ArrayList<Entity> oldResponse = search(oldData, entityType);
		oldData = oldResponse.get(0).getData();

		// Compare and note differences.
		for (int i = 0; i < data.length; i++) {

			if (oldData[i] instanceof Integer) {
				oldData[i] = Integer.toString((int) oldData[i]);
			}

			if (!oldData[i].equals(data[i])) {

				if (oldData[i].toString().trim().length() <= 0) {
					oldData[i] = "Empty";
				}

				updates += oldData[i] + " -> " + data[i] + "\n";
			}
		}

		// If there are differences, display confirm dialog.
		if (updates.trim().length() > 0) {
			int reply = JOptionPane.OK_OPTION;
			if (!isSilent) {
				reply = JOptionPane.showConfirmDialog(tool,
						"Please review the following changes before proceeding:\n" + updates, "Update?",
						JOptionPane.OK_CANCEL_OPTION);
			}

			if (reply == JOptionPane.OK_OPTION) {

				Entity object = createEntityByType(entityType);
				object.setData(data);
				object.setOperation(Eviro.DB_UPDATE);

				ArrayList<Entity> response = (ArrayList<Entity>) client.sendObject(object);

				if (response.size() > 0) {
					return true;
				}
			} else
				return false;
		}
		return false;
	}

	/**
	 * Creates and sends a "create operation" object to the server.
	 * 
	 * @param data
	 *            data to use when creating
	 * @param entityType
	 *            the type of entity to create
	 */
	public void create(Object[] data, int entityType) {
		create(data, entityType, false, false);
	}

	/**
	 * Creates and sends a "create operation" object to the server.
	 * 
	 * @param data
	 *            data to use when creating
	 * @param entityType
	 *            the type of entity to create
	 * @param returnData
	 *            whether to return data or not
	 * @param allowDuplicates
	 *            whether to allow duplicates in the db or not
	 * @return the created resource
	 */
	public ArrayList<Entity> create(Object[] data, int entityType, boolean returnData, boolean allowDuplicates) {

		ArrayList<Entity> response = new ArrayList<Entity>();

		if (!checkData(data))
			return response;

		if (!allowDuplicates)
			response = search(data, entityType);

		if (response.isEmpty()) {
			Entity object = createEntityByType(entityType);
			object.setData(data);

			if (object instanceof Comment)
				object.setOperation(Eviro.DB_ADD_COMMENT);
			else
				object.setOperation(Eviro.DB_ADD);

			response = (ArrayList<Entity>) client.sendObject(object);
		}
		if (returnData)
			return response;

		return null;
	}

	/**
	 * Creates and sends a "search operation" object to the server and then
	 * waits for response.
	 * 
	 * @param data
	 *            data to use when searching
	 * @param entityType
	 *            the type of entity to search for
	 * @return the search result from the server
	 */
	public ArrayList<Entity> search(Object[] data, int entityType) {

		ArrayList<Entity> response = new ArrayList<Entity>();

		if (!checkData(data))
			return response;

		// Create entity, type by entityType
		Entity object = createEntityByType(entityType);//
		// Populate
		object.setData(data);

		if (object instanceof Comment)
			object.setOperation(Eviro.DB_SEARCH_COMMENT);
		else
			object.setOperation(Eviro.DB_SEARCH);

		// Get and return response
		response = (ArrayList<Entity>) client.sendObject(object);
		return response;

	}

	/**
	 * Creates and sends a "get all" object to the server and then waits for
	 * response.
	 * 
	 * @param entityType
	 *            the type of entity to get
	 * @return the result returned from the server
	 */
	public ArrayList<Entity> getAllbyType(int entityType) {

		ArrayList<Entity> response = new ArrayList<Entity>();

		Entity object = createEntityByType(entityType);//

		object.setOperation(Eviro.DB_GETALL);

		response = (ArrayList<Entity>) client.sendObject(object);
		return response;

	}

	/**
	 * Checks an array of objects so that 1. it contains atleast 1 object that
	 * is not null and 2. it contains atleast 1 string with a trimmed lenght of
	 * more than 0.
	 * 
	 * @param data
	 *            the arrays of strings to check
	 * @return whether the controll was successful or not
	 */
	private boolean checkData(Object[] data) {

		escape(data);

		for (Object s : data) {

			if (s != null && ((String) s).trim().length() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Escapes potentionally dangerous chars from data.
	 * 
	 * @param data
	 *            the arrays of strings to check
	 */
	private void escape(Object[] data) {

		for (int i = 0; i < data.length; i++) {

			if (data[i] != null && ((String) data[i]).trim().length() > 0) {
				data[i] = ((String) data[i]).replace("'", "''");
				data[i] = ((String) data[i]).replace("*", "%");
			}

		}

	}

	/**
	 * Instantiates and returns an empty Entity implementation of the specified
	 * entityType.
	 * 
	 * @param entityType
	 *            the type of entity to instantiate
	 * @return the entity that was instantiated
	 */
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

		else if (entityType == Eviro.ENTITY_FORUMMESSAGE) {
			return new ForumMessage();
		}

		else if (entityType == Eviro.ENTITY_USER) {
			return new User();
		}

		else if (entityType == Eviro.ENTITY_COMMENT) {
			return new Comment();
		}

		return null;

	}

	/**
	 * Method for controll of user passwords that are stored encrypted in the
	 * database.
	 * 
	 * @param user
	 *            The user that are trying to login
	 * @param pass
	 *            The password that the user has given as input
	 * @return Boolean if the password was true or false.
	 */
	public boolean checkPassword(String user, String pass) {

		ArrayList<Entity> userList = search(new Object[] { "", user, "" }, Eviro.ENTITY_USER);
		if (userList.isEmpty()) {
			return false;
		} else if (passCryptor.checkPassword(pass, (String) userList.get(0).getData()[2])) {
			if (activeUser == null) {
				setActiveUser(userList.get(0).getData());
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Method for finding properties from the clientConfig.dat file
	 * 
	 * @param property
	 *            Name of the property
	 * @return value of the property, in most cases an encrypted value.
	 */
	public String getProperty(String property) {

		return properties.getProperty(property);
	}

	/**
	 * Method to change properties that are stored in the clientConfig.dat file.
	 * 
	 * @param property
	 *            Name of the property
	 * @param value
	 *            New value to be stored
	 */
	public void setProperty(String property, String value) {

		properties.setProperty(property, value);
		try {
			properties.store(new FileWriter("clientConfig"), "Changed: " + property + " (old = " + value + ")");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get Method the password encryptor
	 * 
	 * @return StrongPasswordEncryptor passCryptor
	 */
	public StrongPasswordEncryptor getPassCryptor() {

		return passCryptor;
	}

	/**
	 * Get method for the user that is currently logged in to the system
	 * 
	 * @return User activeUser
	 */
	public synchronized User getActiveUser() {

		return activeUser;
	}

	/**
	 * Set method for the user that is currently logged in to the system
	 * 
	 * @param data
	 */
	public synchronized void setActiveUser(Object[] data) {

		this.activeUser = new User();
		activeUser.setData(data);
	}

	/**
	 * Get method for the Client
	 * 
	 * @return Client client
	 */
	public Client getClient() {
		return this.client;
	}

}
