import javax.swing.UIManager;

/**
 * Contains constants and code to setup and instantiate the main components of the system. 
 * @author Robin Overgaard
 * @version 0.1
 */
public class Main {
	
	// Constants
	public static final String APP_NAME = "Eviro";
	public static final String APP_VERSION = "v0.1";
	
	/**
	 * Setup system before initialization. 
	 */
	private void setup() {
		
		// Set overall look and field for the system. 
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Initialize the main component(s) of the system. 
	 */
	private void start() {
		
//		new Client();
//		new Server();
		
	}
	
	public static void main(String[] args) {
		
		Main app = new Main();
		
		app.setup();
		app.start();
		
	}
}
