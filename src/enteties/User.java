package enteties;

import java.io.Serializable;

public class User implements Serializable, Entity {

	private int operation;
	private Object[] data;
	
	public User() {
		// TODO Auto-generated constructor stub
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
	public void setData(Object[] data) {
		this.data = data;
	}

	@Override
	public Object[] getData() {
		return this.data;
	}

}
