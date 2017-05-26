package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultDesktopManager;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import client.ClientController;
import shared.Eviro;
import tools.AdminTool;
import tools.ArticleTool;
import tools.CustomerTool;
import tools.InvoiceTool;
import tools.SocialTool;

/**
 * Handles client side gui operations of the system.
 * @author Robin Overgaard
 * @author Peter Sjögren
 * @author nadiaelhaddaoui
 * @version 1.0
 */

public class GUIController {

	private JDesktopPane desktop;
	private ClientController clientController;

	/**
	 * Constructs the client, instantiates a new main workspace window.
	 * @param clientController controller for communication with the client
	 */
	public GUIController(ClientController clientController) {

		this.clientController = clientController;

		new Thread(new Login(clientController)).start();
		while (clientController.getActiveUser() == null)
			;

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				setSystemLookAndFeel();
				JFrame window = new JFrame(
						Eviro.APP_NAME + " " + Eviro.APP_VERSION + " " + clientController.getActiveUser().getData()[1]);
				// window.addKeyListener(keyListener);
				// window.setFocusable(true);
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
				window.setIconImage(
						new ImageIcon(Eviro.APP_ICON).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
			}
		});

	}

	/**
	 * Returns the instance of the GUIController
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
					UIManager.put("nimbusBase", new Color(51, 140, 80));
					break;
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
	public void add(JInternalFrame tool) {
		desktop.add(tool);
		tool.moveToFront();
	}

	/**
	 * The sidebar.
	 * @author Robin Overgaard
	 * @version 1.0
	 */
	private class Sidebar extends JPanel implements ActionListener {

		private JPanel pnlSideNorth = new JPanel();
		private JPanel pnlSideSouth = new JPanel();

		private JComponent top[] = new JComponent[] {
				new ActionJButton("Invoice", "tool_invoice"),
				new ActionJButton("Customer", "tool_customer"),
				new ActionJButton("Article", "tool_article") };

		private JComponent bottom[] = new JComponent[] {
				new ActionJButton("Admin", "tool_admin"),
				new ActionJButton("Social", "tool_social"),
				// new ActionJButton("Settings", "tool_settings"),
				new ActionJButton("Quit", "link_exit") };

		/**
		 * Creates the sidebar.
		 */
		public Sidebar() {

			setPreferredSize(new Dimension(175, 0));
			setLayout(new BorderLayout());
			setBorder(new EmptyBorder(5, 5, 5, 5));

			pnlSideNorth.setLayout(new BoxLayout(pnlSideNorth, BoxLayout.Y_AXIS));
			pnlSideSouth.setLayout(new BoxLayout(pnlSideSouth, BoxLayout.Y_AXIS));

			pnlSideNorth.add(createComponentPanel(top, "Tools"));
			pnlSideSouth.add(createComponentPanel(bottom, "System"));

			add(pnlSideNorth, BorderLayout.NORTH);
			add(pnlSideSouth, BorderLayout.SOUTH);
		}

		/**
		 * Creates a styled panel for groups in the sidebar.
		 * @param objects the group of component to build a panel of
		 * @param title the title displayed on top of the panel
		 * @return the created panel
		 */
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

			case "link_exit":
				System.exit(0);
				break;

			case "tool_article":
				desktop.add(new ArticleTool(clientController, getGUIController()));
				break;

			case "tool_customer":
				desktop.add(new CustomerTool(clientController, getGUIController()));
				break;

			case "tool_invoice":
				desktop.add(new InvoiceTool(clientController, getGUIController()));
				break;

			case "tool_social":
				desktop.add(new SocialTool(clientController, getGUIController()));
				break;

			case "tool_admin":
				new PasswordFrame(desktop.getSize());
				break;

			default:
				JOptionPane.showMessageDialog(desktop, "You clicked on: " + e.getActionCommand());
				break;
			}
		}

		/**
		 * Frame for easy and safe password controll of the AdminTool
		 * @author Peter Sjögren
		 */
		private class PasswordFrame extends JFrame implements ActionListener {

			private JPasswordField passField = new JPasswordField();
			private JLabel label = new JLabel("Enter admin password:  ");
			private JPanel fieldPanel = new JPanel(new GridLayout(1, 2));
			private JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
			private JButton loginButton = new JButton("Log In");
			private JButton abortButton = new JButton("Cancel");
			private BorderLayout layout = new BorderLayout();

			/**
			 * Constructor that builds up the graphics
			 * @param dimension Current size of the desktop window.
			 */
			public PasswordFrame(Dimension dimension) {

				setTitle("Admin Sign In");
				setLayout(layout);
				setSize(new Dimension(300, 80));
				layout.setHgap(5);
				setResizable(false);

				fieldPanel.add(label);
				fieldPanel.add(passField);

				loginButton.addActionListener(this);
				abortButton.addActionListener(this);

				buttonPanel.add(abortButton);
				buttonPanel.add(loginButton);

				add(fieldPanel, BorderLayout.NORTH);
				add(buttonPanel, BorderLayout.SOUTH);

				setLocation(dimension.width / 2 - this.getSize().width / 2,
						dimension.height / 2 - this.getSize().height / 2);
				setVisible(true);

			}

			/**
			 * case: "Log In" checks if the admin password is correct and starts the AdminTool
			 */
			@Override
			public void actionPerformed(ActionEvent e) {

				switch (e.getActionCommand()) {

				case "Log In":
					if (clientController.checkPassword("admin", new String(passField.getPassword()))) {
						desktop.add(new AdminTool(clientController, getGUIController()));
						dispose();
					} else {
						passField.setText("");
						JOptionPane.showMessageDialog(desktop, "Wrong password, try again", "Admin Sign In",
								JOptionPane.ERROR_MESSAGE);
					}
					break;

				case "Cancel":
					dispose();
					break;
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