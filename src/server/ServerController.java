package server;

import klientServerDbDemo.ConnectDB;

public class ServerController {
	
	private ConnectDB dataBase;

	public ServerController() {
		dataBase = new ConnectDB();
	}
	
	public void getMessageFromServer(Object obj) {
		Object[] data = new Object[2];
		data[0] = 1;
		data[1] = obj;
		dataBase.commandHandler(data);
	}

}
