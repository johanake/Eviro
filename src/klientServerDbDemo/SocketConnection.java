package klientServerDbDemo;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class SocketConnection extends Thread{
	private Thread t;
	private Server server;
	private String user;
	private Socket socket;
	private DataInputStream dis;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public SocketConnection(Socket socket, Server server) {
		this.server = server;
		this.socket = socket;
		try {
			dis = new DataInputStream(socket.getInputStream());
			user = dis.readUTF();
			System.out.println(user);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			Thread t1 = new Thread(new Listener());
			t1.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		this.start();
		
	}
	
	public void run(){
		
	}

	private class Listener implements Runnable {
		public void run() {
			while (!socket.isClosed()) {
				try {
					Object[] data = (Object[]) ois.readObject();
					System.out.println("meddelande mottaget i server");
					server.getController().getConnectDB().commandHandler(data);
				} catch (IOException | ClassNotFoundException e) {
					try {
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	public void objectSender(Object[] o) throws IOException {
		if (!socket.isClosed()) {
			oos.writeObject(o);
			oos.flush();
			System.out.println("Meddelande skickat från server");
		} else
			System.out.println("Meddelande inte skickat, Socket stängd");
	}


	public String getUser() {
		return user;
	}

	public void setUser(String mUser) {
		this.user = mUser;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket mSocket) {
		this.socket = mSocket;
	}

}
