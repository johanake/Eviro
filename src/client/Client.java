package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import enteties.User;
import gui.GUIController;
import gui.Login;

/**
 * Handles all traffic to and from the server.
 * 
 * @author Mattias Sundquist
 */
public class Client extends Thread {

	private ClientController clientController;
	private Socket socket = new Socket();
	private ObjectInputStream objInput;
	private ObjectOutputStream objOutput;

	/**
	 * Gives this client a GUI and connects it to the server.
	 * 
	 * @param clientController
	 * @param ip
	 *            The IP of the server.
	 * @param port
	 *            The port of the server.
	 */
	public Client(ClientController clientController) {
		this.clientController = clientController;
		// connectToServer();
		// new GUIController(clientController);
	}

	/**
	 * Creates input and output streams and starts a new Thread.
	 */
	public boolean connectToServer() {

		try {
			System.out.println(clientController.getProperty("port"));
			System.out.println(clientController.getProperty("ip"));

			socket.connect(new InetSocketAddress(clientController.getProperty("ip"),
					Integer.parseInt(clientController.getProperty("port"))), 1000);

			objInput = new ObjectInputStream(socket.getInputStream());
			objOutput = new ObjectOutputStream(socket.getOutputStream());
			// start();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * Closes all streams and threads to disconnect this client from the server.
	 */
	public void disconnect() {

		try {
			interrupt();
			socket.close();
			objOutput.close();
			objInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Streams a message object to the server.
	 * 
	 * @param o
	 *            The object to be sent to the server.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object sendObject(Object o) {
		try {
			objOutput.writeObject(o);
			return objInput.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Waits for incoming messages from the server. Is called in the run method.
	 */
	// public Object waitForObject() {
	// try {
	// returnObject = objInput.readObject();
	// System.out.println("Objekt mottaget i klient");
	// } catch (ClassNotFoundException | IOException e) {
	// e.printStackTrace();
	// }
	// return returnObject;
	//
	// }

	/**
	 * The run method for this clients thread. Calls the "waitForMessage()"
	 * method.
	 */
	// public void run() {
	//
	// while (!interrupted()) {
	// waitForObject();
	// }
	// }

}