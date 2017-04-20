package tools;

import java.awt.BorderLayout;

/*
 * Graphical class to create and search for customers 
 * @author Johan Åkesson
 * 
 */
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import client.ClientController;
import gui.Tool;

public class SearchCustomerGUI extends JPanel implements Tool {

	public final String TOOLNAME = "Search Customer/Create";

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

	private JTextField txtCustomerID = new JTextField("0");
	// JFormattedTextField txtCustomerID = new
	// JFormattedTextField(NumberFormat.getInstance());

	private JTextField txtName = new JTextField("Robin");
	private JTextField txtAddress = new JTextField("Nygatan");
	private JTextField txtZipCode = new JTextField("27461");
	// JFormattedTextField txtZipCode = new
	// JFormattedTextField(NumberFormat.getInstance());
	private JTextField txtTown = new JTextField("Rydsgård");
	private JTextField txtPhoneNbr = new JTextField("0761164333");
	private JTextField txtEmail = new JTextField("Email");
	private JTextField txtVATNbr = new JTextField("930116");

	private JButton txtBalance = new JButton("Balance");
	private JButton btnClosedInvoice = new JButton("Closed Invoice");
	private JButton btnOpenInvoice = new JButton("Open Invoice");
	private JButton btnCreateInvoice = new JButton("Create Invoice");
	private JButton btnComments = new JButton("Open Comments");
	private JButton btnEdit = new JButton("Edit Customer");
	private JButton btnCreate = new JButton("Create Customer");

	private ClientController clientController;

	private boolean editable = false;

	public SearchCustomerGUI(ClientController clientController) {
		this.clientController = clientController;

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(600, 300));
		displayContent();
		addListeners();
		setEditable(editable);

	}

	/*
	 * Method used to create layout and add component
	 */

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
		pnlRight.add(btnCreateInvoice);
		pnlRight.add(btnComments);
		pnlRight.add(btnEdit);
		pnlRight.add(btnCreate);

	}

	/*
	 * Method to enable or disable the editable textfields
	 */

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
	/*
	 * Method to add listeners
	 */

	private void addListeners() {
		ButtonListener listener = new ButtonListener();
		btnEdit.addActionListener(listener);
		btnCreate.addActionListener(listener);
		btnOpenInvoice.addActionListener(listener);
		btnClosedInvoice.addActionListener(listener);
		btnComments.addActionListener(listener);

	}

	@Override
	public String getTitle() {
		return TOOLNAME;
	}

	@Override
	public boolean getRezizable() {
		return false;
	}

	/*
	 * Class to enable button components
	 * 
	 * @author Johan Åkesson
	 */

	private class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == btnEdit) {
				if (editable == false) {
					setEditable(true);
				} else {
					setEditable(false);
				}

			} else if (e.getSource() == btnCreate) {
				clientController.createCustomer(Integer.parseInt(txtCustomerID.getText()), txtName.getText(),
						txtAddress.getText(), txtZipCode.getText(), txtTown.getText(), txtPhoneNbr.getText(),
						txtEmail.getText(), txtVATNbr.getText(), 50);
			} else if (e.getSource() == btnOpenInvoice) {
				clientController.getCustomer(Integer.parseInt(txtCustomerID.getText()));

			} else if (e.getSource() == btnClosedInvoice) {
				clientController.searchCustomer(Integer.parseInt(txtCustomerID.getText()), txtName.getText(),
						txtAddress.getText(), txtZipCode.getText(), txtTown.getText(), txtPhoneNbr.getText(),
						txtEmail.getText(), txtVATNbr.getText(), 50);

			} else if (e.getSource() == btnComments) {
				clientController.updateCustomer(Integer.parseInt(txtCustomerID.getText()), txtName.getText(),
						txtAddress.getText(), txtZipCode.getText(), txtTown.getText(), txtPhoneNbr.getText(),
						txtEmail.getText(), txtVATNbr.getText(), 50);

//				clientController.deleteCustomer(Integer.parseInt(txtCustomerID.getText()));
			}
		}

	}

}
