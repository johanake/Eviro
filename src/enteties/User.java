package enteties;

import java.io.Serializable;

public class User implements Serializable, Entity {

	private int operation;
	private Object[] data;

	public User() {
		// TODO Auto-generated constructor stub
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
		return this.data;
	}

}
