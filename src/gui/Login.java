package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jasypt.util.password.StrongPasswordEncryptor;

import client.ClientController;
import server.ServerController;
import shared.Eviro;
import tools.AdminTool;

/**
 * 
 * GUI class for the Login window with password controll that starts the client.
 * Also gives an admin the possibility to change the server ip and port that are
 * stored in the clientConfig.dat file.
 * 
 * @author Peter Sjögren
 *
 */
public class Login extends JFrame implements ActionListener, Runnable {

	private ClientController clientController;
	private JLabel userLabel = new JLabel("Username");
	private JLabel passLabel = new JLabel("Password");
	private JPanel labelPanel = new JPanel(new GridLayout(2, 1));
	private JTextField userField = new JTextField();
	private JTextField passField = new JPasswordField();
	private JPanel fieldPanel = new JPanel(new GridLayout(2, 1));
	private ImageIcon loginIcon = new ImageIcon(new ImageIcon("images/transparent_green_logo.png").getImage()
			.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
	private JLabel iconLabel = new JLabel();
	private JPanel upperpanel = new JPanel(new GridLayout(1, 3));
	private JButton loginButton = new JButton("Login");
	private JButton adminButton = new JButton("Admin");
	private JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
	private Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
	private BorderLayout layout = new BorderLayout();

	/**
	 * Simple constructor.
	 * 
	 * @param clientController
	 *            ClientController gives access to the checkPassword method
	 *            as well as connectToServer method.
	 */
	public Login(ClientController clientController) {
		this.clientController = clientController;
	}

	/**
	 * Run-method that builds the graphic components, show the Login-window and
	 * starts listening for input.
	 */
	@Override
	public void run() {

		setTitle("Eviro Enterprise System - Sign In");
		setSize(new Dimension(300, 130));
		setResizable(false);
		layout.setHgap(5);
		setLayout(layout);

		labelPanel.add(userLabel);
		labelPanel.add(userField);

		fieldPanel.add(passLabel);
		fieldPanel.add(passField);

		iconLabel.setIcon(loginIcon);

		upperpanel.add(iconLabel);
		upperpanel.add(labelPanel);
		upperpanel.add(fieldPanel);

		buttonPanel.add(adminButton);
		buttonPanel.add(loginButton);

		add(upperpanel, BorderLayout.NORTH);
		add(buttonPanel, BorderLayout.SOUTH);

		loginButton.addActionListener(this);
		adminButton.addActionListener(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(screenDim.width / 2 - this.getSize().width / 2, screenDim.height / 2 - this.getSize().height / 2);
		setVisible(true);
	}

	/**
	 * case "Login" checks the given password and connects to the server if it is correct.
	 * case "Admin" starts the password controll for the AdminFrame
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {

		case "Login":

			if (!clientController.getClient().connectToServer()) {
				JOptionPane.showMessageDialog(this, "Unable to connect to server!\nPlease try again or contact admin.");
			} else {
				if (clientController.checkPassword(userField.getText(), passField.getText())) {
					this.dispose();
				} else {
					userField.setText("");
					passField.setText("");
					JOptionPane.showMessageDialog(null, "Wrong username or password, please try again", "Login Failed",
							JOptionPane.ERROR_MESSAGE);
					clientController.getClient().disconnect();
				}
			}
			break;

		case "Admin":
			new PasswordFrame(screenDim);
			break;
		}
	}

	/**
	 * Password Frame for safe input and handling of server password
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
		 * Constructor that builds the graphic in the frame
		 * @param serverController ServerController for acces to login()-method.
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
		 * case: "Log In" checks the admin password and starts the AdminFrame if it is correct.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			switch (e.getActionCommand()) {

			case "Log In":
				if (clientController.getPassCryptor().checkPassword(new String(passField.getPassword()),
						clientController.getProperty("admin"))) {
					new AdminFrame();
					dispose();
				} else {
					passField.setText("");
					JOptionPane.showMessageDialog(null, "Wrong password, try again", "Admin Sign In",
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
	 * GUI class for changing the server and port that are stored in the clientConfig.dat file.
	 * @author Peter Sjögren
	 */
	private class AdminFrame extends JFrame implements ActionListener {

		private JLabel ipLabel = new JLabel("Server IP");
		private JLabel portLabel = new JLabel("Server Port");
		private JTextField ipField = new JTextField();
		private JTextField portField = new JTextField();
		private JPanel fieldPanel = new JPanel(new GridLayout(2, 2));
		private JButton editButton = new JButton("Edit");
		private JButton saveButton = new JButton("Save");
		private JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
		private BorderLayout layout = new BorderLayout();

		/**
		 * Constructor that builds up the graphics and starts listening for input.
		 */
		public AdminFrame() {

			setTitle("Eviro Enterprise System - Admin");
			setSize(new Dimension(300, 150));
			setResizable(false);
			layout.setHgap(5);
			setLayout(layout);

			ipField.setText(clientController.getProperty("ip"));
			ipField.setEditable(false);
			portField.setText(clientController.getProperty("port"));
			portField.setEditable(false);

			fieldPanel.add(ipLabel);
			fieldPanel.add(ipField);
			fieldPanel.add(portLabel);
			fieldPanel.add(portField);

			editButton.setEnabled(true);
			saveButton.setEnabled(false);
			buttonPanel.add(editButton);
			buttonPanel.add(saveButton);
			add(fieldPanel, BorderLayout.NORTH);
			add(buttonPanel, BorderLayout.SOUTH);

			editButton.addActionListener(this);
			saveButton.addActionListener(this);

			setLocation(screenDim.width / 2 - this.getSize().width / 2,
					screenDim.height / 2 - this.getSize().height / 2);
			setVisible(true);
		}

		/**
		 * case: "Save" stores the current information in the fields to the clientConfig.dat file.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			switch (e.getActionCommand()) {
			case "Edit":
				ipField.setEditable(true);
				portField.setEditable(true);
				editButton.setEnabled(false);
				saveButton.setEnabled(true);
				break;

			case "Save":
				clientController.setProperty("ip", ipField.getText());
				clientController.setProperty("port", portField.getText());
				ipField.setEditable(false);
				portField.setEditable(false);
				editButton.setEnabled(true);
				saveButton.setEnabled(false);
				break;
			}
		}
	}
}
