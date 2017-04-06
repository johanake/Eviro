package client;
import javax.swing.SwingUtilities;

public class Helpers {

	static void isOnEDT() {
	
	if (SwingUtilities.isEventDispatchThread()) {
	    System.out.println("Is running on EDT");
	} else {
	    System.err.println("Is not running on EDT");
	}
	}
	
}
