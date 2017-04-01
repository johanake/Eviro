package klientServerDbDemo;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class Server extends Thread  {
	private Controller controller;
	private ServerSocket serverSocket;
	private HashMap<String, SocketConnection> socketConnections;

	public Server(Controller controller) {
		this.controller = controller;
		try {
			serverSocket = new ServerSocket(1234);
			setSocketConnections(new HashMap<String, SocketConnection>());
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println("Server running");
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				SocketConnection con = new SocketConnection(socket, this);
				String user = con.getUser();
				socketConnections.put(user, con);
				System.out.println("ny connection skapad: " + user);
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

	public HashMap<String, SocketConnection> getSocketConnections() {
		return socketConnections;
	}

	public void setSocketConnections(HashMap<String, SocketConnection> socketConnections) {
		this.socketConnections = socketConnections;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}





}
