package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

import shared.Eviro;

/**
 * GUI class for managing the server. Handles the password controll to start the
 * server, buttons to change the server port and to connect or disconnect the
 * client listener.
 * 
 * @author Mattias Sundquist,
 * @author Peter Sjögren
 */
public class ServerGUI implements ActionListener {

	private Server server;
	private JFrame window = new JFrame(Eviro.APP_NAME + " Server");
	private JPanel pnlMain = new JPanel(new BorderLayout());
	private JPanel pnlSouth = new JPanel(new GridLayout(1, 3));
	private JTextArea txtArea = new JTextArea();
	private JScrollPane scrollPane = new JScrollPane(txtArea);
	private Font font = new Font("Monospaced", Font.BOLD, 15);
	private JButton btnDisconnect = new JButton("Disconnect");
	private JButton btnConnect = new JButton("Connect");
	private JButton btnPort = new JButton("Change Port");

	/**
	 * Constructor that opens up the password controll
	 * 
	 * @param serverController
	 *            ServerController for contact with the rest of the system
	 * @param server
	 *            Server for managing the server port, ServerSocket and client
	 *            listener
	 */
	public ServerGUI(ServerController serverController, Server server) {

		this();
		this.server = server;
		new PasswordFrame(serverController);

	}

	/**
	 * Constructor that builds the information frame with buttons for
	 * ceonnection controll.
	 */
	public ServerGUI() {

		txtArea.setBackground(Color.BLACK);
		txtArea.setEnabled(false);
		txtArea.setFont(font);
		txtArea.setDisabledTextColor(Color.GREEN);
		setSystemLookAndFeel();

		pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlMain.add(scrollPane, BorderLayout.CENTER);
		pnlMain.add(pnlSouth, BorderLayout.SOUTH);

		pnlSouth.add(btnDisconnect);
		pnlSouth.add(btnPort);
		pnlSouth.add(btnConnect);

		window.setMinimumSize(new Dimension(600, 400));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		window.setContentPane(pnlMain);

		btnPort.setEnabled(false);
		btnDisconnect.setEnabled(false);
		btnConnect.setEnabled(false);
		
		btnPort.setBorder(BorderFactory.createEtchedBorder());
		btnConnect.setBorder(BorderFactory.createEtchedBorder());
		btnDisconnect.setBorder(BorderFactory.createEtchedBorder());

		btnDisconnect.addActionListener(this);
		btnConnect.addActionListener(this);
		btnPort.addActionListener(this);

		window.pack();

	}

	/**
	 * Shows incoming strings in the information frame
	 * 
	 * @param text
	 */
	public void append(String text) {

		txtArea.append(text);
		txtArea.setCaretPosition(txtArea.getDocument().getLength());
	}

	/**
	 * Sets the overall look and field for the system to "Nimbus".
	 */
	private void setSystemLookAndFeel() {

		LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
		try {
			UIManager.setLookAndFeel(info[1].getClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * case: "Change Port" tries to save the new port in the serverConfig.dat
	 * file and disconnects the ClientListener that's listening on the old port.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {

		case "Disconnect":
			server.disconnect();
			btnConnect.setEnabled(true);
			btnDisconnect.setEnabled(false);
			btnPort.setEnabled(true);
			break;

		case "Connect":
			server.connect();
			btnConnect.setEnabled(false);
			btnDisconnect.setEnabled(true);
			btnPort.setEnabled(false);

			break;

		case "Change Port":
			String port = JOptionPane.showInputDialog("Choose new server port");

			if (port != null && server.setPort(port)) {
				btnConnect.setEnabled(true);
				btnDisconnect.setEnabled(false);
				JOptionPane.showMessageDialog(null,
						"Server port changed to: " + port + "\nPress connect to start server with new port");
			} else {
				
				JOptionPane.showMessageDialog(null, "Port change failed, server still running on old port.");
			}
			break;
		}
	}

	/**
	 * Password Frame for safe input and handling of server password
	 * 
	 * @author Peter Sjögren
	 */
	private class PasswordFrame extends JFrame implements ActionListener {

		private JPasswordField passField = new JPasswordField();
		private JLabel label = new JLabel("Enter server password:  ");
		private JPanel fieldPanel = new JPanel(new GridLayout(1, 2));
		private JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
		private JButton loginButton = new JButton("Log In");
		private JButton abortButton = new JButton("Cancel");
		private BorderLayout layout = new BorderLayout();
		private Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		private ServerController sc;

		/**
		 * Constructor that builds the graphic in the frame
		 * 
		 * @param serverController
		 *            ServerController for acces to login()-method.
		 */
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
				if (sc.login(new String(passField.getPassword()))) {

					btnDisconnect.setEnabled(true);
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
}
