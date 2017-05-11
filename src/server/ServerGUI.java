package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import shared.Eviro;

public class ServerGUI implements ActionListener {

	private Server server;

	private JTextArea txtArea = new JTextArea();
	private JButton btnConnect = new JButton("Connect");
	private JButton btnDisconnect = new JButton("Disconnect");

	public ServerGUI(ServerController serverController, Server server) {
		this();
		this.server = server;
		serverController.getServerGUI(this);
	}

	public ServerGUI() {
		JFrame window = new JFrame(Eviro.APP_NAME + " Server");
		JPanel pnlMain = new JPanel(new BorderLayout());
		JPanel pnlSouth = new JPanel(new FlowLayout());
		JScrollPane scrollPane = new JScrollPane(txtArea);
		Font font = new Font("Monospaced", Font.BOLD, 15);
		txtArea.setBackground(Color.BLACK);
		txtArea.setEnabled(false);
		txtArea.setFont(font);
		txtArea.setDisabledTextColor(Color.GREEN);
		setSystemLookAndFeel();

		pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlMain.add(scrollPane, BorderLayout.CENTER);
		pnlMain.add(pnlSouth, BorderLayout.SOUTH);
		pnlSouth.add(btnConnect, BorderLayout.WEST);
		pnlSouth.add(btnDisconnect, BorderLayout.EAST);
		window.setMinimumSize(new Dimension(600, 400));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		window.setContentPane(pnlMain);
		
		// KOD SOM KANSKE SKA ÄNDRAS
		
//		btnConnect.addActionListener(this);			
//		btnDisconnect.addActionListener(this);
		btnConnect.setEnabled(false);
		btnDisconnect.setEnabled(false);
		
		// KOD SOM KANSKE SKA ÄNDRAS
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

		if (e.getSource() == btnConnect)
			server.connect();
		if (e.getSource() == btnDisconnect)
			server.disconnect();

	}

}
