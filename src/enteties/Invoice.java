package enteties;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.Serializable;

/**
 * Invoice represents a invoice entity in the system.
 * @author nadiaelhaddaoui
 * @author Robin Overgaard
 */

public class Invoice implements Serializable, Entity, Printable {

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

	@Override
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		// We have only one page, and 'page'
		// is zero-based
		if (page > 0) {
			return NO_SUCH_PAGE;
		}

		// User (0,0) is typically outside the
		// imageable area, so we must translate
		// by the X and Y values in the PageFormat
		// to avoid clipping.
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());

		// Now we perform our rendering
		g.drawString("Invoice No:" + (String) data[0], 100, 100);
		g.drawString("Customer No:" + (String) data[1], 100, 150);
		g.drawString("Buyer:" + (String) data[2], 100, 200);
		g.drawString("Reference:" + (String) data[3], 100, 250);
		g.drawString("Issue date:" + (String) data[4], 100, 300);
		g.drawString("Due date:" + (String) data[5], 100, 350);

		g.drawString("Total sek:" + (String) data[6], 100, 400);

		// tell the caller that this page is part
		// of the printed document
		return PAGE_EXISTS;
	}

}
