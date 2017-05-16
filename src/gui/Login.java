package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class Login extends JFrame implements ActionListener, Runnable {
	private ClientController clientController;

	private JLabel userLabel = new JLabel("Username");
	private JLabel passLabel = new JLabel("Password");
	private JTextField userField = new JTextField("peter");
	private JTextField passField = new JPasswordField("test");
	private JPanel fieldPanel = new JPanel(new GridLayout(2, 2));

	private JButton loginButton = new JButton("Login");
	private JButton adminButton = new JButton("Admin");
	private JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

	private Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
	private BorderLayout layout = new BorderLayout();

	public Login(ClientController clientController) {
		this.clientController = clientController;
	}

	@Override
	public void run() {
		setTitle("Eviro Enterprise System - Sign In");
		setSize(new Dimension(300, 150));
		setResizable(false);
		layout.setHgap(5);
		setLayout(layout);

		fieldPanel.add(userLabel);
		fieldPanel.add(userField);
		fieldPanel.add(passLabel);
		fieldPanel.add(passField);

		buttonPanel.add(adminButton);
		buttonPanel.add(loginButton);
		add(fieldPanel, BorderLayout.NORTH);
		add(buttonPanel, BorderLayout.SOUTH);

		loginButton.addActionListener(this);
		adminButton.addActionListener(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(screenDim.width / 2 - this.getSize().width / 2, screenDim.height / 2 - this.getSize().height / 2);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {

		case "Login":


			
			if (!clientController.getClient().connectToServer()) {
				JOptionPane.showMessageDialog(this, "Unable to connect to server!\nPlease try again or contact admin.");
			}

			else {
				if (clientController.checkPassword(userField.getText(), passField.getText())) {
					this.dispose();
				} else {
					userField.setText("");
					passField.setText("");
					JOptionPane.showMessageDialog(null, "Wrong username or password, please try again", "Login Failed",
							JOptionPane.ERROR_MESSAGE);
				}
			}

			break;

		case "Admin":
			String pass = JOptionPane.showInputDialog(null, "Enter admin password", "Admin Sign In",
					JOptionPane.DEFAULT_OPTION);
			if (clientController.getPassCryptor().checkPassword(pass, clientController.getProperty("admin"))) {
				new AdminLogin();
			}

			break;
		}
	}

	private class AdminLogin extends JFrame implements ActionListener {
		private JLabel ipLabel = new JLabel("Server IP");
		private JLabel portLabel = new JLabel("Server Port");
		private JTextField ipField = new JTextField();
		private JTextField portField = new JTextField();
		private JPanel fieldPanel = new JPanel(new GridLayout(2, 2));

		private JButton editButton = new JButton("Edit");
		private JButton saveButton = new JButton("Save");
		private JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

		private BorderLayout layout = new BorderLayout();

		public AdminLogin() {
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

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(e.getActionCommand());
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

	public void main(String[] args) {
		new Login(null);
	}
}
