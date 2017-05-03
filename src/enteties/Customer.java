package enteties;

import java.io.Serializable;

/**
 * A class which represents a customer in the system
 * @author nadiaelhaddaoui
 * @author Robin Overgaard
 */

public class Customer implements Serializable, EntityInterface {

	private int operation;
	private Object[] data;

	public Customer() {
	}

	public Customer(Object[] data) {
		this.data = data;
	}

	@Override
	public int getOperation() {
		return operation;
	}

	@Override
	public void setOperation(int operation) {
		this.operation = operation;
	}

	@Override
	public Object[] getData() {
		return data;
	}

	@Override
	public void setData(Object[] data) {
		this.data = data;

	}

}
