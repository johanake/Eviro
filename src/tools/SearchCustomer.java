package tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
;

public class SearchCustomer extends JPanel implements Tool {
	
	
	public final String TOOLNAME = "Search Customer";
	
	private JPanel pnlNorth = new JPanel(new BorderLayout());
	private JPanel pnlNorthWest = new JPanel(new GridLayout(7,1));
	private JPanel pnlNorthCenter = new JPanel(new GridLayout(7,1));
//	private JPanel pnlNorthEast = new JPanel(new GridLayout(1,1));
	
	private JPanel pnlZipCountry = new JPanel(new GridLayout(1,2));
	private JPanel pnlSouth = new JPanel(new GridLayout(1,4));
	
	private JLabel lblCustomerID = new JLabel("Customer ID: ");
	private JLabel lblName = new JLabel("Name: ");
	private JLabel lblAddress= new JLabel("Address: ");
	private JLabel lblZipTown = new JLabel("Zip/Town: ");
	private JLabel lblPhoneNbr = new JLabel("Phone number: ");
	private JLabel lblEmail = new JLabel("Email: ");
	private JLabel lblVatNbr = new JLabel("Vat-number: ");
//	private JLabel lblCreditLimit = new JLabel("0", SwingConstants.CENTER);
	
	private JTextField txtCustomerID = new JTextField();
	private JTextField txtName = new JTextField();
	private JTextField txtAddress = new JTextField();
	private JTextField txtZipCode = new JTextField();
	private JTextField txtCity = new JTextField();
	private JTextField txtPhoneNbr = new JTextField();
	private JTextField txtEmail = new JTextField();
	private JTextField txtVATNbr = new JTextField();	
	
	private HashMap<String, String> txtList = new HashMap<String, String>();
	
	private JButton btnEdit = new JButton("Edit");
	private JButton btnUpdate = new JButton("Update");
	private JButton btnSearch = new JButton("Search");
	private JButton btnPurchase = new JButton("Purchase");
	
	
	private ClientController clientController;
	
	
	public SearchCustomer(ClientController clientController){
		this.clientController = clientController;
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(600,275));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlNorth.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlSouth.setBorder(new EmptyBorder(3, 3, 3, 3));		
		displayContent();
		addListeners();

	}	

	
	private void displayContent(){		
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlSouth, BorderLayout.SOUTH);
		
		pnlNorth.add(pnlNorthWest, BorderLayout.WEST);
		pnlNorth.add(pnlNorthCenter, BorderLayout.CENTER);	
//		pnlNorth.add(pnlNorthEast, BorderLayout.EAST);		
		
//		TitledBorder centerBorder = BorderFactory.createTitledBorder("Credit Limit");
//		centerBorder.setTitleJustification(TitledBorder.CENTER);
//		lblCreditLimit.setBorder(centerBorder);
//		pnlNorthEast.add(lblCreditLimit);
		
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
		
	}
	
	private void addListeners(){
		ButtonListener listener = new ButtonListener();
		btnSearch.addActionListener(listener);
	}
	
	private String checkFields(){
		txtList.put("CustomerID", txtCustomerID.getText());
		txtList.put("Name", txtName.getText());
		txtList.put("Address", txtAddress.getText());
		txtList.put("Zip Code", txtZipCode.getText());
		txtList.put("City", txtCity.getText());
		txtList.put("Phone number", txtPhoneNbr.getText());
		txtList.put("Email", txtEmail.getText());
		txtList.put("VAT number", txtVATNbr.getText());
		
		for(Map.Entry<String, String> entry : txtList.entrySet()){
			if(entry.getValue().trim().length() <=0){
				return "Please check following data: " + entry.getKey();
			}
		}
		return "Customer added";
		
	}
	
	private class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnSearch) {	
				System.out.println(checkFields());
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
