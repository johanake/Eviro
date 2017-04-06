package server;

import klientServerDbDemo.ConnectDB;

public class ServerController {
	
	private ConnectDB database;

	public ServerController() {
		ConnectDB = new ConnectDB(controller); // Ska controllern i parametern användas?
	}
	
	public void getMessageFromServer(Object obj) {
		// Add Object array and send to database.
	}

}
