package server;

import java.sql.SQLException;

import klientServerDbDemo.ConnectDB;

public class ServerController {
	
	private ConnectDB dataBase;

	public ServerController() {
		dataBase = new ConnectDB();
	}
	
	public void commandHandler(Object[] data) {
		int com = (int) data[0];
		switch (com) {
		case 1:
			addCustomer(data);
			break;
		}
	}
	
	public void getMessageFromServer(Object obj) {
		Object[] data = new Object[2];
		data[0] = 1;
		data[1] = obj;
		commandHandler(data);
	}
	
	private void addCustomer(Object[] data) {
		String commandstring = "INSERT INTO customer (name, adress, postCode, city, phoneNumber, email, organisationNumber, creditLimit) "
				+ "VALUES (\"" + data[1] + "\")";
		
	}

}
