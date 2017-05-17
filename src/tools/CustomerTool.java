package tools;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;

import client.ClientController;
import enteties.Entity;
import gui.GUIController;
import gui.Table;
import gui.Tool;
import gui.Updatable;
import shared.Eviro;

public class CustomerTool extends Tool implements Updatable {

	private ButtonListener buttonListener;

	private Table tblComments;
	private Table tblInvoices;

	private Tab tabComments = new Tab("Comments");
	private Tab tabFinance = new Tab("Finance");
	private Tab[] tabs = new Tab[] { new Tab("General"), tabFinance, tabComments };

	private LabledTextField ltfNo = new LabledTextField("No");
	private LabledTextField ltfName = new LabledTextField("Name");
	private LabledTextField ltfAddress = new LabledTextField("Address");
	private LabledTextField ltfZip = new LabledTextField("Post Code", true, Eviro.VALIDATOR_INTEGER);
	private LabledTextField ltfCity = new LabledTextField("City");
	private LabledTextField ltfPhone = new LabledTextField("Phone No");
	private LabledTextField ltfEmail = new LabledTextField("Email");
	private LabledTextField ltfVat = new LabledTextField("Vat No");
	private LabledTextField ltfLimit = new LabledTextField("Limit", true, Eviro.VALIDATOR_INTEGER);
	private LabledTextField ltfBalance = new LabledTextField("Balance", false);
	private LabledTextField[] ltfAll = { ltfNo, ltfName, ltfAddress, ltfZip, ltfCity, ltfPhone, ltfEmail, ltfVat, ltfLimit };
	private LabledTextField[] ltfRequired = { ltfName, ltfAddress, ltfZip, ltfCity, ltfPhone, ltfLimit };

	private ActionButton btnNew = new ActionButton("Create New", "create");
	private ActionButton btnEdit = new ActionButton("Edit Customer", "edit");
	private ActionButton btnUpdate = new ActionButton("Save", "update");
	private ActionButton btnFind = new ActionButton("Find", "search");
	private ActionButton btnInvoice = new ActionButton("New Invoice", "invoice");
	private ActionButton btnReset = new ActionButton("Reset", "reset");

	private JButton[] allButtons = { btnNew, btnEdit, btnUpdate, btnFind, btnInvoice, btnReset };
	private JButton[] defaultButtons = { btnNew, btnFind, btnReset };
	private JButton[] lookingButtons = { btnEdit, btnInvoice, btnReset };
	private JButton[] editingButtons = { btnUpdate, btnReset };

	public CustomerTool(ClientController clientController, GUIController guiController) {
		super("Customer", clientController, guiController);
		buttonListener = new ButtonListener();
		setTabs(tabs);
		setContent(0, new JComponent[] { ltfNo, ltfName, ltfAddress, new SplitPanel(ltfZip, ltfCity), ltfPhone, ltfEmail, ltfVat });
		setContent(1, new JComponent[] { new SplitPanel(ltfLimit, ltfBalance) });
		setButtons(defaultButtons);
		ltfBalance.setText("0.00");
		createCommentsTable();
		createInvoiceTable();
		btnNew.setMnemonic(KeyEvent.VK_N);
		btnEdit.setMnemonic(KeyEvent.VK_E);
		btnUpdate.setMnemonic(KeyEvent.VK_U);
		btnFind.setMnemonic(KeyEvent.VK_F);
		btnInvoice.setMnemonic(KeyEvent.VK_I);
		btnReset.setMnemonic(KeyEvent.VK_R);
	}

	private void createCommentsTable() {
		tblComments = new Table(new Object[] { "Date", "Comment" }, true) {

			@Override
			public void editingStopped(ChangeEvent e) {
				int row = getEditingRow();
				int col = getEditingColumn();
				super.editingStopped(e);

				tblComments.getModel().setValueAt(new SimpleDateFormat("yy-MM-dd").format(new Date()), tblComments.getSelectedRow(), 0);

				// TODO Beteende här!

			}

		};

		tblComments.getColumnModel().getColumn(0).setWidth(100);
		tblComments.getColumnModel().getColumn(0).setMaxWidth(100);
		tabComments.setPreferredSize(new Dimension(1, 150));
		tabComments.add(new JScrollPane(tblComments));
	}

