import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
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
 * 
 * @author Robin Overgaard
 * @version 0.1
 */

public class ClientController {

	/**
	 * Constructs the client, instantiates a new main workspace window.
	 * @return 
	 */
	public ClientController() {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setSystemLookAndFeel();
				JFrame window = new JFrame(Main.APP_NAME + " " + Main.APP_VERSION);
				JPanel pnlMain = new JPanel(new BorderLayout());
				JDesktopPane desktop = new JDesktopPane();
				pnlMain.add(desktop, BorderLayout.CENTER);
				pnlMain.add(new Sidebar(), BorderLayout.WEST);
				window.setContentPane(pnlMain);
				window.setExtendedState(JFrame.MAXIMIZED_BOTH);
				window.setMinimumSize(new Dimension(800, 600));
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.setVisible(true);

				window.setJMenuBar(new Menu()); 
//				desktop.add(new Toolbox("Tool 1", new Tool())); 
				desktop.add(new Toolbox("Tool 2", new Demotool(), false)); 
//				desktop.add(new Toolbox("Tool 3", new Tool())); 
				desktop.add(new Toolbox("Create Costumer", new SearchCustomerGUI(), true));


				

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
	 * The sidebar.
	 * 
	 * @author Robin Overgaard
	 * @version 1.0
	 */
	private class Sidebar extends JPanel implements ActionListener{
		
		private JPanel pnlSideNorth = new JPanel();
		private JPanel pnlSideSouth = new JPanel();
		
		/////////////////////////////////////////////////////////////////
		///////////////////////////EXAMPLES//////////////////////////////
		/////////////////////////////////////////////////////////////////
		
		private JComponent exampleTools[] = new JComponent[] { 
				new JButton("Customers"), 
				new JButton("Products"),
				new JButton("Invoice"), 
				new JButton("Transactions") };
		
		private JComponent exampleTools2[] = new JComponent[] { 
				new JButton("Products"),
				new JButton("Adjust"), 
				new JButton("Stocktake") };
		
		private JComponent exampleTools3[] = new JComponent[] { 
				new JButton("Users"),
				new JButton("Database"),
				new JButton("Settings") };

		private JComponent exampleInfo[] = new JComponent[] { 
				new JLabel(Main.APP_NAME, JLabel.CENTER), 
				new JLabel(Main.APP_VERSION, JLabel.CENTER) };
		
		private JComponent exampleShortcuts[] = new JComponent[] { 
				new JButton("Log out"), 
				new JButton("Shutdown") };
		
		/////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////
		
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
			
			JPanel pnlToolGroup = new JPanel();
			
			pnlToolGroup.setLayout(new GridLayout(objects.length, 1));
			pnlToolGroup.setBorder(new TitledBorder(title));
			
			for (JComponent b : objects) {
				pnlToolGroup.add(b);
				
				if (b instanceof JButton) {
					((JButton) b).addActionListener(this);
					((JButton) b).setActionCommand(((JButton) b).getText());
				}
			}
			
			return pnlToolGroup;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			JOptionPane.showMessageDialog(null, "You clicked on: " + e.getActionCommand() + "!");
			
			
			
		}
		
	}

}