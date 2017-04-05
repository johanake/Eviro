import java.awt.BorderLayout;
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
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class SearchCustomerGUI extends JPanel implements Tool {
	
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

	
	JTextField txtCustomerID = new JTextField("CustomerID");
	JTextField txtName = new JTextField("Name");
	JTextField txtAddress = new JTextField("Address");
	JTextField txtZipCode = new JTextField("ZipCode");
	JTextField txtTown = new JTextField("Town");
	JTextField txtPhoneNbr = new JTextField("Phonenumber");
	JTextField txtEmail = new JTextField("Email");
	JTextField txtVATNbr = new JTextField("VAT-nbr");
	
	JButton txtBalance = new JButton("Balance");	
	JButton btnClosedInvoice = new JButton("Closed Invoice");
	JButton btnOpenInvoice = new JButton("Open Invoice");
	JButton btnCreateInvoice = new JButton("Create Invoice");
	JButton btnComments = new JButton("Open Comments");
	JButton btnEdit = new JButton("Edit Customer");
	JButton btnCreate = new JButton("Create Customer");
	
	private boolean editable = false;

	
	public SearchCustomerGUI(){
		
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

			}

		}

	}
	
	public void addListeners() {
		ButtonListener listener = new ButtonListener();
		btnEdit.addActionListener(listener);

	}

	@Override
	public String getTitle() {
		return null;
	}

}
