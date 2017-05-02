package enteties;

import java.io.Serializable;

/**
 * A class which represents an invoice in the system
 * @author nadiaelhaddaoui
 */

public class Invoice implements Serializable, EntityInterface {

	// private int invoiceId;
	// private int customerId;
	// private String contact;
	// private String noteInvoice;
	// private Timestamp timeStamp;
	// private int paymentPeriod;
	// private double price;
	private int operation;
	private Object[] data = new Object[] {};

	/**
	 * Invoice constructor
	 * @param incoiveId A unique Id
	 * @param customerId The customer which this invoice relates to
	 * @param contact The contactinformation for this invoice
	 * @param noteInvoice Notes for this invoice
	 * @param timeStamp The time which this invoice was created
	 * @param paymentPeriod The paymentperiod for this invoice
	 * @param price The price for this invoice
	 */
	// public Invoice(int incoiveId, int customerId, String contact, String noteInvoice, Timestamp timeStamp, int paymentPeriod, double price) {
	// this.invoiceId = incoiveId;
	// this.customerId = customerId;
	// this.contact = contact;
	// this.noteInvoice = noteInvoice;
	// this.timeStamp = timeStamp;
	// this.paymentPeriod = paymentPeriod;
	// this.price = price;
	// }

	public Invoice(Object[] data) {
		this.data = data;
	}

	public String getInvoiceNbr() {
		return (String) data[0];
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
	public Object[] getAllInObjects() {
		return data;
	}

}
