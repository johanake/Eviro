package tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;

import client.ClientController;
import enteties.Entity;
import enteties.Invoice;
import gui.GUIController;
import gui.Table;
import gui.Tool;
import gui.Updatable;
import shared.Eviro;

/**
 * Tool created for handling invoices, GUI through Tool(super) with functions to create, search and alter information
 * @author Robin Overgaard
 * @author Johan Åkesson
 * @author nadiaelhaddaoui 
 * @version 1.1
 */
public class InvoiceTool extends Tool implements Updatable {
	
	public static final String INVOICE_OPEN = "Open"; 
	public static final String INVOICE_CREDITED = "Credited";
	public static final String INVOICE_PAID = "Paid";
	
	private ButtonListener buttonListener;

	private LabledTextField ltfCustNo = new LabledTextField("Customer");
	private LabledTextField ltfInvNo = new LabledTextField("Invoice-No");
	private LabledTextField ltfBuyer = new LabledTextField("Buyer");
	private LabledTextField ltfRef = new LabledTextField("Reference");
	private LabledTextField ltfSum = new LabledTextField("Total (SEK)");
	private LabledTextField ltfCreated = new LabledTextField("Created");
	private LabledTextField ltfDue = new LabledTextField("Due");
	private LabledTextField ltfStatus = new LabledTextField("Status", false);

	private LabledTextField[] ltfAll = { ltfInvNo, ltfCustNo, ltfBuyer, ltfRef, ltfCreated, ltfDue, ltfSum, ltfStatus };

	private ActionButton btnNew = new ActionButton("Create", "create");
	private ActionButton btnReset = new ActionButton("Reset", "reset");
	private ActionButton btnFind = new ActionButton("Find", "search");
	private ActionButton btnCredit = new ActionButton("Credit", "credit");
	private ActionButton btnPrint = new ActionButton("Print", "print");
	private ActionButton btnBook = new ActionButton("Create", "book");
	private ActionButton btnArticle = new ActionButton("Add article", "article");

	private JButton[] allButtons = { btnNew, btnReset, btnFind, btnPrint, btnCredit, btnBook, btnArticle };
	private JButton[] defaultButtons = { btnReset, btnFind };
	private JButton[] editingButtons = { btnReset, btnArticle, btnNew };
	private JButton[] lookingButtons = { btnReset, btnPrint, btnCredit };
	private JButton[] creditButtons = { btnReset, btnArticle, btnBook };

	private JScrollPane scrollPane;

	private Table articles = null;

	private CustomerTool customerGUI;
	private ClientController clientController;
	private Updatable temp;

	/**
	 * Constructs and sets the content for InvoiceTool
	 * @param customerGUI instance of customerGUI
	 * @param clientController instance of clientController
	 * @param guiController instance of guiController
	 * @param customer customerNumber
	 */
	public InvoiceTool(CustomerTool customerGUI, ClientController clientController, GUIController guiController, String customer) {

		super("Invoice", clientController, guiController);
		this.customerGUI = customerGUI;
		this.clientController = clientController;
		setup();
		articles = new Table(new Object[] { "Article No", "Name", "Unit Price", "Qty", "Total" }, true) {

			/*
			 * (non-Javadoc)
			 * @see javax.swing.JTable#editingStopped(javax.swing.event.ChangeEvent)
			 */
			@Override
			public void editingStopped(ChangeEvent e) {

				// TODO Behöver skrivas om så den funkar även om man ändrar ordning
				int row = getEditingRow();
				int col = getEditingColumn();
				super.editingStopped(e);

				calculate(this, row, col);

			}

		};

		scrollPane = new JScrollPane(articles);
		scrollPane.setPreferredSize(new Dimension(1, 150));
		pnlCenter.add(scrollPane, BorderLayout.CENTER);
		setButtons(editingButtons);

		ltfCustNo.setText(customer);
		ltfCreated.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		ltfDue.setText("30");
		ltfSum.setText("0.00");

		setTfEditable(ltfAll, false);
		setTfEditable(ltfBuyer, true);
		setTfEditable(ltfRef, true);

		setupButtonShortcuts();

	}

