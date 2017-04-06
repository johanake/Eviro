package server;

import java.sql.SQLException;


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
		Object[] data = (Object[]) obj;
		commandHandler(data);
	}
	
	private void addCustomer(Object[] data) {
		Customer c = (Customer) data[1];
		String commandstring = "INSERT INTO customer (name, adress, zipCode, city, phoneNumber, email, organisationNumber, creditLimit) "
				+ "VALUES (\"" + c.getName() + "\",\"" + c.getAdress() + "\",\"" + c.getZipCode() + "\",\"" + c.getTown() + "\",\"" 
				+ c.getPhoneNumber() + "\",\"" + c.getEmail() + "\",\"" + c.getVatNumber() + "\"," + 0  + ")";
		dataBase.executeInsertQuery(commandstring);
		
	}

}
