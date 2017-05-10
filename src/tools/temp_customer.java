package tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;

import client.ClientController;
import client.Eviro;
import enteties.Entity;
import gui.GUIController;
import gui.SuperTool;
import gui.Updatable;

public class temp_customer extends SuperTool implements Updatable {

	private ButtonListener buttonListener;
	private Tab[] tabs = new Tab[] { new Tab("General"), new Tab("Finance"), new Tab("Comments") };

	private LabledTextField cust_no = new LabledTextField("No");
	private LabledTextField cust_name = new LabledTextField("Name");
	private LabledTextField cust_address = new LabledTextField("Address");
	private LabledTextField cust_zip = new LabledTextField("Post Code");
	private LabledTextField cust_city = new LabledTextField("City");
	private LabledTextField cust_phone = new LabledTextField("Phone No");
	private LabledTextField cust_email = new LabledTextField("Email");
	private LabledTextField cust_vat = new LabledTextField("Vat No");
	private LabledTextField cust_limit = new LabledTextField("Limit");
	private LabledTextField cust_balance = new LabledTextField("Balance", false);

	private LabledTextField[] cust_info = { cust_no, cust_name, cust_address, cust_zip, cust_city, cust_phone, cust_email, cust_vat, cust_limit };

	private JButton[] buttons = new JButton[] {
			new ActionButton("Edit", "edit"),
			new ActionButton("Update", "update"),
			new ActionButton("Search", "search"),
			new ActionButton("Invoice", "invoice"),
			new ActionButton("Clear fields", "reset"), };

	public temp_customer(ClientController clientController, GUIController guiController) {
		super("Customer", clientController, guiController);
		buttonListener = new ButtonListener();
		setTabs(tabs);
		setContent(0, new JComponent[] { cust_no, cust_name, cust_address, new SplitPanel(cust_zip, cust_city), cust_phone, cust_email, cust_vat });
		setContent(1, new JComponent[] { new SplitPanel(cust_limit, cust_balance) });
		setButtons(buttons);
	}

	private String[] getText() {

		String[] text = new String[cust_info.length];

		for (int i = 0; i < cust_info.length; i++) {
			text[i] = cust_info[i].getText();
		}

		return text;

	}

	private void search() {

		ArrayList<Entity> customerList = clientCtrlr.search(getText(), Eviro.ENTITY_CUSTOMER);

		if (customerList.size() == 0) {
			popupMessage("No matches, try again by changing or adding information in your search.");
		} else if (customerList.size() == 1) {
			updateGUI(customerList.get(0).getData());
		} else {
			guiCtrlr.popup(new SearchResults(
					new Object[] { "Customer ID", "Name", "Address", "Zip Code", "City", "Phone number", "Email", "VAT number", "Credit Limit" },
					this, customerList));
		}
	}

	private void invoice() {
		guiCtrlr.popup(new InvoiceGUI(clientCtrlr, cust_no.getText()));
	}

	private void reset() {

		for (int i = 0; i < cust_info.length; i++) {

			cust_info[i].setText(null);
			// cust_info[i].setBackground(fieldGray);
			// cust_info[i].setEditable(false);
		}

		cust_balance.setText(null);

	}

	private void update() {
		if (clientCtrlr.update(this, getText(), Eviro.ENTITY_CUSTOMER)) {
			// for (JTextField t : txtAll) {
			// t.setEditable(false);
			// t.setBackground(fieldGray);
			// }
			popupMessage("Update succesfull!");
		} else {
			updateGUI(clientCtrlr.search(new Object[] { cust_no.getText(), null, null, null, null, null, null, null, null }, Eviro.ENTITY_CUSTOMER)
					.get(0).getData());
			popupMessage("Update aborted!");
		}
	}

	private class ButtonListener implements ActionListener {

		public ButtonListener() {
			for (JButton btn : buttons) {
				btn.addActionListener(this);
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			System.out.println(e.getActionCommand());

			switch (e.getActionCommand()) {

			case "edit":

				break;

			case "update":
				update();
				break;

			case "search":
				search();
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
	public void updateGUI(Object[] values) {
		for (int i = 0; i < cust_info.length; i++) {

			if (values[i] instanceof Integer) {
				values[i] = Integer.toString((int) values[i]);
			}

			cust_info[i].setText((String) values[i]);
			// cust_info[i].setBackground(fieldGray);
			// cust_info[i].setEditable(false);
		}

	}
}
