package enteties;

import java.io.Serializable;

/**
 * A class which represents an invoice in the system
 * @author nadiaelhaddaoui
 * @author Robin Overgaard
 */

public class Invoice implements Serializable, EntityInterface {

	private int operation;
	private Object[] data;

	public Invoice() {
	}

	public Invoice(Object[] data) {
		this.data = data;
	}

	public String getInvoiceNbr() {
		return (String) data[0];
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
