package gui;

import javax.swing.SwingUtilities;

public class Helpers {

	/**
	 * Method that can be used to check whether a block of code is executed by the EDT or not.
	 */
	static void isOnEDT() {

		if (SwingUtilities.isEventDispatchThread()) {
			System.out.println("Is running on EDT");
		} else {
			System.err.println("Is not running on EDT");
		}
	}

}
