package klient.server.db.demo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ClientGUI extends Thread implements ActionListener{
	private Client mClient;
	private JFrame frame = new JFrame();;
	private JLabel mainLabel;
	private JLabel msgViewLabel;
	private JLabel msgWriteLabel;
	private JLabel onlineLabel;
	
	public ClientGUI(Client client) {
		mClient = client;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(600,400));
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
