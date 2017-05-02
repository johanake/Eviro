package enteties;

import java.io.Serializable;

/**
 * @author Robin Overgaard
 * @version
 */
public class Transaction implements Serializable, EntityInterface {

	private int operation;
	private Object[] data = new Object[] {};

	/**
	 * @param data
	 */
	public Transaction(Object[] data) {
		this.data = data;
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
	 * @see enteties.EntityInterface#getOperation()
	 */
	@Override
	public int getOperation() {
		return operation;
	}

	/*
	 * (non-Javadoc)
	 * @see enteties.EntityInterface#getAllInObjects()
	 */
	@Override
	public Object[] getAllInObjects() {
		return data;
	}

}
