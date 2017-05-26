package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import enteties.Entity;

/**
 * Server class that handles all client conncections.
 * @author Mattias Sundquist
 * @author Peter Sjögren
 * @author Robin Overgaard
 * @author nadiaelhaddaoui
 */
public class Server {

	private ServerController serverController;
	private ServerSocket serverSocket = null;
	private Thread listenerThread = null;

	/**
	 * Constructor that creates the ServerController, which handles most of the communication and logic in the server system.
	 */
	public Server() {

		serverController = new ServerController(this);

	}

	/**
	 * Creates a new ClientListener and gives the commando to start listening.
	 */
	public void connect() {

		listenerThread = new Thread(new ClientListener(Integer.parseInt(serverController.getProperty("port"))));
		listenerThread.start();

	}

	/**
	 * Disconnects the server
	 */
	public void disconnect() {

		listenerThread.interrupt();
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method for changing the server port and saving the incoming value in the serverConfig.dat file.
	 * @param port New port
	 * @return true if the change was succesfall, false otherwise.
	 */
	public boolean setPort(String port) {

		return serverController.setProperty("port", port);
	}

	/**
	 * Method for checking if a ClientListener is online or not
	 * @return true or false pending on the status of the listenerThread.
	 */
	public synchronized boolean isOnline() {

		if (listenerThread != null) {
			return listenerThread.isAlive();
		}
		return false;
	}

	/**
	 * Listener class that listens for incoming client connections and creates a separate ClientConnection for every client that connects.
	 * @author Peter Sjögren
	 */
	private class ClientListener implements Runnable {

		public ClientListener(int port) {
			try {
				serverSocket = new ServerSocket(port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			serverController.logAppend("Server running!");
			while (!listenerThread.interrupted()) {
				try {
					new ClientConnection(serverSocket.accept());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			serverController.logAppend("Server closed!");
		}
	}

	/**
	 * Handles all logic for the clients connected to this server. Each client gets its own instance of ClientConnection.
	 * @author Mattias Sundquist
	 */
	private class ClientConnection extends Thread {

		private ObjectOutputStream objOutput;
		private ObjectInputStream objInput;
		private Socket socket;

		/**
		 * Sets up input and output streams and starts a new Thread.
		 * @param socket The client which connected to the server.
		 */
		public ClientConnection(Socket socket) {

			this.socket = socket;
			serverController.logAppend("Client " + socket.getInetAddress() + " connected!");

			try {
				objOutput = new ObjectOutputStream(socket.getOutputStream());
				objInput = new ObjectInputStream(socket.getInputStream());
				this.start();
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
				objOutput.close();
				objInput.close();
				socket.close();
				this.interrupt();
			} catch (IOException e) {
				e.printStackTrace();
				this.interrupt();
			}
		}

		/**
		 * The run method for this clients thread. Listens for incoming messages and sends them to the ServerController.
		 */
		@Override
		public void run() {

			while (!interrupted()) {
				try {
					objOutput.writeObject(serverController.operationHandler((Entity) objInput.readObject()));
					objOutput.flush();
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
					disconnect();
				}
			}
			disconnect();
		}
	}
}
