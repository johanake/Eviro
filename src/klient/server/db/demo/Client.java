package klient.server.db.demo;

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
	private ClientGUI gui;
	private String name, ipAddress;
	private int serverPort;
	private Socket socket;
	ObjectOutputStream oos;
	DataOutputStream dos;

	public Client(String name, String ipAddress, int serverPort) {
		this.gui = new ClientGUI(this);
		this.name = name;
		this.ipAddress = ipAddress;
		this.serverPort = serverPort;
		this.start();
	}

	public void run() {
		try {
			socket = new Socket(ipAddress, serverPort);
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(name);
			dos.flush();
			oos = new ObjectOutputStream(socket.getOutputStream());

			Thread t1 = new Thread(new MessageListener());
			t1.start();
			messageSender(new Message("Client2", name, "text", new ImageIcon("images/gubbe.jpg")));

		} catch (Exception e) {
		}
	}

	private class MessageListener implements Runnable {
		private ObjectInputStream ois;

		public void run() {
			if (!socket.isClosed()) {
				try {
					ois = new ObjectInputStream(socket.getInputStream());
					while (!socket.isClosed()) {
						Message msg = (Message) ois.readObject();
						System.out.println("meddelande mottaget från: " + msg.getmReciever());
						System.out.println(msg.getmMessage());
					}
				} catch (IOException | ClassNotFoundException e1) {
					System.out.println("Meddelanden kan inte tas emot, stänger socket");
					try {
						socket.close();
					} catch (IOException e) {

					}
					e1.printStackTrace();
				}
			}
		}
	}

	public void messageSender(Message msg) throws IOException {
		if (!socket.isClosed()) {
			oos.writeObject(msg);
			oos.flush();
			System.out.println("Meddelande skickat från: " + name);
		} else
			System.out.println("Meddelande inte skickat, Socket stängd");
	}

	public static void main(String[] args) throws InterruptedException {
		String name = JOptionPane.showInputDialog("Ange användarnamn");
		name.toLowerCase(Locale.ROOT);
		Client client = new Client(name, "127.0.0.1", 1234);
	}
}
