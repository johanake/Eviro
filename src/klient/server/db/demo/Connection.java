package klient.server.db.demo;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Connection extends Thread{
	private Server mServer;
	private String mUser;
	private Socket mSocket;
	private DataInputStream dis;
	private ObjectInputStream ois;
	ObjectOutputStream oos;

	public Connection(Socket socket, Server server) {
		mServer = server;
		mSocket = socket;
		this.start();
	}
	public void run() {
		try {
			dis = new DataInputStream(mSocket.getInputStream());
			mUser = dis.readUTF();
			System.out.println(mUser);
			mServer.getConnections().put(mUser, this);
			oos = new ObjectOutputStream(mSocket.getOutputStream());

			System.out.println("Connection sparad med namn: " + mServer.getConnections().get(mUser).getmUser());
			
			checkMessages(mUser);
			Thread t1 = new Thread(new MessageListener());
			t1.start();
		} catch (IOException e) {
			System.out.println(mUser + " är nedkopplad");
			mServer.getConnections().remove(mUser);
			e.printStackTrace();
			
		}

	}
	
	private class MessageListener implements Runnable {
			public void run() {
			try {
				ois = new ObjectInputStream(mSocket.getInputStream());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			while(!mSocket.isClosed()){
				try {
					Message msg = (Message) ois.readObject();
					if(mServer.getConnections().containsKey(msg.getmReciever())){
						mServer.getConnections().get(msg.getmReciever()).messageSender(msg);
					} else {
						mServer.getMessages().add(msg);
						System.out.println(msg.getmReciever() + " inte online, sparar meddelande");
					}
				} catch (IOException | ClassNotFoundException e) {
					try {
						mSocket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}			
		}
	}
	
	public void messageSender(Message msg) throws IOException{
		if (!mSocket.isClosed()){
			oos.writeObject(msg);
			oos.flush();
			System.out.println("Meddelande skickat");
		} else System.out.println("Meddelande inte skickat, Socket stängd");
	}
	
	private void checkMessages(String str) throws IOException{
		System.out.println("Messages size: " + mServer.getMessages().size());

		for (int i = 0; i < mServer.getMessages().size(); i++){
			if (mServer.getMessages().get(i).getmReciever().equals(str)){
				messageSender(mServer.getMessages().get(i));
				System.out.println("Old Message sent");
				mServer.getMessages().remove(i);
			}
		}
		
		System.out.println("Messages size: " + mServer.getMessages().size());
		mServer.getMessages().trimToSize();

	}

	public String getmUser() {
		return mUser;
	}

	public void setmUser(String mUser) {
		this.mUser = mUser;
	}

	public Socket getmSocket() {
		return mSocket;
	}

	public void setmSocket(Socket mSocket) {
		this.mSocket = mSocket;
	}

}
