package klientServerDbDemo;


import java.awt.Label;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Client extends Thread {
	private String name, ipAddress;
	private int serverPort;
	private Socket socket;
	private ObjectOutputStream oos;
	private DataOutputStream dos; 
	public Client(String name, String ipAddress, int serverPort) {
		this.name = name;
		this.ipAddress = ipAddress;
		this.serverPort = serverPort;
		this.start();
	}

	public void run() {
		try {
			socket = new Socket(ipAddress,serverPort); 
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(name);
			dos.flush();
			oos = new ObjectOutputStream(socket.getOutputStream());

			Thread t1 = new Thread(new MessageListener());
			System.out.println(socket.isConnected());
			t1.start();
			
			
		} catch(Exception e) { 
		}
	}

	private class MessageListener implements Runnable{
		private ObjectInputStream ois;
		public void run() {
			if (!socket.isClosed()){
				try {
					ois = new ObjectInputStream(socket.getInputStream());
					while(!socket.isClosed()) {
						Object[] o = (Object[]) ois.readObject();
						System.out.println(o[0] + " " + o[1]);
					}
				} catch (IOException | ClassNotFoundException e1) {
					try {
						socket.close();
					} catch (IOException e) {

					}
					e1.printStackTrace();
				}
			}
		}
	}

	public void messageSender(Object[] o) throws IOException{
		if (!socket.isClosed()){
			oos.writeObject(o);
			oos.flush();
			System.out.println("meddelande skickat");
		} else System.out.println("Meddelande inte skickat, Socket stängd");
	}
}
