package enteties;

import java.io.Serializable;

/**
 * Invoice represents a invoice entity in the system.
 * @author nadiaelhaddaoui
 * @author Robin Overgaard
 */

public class Invoice implements Serializable, Entity {

	private int operation;
	private Object[] data;

	/**
	 * Creates a empty invoice entity object.
	 */
	public Invoice() {
	}

	/**
	 * Creates a invoice entity object and populates it's data variable.
	 * @param data invoice related data
	 */
	public Invoice(Object[] data) {
		this.data = data;
	}

	/**
	 * Returns the invoice number from the invoice entity object.
	 * @return the invoice number
	 */
	public String getInvoiceNbr() {
		return (String) data[0];
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
