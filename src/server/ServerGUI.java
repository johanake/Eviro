package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import shared.Eviro;

public class ServerGUI implements ActionListener {

	private Server server;
	private JFrame window = new JFrame(Eviro.APP_NAME + " Server");
	private JPanel pnlMain = new JPanel(new BorderLayout());
	private JPanel pnlSouth = new JPanel(new FlowLayout());
	private JTextArea txtArea = new JTextArea();
	private JScrollPane scrollPane = new JScrollPane(txtArea);
	private Font font = new Font("Monospaced", Font.BOLD, 15);
	private JButton btnDisconnect = new JButton("Disconnect & Close Server");

	public ServerGUI(ServerController serverController, Server server) {

		this();
		this.server = server;
		new PasswordFrame(serverController);
	}

	public ServerGUI() {

		txtArea.setBackground(Color.BLACK);
		txtArea.setEnabled(false);
		txtArea.setFont(font);
		txtArea.setDisabledTextColor(Color.GREEN);
		setSystemLookAndFeel();

		pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlMain.add(scrollPane, BorderLayout.CENTER);
		pnlMain.add(pnlSouth, BorderLayout.SOUTH);
		pnlSouth.add(btnDisconnect, BorderLayout.CENTER);
		window.setMinimumSize(new Dimension(600, 400));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		window.setContentPane(pnlMain);

		btnDisconnect.setForeground(Color.red);
		btnDisconnect.setEnabled(false);
		btnDisconnect.addActionListener(this);

		window.pack();
	}

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

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnDisconnect)
			server.disconnect();
		window.dispose();
	}

	/**
	 * 
	 * @author peter
	 *
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
					btnDisconnect.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));
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
