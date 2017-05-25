package server;

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

public class PasswordFrame extends JFrame implements ActionListener {
		private JPasswordField passField = new JPasswordField();
		private JLabel label = new JLabel("Enter server password:  ");
		private JPanel fieldPanel = new JPanel(new GridLayout(1, 2));
		private JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
		private JButton loginButton = new JButton("Log In");
		private JButton abortButton = new JButton("Cancel");
		private BorderLayout layout = new BorderLayout();
		private Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		private ServerController sc;

		public PasswordFrame(ServerController serverController) {
			sc = serverController;
			setTitle("Server Sign In");
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
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {

			case "Log In":
				if ( sc.login( new String(passField.getPassword()) ) ) {
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