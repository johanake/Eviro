package server;

/**
 * Interface for objects that should contain getters and setters for operation type and sql query.
 * @author Robin Overgaard
 * @version 1.0
 */
public interface Queryable {

	/**
	 * Sets the operation mode for the object.
	 * @param operation the operation mode for the object. 
	 */
	public void setOperation(int operation);

	/**
	 * Returns the operation mode for the object.
	 * @return the operation mode for the object. 
	 */
	public int getOperation();

	/**
	 * Sets the query for the object.
	 * @param query the query for the object.
	 */
	public void setQuery(String query);

	/**
	 * Returns the query for the object.
	 * @return the query for the object.
	 */
	public String getQuery();
	
}