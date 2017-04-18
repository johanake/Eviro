package server;

/**
 * Serverside controller class for logic and data transportation between server and database. 
 * @author Robin Overgaard
 * @version 1.0
 */
public class ServerController {

	private Database database;

	/**
	 * Constructor that instatntiates the database connection. 
	 */
	public ServerController() {
		database = new Database();
	}

	/**
	 * Extracts query information and executes query on the database. 
	 * @param data the object that contains query information
	 * @return the result of the database query
	 */
	public Object getDataFromDb(Object data) {
		Queryable queryable =(Queryable) data;
		return query(queryable.getOperation(), queryable.getQuery());
	}
	
	/**
	 * Passes a query to the database connection.
	 * @param operation the operation type
	 * @param query the query to be passed
	 * @return the result of the query execution or null if the query has no return result
	 */
	public synchronized Object query(int operation, String query) {

		switch (operation) {

		case Database.INSERT:
			return database.insert(query);

		case Database.SELECT:
			return database.select(query);	
		}

		return null;

	}

}
