package tools;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;

import client.ClientController;
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
	private LabledTextField ltfZip = new LabledTextField("Post Code");
	private LabledTextField ltfCity = new LabledTextField("City");
	private LabledTextField ltfPhone = new LabledTextField("Phone No");
	private LabledTextField ltfEmail = new LabledTextField("Email");
	private LabledTextField ltfVat = new LabledTextField("Vat No");
	private LabledTextField ltfLimit = new LabledTextField("Limit", true, Eviro.VALIDATOR_INTEGER);
	private LabledTextField ltfBalance = new LabledTextField("Balance", false);
	private LabledTextField[] ltfAll = { ltfNo, ltfName, ltfAddress, ltfZip, ltfCity, ltfPhone, ltfEmail, ltfVat, ltfLimit };
	private LabledTextField[] ltfRequired = { ltfName, ltfAddress, ltfZip, ltfCity, ltfPhone, ltfLimit };

	private ActionButton btnNew = new ActionButton("Create New", "create");
	private ActionButton btnEdit = new ActionButton("Edit", "edit");
	private ActionButton btnUpdate = new ActionButton("Save", "update");
	private ActionButton btnFind = new ActionButton("Find", "search");
	private ActionButton btnInvoice = new ActionButton("Invoice", "invoice");
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

		createCommentsTable();
		createInvoiceTable();

	}

	private void createCommentsTable() {
		tblComments = new Table(new Object[] { "Date", "Comment" }, true) {
			@Override
			public void editingStopped(ChangeEvent e) {
				int row = getEditingRow();
				int col = getEditingColumn();
				super.editingStopped(e);
				getColumnModel().getColumn(0).setWidth(100);
				getColumnModel().getColumn(0).setMaxWidth(100);

				// TODO Beteende här!

			}
		};

		tabComments.setPreferredSize(new Dimension(1, 150));
		tabComments.add(new JScrollPane(tblComments));
	}

	private void createInvoiceTable() {

		tblInvoices = new Table(new Object[] { "Invoice-No", "Buyer", "Created", "Total" }, false) {
			@Override
			public void editingStopped(ChangeEvent e) {
				int row = getEditingRow();
				int col = getEditingColumn();
				super.editingStopped(e);

				// TODO Beteende här!

			}
		};

		tblInvoices.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				if (me.getClickCount() == 2 && row >= 0) {
					// TODO Beteende här!
				}
			}
		});

		tabFinance.add(new JScrollPane(tblInvoices));

	}

	private void invoice() {
		guiCtrlr.add(new InvoiceTool(clientCtrlr, guiCtrlr, ltfNo.getText()));
	}

	private void reset() {

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

	@Override
	public Updatable getThis() {
		return this;
	}
}
