package tools;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

import javax.swing.JPanel;

public class CreateCustomer extends JPanel implements Tool {
	
	public final String TOOLNAME = "Create Customer";
	
	private JPanel pnlNorth = new JPanel(new BorderLayout());
	private JPanel pnlNorthGridWest = new JPanel(new GridLayout(7,1));
	private JPanel pnlNorthGridCenter = new JPanel(new GridLayout(7,1));	
	
	private JPanel pnlZipCountry = new JPanel(new GridLayout(1,2));
	private JPanel pnlSouth = new JPanel(new BorderLayout());
	
	
	private JLabel lblName = new JLabel("Name: ");
	private JLabel lblAddress= new JLabel("Address: ");
	private JLabel lblZipTown = new JLabel("Zip/Town: ");
	private JLabel lblPhoneNbr = new JLabel("Phone number: ");
	private JLabel lblEmail = new JLabel("Email: ");
	private JLabel lblVatNbr = new JLabel("Vat-number: ");
	private JLabel lblCreditLimit = new JLabel("Credit Limit: ");
	
	
	private JTextField txtName = new JTextField();
	private JTextField txtAddress = new JTextField();
	private JTextField txtZipCode = new JTextField();
	private JTextField txtTown = new JTextField();
	private JTextField txtPhoneNbr = new JTextField();
	private JTextField txtEmail = new JTextField();
	private JTextField txtVATNbr = new JTextField();
	private JTextField txtCreditLimit = new JTextField();
	
	
	private JButton btnCreate = new JButton("Create Customer");
	
	
	private ClientController clientController;
	
	
	public CreateCustomer(ClientController clientController){
		this.clientController = clientController;
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(400,275));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlNorth.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlSouth.setBorder(new EmptyBorder(3, 3, 3, 3));
		displayContent();
		addListeners();

	}
	
	private void displayContent(){		
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlSouth, BorderLayout.SOUTH);
		
		pnlNorth.add(pnlNorthGridWest, BorderLayout.WEST);
		pnlNorth.add(pnlNorthGridCenter, BorderLayout.CENTER);		
		
		pnlNorthGridWest.add(lblName);
		pnlNorthGridWest.add(lblAddress);
		pnlNorthGridWest.add(lblZipTown);
		pnlNorthGridWest.add(lblPhoneNbr);
		pnlNorthGridWest.add(lblEmail);
		pnlNorthGridWest.add(lblVatNbr);
		pnlNorthGridWest.add(lblCreditLimit);
		
		pnlNorthGridCenter.add(txtName);
		pnlNorthGridCenter.add(txtAddress);
		pnlNorthGridCenter.add(pnlZipCountry);
		pnlZipCountry.add(txtZipCode);
		pnlZipCountry.add(txtTown);		
		pnlNorthGridCenter.add(txtPhoneNbr);
		pnlNorthGridCenter.add(txtEmail);
		pnlNorthGridCenter.add(txtVATNbr);
		pnlNorthGridCenter.add(txtCreditLimit);
		pnlSouth.add(btnCreate, BorderLayout.EAST);
		
	}
	
	private void addListeners(){
		ButtonListener listener = new ButtonListener();
		btnCreate.addActionListener(listener);
	}
	
	private boolean checkFields(){
		
		return true;
		
	}
	
	private class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnCreate) {	

			}

		}

	}

	@Override
	public String getTitle() {		
		return TOOLNAME;
	}

	@Override
	public boolean getRezizable() {
		// TODO Auto-generated method stub
		return false;
	}

}
