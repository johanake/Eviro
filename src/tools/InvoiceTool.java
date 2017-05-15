package tools;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import client.ClientController;
import enteties.Entity;
import gui.GUIController;
import gui.Table;
import gui.Tool;
import gui.Updatable;
import shared.Eviro;

public class InvoiceTool extends Tool implements Updatable {

	private ButtonListener buttonListener;

	private LabledTextField ltfCustNo = new LabledTextField("Customer-");
	private LabledTextField ltfInvNo = new LabledTextField("Invoice-No");
	private LabledTextField ltfBuyer = new LabledTextField("Buyer");
	private LabledTextField ltfRef = new LabledTextField("Reference");
	private LabledTextField ltfSum = new LabledTextField("Total (SEK)");
	private LabledTextField ltfCreated = new LabledTextField("Created");
	private LabledTextField ltfDue = new LabledTextField("Due");

	private LabledTextField[] ltfAll = { ltfInvNo, ltfCustNo, ltfBuyer, ltfRef, ltfCreated, ltfDue, ltfSum };

	private ActionButton btnNew = new ActionButton("Create", "create");
	private ActionButton btnReset = new ActionButton("Reset", "reset");
	private ActionButton btnFind = new ActionButton("Find", "search");
	private ActionButton btnCredit = new ActionButton("Credit", "credit");

	private JButton[] allButtons = { btnNew, btnReset, btnFind };
	private JButton[] defaultButtons = { btnFind, btnReset };
	private JButton[] editingButtons = { btnNew, btnReset };
	private JButton[] lookingButtons = { btnCredit, btnReset };

	private Table articles = new Table(this, new Object[] { "Article No", "Name", "Price", "Quantity", "Sum" }, true);

	public InvoiceTool(ClientController clientController, GUIController guiController, String customer) {

		super("Invoice", clientController, guiController);
		setup();
		setButtons(editingButtons);

		ltfCustNo.setText(customer);
		ltfDue.setText("30");
		ltfCreated.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		setTfEditable(ltfAll, false);
		setTfEditable(ltfBuyer, true);
		setTfEditable(ltfRef, true);

		pnlCenter.add(new JScrollPane(articles), BorderLayout.CENTER);

	}

	public InvoiceTool(ClientController clientController, GUIController guiController) {

		super("Invoice", clientController, guiController);
		setup();
		setButtons(defaultButtons);
		setTfEditable(ltfAll, true);

	}

	public void setup() {

		super.setMaximizable(true);
		buttonListener = new ButtonListener();
		setContent(new JComponent[] { new SplitPanel(ltfCustNo, ltfInvNo), ltfBuyer, ltfRef, ltfSum, new SplitPanel(ltfCreated, ltfDue) });
		pnlCenter.add(new JScrollPane(articles), BorderLayout.CENTER);

	}

	private void reset() {

		articles.reset();
		setTfEditable(ltfAll, true);
		setButtons(defaultButtons);
		setTitle("Invoice");

		for (int i = 0; i < ltfAll.length; i++) {

			ltfAll[i].setText(null);
		}

	}

	private class ButtonListener implements ActionListener {

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
				create(getThis(), Eviro.ENTITY_INVOICE);
				createTransactions(ltfInvNo.getText());
				// ArrayList<Entity> response = clientCtrlr.create(createInvoice(), Eviro.ENTITY_INVOICE, true);
				// String invoiceNo = (String) response.get(0).getData()[0];
				// createTransactions(invoiceNo);

				break;

			case "reset":
				reset();
				break;

			case "search":
				search(getThis(), ltfAll, Eviro.ENTITY_INVOICE);
				break;

			default:

				break;
			}
		}
	}

	public void getArticle(String articleno, int row) {

		ArrayList<Entity> response = clientCtrlr.search(new String[] { articleno, null, null, null, null, null, null, null, null },
				Eviro.ENTITY_PRODUCT);

		if (response.size() == 1) {
			Object[] article = response.get(0).getData();

			Object[] info = new Object[5];
			info[0] = article[0];
			info[1] = article[1];
			info[2] = article[3];
			info[3] = "1";
			info[4] = Integer.toString(Integer.parseInt((String) info[2]) * Integer.parseInt((String) info[3]));

			articles.populate(info, row);
		}

		else {
			articles.populate(new Object[] { null, null, null, null, null }, row);
		}

	}

	private boolean createTransactions(String invoiceNbr) {

		// if (articles.getValueAt(0, 0) != null) {
		// popupMessage("Please add row(s) to invoice before creating.");
		// return false;
		// }

		for (int i = 0; i < articles.getRowCount(); i++) {

			if (articles.getValueAt(i, 0) != null) {
				String[] trans = new String[5];
				trans[0] = null; // Id set by db.
				trans[1] = invoiceNbr;
				trans[2] = (String) articles.getValueAt(i, 0); // Productid
				trans[3] = (String) articles.getValueAt(i, 3); // Quantity
				trans[4] = (String) articles.getValueAt(i, 4); // Price
				clientCtrlr.create(trans, Eviro.ENTITY_TRANSACTION, false);
			}

		}

		return true;

	}

	private void getTransactions(String invoiceNbr) {

		ArrayList<Entity> transaction = clientCtrlr.search(new Object[] { null, invoiceNbr, null, null, null }, Eviro.ENTITY_TRANSACTION);

		for (int i = 0; i < transaction.size(); i++) {

			ArrayList<Entity> article = clientCtrlr.search(
					new String[] { (String) transaction.get(i).getData()[2], null, null, null, null, null, null, null, null },
					Eviro.ENTITY_PRODUCT);

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

	public void setTotalPrice() {

		Double sum = 0.00;

		for (int i = 0; i < articles.getRowCount(); i++) {

			if (articles.getValueAt(i, 0) != null) {
				sum += Double.parseDouble((String) articles.getValueAt(i, 4));
			}

		}

		ltfSum.setText(Double.toString(sum));

	}

	@Override
	public void setValues(Object[] values) {

		getTransactions((String) values[0]);
		setTfEditable(ltfAll, false);
		btnCredit.setEnabled(false);
		setButtons(lookingButtons);
		setTitle(values[0] + " - " + values[2]);

		articles = new Table(this, new Object[] { "Article No", "Name", "Price", "Quantity", "Sum" }, true);

		for (int i = 0; i < ltfAll.length; i++) {

			if (values[i] instanceof Integer) {
				values[i] = Integer.toString((int) values[i]);
			}

			ltfAll[i].setText((String) values[i]);
		}

	}

	@Override
	public String[] getValues() {
		return getValues(false);
	}

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

	@Override
	public Updatable getThis() {
		return this;
	}

}