	/**
	 * Construtcs and sets the content for InvoiceTool
	 * @param clientController instance of clientController
	 * @param guiController instance of guiController
	 */
	public InvoiceTool(ClientController clientController, GUIController guiController) {

		super("Invoice", clientController, guiController);
		articles = new Table(new Object[] { "Article No", "Name", "Unit Price", "Qty", "Total" }, false);
		setup();
		setButtons(defaultButtons);
		setTfEditable(ltfAll, true);
		setupButtonShortcuts();

	}

	/**
	 * Sets the total price
	 * @param table instance of table
	 * @param row row
	 * @param col column
	 */
	public void calculate(Table table, int row, int col) {

		if (col == 0) {
			getArticle((String) table.getModel().getValueAt(row, col), row);
		}

		else if (col == 2 || col == 3) {

			Double price, total;
			int quantity;

			// replaceAll(",", "."); TODO Implement?

			try {
				price = Double.parseDouble((String) table.getModel().getValueAt(row, 2));
				quantity = Integer.parseInt((String) table.getModel().getValueAt(row, 3));
				total = price * quantity;

			} catch (NumberFormatException nfe) {
				total = 0.0;
				price = 0.0;
				quantity = 0;
			}

			table.getModel().setValueAt(Double.toString(total), row, 4);
			table.getModel().setValueAt(Double.toString(price), row, 2);
			table.getModel().setValueAt(Integer.toString(quantity), row, 3);

		}

		else if (col == 4) {

			Double price, total;
			int quantity;

			try {
				quantity = Integer.parseInt((String) table.getModel().getValueAt(row, 3));
				total = Double.parseDouble((String) table.getModel().getValueAt(row, 4));
				price = total / quantity;
			} catch (NumberFormatException nfe) {
				total = 0.0;
				price = 0.0;
				quantity = 0;
			}
			table.getModel().setValueAt(Double.toString(total), row, 4);
			table.getModel().setValueAt(Double.toString(price), row, 2);
			table.getModel().setValueAt(Integer.toString(quantity), row, 3);

		}

		setTotalPrice();

	}

	/**
	 * Creates button shortcuts
	 */
	private void setupButtonShortcuts() {

		btnNew.setMnemonic(KeyEvent.VK_N);
		btnFind.setMnemonic(KeyEvent.VK_F);
		btnReset.setMnemonic(KeyEvent.VK_R);
		btnCredit.setMnemonic(KeyEvent.VK_C);
		btnPrint.setMnemonic(KeyEvent.VK_P);
		btnBook.setMnemonic(KeyEvent.VK_C);
		btnArticle.setMnemonic(KeyEvent.VK_A);

	}

	/**
	 * Sets the content
	 */
	public void setup() {

		super.setMaximizable(true);
		buttonListener = new ButtonListener();
		setContent(new JComponent[] {
				new SplitPanel(ltfCustNo, ltfInvNo),
				ltfBuyer,
				ltfRef,
				ltfSum,
				new SplitPanel(ltfCreated, ltfDue),
				ltfStatus });

	}

	/**
	 * Searches for specific invoice
	 * @param invoiceNo invoiceId
	 */
	public void search(String invoiceNo) {

		ltfInvNo.setText(invoiceNo);
		search(getThis(), ltfAll, Eviro.ENTITY_INVOICE);
	}

	/**
	 * Updates invoice status
	 * @param status status
	 */
	public void updateStatus(String status) {
		ltfStatus.setText(status);
	}

	/**
	 * Method to invert an invoice and
	 */
	public void credit() {

		ltfCreated.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		if (scrollPane != null)
			pnlCenter.remove(scrollPane);

		articles = new Table((DefaultTableModel) articles.getModel(), true) {

			/*
			 * (non-Javadoc)
			 * @see javax.swing.JTable#editingStopped(javax.swing.event.ChangeEvent)
			 */
			@Override
			public void editingStopped(ChangeEvent e) {

				// TODO Behöver skrivas om så den funkar även om man ändrar ordning
				int row = getEditingRow();
				int col = getEditingColumn();
				super.editingStopped(e);

				calculate(this, row, col);

			}

		};

		for (int i = 0; i < articles.getRowCount(); i++) {
			if (articles.getModel().getValueAt(i, 0) != null) {
				articles.getModel().setValueAt("-" + articles.getModel().getValueAt(i, 2), i, 2);
				articles.getModel().setValueAt("-" + articles.getModel().getValueAt(i, 4), i, 4);
			}
		}

		scrollPane = new JScrollPane(articles);
		scrollPane.setPreferredSize(new Dimension(1, 150));
		pnlCenter.add(scrollPane, BorderLayout.CENTER);
		setTotalPrice();
	}