	private void createInvoiceTable() {

		tblInvoices = new Table(new Object[] { "Created", "Invoice-No", "Buyer", "Total" }, false);

		tblInvoices.setToolTipText("Double click to open invoice");
		tblInvoices.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				if (me.getClickCount() == 2 && row >= 0) {

					if (tblInvoices.getModel().getValueAt(tblInvoices.getSelectedRow(), 1) != null) {
						InvoiceTool invoiceTool = new InvoiceTool(clientCtrlr, guiCtrlr);
						invoiceTool.search((String) tblInvoices.getModel().getValueAt(tblInvoices.getSelectedRow(), 1));
						guiCtrlr.add(invoiceTool);
					}
				}
			}
		});

		tabFinance.setPreferredSize(new Dimension(1, 150));
		tabFinance.add(new JScrollPane(tblInvoices));

	}

	public void getInvoices(String customerNo) {

		ArrayList<Entity> invoices = clientCtrlr.search(new Object[] { null, customerNo, null, null, null, null, null }, Eviro.ENTITY_INVOICE);

		Double balance = 0.00;
		ltfBalance.setText("0.00");

		for (int i = 0; i < invoices.size(); i++) {

			balance += Double.parseDouble((String) invoices.get(i).getData()[6]);

			Object[] invoiceData = new Object[4];

			invoiceData[0] = invoices.get(i).getData()[4];
			invoiceData[1] = invoices.get(i).getData()[0];
			invoiceData[2] = invoices.get(i).getData()[2];
			invoiceData[3] = invoices.get(i).getData()[6];

			ltfBalance.setText(Double.toString(balance));
			tblInvoices.populate(invoiceData, i);
		}

	}

	private void invoice() {

		Double balance = Double.parseDouble(ltfBalance.getText());
		Double limit = Double.parseDouble(ltfLimit.getText());

		if (limit == 0) {
			popupMessage("Unable to create invoice, customer credit limit is: 0.00:-", "Credit limit error", JOptionPane.ERROR_MESSAGE);
			// popupMessage("Unable to create invoice, customer credit limit is: 0.00:-");

		} else if (balance > limit) {

			int reply = JOptionPane.showConfirmDialog(this, "The credit limit is exceeded by " + (balance - limit) + ":-, proceed?", "Proceed?",
					JOptionPane.OK_CANCEL_OPTION);

			if (reply == JOptionPane.OK_OPTION) {

				guiCtrlr.add(new InvoiceTool(getThisTool(), clientCtrlr, guiCtrlr, ltfNo.getText()));

			}

		}

		else {
			guiCtrlr.add(new InvoiceTool(getThisTool(), clientCtrlr, guiCtrlr, ltfNo.getText()));
		}
	}

	private void reset() {

		if (tblInvoices != null)
			tblInvoices.reset();

		if (tblComments != null)
			tblComments.reset();

		setTfEditable(ltfAll, true);
		setButtons(defaultButtons);
		setTitle("Customer");

		for (int i = 0; i < ltfAll.length; i++) {

			ltfAll[i].setText(null);
		}

		ltfBalance.setText(null);

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
				if (validate(ltfRequired)) {
					ltfNo.setText(null);
					create(getThis(), Eviro.ENTITY_CUSTOMER);
				}
				break;

			case "edit":
				setButtons(editingButtons);
				setTfEditable(ltfAll, true);
				setTfEditable(ltfNo, false);
				break;

			case "update":
				if (validate(ltfRequired)) {
					if (update(getThis(), Eviro.ENTITY_CUSTOMER)) {
						setButtons(lookingButtons);
						setTfEditable(ltfAll, false);
					}
				}
				break;

			case "search":
				search(getThis(), ltfAll, Eviro.ENTITY_CUSTOMER);
				break;

			case "invoice":
				invoice();
				break;

			case "reset":
				reset();
				break;

			default:

				break;
			}
		}
	}

	@Override
	public void setValues(Object[] values) {

		getInvoices((String) values[0]);

		setTfEditable(ltfAll, false);
		setButtons(lookingButtons);
		setTitle(values[0] + " - " + values[1]);

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

	private CustomerTool getThisTool() {
		return this;
	}

	@Override
	public Updatable getThis() {
		return this;
	}
}
