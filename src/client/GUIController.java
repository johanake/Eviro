package client;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

import tools.CreateCustomer;
import tools.InvoiceGUI;
import tools.SearchCustomerGUI;

/**
 * Handles client side gui operations of the system.
 * @author Robin Overgaard
 * @version 0.1
 */

public class GUIController {
	
	private JDesktopPane desktop;
	private ClientController clientController;

	/**
	 * Constructs the client, instantiates a new main workspace window.
	 * @param clientController controller for communication with client
	 */
	public GUIController(ClientController clientController) {
		
		this.clientController = clientController;
		
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
	 * The sidebar.
	 * @author Robin Overgaard
	 * @version 1.0
	 */
	private class Sidebar extends JPanel implements ActionListener {
		
		private ArrayList<Toolbox> activeTools = new ArrayList<Toolbox>();
		private JPanel pnlSideNorth = new JPanel();
		private JPanel pnlSideSouth = new JPanel();
		
		private JComponent quick[] = new JComponent[] { 
				new ActionJButton("Customer", "find_cust"), 
				new ActionJButton("Invoice", "find_inv"),
				new ActionJButton("Transaction", "find_trans"), 
				new ActionJButton("Article", "find_art") };

		private JComponent tools[] = new JComponent[] { 
				new ActionJButton("Customer", "tool_customer"), 
				new ActionJButton("Invoice", "tool_invoice") };

		private JComponent exampleInfo[] = new JComponent[] { 
			
				new JLabel(Main.APP_NAME, JLabel.CENTER), 
				new JLabel(Main.APP_VERSION, JLabel.CENTER) };
		
//				new JLabel(new ImageIcon (new ImageIcon(ClientController.class.getResource(Main.APP_ICON)).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH))),
		
		private JComponent exampleShortcuts[] = new JComponent[] { 
				
				new ActionJButton("Settings", "tool_settings"), 
				new ActionJButton("Quit", "link_exit") };
		
		public Sidebar() {
		
			setPreferredSize(new Dimension(175,0));
			setLayout(new BorderLayout());
			setBorder(new EmptyBorder(10,10,10,10));
			
			pnlSideNorth.setLayout(new BoxLayout(pnlSideNorth, BoxLayout.Y_AXIS));
			pnlSideSouth.setLayout(new BoxLayout(pnlSideSouth, BoxLayout.Y_AXIS));
			
			pnlSideNorth.add(createComponentPanel(tools, "Tools"));
			pnlSideNorth.add(createComponentPanel(quick, "Find"));
			
//			pnlSideSouth.add(createComponentPanel(exampleInfo, "Information"));
			pnlSideSouth.add(createComponentPanel(exampleShortcuts, "System"));
			
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

			switch (e.getActionCommand()) {

			case "tool_customer":
				desktop.add(new Toolbox(new CreateCustomer(clientController)));
//				desktop.add(new Toolbox(new SearchCustomerGUI(clientController)));
				break;

			case "tool_invoice":
				desktop.add(new Toolbox(new InvoiceGUI(clientController)));
				break;

			case "link_exit":
				System.exit(0);
				break;

			default:
				JOptionPane.showMessageDialog(desktop, "You clicked on: " + e.getActionCommand());
				break;
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
	}
}