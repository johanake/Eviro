package enteties;

/**
 * @author Robin Overgaard
 * @version 1.0
 */
public interface EntityInterface {

	/**
	 * @param operation
	 */
	public void setOperation(int operation);

	/**
	 * @return
	 */
	public int getOperation();

	/**
	 * @return
	 */
	public Object[] getAllInObjects();

}
