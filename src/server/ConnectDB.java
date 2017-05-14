package server;

import java.sql.Connection;
import java.sql.DriverManager;
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
	
	private Connection connection;
	private Statement stmt;

	/**
	 * Connects to the database
	 * 
	 * @param serverController
	 */
	public ConnectDB(ServerController sc) {
		try {
			connection = DriverManager.getConnection(sc.decrypt("url"), sc.decrypt("user"), sc.decrypt("password"));
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a new query in the database
	 * 
	 * @param query
	 *            The information to add to the database.
	 */
	public synchronized void executeInsertOrDeleteQuery(String query) {
		try {
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