	/**
	 * Creates an credit invoice
	 */
	private void createCreditInvoice() {
		ltfInvNo.setText(null);
		// update(new , Eviro.ENTITY_INVOICE);
		create(getThis(), Eviro.ENTITY_INVOICE);
		createTransactions(ltfInvNo.getText());

	}

	/**
	 * Method to print invoice
	 */
	public void print() {

		PrinterJob pj = PrinterJob.getPrinterJob();
		pj.setPrintable(new Invoice(getValues()));
		if (pj.printDialog()) {
			try {
				pj.print();
			} catch (PrinterException exc) {
				System.out.println(exc);
			}
		}

	}

	/**
	 * Adds article to table
	 * @param articleno article number
	 * @param row row
	 */
	public void getArticle(String articleno, int row) {

		ArrayList<Entity> response = clientCtrlr.search(
				new String[] { articleno, null, null, null, null, null, null, null, null }, Eviro.ENTITY_PRODUCT);

		if (response.size() == 1) {
			Object[] article = response.get(0).getData();

			Object[] info = new Object[5];
			info[0] = article[0];
			info[1] = article[1];
			info[2] = article[3];
			info[3] = "1";
			info[4] = Double.toString(Double.parseDouble((String) info[2]) * Double.parseDouble((String) info[3]));

			articles.populate(info, row);
		}

		else {
			articles.populate(new Object[] { null, null, null, null, null }, row);
		}

	}

	/**
	 * Creates transactions with products for a specific invoice
	 * @param invoiceNbr invoiceNbr
	 * @return
	 */
	private boolean createTransactions(String invoiceNbr) {

		for (int i = 0; i < articles.getModel().getRowCount(); i++) {

			if (articles.getModel().getValueAt(i, 0) != null) {

				String[] trans = new String[5];
				trans[0] = null; // Id set by db.
				trans[1] = invoiceNbr;
				trans[2] = (String) articles.getModel().getValueAt(i, 0); // Productid
				trans[3] = (String) articles.getModel().getValueAt(i, 3); // Quantity
				trans[4] = (String) articles.getModel().getValueAt(i, 4); // Price
				clientCtrlr.create(trans, Eviro.ENTITY_TRANSACTION, false, false);
			}

		}

		return true;

	}

	/**
	 * Returns transactions for a specific invoice
	 * @param invoiceNbr invoice number
	 */
	private void getTransactions(String invoiceNbr) {

		ArrayList<Entity> transaction = clientCtrlr.search(new Object[] { null, invoiceNbr, null, null, null },
				Eviro.ENTITY_TRANSACTION);

		for (int i = 0; i < transaction.size(); i++) {

			ArrayList<Entity> article = clientCtrlr.search(new String[] {
					(String) transaction.get(i).getData()[2],
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null }, Eviro.ENTITY_PRODUCT);

			Object[] articleData = article.get(0).getData();
			Object[] transactionData = transaction.get(i).getData();

			Object[] row = new Object[5];
			row[0] = transactionData[2];
			row[1] = articleData[1];
			row[2] = Double.parseDouble((String) transactionData[4]) / Double.parseDouble((String) transactionData[3]); // Price
			row[3] = transactionData[3]; // Quantity
			row[4] = transactionData[4]; // Total

			articles.populate(row, i);
		}

	}

	/**
	 * Sets the total price
	 */
	public void setTotalPrice() {

		Double sum = 0.00;

		for (int i = 0; i < articles.getRowCount(); i++) {

			if (articles.getModel().getValueAt(i, 0) != null) {
				sum += Double.parseDouble((String) articles.getModel().getValueAt(i, 4));
			}

		}

		ltfSum.setText(Double.toString(sum));

	}

