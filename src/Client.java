import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Thread {

	private String ip;
	private int port;
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	public Client(String ip, int port) {
		new ClientController();
		this.ip = ip;
		this.port = port;
		connectToServer();
	}

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

	public void sendMessage(Object message) {

		try {
			output.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void waitForMessage() {

		try {
			input.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {

		while (!interrupted()) {
			waitForMessage();
		}
	}

}