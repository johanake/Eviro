package enteties;

import java.io.Serializable;

/**
 * ForumMessage represents a forummessage entity in the system.
 * @author Matthias Sundqvist
 * @version 1.0
 */

public class ForumMessage implements Serializable, Entity {

	private int operation;
	private Object[] data;

	/**
	 * Creats a empty forummessage object
	 */
	public ForumMessage() {
	}

	/**
	 * Creats a formmussage with input data
	 * @param data a message/text
	 */
	public ForumMessage(Object[] data) {
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * @see enteties.Entity#setOperation(int)
	 */
	@Override
	public void setOperation(int operation) {
		this.operation = operation;
	}

	/*
	 * (non-Javadoc)
	 * @see enteties.Entity#getOperation()
	 */
	@Override
	public int getOperation() {
		return operation;
	}

	/*
	 * (non-Javadoc)
	 * @see enteties.Entity#setData(java.lang.Object[])
	 */
	@Override
	public void setData(Object[] data) {

		this.data = data;

	}

	/*
	 * (non-Javadoc)
	 * @see enteties.Entity#getData()
	 */
	@Override
	public Object[] getData() {
		return data;
	}

}
