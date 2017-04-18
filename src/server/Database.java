package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.sql.ResultSetMetaData;

/**
 * Boundary class that establishes a connection to and executes querys on a database. 
 * @author Robin Overgaard
 * @author Peter Folke
 * @author Mattias Sundquist
 * @version 1.0
 */
public class Database {

	public static final int INSERT = 0;
	public static final int SELECT = 1;
	public static final int DELETE = 2;

	private String connectionString = "jdbc:mysql://195.178.232.16:3306/m10p4305";
	private Connection connection;
	private Statement stmt;

	/**
	 * Establishes a connection to the database. 
	 */
	public Database() {
		try {
			connection = DriverManager.getConnection(connectionString, "m10p4305", "ultraultra");
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



	/**
	 * Execute a query that selects data from the database. 
	 * @param query the query
	 * @return mapped result of query
	 */
	Object select(String query) {


		HashMap<String, String> result = new HashMap<String, String>();

		try {

			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();

			rs.next();

			for (int i = 1; i <= cols; i++) {
				result.put(rsmd.getColumnName(i), rs.getString(i));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;

	}

	/**
	 * Execute a query that inserts or update data into the database.
	 * @param query the query
	 * @return the id of the newly created row in the database or null if the query is an update
	 */
	Object insert(String query) {
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
