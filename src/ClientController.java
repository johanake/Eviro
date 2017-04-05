import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * Handles all major client side logical operations of the system.
 * @author Robin Overgaard
 * @version 0.1
 */

public class ClientController {
	
	private JDesktopPane desktop;

	/**
	 * Constructs the client, instantiates a new main workspace window.
	 */
	public ClientController() {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setSystemLookAndFeel();
				JFrame window = new JFrame(Main.APP_NAME + " " + Main.APP_VERSION);
				JPanel pnlMain = new JPanel(new BorderLayout());
				desktop = new JDesktopPane();
				
				window.setContentPane(pnlMain);
				window.add(desktop, BorderLayout.CENTER);
				window.add(new Sidebar(), BorderLayout.WEST);
				window.setExtendedState(JFrame.MAXIMIZED_BOTH);
				window.setMinimumSize(new Dimension(800, 600));
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.setVisible(true);
				window.setJMenuBar(new Menu()); 
				window.setIconImage(new ImageIcon(Main.APP_ICON).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
			}
		});

	}
	
	/**
	 * Sets the overall look and field for the system.
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
	
	/**
	 * Customization of JButton that takes it's ActionCommand as a parameter in the constructor.
	 * @author Robin Overgaard
	 * @version 1.0
	 */
	private class ActionJButton extends JButton {
		
		/**
		 * Constructor.
		 * @param text the text to display on this button
		 */
		public ActionJButton(String text) {
			super(text);
		}
		
		/**
		 * Constructor.
		 * @param text the text to display on this button
		 * @param action the action command for this button
		 */
		public ActionJButton(String text, String action) {
			this(text);
			this.setActionCommand(action);
		}
		
	}
	
	/**
	 * The sidebar.
	 * @author Robin Overgaard
	 * @version 1.0
	 */
	private class Sidebar extends JPanel implements ActionListener {
		
		private JPanel pnlSideNorth = new JPanel();
		private JPanel pnlSideSouth = new JPanel();
		
		private JComponent exampleTools[] = new JComponent[] { 
				new ActionJButton("Customers"), 
				new ActionJButton("Products"),
				new ActionJButton("Invoice"), 
				new ActionJButton("Transactions") };
		
		private JComponent exampleTools2[] = new JComponent[] { 
				new ActionJButton("Products"),
				new ActionJButton("Adjust"), 
				new ActionJButton("Stocktake") };
		
		private JComponent exampleTools3[] = new JComponent[] { 
				new ActionJButton("Users"),
				new ActionJButton("Database"),
				new ActionJButton("Settings") };

		private JComponent exampleInfo[] = new JComponent[] { 
			
				new JLabel(Main.APP_NAME, JLabel.CENTER), 
				new JLabel(Main.APP_VERSION, JLabel.CENTER) };
		
//				new JLabel(new ImageIcon (new ImageIcon(ClientController.class.getResource(Main.APP_ICON)).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH))),
		
		private JComponent exampleShortcuts[] = new JComponent[] { 
				
				new ActionJButton("Log out"), 
				new ActionJButton("Shutdown") };
		
		public Sidebar() {
			
			setLayout(new BorderLayout());
			setBorder(new EmptyBorder(10,10,10,10));
			
			pnlSideNorth.setLayout(new BoxLayout(pnlSideNorth, BoxLayout.Y_AXIS));
			pnlSideSouth.setLayout(new BoxLayout(pnlSideSouth, BoxLayout.Y_AXIS));
			
			pnlSideNorth.add(createComponentPanel(exampleTools, "Tools"));
			pnlSideNorth.add(createComponentPanel(exampleTools2, "Storage"));
			pnlSideNorth.add(createComponentPanel(exampleTools3, "Admin Tools"));
			pnlSideSouth.add(createComponentPanel(exampleInfo, "Information"));
			pnlSideSouth.add(createComponentPanel(exampleShortcuts, "Shortcuts"));
			
			add(pnlSideNorth, BorderLayout.NORTH);
			add(pnlSideSouth, BorderLayout.SOUTH);
		}
		
		private JPanel createComponentPanel(JComponent[] objects, String title) {
			
			JPanel pnl = new JPanel();
			
			pnl.setLayout(new GridLayout(objects.length, 1));
			pnl.setBorder(new TitledBorder(title));
			
			for (JComponent b : objects) {
				pnl.add(b);
				
				if (b instanceof JButton) {
					((JButton) b).addActionListener(this);
				}
			}
			
			return pnl;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			System.out.println("You clicked on: " + e.getActionCommand());
			
			switch (e.getActionCommand()) {
			
			case "Customers":
				desktop.add(new Toolbox("Create Costumer", new SearchCustomerGUI(), false));
				break;
			
			case "Invoice":
				desktop.add(new Toolbox("Invoice", new InvoiceGUI(), true));
				break;
				
			default:
				JOptionPane.showMessageDialog(null, "You clicked on: " + e.getActionCommand() + "!");
				break;
			}
			
		}
		
	}

}