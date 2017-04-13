package enteties;
import java.io.Serializable;
import java.sql.Timestamp;


/**
 * @author nadiaelhaddaoui
 */

public class Invoice implements Serializable{
	
	private int incoiveId;
	private int customerId;
	private String contact;
	private String noteInvoice;
	private Timestamp timeStamp;
	private int paymentPeriod;
	private double price;
	
	public Invoice(int incoiveId, int customerId, String contact, String noteInvoice, Timestamp timeStamp, int paymentPeriod,
			double price) {
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
	

}
