package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import enteties.Entity;

/**
 * Handles all Client.
 * 
 * @author Mattias Sundquist
 *
 */
public class Server extends Thread {

	private ServerSocket serverSocket;
	private ServerController serverController;

	/**
	 * Starts the server.
	 * 
	 * @param port The port to which the server will be listening
	 */
	public Server(int port) {
		serverController = new ServerController();
//		new ServerGUI(serverController, this);
		try {
			serverSocket = new ServerSocket(port);
			start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starts the server
	 */
	public void connect() {

		start();
	}

	/**
	 * Disconnects the server
	 */
	public void disconnect() {

		interrupt();
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Listens for incoming clients and sends them to the ClientConnection class.
	 */
	public void run() {

		serverController.logAppend("Server running!");
		while (!interrupted()) {
			try {
				new ClientConnection(serverSocket.accept());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		serverController.logAppend("Server closed!");
	}

	/**
	 * Handles all logic for the clients connected to this server. Each client gets its own instance of
	 * ClientConnection.
	 * 
	 * @author Mattias Sundquist
	 *
	 */
	private class ClientConnection extends Thread {

		private ObjectOutputStream objOutput;
		private ObjectInputStream objInput;
		private Socket socket;

		/**
		 * Sets up input and output streams and starts a new Thread.
		 * 
		 * @param socket The client which connected to the server.
		 */
		public ClientConnection(Socket socket) {
			this.socket = socket;
			serverController.logAppend("Client " + socket.getInetAddress() + " connected!");
			try {
				objOutput = new ObjectOutputStream(socket.getOutputStream());
				objInput = new ObjectInputStream(socket.getInputStream());
				start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Disconnects the client from the server. Closes all streams and threads.
		 */
		private void disconnect() {

			serverController.logAppend("Client " + socket.getInetAddress() + " disconnected!");
			try {
				socket.close();
				objOutput.close();
				objInput.close();
				interrupt();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * The run method for this clients thread. Listens for incoming messages and sends them to the ServerController.
		 */
		public void run() {

			while (!interrupted()) {
				try {
					objOutput.writeObject(serverController.operationHandler((Entity) objInput.readObject()));
					objOutput.flush();
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
			disconnect();
		}
	}
}
