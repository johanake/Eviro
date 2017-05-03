package enteties;

import java.io.Serializable;

/**
 * A class which represents a product in the system
 * @author nadiaelhaddaoui
 * @author Robin Overgaard
 */

public class Product implements Serializable, EntityInterface {

	private int operation;
	private Object[] data;

	public Product() {
	}

	public Product(Object[] data) {
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
