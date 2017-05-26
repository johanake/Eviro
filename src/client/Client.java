package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Handles all traffic to and from the server.
 * @author Mattias Sundquist
 * @author Peter Sjögren
 * @author Robin Overgaard
 * @author nadiaelhaddaoui
 */
public class Client extends Thread {

	private ClientController clientController;
	private Socket socket;
	private ObjectInputStream objInput;
	private ObjectOutputStream objOutput;

	/**
	 * Simple constructor
	 * @param clientController
	 */
	public Client(ClientController clientController) {

		this.clientController = clientController;
	}

	/**
	 * Creates input and output streams and starts a new Thread.
	 */
	public boolean connectToServer() {

		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(clientController.getProperty("ip"),
					Integer.parseInt(clientController.getProperty("port"))), 1000);

			objInput = new ObjectInputStream(socket.getInputStream());
			objOutput = new ObjectOutputStream(socket.getOutputStream());
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
			objOutput.close();
			objInput.close();
			socket.close();
			socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Streams a message object to the server.
	 * @param o The object to be sent to the server.
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
}