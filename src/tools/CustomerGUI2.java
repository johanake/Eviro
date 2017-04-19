package tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import client.ClientController;
import gui.Tool;

public class CustomerGUI2 extends JPanel implements Tool {

	public final String TOOLNAME = "Kund";

	private ClientController controller;
	
	private JPanel pnlNorth = new JPanel(new BorderLayout());
	private JPanel pnlInfo = new JPanel(new BorderLayout());
	private JPanel pnlInfoCenter;
	private JPanel pnlInfoNorth = new JPanel(new BorderLayout());
	private JPanel pnlInfoNorthLeft = new JPanel(new GridLayout(8,1,0,0));
	private JPanel pnlInfoNorthRight = new JPanel(new GridLayout(8,1,0,0));
	
	private JPanel pnlFinance = new JPanel(new BorderLayout());
	private JPanel pnlFinanceCenter;
	private JPanel pnlFinanceNorth = new JPanel(new BorderLayout());
	private JPanel pnlFinanceNorthLeft = new JPanel(new GridLayout(1,1,0,0));
	private JPanel pnlFinanceNorthRight = new JPanel(new GridLayout(1,1,0,0));
	
	private JPanel pnlComments = new JPanel(new BorderLayout());
	private JTextField txtIdNbr = new JTextField("Kundnummer:");
	private JTextField txtName = new JTextField("Namn:");
	private JTextField txtAddress = new JTextField("Adress:");
	private JTextField txtCity = new JTextField("Stad:");
	private JTextField txtZipNbr = new JTextField("Postnummer:");
	private JTextField txtPhoneNbr = new JTextField("Telefonnummer:");
	private JTextField txtEmail = new JTextField("Epost:");
	private JTextField txtVatNbr = new JTextField("Org-nummer:");
	private JTextField[] txtInfoAll = {txtIdNbr,txtName,txtAddress,txtCity,txtZipNbr,txtPhoneNbr,txtEmail,txtVatNbr};

	private JButton btnNew = new JButton("Skapa ny");
	private JButton btnGet = new JButton("Sök kund");
	private JButton btnSave = new JButton("Spara");
	private JButton[] btnAll = {btnNew,btnGet,btnSave};
	
	private JLabel lblIdNbr = new JLabel("Ingen kund vald, skapa eller hämta en kund nedan.");
	private JLabel lblName = new JLabel("");
	

	public CustomerGUI2(ClientController controller) {
		this.controller = controller;
		new ButtonListener();
		setupLayout();
	}
	
	public CustomerGUI2(ClientController controller, int IdNbr) {
		this(controller);
		setValues(controller.selectCustomers(IdNbr));
	}

	private void setupLayout() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(400,350));
		pnlNorth.setBorder(BorderFactory.createEmptyBorder(10, 10, 6, 10));
		pnlNorth.add(lblIdNbr, BorderLayout.WEST);	
		pnlNorth.add(lblName, BorderLayout.CENTER);	
		pnlInfoNorthLeft.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
		addLabeledTextFields(pnlInfoNorthLeft,pnlInfoNorthRight,txtInfoAll);
		pnlInfoNorth.add(pnlInfoNorthLeft, BorderLayout.WEST);
		pnlInfoNorth.add(pnlInfoNorthRight, BorderLayout.CENTER);
		pnlInfoNorth.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pnlInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		pnlInfo.add(pnlInfoNorth, BorderLayout.NORTH);
		pnlInfoCenter = Buttons();
		pnlInfo.add(pnlInfoCenter, BorderLayout.SOUTH);
		add(pnlNorth, BorderLayout.NORTH);
		add(TabbedPane());
	}
	
	private void addLabeledTextFields(JPanel left, JPanel right, JTextField[] textFields) {
		
		for (JTextField txt : textFields) {
			left.add(new JLabel(txt.getText()));
			right.add(txt);
			txt.setText("");
		}

	}

	private JPanel Buttons() {

		JPanel pnl = new JPanel(new GridLayout());
		pnl.setBorder(BorderFactory.createCompoundBorder(
		BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(146, 151, 161)),
		BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		pnl.add(btnNew);
		pnl.add(btnGet);
		pnl.add(btnSave);
		
		return pnl;

	}

	private JTabbedPane TabbedPane() {

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Information", pnlInfo);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		tabbedPane.addTab("Ekonomi", pnlFinance);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		tabbedPane.addTab("Kommentarer", pnlComments);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		return tabbedPane;

	}

	private void setValues(ArrayList<HashMap> values) {

		if (values.size() > 1) {
			
			for (HashMap map : values) {
				
				System.out.println(map);

			}
			
			
		}
		else {
			
			HashMap<String, String> customerData = values.get(0);
				
				txtIdNbr.setText(customerData.get("customerId"));
				txtName.setText(customerData.get("name"));
				txtAddress.setText(customerData.get("adress"));
				txtZipNbr.setText(customerData.get("zipCode"));
				txtCity.setText(customerData.get("city"));
				txtPhoneNbr.setText(customerData.get("phoneNumber"));
				txtEmail.setText(customerData.get("email"));
				txtVatNbr.setText(customerData.get("organisationNumber"));
				lblIdNbr.setText(customerData.get("customerId"));
				lblName.setText(" " + txtName.getText());
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

	private class ButtonListener implements ActionListener {

		public ButtonListener() {
			for (JButton btn : btnAll) {
				btn.addActionListener(this);
			}
		}

		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource() == btnNew) {
				setValues(controller.createCustomer());
			}
			
			if (e.getSource() == btnGet) {
//				setValues(controller.selectCustomers(Integer.parseInt(txtIdNbr.getText())));
				
				if (txtIdNbr.getText().length() > 0) {
					int id = Integer.parseInt(txtIdNbr.getText());
					setValues(controller.selectCustomers(id));
				}
				
				else {
					TreeMap<String, String> args = new TreeMap<String, String>();
				
					args.put("name", txtName.getText());
					args.put("adress", txtAddress.getText());
					args.put("zipCode", txtZipNbr.getText());
					args.put("city", txtCity.getText());
					args.put("phoneNumber", txtPhoneNbr.getText());
					args.put("email", txtEmail.getText());
					args.put("organisationNumber", txtVatNbr.getText());
					
					setValues(controller.selectCustomers(args));
				}

				
			}
			
			if (e.getSource() == btnSave) {
				controller.updateCustomer(Integer.parseInt(lblIdNbr.getText()), txtName.getText(),
						txtAddress.getText(), Integer.parseInt(txtZipNbr.getText()), txtCity.getText(),
						txtPhoneNbr.getText(), txtEmail.getText(), Integer.parseInt(txtVatNbr.getText()));
			}
			
		}

	}
}
