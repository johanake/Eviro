package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDB extends Thread{
	private static String connectionString = "jdbc:mysql://195.178.232.16:3306/m10p4305";
	private static Connection connection;
	private static Statement stmt;
	
	public ConnectDB() {
		start();
	}
	
	public void run(){
		try {
			connection = DriverManager.getConnection(connectionString, "m10p4305", "ultraultra");
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void executeInsertQuery(String query){
		try {
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
