package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles the connection to the database.
 * @author Peter Folke, Mattias Sundquist
 */
public class ConnectDB {

	private Connection connection;
	private Statement stmt;

	/**
	 * Connects to the database
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
	 * @param query The information to add to the database.
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

	public synchronized String executeUpdateQueryAndReturnGeneratedId(String query) {

		try {
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();

			if (rs.next()) {
				return rs.getString(1);
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

}
