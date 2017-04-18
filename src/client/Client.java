package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import gui.GUIController;

/**
 * Handles communication with the server. 
 * @author Mattias Sundquist
 * @author Robin Overgaard
 * @version 1.0
 */
public class Client {

	private String ip;
	private int port;
	private Socket socket;
	private ObjectInputStream objInput;
	private ObjectOutputStream objOutput;

	/**
	 * Gives this client a GUI and connects it to the server.
	 * @param ip The IP of the server.
	 * @param port The port of the server.
	 */
	public Client(String ip, int port) {

		new GUIController(new ClientController(this));

		this.ip = ip;
		this.port = port;
		connectToServer();

	}

	/**
	 * Creates input and output streams. 
	 */
	private void connectToServer() {

		try {
			socket = new Socket(ip, port);
			objInput = new ObjectInputStream(socket.getInputStream());
			objOutput = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Closes all streams to disconnect this client from the server.
	 */
	public void disconnect() {

		try {
			socket.close();
			objOutput.close();
			objInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends an object to the server using the outputstream.
	 * @param obj the object to send
	 */
	public void sendObject(Object obj) {
		try {
			objOutput.writeObject(obj);
			objOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Waits for an object to arrive on the inputstream. 
	 * @return the object that was recieved or null if no object is recieved. 
	 */
	public Object waitForResponse() {
		try {
			socket.setSoTimeout(1000);
			return objInput.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	

}