package server;

import java.sql.Connection;
import java.sql.DriverManager;
<<<<<<< HEAD
=======
import java.sql.PreparedStatement;
>>>>>>> origin/peterbranch
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.jasypt.util.*;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * Handles the connection to the database.
 * 
 * @author Peter Folke, Mattias Sundquist
 *
 */
public class ConnectDB {

	private String connectionString = "jdbc:mysql://195.178.232.16:3306/m10p4305";
	private Connection connection;
	private Statement stmt;
<<<<<<< HEAD
	private FileReader reader;
	private Properties properties = new Properties();
	private BasicTextEncryptor textCryptor = new BasicTextEncryptor();
	private StrongPasswordEncryptor passCryptor = new StrongPasswordEncryptor();


=======
	private PreparedStatement ps;
	
>>>>>>> origin/peterbranch
	/**
	 * Connects to the database
	 */
	public ConnectDB() {
		try {
			reader = new FileReader("config");
			properties.load(reader);

//			String pass = null;
			String pass = "eviroadmin";
			while(pass == null){
				pass = login();
			}
			textCryptor.setPassword(pass);
			
			
			connection = DriverManager.getConnection(textCryptor.decrypt(properties.getProperty("url")),
					textCryptor.decrypt(properties.getProperty("user")),
					textCryptor.decrypt(properties.getProperty("password")));
			stmt = connection.createStatement();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private String login(){
		String input = JOptionPane.showInputDialog(null, "Login with server password", "Server Login", JOptionPane.OK_OPTION);
		if(passCryptor.checkPassword(input, properties.getProperty("admin"))){
			return input;
		} 
		else{
			return null;
		}
	}

	/**
	 * Adds a new query in the database
	 * 
	 * @param query
	 *            The information to add to the database.
	 */
<<<<<<< HEAD
	public synchronized void executeInsertOrDeleteQuery(String query) {
=======
	public synchronized boolean executeInsertDeleteQuery(String query) {
		boolean executed = false;
		try {
			executed = !stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return executed;
				
	}
	
/// exempel på att hämta resultset för att kunna skicka tilbaka antingen i sin helhet eller utbruten data.	
	public synchronized ResultSet executeSelectQuery(String query){
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(query); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public synchronized PreparedStatement executeUpdateQuery(String query){
>>>>>>> origin/peterbranch
		try {
			ps = connection.prepareStatement(query);			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ps;
	}

	public synchronized ResultSet executeGetQuery(String query) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public synchronized void executeUpdateQuery(String query) {
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
