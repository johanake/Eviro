package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultDesktopManager;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import client.ClientController;
import client.Eviro;
import tools.CreateCustomer;
import tools.CustomerGUI;
import tools.ForumGUI;
import tools.ProductGUI;
import tools.temp_article;
import tools.temp_customer;

/**
 * Handles client side gui operations of the system.
 * @author Robin Overgaard
 * @version 1.0
 */

// Nadia testar

public class GUIController {
	private KeyPress keyListener = new KeyPress();
	private JDesktopPane desktop;
	private ClientController clientController;

	/**
	 * Constructs the client, instantiates a new main workspace window.
	 * @param clientController controller for communication with the client
	 */
	public GUIController(ClientController clientController) {

		this.clientController = clientController;

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				setSystemLookAndFeel();
				JFrame window = new JFrame(Eviro.APP_NAME + " " + Eviro.APP_VERSION);
				window.addKeyListener(keyListener);
				window.setFocusable(true);
				JPanel pnlMain = new JPanel(new BorderLayout());
				desktop = new JDesktopPane();

				desktop.setDesktopManager(new DefaultDesktopManager() {
					@Override
					protected Rectangle getBoundsForIconOf(JInternalFrame f) {
						Rectangle r = super.getBoundsForIconOf(f);
						r.width = 200;
						return r;
					}
				});

				window.setContentPane(pnlMain);
				window.add(desktop, BorderLayout.CENTER);
				window.add(new Sidebar(), BorderLayout.WEST);
				window.setExtendedState(JFrame.MAXIMIZED_BOTH);
				window.setMinimumSize(new Dimension(1200, 720));
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.setVisible(true);
				// window.setJMenuBar(new Menu());
				window.setIconImage(new ImageIcon(Eviro.APP_ICON).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
			}
		});

	}

	/**
	 * Returns an instance of the GUIController
	 */
	private GUIController getGUIController() {
		return this;
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
					// UIManager.setLookAndFeel(new NimbusLookAndFeel() {
					//
					// @Override
					// public UIDefaults getDefaults() {
					// UIDefaults ret = super.getDefaults();
					// ret.put("defaultFont", new Font(Font.SANS_SERIF, 0, 11));
					// return ret;
					// }
					//
					// });

				}

				else {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
	 * Adds a new toolbox to the desktop
	 * @param tool the tool to open
	 */
	public void popup(Tool tool) {
		Toolbox toolbox = new Toolbox(tool);
		desktop.add(toolbox);

	}

	/**
	 * Activates listener to a computer keyboard
	 * @author nadiaelhaddaoui
	 */
	private class KeyPress implements KeyListener {
		// Set<Character> pressed = new HashSet<Character>();

		@Override
		public synchronized void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();

			// pressed.add((char) e.getKeyCode());
			switch (e.getKeyCode()) {
			case KeyEvent.VK_F1: {
				popup(new CreateCustomer(clientController));
				System.out.println("1. Du tryckte på F1");
				break;
			}
			case KeyEvent.VK_F2: {
				popup(new CustomerGUI(clientController, getGUIController()));
				System.out.println("2. Du tryckte på F2");
				break;
			}
			case KeyEvent.VK_F3: {
				System.out.println("3. Du tryckte på F3");
				break;
			}
			default: {
				System.out.println("Annan knapp " + KeyEvent.getKeyText(keyCode));
				e.consume();
				break;
			}
			}
		}

		@Override
		public synchronized void keyReleased(KeyEvent e) {
			e.consume();

		}

		@Override
		public void keyTyped(KeyEvent e) {
			e.consume();
		}

	}

	/**
	 * The sidebar.
	 * @author Robin Overgaard
	 * @version 1.0
	 */
	private class Sidebar extends JPanel implements ActionListener {

		// private ArrayList<Toolbox> activeTools = new ArrayList<Toolbox>();
		private JPanel pnlSideNorth = new JPanel();
		private JPanel pnlSideSouth = new JPanel();

		private JComponent top[] = new JComponent[] {
				new ActionJButton("Customer", "tool_cust"),
				new ActionJButton("Customer", "tool_customer"),
				new ActionJButton("Invoice", "tool_inv"),
				new ActionJButton("Article", "tool_art") };

		private JComponent test[] = new JComponent[] { new ActionJButton("Customer", "test_customer"), new ActionJButton("Article", "test_article") };

		private JComponent bottom[] = new JComponent[] {
				new ActionJButton("Forum", "tool_forum"),
				new ActionJButton("Settings", "tool_settings"),
				new ActionJButton("Quit", "link_exit") };

		public Sidebar() {

			setPreferredSize(new Dimension(175, 0));
			setLayout(new BorderLayout());
			setBorder(new EmptyBorder(5, 5, 5, 5));

			pnlSideNorth.setLayout(new BoxLayout(pnlSideNorth, BoxLayout.Y_AXIS));
			pnlSideSouth.setLayout(new BoxLayout(pnlSideSouth, BoxLayout.Y_AXIS));

			pnlSideNorth.add(createComponentPanel(top, "Tools"));
			pnlSideNorth.add(createComponentPanel(test, "Tools"));
			pnlSideSouth.add(createComponentPanel(bottom, "System"));

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

		/*
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event. ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			switch (e.getActionCommand()) {

			case "tool_customer":
				popup(new CreateCustomer(clientController));
				break;

			case "link_exit":
				System.exit(0);
				break;

			case "test_article":
				desktop.add(new temp_article(clientController, getGUIController()));
				break;

			case "test_customer":
				desktop.add(new temp_customer(clientController, getGUIController()));
				break;

			case "tool_cust":
				popup(new CustomerGUI(clientController, getGUIController()));
				break;

			case "tool_art":
				popup(new ProductGUI(clientController, getGUIController()));
				break;

			case "tool_forum":
				popup(new ForumGUI(clientController));
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
				this.setActionCommand(text);
			}

			/**
			 * Constructor.
			 * @param text the text to display on this button
			 * @param action the action command for this button
			 */
			public ActionJButton(String text, String action) {
				super(text);
				this.setActionCommand(action);
			}

		}
	}
}