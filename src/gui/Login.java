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
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jasypt.util.password.StrongPasswordEncryptor;

import client.ClientController;
import shared.Eviro;

public class Login extends JFrame implements ActionListener, Runnable {
	private ClientController clientController;

	private JLabel userLabel = new JLabel("Username");
	private JLabel passLabel = new JLabel("Password");
	private JTextField userField = new JTextField("peter");
	private JTextField passField = new JTextField("imreimre");
	private JButton loginButton = new JButton("Login");
	private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

	private StrongPasswordEncryptor passCryptor = new StrongPasswordEncryptor();

	public Login(ClientController clientController) {
		this.clientController = clientController;
	}

	@Override
	public void run() {
		setTitle("Eviro Enterprise System - Sign In");
		setSize(new Dimension(400, 150));
		setResizable(false);
		GridLayout layout = new GridLayout(3, 2);
		layout.setHgap(5);
		setLayout(layout);

		add(userLabel);
		add(userField);
		add(passLabel);
		add(passField);
		add(loginButton);

		loginButton.addActionListener(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {

		case "Login":
			if (clientController.checkPassword(userField.getText(), passField.getText())) {
				this.dispose();
			} else {
				userField.setText("");
				passField.setText("");
				JOptionPane.showMessageDialog(null, "Wrong username or password, please try again", "Login Failed",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
