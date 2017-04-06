package client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Handles all traffic to and from the server.
 * 
 * @author Mattias Sundquist
 *
 */
public class Client extends Thread {

	private String ip;
	private int port;
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	/**
	 * Gives this client a GUI and connects it to the server.
	 * 
	 * @param ip
	 *            The IP of the server.
	 * @param port
	 *            The port of the server.
	 */
	public Client(String ip, int port) {

		new GUIController(new ClientController(this));

		this.ip = ip;
		this.port = port;
		connectToServer();

	}

	/**
	 * Creates input and output streams and starts a new Thread.
	 */
	private void connectToServer() {

		try {
			socket = new Socket(ip, port);
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Closes all streams and threads to disconnect this client from the server.
	 */
	public void disconnect() {

		try {
			interrupt();
			socket.close();
			output.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Streams a message object to the server.
	 * 
	 * @param message
	 *            The object to be sent to the server.
	 */
	public void sendMessage(Object[] message) {

		try {
			output.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Waits for incoming messages from the server. Is called in the run method.
	 */
	private void waitForMessage() {

		try {
			input.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The run method for this clients thread. Calls the "waitForMessage()" method.
	 */
	public void run() {

		while (!interrupted()) {
			waitForMessage();
		}
	}

}