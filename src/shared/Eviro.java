package shared;

import javax.swing.SwingUtilities;

import client.Client;
import server.Server;

/**
 * Contains constants and code to setup and start the system.
 * @author Robin Overgaard
 * @version 1.0
 */
public class Eviro {

	// Constants
	public static final String APP_NAME = "Eviro Enterprise System";
	public static final String APP_VERSION = "v0.1";
	public static final String APP_ICON = "images/eviro_icon.png";

	public static final int DB_ADD = 1;
	public static final int DB_SEARCH = 2;
	public static final int DB_UPDATE = 3;
	public static final int DB_DELETE = 4;
	public static final int DB_GETALL = 5;

	public static final int ENTITY_CUSTOMER = 1;
	public static final int ENTITY_INVOICE = 2;
	public static final int ENTITY_PRODUCT = 3;
	public static final int ENTITY_TRANSACTION = 4;
	public static final int ENTITY_FORUMMESSAGE = 5;
	public static final int ENTITY_USER = 6;

	public static final int VALIDATOR_INTEGER = 1;
	public static final int VALIDATOR_DOUBLE = 2;

	/**
	 * Setup system before instantiation.
	 */
	private void setup() {

	}

	/**
	 * Instantiate the main component(s) of the system.
	 */
	private void start() {
		
		new Server(3500);
		new Client("127.0.0.1", 3500);
<<<<<<< HEAD
		new Client("127.0.0.1", 3500);
		//Jag vill ha det på detta VISET!!!!!!!!!!!!!!!!
=======
>>>>>>> origin/master

	}

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

	public static void main(String[] args) {

		Eviro app = new Eviro();
		app.setup();
		app.start();

	}

	static void isOnEDT() {

		if (SwingUtilities.isEventDispatchThread()) {
			System.out.println("Is running on EDT");
		} else {
			System.err.println("Is not running on EDT");
		}
	}

}