	/**
	 * Adds specific article(s) to table
	 * @param values
	 */
	public void addArticle(String[] values) {

		int rowCount = articles.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			if (articles.getValueAt(i, 0) == null) {
				rowCount = i;
				break;
			}
		}
		articles.populate(values, rowCount);
		calculate(articles, rowCount, 3);
	}

	/**
	 * Returns an instance of InvoiceTool
	 * @return instance of InvoiceTool
	 */
	public InvoiceTool getThisTool() {
		return this;
	}

	/**
	 * Resets the tool values and behaviour.
	 */
	private void reset() {

		if (articles != null)
			articles.reset();

		setTfEditable(ltfAll, true);
		setTfEditable(ltfStatus, false);
		setButtons(defaultButtons);
		setTitle("Invoice");

		for (int i = 0; i < ltfAll.length; i++) {

			ltfAll[i].setText(null);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see gui.Updatable#setValues(java.lang.Object[])
	 */
	@Override
	public void setValues(Object[] values) {

		if (scrollPane != null)
			pnlCenter.remove(scrollPane);

		articles = new Table((DefaultTableModel) articles.getModel(), false);
		scrollPane = new JScrollPane(articles);
		scrollPane.setPreferredSize(new Dimension(1, 150));
		pnlCenter.add(scrollPane, BorderLayout.CENTER);

		this.setBounds(this.getX(), this.getY(), this.getWidth(), 450);
		this.setMinimumSize(new Dimension(this.getMinimumSize().width, 450));

		getTransactions((String) values[0]);
		setTfEditable(ltfAll, false);
		setButtons(lookingButtons);
		setTitle(values[0] + " - " + values[2]);

		for (int i = 0; i < ltfAll.length; i++) {

			if (values[i] instanceof Integer) {
				values[i] = Integer.toString((int) values[i]);
			}

			else if (values[i] instanceof Double) {
				values[i] = Double.toString((Double) values[i]);
			}

			ltfAll[i].setText((String) values[i]);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see gui.Updatable#getValues()
	 */
	@Override
	public String[] getValues() {

		return getValues(false);
	}

	/*
	 * (non-Javadoc)
	 * @see gui.Updatable#getValues(boolean)
	 */
	@Override
	public String[] getValues(boolean getNames) {

		String[] text = new String[ltfAll.length];
		for (int i = 0; i < ltfAll.length; i++) {

			if (getNames)
				text[i] = ltfAll[i].getName();
			else {
				text[i] = ltfAll[i].getText();
			}

		}

		return text;

	}

	/*
	 * (non-Javadoc)
	 * @see gui.Updatable#getThis()
	 */
	@Override
	public Updatable getThis() {

		return this;
	}

	/**
	 * ActionListener implementatrion that listens for gui button clicks.
	 * @author Robin Overgaard
	 * @version 1.0
	 */
	private class ButtonListener implements ActionListener {
		InvoiceTool invoice;

		public ButtonListener() {
			for (JButton btn : allButtons) {
				btn.addActionListener(this);
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			switch (e.getActionCommand()) {

			case "create":
				ltfInvNo.setText(null);
				ltfStatus.setText(INVOICE_OPEN);
				create(getThis(), Eviro.ENTITY_INVOICE);
				createTransactions(ltfInvNo.getText());
				String no = customerGUI.getValues()[0];
				customerGUI.getInvoices(no);

				break;

			case "reset":
				reset();
				break;

			case "print":
				print();
				break;

			case "search":
				search(getThis(), ltfAll, Eviro.ENTITY_INVOICE);
				break;

			case "credit":
				invoice = new InvoiceTool(clientCtrlr, guiCtrlr);
				invoice.setValues(getValues());
				invoice.updateStatus(INVOICE_CREDITED);

				setButtons(creditButtons);
				setTfEditable(ltfBuyer, true);
				credit();
				break;

			case "book":
				createCreditInvoice();
				update(invoice.getThis(), Eviro.ENTITY_INVOICE, true);
				// update(temp, Eviro.ENTITY_INVOICE);
				break;

			case "article":
				guiCtrlr.add(new ArticleTool(getThisTool(), clientCtrlr, guiCtrlr));
				break;

			default:

				break;
			}
		}
	}

}
