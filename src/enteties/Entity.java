package enteties;

/**
 * Interface that defines must-have functionality for entity classes in the system.
 * @author Robin Overgaard
 * @version 1.0
 */
public interface Entity {

	/**
	 * Sets database operation to be executed on the entity.
	 * @param operation database operation to be executed on the entity
	 */
	public void setOperation(int operation);

	/**
	 * Returns the database operation to be executed on the entity.
	 * @return database operation to be executed on the entity
	 */
	public int getOperation();

	/**
	 * Sets the data that should be stored in the entity.
	 * @param data the data that should be stored in the entity.
	 */
	public void setData(Object[] data);

	/**
	 * Returns the data that is stored in the entity.
	 * @return data that is stored in the entity.
	 */
	public Object[] getData();

}
