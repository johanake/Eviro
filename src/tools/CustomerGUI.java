package tools;

import java.awt.BorderLayout;
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
import enteties.Customer;
import gui.GUIController;
import gui.Tool;

public class CustomerGUI extends JPanel implements Tool {

	public final String TOOLNAME = "Customer";

	private JPanel pnlNorth = new JPanel(new BorderLayout());
	private JPanel pnlNorthWest = new JPanel(new GridLayout(7, 1));
	private JPanel pnlNorthCenter = new JPanel(new GridLayout(7, 1));

	private JPanel pnlZipCountry = new JPanel(new GridLayout(1, 2));
	private JPanel pnlSouth = new JPanel(new GridLayout(1, 4));

	private JLabel lblCustomerID = new JLabel("Customer ID: ");
	private JLabel lblName = new JLabel("Name: ");
	private JLabel lblAddress = new JLabel("Address: ");
	private JLabel lblZipTown = new JLabel("Zip/Town: ");
	private JLabel lblPhoneNbr = new JLabel("Phone number: ");
	private JLabel lblEmail = new JLabel("Email: ");
	private JLabel lblVatNbr = new JLabel("Vat-number: ");
	// private JLabel lblCreditLimit = new JLabel("0", SwingConstants.CENTER);

	private JTextField txtCustomerID = new JTextField();
	private JTextField txtName = new JTextField();
	private JTextField txtAddress = new JTextField();
	private JTextField txtZipCode = new JTextField();
	private JTextField txtCity = new JTextField();
	private JTextField txtPhoneNbr = new JTextField();
	private JTextField txtEmail = new JTextField();
	private JTextField txtVATNbr = new JTextField();
	private JTextField[] txtAll = { txtCustomerID, txtName, txtAddress, txtZipCode, txtCity, txtPhoneNbr, txtEmail,
			txtVATNbr };

	private JButton btnEdit = new JButton("Edit");
	private JButton btnUpdate = new JButton("Update");
	private JButton btnSearch = new JButton("Search");
	private JButton btnPurchase = new JButton("Invoice");
	private JButton btnClear = new JButton("Clear fields");

	private ClientController clientController;
	private GUIController guiController;

	public CustomerGUI(ClientController clientController, GUIController guiController) {
		this.clientController = clientController;
		this.guiController = guiController;

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(600, 275));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlNorth.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlSouth.setBorder(new EmptyBorder(3, 3, 3, 3));

		displayContent();
		addListeners();

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

		pnlNorthCenter.add(txtCustomerID);
		pnlNorthCenter.add(txtName);
		pnlNorthCenter.add(txtAddress);
		pnlNorthCenter.add(pnlZipCountry);
		pnlZipCountry.add(txtZipCode);
		pnlZipCountry.add(txtCity);
		pnlNorthCenter.add(txtPhoneNbr);
		pnlNorthCenter.add(txtEmail);
		pnlNorthCenter.add(txtVATNbr);

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
	}

	private void setText(Object[] resultArray) {
		for (int i = 0; i < txtAll.length; i++) {
			txtAll[i].setText((String) resultArray[i]);
		}
	}

	private Object[] getText() {
		Object[] info = new Object[8];
		info[0] = txtAll[0].getText();
		info[1] = txtAll[1].getText();
		info[2] = txtAll[2].getText();
		info[3] = txtAll[3].getText();
		info[4] = txtAll[4].getText();
		info[5] = txtAll[5].getText();
		info[6] = txtAll[6].getText();
		info[7] = txtAll[7].getText();

		return info;
	
	}

	private void displayMessage(String txt) {
		JOptionPane.showMessageDialog(null, txt);
	}

	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnSearch) {
				ArrayList<Customer> customerList = clientController.searchCustomer(getText(), 0);

				if (customerList.size() == 0) {
					displayMessage("No customers found, try again by changing or adding information in your search.");
				} else if (customerList.size() == 1) {
					setText(customerList.get(0).getAllInObjects());
				} else {
					guiController.popup(new SearchResults(
							new Object[] { "Customer ID", "Name", "Address", "Zip Code", "City", "Phone number",
									"Email", "VAT number", "Credit Limit" },
							0,
							customerList));
				}
			} else if (e.getSource() == btnUpdate) {
				clientController.updateCustomer(txtAll[0].getText(), txtName.getText(), txtAddress.getText(),
						txtZipCode.getText(), txtCity.getText(), txtPhoneNbr.getText(), txtEmail.getText(),
						txtVATNbr.getText(), 0);
				displayMessage("Update succesfull!");

			} else if (e.getSource() == btnPurchase) {
				guiController.popup(new InvoiceGUI(clientController, txtCustomerID.getText()));
			} else if (e.getSource() == btnClear) {
				for(JTextField t: txtAll){
					t.setText("");
				}
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

}
