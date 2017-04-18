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
 * @author Robin Overgaard
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
	 * @param port The port to which the server will be listening
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
	public class ClientConnection extends Thread {

		private ObjectOutputStream objOutput;
		private ObjectInputStream objInput;
		private Socket socket;
		private ServerController controller;

		/**
		 * Sets up input and output streams and starts a new Thread.
		 * 
		 * @param socket The client which connected to the server.
		 */
		public ClientConnection(Socket socket) {
			controller = new ServerController();
			this.socket = socket;
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

			try {
				socket.close();
				objOutput.close();
				objInput.close();
				interrupt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			log.info("Client disconnected");
		}

		
		/**
		 * Sends an object to the client using the outputstream.
		 * @param obj the object to send
		 */
		private void send(Object obj) {
			
			try {
				
				if (obj != null) {
					objOutput.writeObject(obj);
					objOutput.flush();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		/**
		 * The run method for this clients thread. Listens for incoming messages and sends them to the ServerController.
		 */
		public void run() {

			log.info("Client Connected from " + socket.getLocalAddress());
			while (!interrupted()) {

				try {
					send(controller.getDataFromDb(objInput.readObject()));
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}

			}
			disconnect();
		}
		

	}

}
