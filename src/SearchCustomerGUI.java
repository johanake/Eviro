import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class SearchCustomerGUI extends JPanel{
	JPanel pnlLeft = new JPanel(new GridLayout(7, 1));
	JPanel pnlMiddle = new JPanel(new GridLayout(7, 1));
	JPanel pnlRight = new JPanel(new GridLayout(7, 1));
	JPanel pnlZipTown = new JPanel(new GridLayout(1, 2));
	JPanel empty = new JPanel();
	
	JLabel lblCustomerID = new JLabel("Customer ID: ");
	JLabel lblname = new JLabel("Name: ");
	JLabel lblAddress= new JLabel("Address: ");
	JLabel lblZipTown = new JLabel("Zip/Town: ");
	JLabel lblPhoneNbr = new JLabel("Phone number: ");
	JLabel lblEmail = new JLabel("Email: ");
	JLabel lblVatNbr = new JLabel("Vat-number: ");

	
	JTextField txtcustomerID = new JTextField("CustomerID");
	JTextField txtname = new JTextField("Name");
	JTextField txtaddress = new JTextField("Address");
	JTextField txtzipCode = new JTextField("ZipCode");
	JTextField txttown = new JTextField("Town");
	JTextField txtphoneNbr = new JTextField("Phonenumber");
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
		
		pnlMiddle.add(txtcustomerID);
		pnlMiddle.add(txtname);
		pnlMiddle.add(txtaddress);
		pnlMiddle.add(pnlZipTown);		
		pnlZipTown.add(txtzipCode);
		pnlZipTown.add(txttown);
		pnlMiddle.add(txtphoneNbr);	
		pnlMiddle.add(txtEmail);
		pnlMiddle.add(txtVATNbr);
		
		pnlRight.add(btnClosedInvoice);
		pnlRight.add(btnOpenInvoice);
		pnlRight.add(btnCreateInvoice);
		pnlRight.add(btnComments);
		pnlRight.add(btnEdit);
		pnlRight.add(btnCreate);
		
		
	}
	
	public void setEditable(Boolean editable){
		this.editable = editable;
		txtcustomerID.setEditable(editable);
		txtname.setEditable(editable);
		txtaddress.setEditable(editable);
		txtzipCode.setEditable(editable);
		txttown.setEditable(editable);
		txtphoneNbr.setEditable(editable);
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

}
