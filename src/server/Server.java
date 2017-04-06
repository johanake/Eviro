package server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Handles all Client and logs all traffic.
 * 
 * @author Mattias Sundquist
 *
 */
public class Server extends Thread {

	private ServerSocket serverSocket;
	private Logger log = Logger.getLogger("log");
	private FileHandler fhLog;
	private SimpleFormatter sfLog = new SimpleFormatter();

	/**
	 * Sets up the logger and starts the server.
	 * 
	 * @param port
	 *            The port to which the server will be listening
	 */
	public Server(int port) {
		try {
			File logDir = new File("logs");
			logDir.mkdir();
			fhLog = new FileHandler("logs/log" + new SimpleDateFormat("ddMMYY").format(new Date()) + ".log", true);
			fhLog.setFormatter(sfLog);
			log.addHandler(fhLog);
			log.setUseParentHandlers(false);

			serverSocket = new ServerSocket(port);
			start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Listens for incoming clients and sends them to the ClientConnection class.
	 */
	public void run() {

		log.info("Server running");
		while (!interrupted()) {
			try {
				new ClientConnection(serverSocket.accept());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Handles all logic for the clients connected to this server. Each client gets its own instance of
	 * ClientConnection.
	 * 
	 * @author Mattias Sundquist
	 *
	 */
	private class ClientConnection extends Thread {

		private ObjectOutputStream output;
		private ObjectInputStream input;
		private Socket socket;

		/**
		 * Sets up input and output streams and starts a new Thread.
		 * 
		 * @param socket
		 *            The client which connected to the server.
		 */
		public ClientConnection(Socket socket) {
			this.socket = socket;
			try {
				output = new ObjectOutputStream(socket.getOutputStream());
				input = new ObjectInputStream(socket.getInputStream());
				start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Disconnects the client from the server. Closes all streams and threads.
		 */
		private void disconnect() {

			try {
				socket.close();
				output.close();
				input.close();
				interrupt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			log.info("Client disconnected");
		}

		/**
		 * The run method for this clients thread. Listens for incoming messages and sends them to the ServerController.
		 */
		public void run() {

			log.info("Client Connected from " + socket.getLocalAddress());
			Object obj;
			while (!interrupted()) {

				try {
					obj = input.readObject();
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}

				// Send object to ServerController

			}
			disconnect();
		}

	}

}
