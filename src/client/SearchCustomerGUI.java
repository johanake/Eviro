package client;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class SearchCustomerGUI extends JPanel implements Tool {
	
	public final String TOOL_NAME = "Search Customer/Create";
	
	JPanel pnlLeft = new JPanel(new GridLayout(7, 1));
	JPanel pnlMiddle = new JPanel(new GridLayout(7, 1));
	JPanel pnlRight = new JPanel(new GridLayout(7, 1));
	JPanel pnlZipTown = new JPanel(new GridLayout(1, 2));
	
	JLabel lblCustomerID = new JLabel("Customer ID: ");
	JLabel lblname = new JLabel("Name: ");
	JLabel lblAddress= new JLabel("Address: ");
	JLabel lblZipTown = new JLabel("Zip/Town: ");
	JLabel lblPhoneNbr = new JLabel("Phone number: ");
	JLabel lblEmail = new JLabel("Email: ");
	JLabel lblVatNbr = new JLabel("Vat-number: ");
	JLabel lblCreditLimit = new JLabel("5000", SwingConstants.CENTER);

	
	JTextField txtCustomerID = new JTextField("0");
//	JFormattedTextField txtCustomerID = new JFormattedTextField(NumberFormat.getInstance());
	
	JTextField txtName = new JTextField("Robin");
	JTextField txtAddress = new JTextField("Nygatan");
	JTextField txtZipCode = new JTextField("27461");
//	JFormattedTextField txtZipCode = new JFormattedTextField(NumberFormat.getInstance());
	JTextField txtTown = new JTextField("Rydsgård");
	JTextField txtPhoneNbr = new JTextField("0761164333");
	JTextField txtEmail = new JTextField("Email");
	JTextField txtVATNbr = new JTextField("930116");
	
	JButton txtBalance = new JButton("Balance");	
	JButton btnClosedInvoice = new JButton("Closed Invoice");
	JButton btnOpenInvoice = new JButton("Open Invoice");
	JButton btnCreateInvoice = new JButton("Create Invoice");
	JButton btnComments = new JButton("Open Comments");
	JButton btnEdit = new JButton("Edit Customer");
	JButton btnCreate = new JButton("Create Customer");
	
	ClientController clientController; 
	
	private boolean editable = false;

	
	public SearchCustomerGUI(ClientController clientController){
		this.clientController = clientController; 
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setLayout(new BorderLayout());
				setPreferredSize(new Dimension(600,300));
				
				displayContent();
				addListeners();
				setEditable(editable);


			}
		});
		
	}
	
	public void displayContent(){
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
	
	public void setEditable(Boolean editable){
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
	
	
	
	private class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == btnEdit) {
				if(editable == false){
					setEditable(true);
				}else{
					setEditable(false);
				}

			}else if(e.getSource() == btnCreate){
				clientController.createCustomer(Integer.parseInt(txtCustomerID.getText()), txtName.getText(), txtAddress.getText(), Integer.parseInt(txtZipCode.getText()), txtTown.getText(), txtPhoneNbr.getText(), txtEmail.getText(), Integer.parseInt(txtVATNbr.getText()));
			}

		}

	}
	
	public void addListeners() {
		ButtonListener listener = new ButtonListener();
		btnEdit.addActionListener(listener);
		btnCreate.addActionListener(listener);

	}

	@Override
	public String getTitle() {
		return TOOL_NAME;
	}

	@Override
	public boolean getRezizable() {
		return false;
	}

}
