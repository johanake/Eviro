package gui;

/**
 * Interface that defines must-have functionality for tools that send and recieves data from and to the server.
 * @author Robin Overgaard
 * @version 1.0
 */
public interface Updatable {

	/**
	 * Updates fields with values.
	 * @param values array of objects to update the fields with
	 */
	public void setValues(Object[] values);

	/**
	 * Gets the by a user entered values from gui fields.
	 * @return string array of recieved values
	 */
	public String[] getValues();

	/**
	 * Gets the by a user entered values from gui fields.
	 * @param getNames whether to also get the names of the fields
	 * @return string array of recieved values
	 */
	public String[] getValues(boolean getNames);

	/**
	 * Returns the instance of the object implementing this interface.
	 * @return the instance of the object implementing this interface
	 */
	public Updatable getThis();

}
