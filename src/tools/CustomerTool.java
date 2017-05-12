package tools;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import client.ClientController;
import gui.GUIController;
import gui.Table;
import gui.Tool;
import gui.Updatable;
import shared.Eviro;

public class CustomerTool extends Tool implements Updatable {

	private ButtonListener buttonListener;
	
	Tab tabComments = new Tab("Comments");
	Table tblComments = new Table(this, new Object[]{"Date", "Comments"});
	
	private Tab[] tabs = new Tab[] { new Tab("General"), new Tab("Finance"), tabComments };

	private LabledTextField ltfNo = new LabledTextField("No");
	private LabledTextField ltfName = new LabledTextField("Name");
	private LabledTextField ltfAddress = new LabledTextField("Address");
	private LabledTextField ltfZip = new LabledTextField("Post Code");
	private LabledTextField ltfCity = new LabledTextField("City");
	private LabledTextField ltfPhone = new LabledTextField("Phone No");
	private LabledTextField ltfEmail = new LabledTextField("Email");
	private LabledTextField ltfVat = new LabledTextField("Vat No");
	private LabledTextField ltfLimit = new LabledTextField("Limit");
	private LabledTextField ltfBalance = new LabledTextField("Balance", false);
	private LabledTextField[] ltfAll = { ltfNo, ltfName, ltfAddress, ltfZip, ltfCity, ltfPhone, ltfEmail, ltfVat, ltfLimit };

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
		
		tabComments.setPreferredSize(new Dimension(1, 150));
		tabComments.add(new JScrollPane(tblComments));
		tblComments.getColumnModel().getColumn(0).setMinWidth(50);
		tblComments.getColumnModel().getColumn(0).setMaxWidth(50);
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
				ltfNo.setText(null);
				create(getThis(), Eviro.ENTITY_CUSTOMER);
				break;

			case "edit":
				setButtons(editingButtons);
				setTfEditable(ltfAll, true);
				setTfEditable(ltfNo, false);
				break;

			case "update":
				if (update(getThis(), Eviro.ENTITY_CUSTOMER)) {
					setButtons(lookingButtons);
					setTfEditable(ltfAll, false);
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

		String[] text = new String[ltfAll.length];

		for (int i = 0; i < ltfAll.length; i++) {
			text[i] = ltfAll[i].getText();
		}

		return text;

	}

	@Override
	public Updatable getThis() {
		return this;
	}
}
