package gui;
/**
 * Interface for Tools in the system.
 * 
 * @author Robin Overgaard
 * @version 1.0
 */

public interface Tool {

	/**
	 * Returns the name of the tool.
	 * @return the name of the tool
	 */
	public String getTitle();

	/**
	 * Returns whether the tool should be rezizable or not.
	 * @return whether the tool should be rezizable or not
	 */
	public boolean getRezizable();

}