package enteties;

import java.io.Serializable;

/**
 * Comment represents a comment entity in the system.
 * @author nadiaelhaddaoui
 * @author Robin Overgaard
 */

public class Comment implements Serializable, Entity {

	private int operation;
	private Object[] data;

	/**
	 * Creates a empty comment entity object.
	 */
	public Comment() {
	}

	/**
	 * Creates a comment entity object and populates it's data variable.
	 * @param data product related data
	 */
	public Comment(Object[] data) {
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * @see enteties.EntityInterface#getOperation()
	 */
	@Override
	public int getOperation() {
		return operation;
	}

	/*
	 * (non-Javadoc)
	 * @see enteties.EntityInterface#setOperation(int)
	 */
	@Override
	public void setOperation(int operation) {
		this.operation = operation;
	}

	/*
	 * (non-Javadoc)
	 * @see enteties.EntityInterface#getData()
	 */
	@Override
	public Object[] getData() {
		return data;
	}

	/*
	 * (non-Javadoc)
	 * @see enteties.EntityInterface#setData(java.lang.Object[])
	 */
	@Override
	public void setData(Object[] data) {
		this.data = data;
	}

}
