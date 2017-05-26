package shared;

import javax.swing.SwingUtilities;

import client.ClientController;
import server.Server;

/**
 * Contains constants and code to setup and start the system.
 * @author Robin Overgaard, Peter Sjögren
 * @version 1.0
 */
public class Eviro {

	// Constants
	public static final String APP_NAME = "Eviro Enterprise System";
	public static final String APP_VERSION = "Alpha 1.0";
	public static final String APP_ICON = "images/eviro_icon.png";

	public static final int DB_ADD = 1;
	public static final int DB_ADD_COMMENT = 7;
	public static final int DB_SEARCH = 2;
	public static final int DB_SEARCH_COMMENT = 6;
	public static final int DB_UPDATE = 3;
	public static final int DB_DELETE = 4;
	public static final int DB_GETALL = 5;

	public static final int ENTITY_CUSTOMER = 1;
	public static final int ENTITY_INVOICE = 2;
	public static final int ENTITY_PRODUCT = 3;
	public static final int ENTITY_TRANSACTION = 4;
	public static final int ENTITY_FORUMMESSAGE = 5;
	public static final int ENTITY_USER = 6;
	public static final int ENTITY_COMMENT = 7;

	public static final int VALIDATOR_INTEGER = 1;
	public static final int VALIDATOR_DOUBLE = 2;

	/**
	 * Instantiate the main component(s) of the system.
	 * Waits for the Server to start listening for clients before the client is started.
	 */
	private void start() {

		Server server = new Server();
		
		Thread clientThread = new ClientController();
		
		while ( !server.isOnline() ) {};

		clientThread.start();
		
	}

	/**
	 * Returns the name of a specified entiy type.
	 * @param entityType the specified entity type
	 * @return the name of the specified entity type
	 */
	public static String getEntityNameByNumber(int entityType) {

		switch (entityType) {

		case ENTITY_CUSTOMER:
			return "customer";

		case ENTITY_INVOICE:
			return "invoice";

		case ENTITY_PRODUCT:
			return "article";

		case ENTITY_TRANSACTION:
			return "transaction";

		case ENTITY_FORUMMESSAGE:
			return "post";

		case ENTITY_USER:
			return "user";

		default:
			return "entity";

		}

	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Eviro app = new Eviro();
		app.start();

	}

	/**
	 * Used to test whether a piece of code is executed by the EDt or not
	 */
	static void isOnEDT() {

		if (SwingUtilities.isEventDispatchThread()) {
			System.out.println("Is running on EDT");
		} else {
			System.err.println("Is not running on EDT");
		}
	}

}
