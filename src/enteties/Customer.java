package enteties;

import java.io.Serializable;

/**
 * Customer represents a customer entity in the system.
 * @author nadiaelhaddaoui
 * @author Robin Overgaard
 */

public class Customer implements Serializable, Entity {

	private int operation;
	private Object[] data;

	/**
	 * Creates a empty customer entity object.
	 */
	public Customer() {
	}

	/**
	 * Creates a customer entity object and populates it's data variable.
	 * @param data customer related data
	 */
	public Customer(Object[] data) {
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
