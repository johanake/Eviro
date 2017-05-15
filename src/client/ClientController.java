package client;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.jasypt.util.password.StrongPasswordEncryptor;

import enteties.Customer;
import enteties.Entity;
import enteties.ForumMessage;
import enteties.Invoice;
import enteties.Product;
import enteties.Transaction;
import enteties.User;
import gui.Tool;
import shared.Eviro;

/**
 * Creates and sends objects to the client.
 * @author Robin Overgaard
 * @author Johan Åkesson
 * @version 1.1
 */
public class ClientController {
	private StrongPasswordEncryptor passCryptor = new StrongPasswordEncryptor();
	private boolean online = true;
	private Client client;

	/**
	 * Creates a ClientController object.
	 * @param client the client of the system
	 */
	public ClientController(Client client) {
		this.client = client;
	}

	public boolean checkPassword(String user, String pass) {
		ArrayList<Entity> userList = search(new Object[] { "", user, "" }, Eviro.ENTITY_USER);
		if (userList.isEmpty()) {
			return false;
		} else if (passCryptor.checkPassword(pass, (String) userList.get(0).getData()[2])) {
			if (isOnline() == false) {
				setOnline(true);
			}
			return true;
		} else {
			return false;
		}
	}

	public StrongPasswordEncryptor getPassCryptor() {
		return passCryptor;
	}

	/**
	 * Creates and sends a "update operation" object to the server.
	 * @param data data to use when updating
	 * @param entityType the type of entity to update
	 */
	public boolean update(String[] data, int entityType) {
		return update(null, data, entityType);
	}

	/**
	 * Creates and sends a "update operation" object to the server.
	 * @param tool frame to display popup relative to
	 * @param data data to use when updating
	 * @param entityType the type of entity to update
	 */
	public boolean update(Tool tool, String[] data, int entityType) {

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

			int reply = JOptionPane.showConfirmDialog(tool, "Please review the following changes before proceeding:\n" + updates, "Update?",
					JOptionPane.OK_CANCEL_OPTION);

			if (reply == JOptionPane.OK_OPTION) {

				Entity object = createEntityByType(entityType);
				object.setData(data);
				object.setOperation(Eviro.DB_UPDATE);

				ArrayList<Entity> response = (ArrayList<Entity>) client.sendObject(object);

				if (response.size() > 0) {
					return true;
				}

			} else {
				return false;
			}

		}
		return false;
	}

	/**
	 * Creates and sends a "create operation" object to the server.
	 * @param data data to use when creating
	 * @param entityType the type of entity to create
	 */
	public void create(Object[] data, int entityType) {

		create(data, entityType, false);
	}

	public ArrayList<Entity> create(Object[] data, int entityType, boolean returnData) {

		ArrayList<Entity> response = new ArrayList<Entity>();

		if (!checkData(data))
			return response;

		response = search(data, entityType);
		if (response.isEmpty()) {
			Entity object = createEntityByType(entityType);
			object.setData(data);
			object.setOperation(Eviro.DB_ADD);
			response = (ArrayList<Entity>) client.sendObject(object);
		}
		if (returnData)
			return response;

		return null;
	}

	/**
	 * Creates and sends a "search operation" object to the server and then waits for response.
	 * @param data data to use when searching
	 * @param entityType the type of entity to search for
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
		object.setOperation(Eviro.DB_SEARCH);

		// Get and return response
		response = (ArrayList<Entity>) client.sendObject(object);
		return response;

	}

	/**
	 * Creates and sends a "get all" object to the server and then waits for response.
	 * @param entityType the type of entity to get
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
	 * Checks an array of objects so that 1. it contains atleast 1 object that is not null and 2. it contains atleast 1 string with a trimmed lenght of more than 0.
	 * @param data the arrays of strings to check
	 * @return whether the controll was successful or not
	 */
	private boolean checkData(Object[] data) {

		for (Object s : data) {

			if (s != null && ((String) s).trim().length() > 0) {
				return true;
			}

		}

		return false;
	}

	/**
	 * Instantiates and returns an empty Entity implementation of the specified entityType.
	 * @param entityType the type of entity to instantiate
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

		return null;

	}

	public ArrayList<Entity> getAll(int entityType) {

		ArrayList<Entity> response = new ArrayList<Entity>();

		Entity object = createEntityByType(entityType);//

		object.setOperation(Eviro.DB_GETALL);

		response = (ArrayList<Entity>) client.sendObject(object);
		return response;

	}

	public synchronized boolean isOnline() {
		return online;
	}

	public synchronized void setOnline(boolean online) {
		this.online = online;
	}

	// /**
	// * Adds a new forum message to the database.
	// * @param res the message to add.
	// */
	// public void addForumMessage(ForumMessage msg) {
	// msg.setOperation(Eviro.DB_ADD);
	// client.sendObject(msg);
	// }

}
