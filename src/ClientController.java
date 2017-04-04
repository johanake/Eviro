import java.awt.Dimension;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Handles all major client side logical operations of the system.
 * 
 * @author Robin Overgaard
 * @version 0.1
 */
public class ClientController {

	/**
	 * Constructs the client, instantiates a new main workspace window. 
	 */
	public ClientController() {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setSystemLookAndFeel();
				JFrame window = new JFrame(Main.APP_NAME + " " + Main.APP_VERSION);
				window.setExtendedState(JFrame.MAXIMIZED_BOTH);
				window.setContentPane(new JDesktopPane());
				window.setMinimumSize(new Dimension(800, 600));
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.setVisible(true);
				window.setJMenuBar(new Menu()); // Example, might not be instantiated like this. 
				window.add(new Toolbox("Tool 1", new Tool())); // Example, will not be instantiated like this. 
				window.add(new Toolbox("Tool 2", new Demotool())); // Example, will not be instantiated like this. 
				window.add(new Toolbox("Tool 3", new Tool())); // Example, will not be instantiated like this.
				window.add(new Toolbox("Johan", new SearchCustomerGUI()));
			}
		});

	}

	/**
	 * 	Sets the overall look and field for the system.
	 */
	private void setSystemLookAndFeel() {

		try {
			
			for (LookAndFeelInfo lnfi : UIManager.getInstalledLookAndFeels()) {
				
						
				if ("Nimbus".equals(lnfi.getName())) {
					UIManager.setLookAndFeel(lnfi.getClassName());
					break;
				}
				
			}

		} catch (Exception e) {
			
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
		
	}
	
}