package tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import client.ClientController;
import gui.Tool;

public class CustomerGUI extends JPanel implements Tool {

	public final String TOOLNAME = "Customer";

	private JPanel pnlLeft = new JPanel(new GridLayout(7, 1));
	private JPanel pnlMiddle = new JPanel(new GridLayout(7, 1));
	private JPanel pnlRight = new JPanel(new GridLayout(7, 1));
	private JPanel pnlZipTown = new JPanel(new GridLayout(1, 2));
	private JLabel lblCustomerID = new JLabel("Customer ID: ");
	private JLabel lblname = new JLabel("Name: ");
	private JLabel lblAddress = new JLabel("Address: ");
	private JLabel lblZipTown = new JLabel("Zip/Town: ");
	private JLabel lblPhoneNbr = new JLabel("Phone number: ");
	private JLabel lblEmail = new JLabel("Email: ");
	private JLabel lblVatNbr = new JLabel("Vat-number: ");
	private JLabel lblCreditLimit = new JLabel("5000", SwingConstants.CENTER);
	private JTextField txtCustomerID = new JTextField("");
	private JTextField txtName = new JTextField("");
	private JTextField txtAddress = new JTextField("");
	private JTextField txtZipCode = new JTextField("");
	private JTextField txtTown = new JTextField("");
	private JTextField txtPhoneNbr = new JTextField("");
	private JTextField txtEmail = new JTextField("");
	private JTextField txtVATNbr = new JTextField("");
	private JButton txtBalance = new JButton("Balance");
	private JButton btnClosedInvoice = new JButton("Closed Invoice");
	private JButton btnOpenInvoice = new JButton("Open Invoice");
	private JButton btnSelect = new JButton("SELECT");
	private JButton btnUpdate = new JButton("UPDATE");
	private JButton btnEdit = new JButton("Edit Customer");
	private boolean editable = false;
	private ClientController clientController;

	private JButton btnCreate = new JButton("CREATE NEW");

	public CustomerGUI(ClientController clientController) {
		this.clientController = clientController;
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(600, 300));
		
		txtCustomerID = new JTextField("");
		txtName = new JTextField("");
		txtAddress = new JTextField("");
		txtZipCode = new JTextField("");
		txtTown = new JTextField("");
		txtPhoneNbr = new JTextField("");
		txtEmail = new JTextField("");
		txtVATNbr = new JTextField("");
		displayContent();
		addListeners();
		setEditable(editable);

	}

	private void displayContent() {

		add(pnlLeft, BorderLayout.WEST);
		add(pnlMiddle, BorderLayout.CENTER);
		add(pnlRight, BorderLayout.EAST);
		pnlLeft.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlMiddle.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlRight.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlLeft.add(lblCustomerID);
		pnlLeft.add(lblname);
		pnlLeft.add(lblAddress);
		pnlLeft.add(lblZipTown);
		pnlLeft.add(lblPhoneNbr);
		pnlLeft.add(lblEmail);
		pnlLeft.add(lblVatNbr);
		pnlMiddle.add(txtCustomerID);
		pnlMiddle.add(txtName);
		pnlMiddle.add(txtAddress);
		pnlMiddle.add(pnlZipTown);
		pnlZipTown.add(txtZipCode);
		pnlZipTown.add(txtTown);
		pnlMiddle.add(txtPhoneNbr);
		pnlMiddle.add(txtEmail);
		pnlMiddle.add(txtVATNbr);
		TitledBorder centerBorder = BorderFactory.createTitledBorder("Credit Limit");
		centerBorder.setTitleJustification(TitledBorder.CENTER);
		lblCreditLimit.setBorder(centerBorder);
		pnlRight.add(lblCreditLimit);
		pnlRight.add(btnClosedInvoice);
		pnlRight.add(btnOpenInvoice);
		pnlRight.add(btnSelect);
		pnlRight.add(btnUpdate);
		pnlRight.add(btnEdit);
		pnlRight.add(btnCreate);
	}

	private void setEditable(Boolean editable) {
		this.editable = editable;
		txtCustomerID.setEditable(editable);
		txtName.setEditable(editable);
		txtAddress.setEditable(editable);
		txtZipCode.setEditable(editable);
		txtTown.setEditable(editable);
		txtPhoneNbr.setEditable(editable);
		txtEmail.setEditable(editable);
		txtVATNbr.setEditable(editable);
	}

	private void addListeners() {
		ButtonListener listener = new ButtonListener();
		btnEdit.addActionListener(listener);
		btnCreate.addActionListener(listener);
		btnUpdate.addActionListener(listener);
		btnSelect.addActionListener(listener);
	}

	@Override
	public String getTitle() {
		return TOOLNAME;
	}

	@Override
	public boolean getRezizable() {
		return false;
	}

	private void setFields(HashMap<String, String> values) {

		txtCustomerID.setText(values.get("customerId"));
		txtName.setText(values.get("name"));
		txtAddress.setText(values.get("adress"));
		txtZipCode.setText(values.get("zipCode"));
		txtTown.setText(values.get("city"));
		txtPhoneNbr.setText(values.get("phoneNumber"));
		txtEmail.setText(values.get("email"));
		txtVATNbr.setText(values.get("organisationNumber"));

	}

	private void setFields() {

		txtName.setText("");
		txtAddress.setText("");
		txtZipCode.setText("");
		txtTown.setText("");
		txtPhoneNbr.setText("");
		txtEmail.setText("");
		txtVATNbr.setText("");

	}

	private class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == btnEdit) {
				if (editable == false) {
					setEditable(true);
				} else {
					setEditable(false);
				}

			} else if (e.getSource() == btnCreate) {

				// setFields();
				setEditable(true);

				setFields(clientController.createCustomer());

			} else if (e.getSource() == btnUpdate) {

				clientController.updateCustomer(Integer.parseInt(txtCustomerID.getText()), txtName.getText(),
						txtAddress.getText(), Integer.parseInt(txtZipCode.getText()), txtTown.getText(),
						txtPhoneNbr.getText(), txtEmail.getText(), Integer.parseInt(txtVATNbr.getText()));

			} else if (e.getSource() == btnSelect) {
				
				int id = Integer.parseInt(txtCustomerID.getText());
				setFields(clientController.selectCustomer(id));
			}
		}

	}



}
