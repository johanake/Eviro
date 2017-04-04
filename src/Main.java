import server.Server;

/**
 * Contains constants and code to setup and instantiate the main components of the system.
 * 
 * @author Robin Overgaard
 * @version 0.1
 */
public class Main {

	// Constants
	public static final String APP_NAME = "Eviro Enterprise System";
	public static final String APP_VERSION = "v0.1";

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

	}

	public static void main(String[] args) {

		Main app = new Main();
		app.setup();
		app.start();

	}

}
