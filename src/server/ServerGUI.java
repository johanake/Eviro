package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import client.Eviro;

public class ServerGUI implements ActionListener {

	ServerController serverController;
	Server server;

	JTextArea txtArea = new JTextArea();
	JButton btnConnect = new JButton("Connect");
	JButton btnDisconnect = new JButton("Disconnect");

	public ServerGUI(ServerController serverController, Server server) {
		this();
		this.serverController = serverController;
		this.server = server;
		serverController.getServerGUI(this);
	}

	public ServerGUI() {
		JFrame window = new JFrame(Eviro.APP_NAME + " Server");
		JPanel pnlMain = new JPanel(new BorderLayout());
		JPanel pnlSouth = new JPanel(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(txtArea);
		pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlMain.add(scrollPane, BorderLayout.CENTER);
		pnlMain.add(pnlSouth, BorderLayout.SOUTH);
		pnlSouth.add(btnConnect, BorderLayout.WEST);
		pnlSouth.add(btnDisconnect, BorderLayout.EAST);
		setSystemLookAndFeel();
		window.setMinimumSize(new Dimension(600, 400));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		window.setContentPane(pnlMain);
		btnConnect.addActionListener(this);
		btnDisconnect.addActionListener(this);
	}

	public void append(String text) {

		txtArea.append(text);
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

		if (e.getSource() == btnConnect)
			server.connect();
		if (e.getSource() == btnDisconnect)
			server.disconnect();

	}

}
