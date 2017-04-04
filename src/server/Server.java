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

public class Server extends Thread {

	private ServerSocket serverSocket;
	private Logger log = Logger.getLogger("log");
	private FileHandler fhLog;
	private SimpleFormatter sfLog = new SimpleFormatter();

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

	private class ClientConnection extends Thread {

		private ObjectOutputStream output;
		private ObjectInputStream input;
		private Socket socket;

		public ClientConnection(Socket socket) {
			this.socket = socket;
			try {
				output = new ObjectOutputStream(socket.getOutputStream());
				input = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			start();
		}

		private void disconnect() {

			try {
				socket.close();
				output.close();
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			log.info("Client disconnected");
		}

		public void run() {

			log.info("Client Connected from " + socket.getLocalAddress());
			while (!interrupted()) {

			}
			disconnect();
		}

	}

}
