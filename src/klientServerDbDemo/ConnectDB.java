package klientServerDbDemo;
import java.io.IOException;
import java.sql.*;
import java.util.Observable;
import java.util.Observer;

public class ConnectDB {
	private Controller controller;
	private static String connectionString = "jdbc:mysql://195.178.232.16:3306/m10p4305";
	private static Connection connection;
	private static Statement stmt;

	public ConnectDB() {
		this.controller = controller;
		try {
			connection = DriverManager.getConnection(connectionString, "m10p4305", "ultraultra");
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Tar emot en en Object[] innehållandes en int på första plats för att
	 * bestämma vilket kommando som skall utföras mot databasen och på de andra
	 * platserna datan som skall skickas eller frågas som.
	 * 
	 * @param
	 */
	public synchronized void commandHandler(Object[] data) {
		int com = (int) data[0];
		switch (com) {
		case 1:
			addCustomer(data);
			break;
		case 2:
			getCustomer(data);
			break;
		}
	}

	/**
	 * Lägger till en kund i databasen, id autogenereas och namnet på kunden
	 * finns i data[1]
	 * 
	 * @param data
	 */
	private synchronized void addCustomer(Object[] data) {
		System.out.println(data[1]);
		String commandstring = "INSERT INTO customers (name) VALUES (\"" + data[1] + "\")";
		System.out.println(commandstring);
		try {
			stmt.execute(commandstring);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Hämtar namnet på en kund med ett visst id, id finns i data[1]
	 * 
	 * @param data
	 * @return
	 */
	public synchronized void getCustomer(Object[] data) {
		ResultSet resultSet;
		int returnCom = (int) data[0];
		int id = (int) data[1];
		String customerName = null;

		try {
			resultSet = stmt.executeQuery("SELECT * FROM customers WHERE id = " + id); // hämtar endast rader med korrekt id
			while (resultSet.next()) {
				customerName = resultSet.getString(2); // id är kolumn 1, namn är kolumn 2, därför körs .getString(2)
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Object[] o = { returnCom, customerName };
		try {
			controller.getServer().getSocketConnections().get("test").objectSender(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
