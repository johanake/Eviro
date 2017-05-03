package client;

import java.util.ArrayList;

import enteties.Customer;
import enteties.Entity;
import enteties.Invoice;
import enteties.Product;
import enteties.Transaction;

/**
 * Creates and sends objects to the client.
 * @author Robin Overgaard
 * @author Johan Åkesson
 * @version 1.1
 */
public class ClientController {

	private Client client;

	/**
	 * Creates a ClientController object.
	 * @param client the client of the system
	 */
	public ClientController(Client client) {
		this.client = client;
	}

	/**
	 * Creates and sends a "update operation" object to the server.
	 * @param data data to use when updating
	 * @param entityType the type of entity to update
	 */
	public void update(Object[] data, int entityType) {
		Entity object = createEntityByType(entityType);
		object.setData(data);
		object.setOperation(Eviro.DB_UPDATE);
		client.sendObject(object);
	}

	/**
	 * Creates and sends a "create operation" object to the server.
	 * @param data data to use when creating
	 * @param entityType the type of entity to create
	 */
	public void create(Object[] data, int entityType) {
		create(data, entityType, false);
	}

	/**
	 * Creates and sends a "create operation" object to the server and then waits for response.
	 * @param data data to use when creating
	 * @param entityType the type of entity to create
	 * @param returnId whether the method should return the id of the created database row or not
	 * @return the search result from the server
	 */
	public String create(Object[] data, int entityType, boolean returnId) {
		Entity object = createEntityByType(entityType);
		object.setData(data);
		object.setOperation(Eviro.DB_ADD);
		ArrayList<Entity> response = (ArrayList<Entity>) client.sendObject(object);

		if (returnId)
			return response.get(0).getData()[0].toString();

		return null;
	}

	/**
	 * Creates and sends a "search operation" object to the server and then waits for response.
	 * @param data data to use when searching
	 * @param entityType the type of entity to search for
	 * @return the search result from the server
	 */
	public ArrayList<Entity> search(Object[] data, int entityType) {

		// Create entity, type by entityType
		Entity object = createEntityByType(entityType);

		// Populate
		object.setData(data);
		object.setOperation(Eviro.DB_SEARCH);

		// Get and return response
		ArrayList<Entity> response = null;
		response = (ArrayList<Entity>) client.sendObject(object);
		return response;

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

		return null;

	}

}
