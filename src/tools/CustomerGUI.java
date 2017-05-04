package tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import client.ClientController;
import client.Eviro;
import enteties.Entity;
import gui.GUIController;
import gui.Tool;
import gui.Updatable;

public class CustomerGUI extends JPanel implements Tool, Updatable {

	public final String TOOLNAME = "Customer";

	private JPanel pnlNorth = new JPanel(new BorderLayout());
	private JPanel pnlNorthWest = new JPanel(new GridLayout(8, 1));
	private JPanel pnlNorthCenter = new JPanel(new GridLayout(8, 1));

	private JPanel pnlZipCountry = new JPanel(new GridLayout(1, 2));
	private JPanel pnlSouth = new JPanel(new GridLayout(1, 4));

	private JLabel lblCustomerID = new JLabel("Customer ID: ");
	private JLabel lblName = new JLabel("Name: ");
	private JLabel lblAddress = new JLabel("Address: ");
	private JLabel lblZipTown = new JLabel("Zip/Town: ");
	private JLabel lblPhoneNbr = new JLabel("Phone number: ");
	private JLabel lblEmail = new JLabel("Email: ");
	private JLabel lblVatNbr = new JLabel("Vat-number: ");
	private JLabel lblCreditLimit = new JLabel("Credit-limit: ");

	private JTextField txtCustomerID = new JTextField();
	private JTextField txtName = new JTextField();
	private JTextField txtAddress = new JTextField();
	private JTextField txtZipCode = new JTextField();
	private JTextField txtCity = new JTextField();
	private JTextField txtPhoneNbr = new JTextField();
	private JTextField txtEmail = new JTextField();
	private JTextField txtVATNbr = new JTextField();
	private JTextField txtCreditLimit = new JTextField();
	private JTextField[] txtAll = { txtCustomerID, txtName, txtAddress, txtZipCode, txtCity, txtPhoneNbr, txtEmail,
			txtVATNbr, txtCreditLimit };

	private JButton btnEdit = new JButton("Edit");
	private JButton btnUpdate = new JButton("Update");
	private JButton btnSearch = new JButton("Search");
	private JButton btnPurchase = new JButton("Invoice");
	private JButton btnClear = new JButton("Clear fields");

	private Color fieldGray = Color.getHSBColor(0, 0, Float.parseFloat("0.95"));

	private ClientController clientController;
	private GUIController guiController;

	public CustomerGUI(ClientController clientController, GUIController guiController) {
		this.clientController = clientController;
		this.guiController = guiController;

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(500, 300));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlNorth.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlSouth.setBorder(new EmptyBorder(3, 3, 3, 3));

		displayContent();
		addListeners();

	}

	private CustomerGUI getCustomerGUI() {
		return this;
	}

	private void displayContent() {
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlSouth, BorderLayout.SOUTH);

		pnlNorth.add(pnlNorthWest, BorderLayout.WEST);
		pnlNorth.add(pnlNorthCenter, BorderLayout.CENTER);

		pnlNorthWest.add(lblCustomerID);
		pnlNorthWest.add(lblName);
		pnlNorthWest.add(lblAddress);
		pnlNorthWest.add(lblZipTown);
		pnlNorthWest.add(lblPhoneNbr);
		pnlNorthWest.add(lblEmail);
		pnlNorthWest.add(lblVatNbr);
		pnlNorthWest.add(lblCreditLimit);

		pnlNorthCenter.add(txtCustomerID);
		pnlNorthCenter.add(txtName);
		pnlNorthCenter.add(txtAddress);
		pnlNorthCenter.add(pnlZipCountry);
		pnlZipCountry.add(txtZipCode);
		pnlZipCountry.add(txtCity);
		pnlNorthCenter.add(txtPhoneNbr);
		pnlNorthCenter.add(txtEmail);
		pnlNorthCenter.add(txtVATNbr);
		pnlNorthCenter.add(txtCreditLimit);

		pnlSouth.add(btnEdit);
		pnlSouth.add(btnUpdate);
		pnlSouth.add(btnSearch);
		pnlSouth.add(btnPurchase);
		pnlSouth.add(btnClear);

	}

	private void addListeners() {
		ButtonListener listener = new ButtonListener();
		btnSearch.addActionListener(listener);
		btnUpdate.addActionListener(listener);
		btnPurchase.addActionListener(listener);
		btnClear.addActionListener(listener);
		btnEdit.addActionListener(listener);

	}

	private String[] getText() {

		String[] text = new String[txtAll.length];

		for (int i = 0; i < txtAll.length; i++) {
			text[i] = txtAll[i].getText();
		}

		return text;

	}

	private void displayMessage(String txt) {
		JOptionPane.showMessageDialog(null, txt);

	}

	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == btnSearch) {
				search();
			} else if (e.getSource() == btnUpdate) {
				update();
			} else if (e.getSource() == btnPurchase) {
				guiController.popup(new InvoiceGUI(clientController, txtCustomerID.getText()));
			} else if (e.getSource() == btnClear) {
				clear();
			} else if (e.getSource() == btnEdit) {
				edit();
			}
		}
	}
	private void search(){
		ArrayList<Entity> customerList = clientController.search(getText(), Eviro.ENTITY_CUSTOMER);

		if (customerList.size() == 0) {
			displayMessage("No matches, try again by changing or adding information in your search.");
		} else if (customerList.size() == 1) {
			updateGUI(customerList.get(0).getData());
		} else {
			guiController.popup(new SearchResults(new Object[] { "Customer ID", "Name", "Address", "Zip Code",
					"City", "Phone number", "Email", "VAT number", "Credit Limit" }, getCustomerGUI(),
					customerList));
		}
	}
	
	private void update() {
		if (clientController.update(getText(), Eviro.ENTITY_CUSTOMER)) {
			for (JTextField t : txtAll) {
				t.setEditable(false);
				t.setBackground(fieldGray);
			}
			displayMessage("Update succesfull!");
		} else {
			updateGUI(clientController.search(new Object[] {txtCustomerID.getText(),null,null,null,null,null,null,null,null}, Eviro.ENTITY_CUSTOMER).get(0).getData());
			displayMessage("Update aborted!");
		}			
	}
	
	private void clear() {
		for (JTextField t : txtAll) {
			t.setText("");
			t.setBackground(Color.WHITE);
			t.setEditable(true);
		}			
	}
	
	private void edit() {
		for (JTextField t : txtAll) {
			if (!t.equals(txtCustomerID)) {
				t.setEditable(!t.isEditable());
				if (t.getBackground() == Color.WHITE) {
					t.setBackground(fieldGray);
				} else
					t.setBackground(Color.WHITE);
			}
		}			
	}

	@Override
	public String getTitle() {
		return TOOLNAME;
	}

	@Override
	public boolean getRezizable() {
		return true;
	}

	@Override
	public void updateGUI(Object[] values) {

		for (int i = 0; i < txtAll.length; i++) {

			if (values[i] instanceof Integer) {
				values[i] = Integer.toString((int) values[i]);
			}

			txtAll[i].setText((String) values[i]);
			txtAll[i].setBackground(fieldGray);
			txtAll[i].setEditable(false);
		}

	}

}
