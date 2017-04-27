package enteties;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * A class which represents an invoice in the system
 * @author nadiaelhaddaoui
 */

public class Invoice implements Serializable, EntityInterface {

	private int incoiveId;
	private int customerId;
	private String contact;
	private String noteInvoice;
	private Timestamp timeStamp;
	private int paymentPeriod;
	private double price;
	private int operation;
	private final String[] COLUMNNAMES = { "incoiveId", "customerId", "contact", "noteInvoice", "timeStamp", "price" };
	private final String TABLENAME = "customer";

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
	public Invoice(int incoiveId, int customerId, String contact, String noteInvoice, Timestamp timeStamp, int paymentPeriod, double price) {
		this.incoiveId = incoiveId;
		this.customerId = customerId;
		this.contact = contact;
		this.noteInvoice = noteInvoice;
		this.timeStamp = timeStamp;
		this.paymentPeriod = paymentPeriod;
		this.price = price;
	}

	public int getIncoiveId() {
		return incoiveId;
	}

	public void setIncoiveId(int incoiveId) {
		this.incoiveId = incoiveId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getNoteInvoice() {
		return noteInvoice;
	}

	public void setNoteInvoice(String noteInvoice) {
		this.noteInvoice = noteInvoice;
	}

	public Timestamp getMade() {
		return timeStamp;
	}

	public void setMade(Timestamp made) {
		this.timeStamp = made;
	}

	public int getIdk() {
		return paymentPeriod;
	}

	public void setIdk(int idk) {
		this.paymentPeriod = idk;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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
		return new Object[] { incoiveId, customerId, contact, noteInvoice, timeStamp, price };
	}

	@Override
	public String[] getColumnNames() {
		return COLUMNNAMES;
	}

	@Override
	public String getTableName() {
		return TABLENAME;
	}

}
