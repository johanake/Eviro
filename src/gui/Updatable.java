package gui;

public interface Updatable {

	/**
	 * Updates the gui with values from an array of objects.
	 * @param values array of values to updaate the gui with
	 */
	// public void updateGUI(Object[] values);

	public void setValues(Object[] values);

	public String[] getValues();

	public String[] getValues(boolean getNames);

	public Updatable getThis();

}
