package klientServerDbDemo;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends Thread{
	private ServerSocket serverSocket;
	private HashMap<String, Connection> connections;
	private ArrayList<Message> messages;
	
	public Server() throws IOException {
		serverSocket = new ServerSocket(1234);
		connections = new HashMap<String,Connection>();
		messages = new ArrayList<Message>();
		this.start();
	}

	public void run(){
		System.out.println("Server running"); 
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				Connection con = new Connection(socket, this);
				System.out.println("ny connection skapad");
			} catch(IOException e) 
				{ System.err.println(e);
				} 
		}	
	}
	
	public HashMap<String, Connection> getConnections() {
		return connections;
	}
	
	public void setConnections(HashMap<String, Connection> connections) {
		this.connections = connections;
	}
	

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}

	public static void main(String[] args) throws IOException{
		new Server();
	}
}
