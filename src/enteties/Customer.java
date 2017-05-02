package enteties;

import java.io.Serializable;

/**
 * A class that represents a Customer in the system.
 * @author nadiaelhaddaoui
 */

public class Customer implements Serializable, EntityInterface {

	private static final long serialVersionUID = 1L;
	private int operation;

	private Object[] data;
	// private Object[] data = new Object[]{customerId, name, address, zipCode, city, phoneNumber, email, vatNumber, creditLimit};

	public Customer(Object[] data) {
		this.data = data;
	}

	@Override
	public void setOperation(int operation) {

		this.operation = operation;
	}

	@Override
	public int getOperation() {

		return operation;
	}

	@Override
	public Object[] getAllInObjects() {;
		return data;
	}

}
