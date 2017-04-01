package klientServerDbDemo;
import java.util.Observable;
import java.util.Observer;

public class Controller {
	private ConnectDB connectDB;
	private Server server;

	public Controller() {
		setConnectDB(new ConnectDB(this));
		setServer(new Server(this));
	}
	
	public ConnectDB getConnectDB() {
		return connectDB;
	}

	public void setConnectDB(ConnectDB connectDB) {
		this.connectDB = connectDB;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}
	
	public static void main(String[] args){
		new Controller();
	}
}
