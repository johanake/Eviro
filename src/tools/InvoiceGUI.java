package tools;
import java.awt.BorderLayout;



import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import client.ClientController;
import gui.Tool;


/*
 * Graphical class to create invoice interface
 * @author Johan Åkesson
 */

public class InvoiceGUI extends JPanel implements Tool {
	
	private JPanel pnlTop = new JPanel(new GridLayout(1,3));
	private JPanel pnlTopLeft = new JPanel(new GridLayout(6, 1));
	private JPanel pnlTopMiddle = new JPanel(new GridLayout(6, 1));
	private JPanel pnlTopRight = new JPanel(new BorderLayout());
	private JPanel pnlZipTown = new JPanel(new GridLayout(1, 2));
	private JPanel pnlProduct = new JPanel(new BorderLayout());
	private JPanel pnlAddProduct = new JPanel(new GridLayout(1,5));
	
	private JLabel lblInvoice = new JLabel("Invoice number: ");
	private JLabel lblName = new JLabel("Name: ");
	private JLabel lblAddress = new JLabel("Address: ");
	private JLabel lblZipTown = new JLabel("ZipCode/Town: ");
	private JLabel lblContact = new JLabel("Contact: ");
	private JLabel lblInvoiceRef = new JLabel("Invoice reference: ");
	private JLabel lblProductNbr = new JLabel("Product number: ", SwingConstants.CENTER);
	private JLabel lblQuantity = new JLabel("Quantity: ", SwingConstants.CENTER);
	
	private JTextField txtInvoice = new JTextField("Invoice number");
	private JTextField txtName = new JTextField("Name");
	private JTextField txtAddress = new JTextField("Address");
	private JTextField txtZipCode = new JTextField("Zip Code");
	private JTextField txtTown = new JTextField("Town");
	private JTextField txtContact = new JTextField("Contact");
	private JTextField txtInvoiceRef = new JTextField("Invoice reference");
	private JTextField txtProductNbr = new JTextField();
	private JTextField txtQuantity = new JTextField();
	
	private JButton btnCreate = new JButton("Create");
	private JButton btnAddProduct = new JButton("ADD");

	private JTextArea txtPnProducts = new JTextArea();

	/*
	 * Constructs the GUI with swing components
	 */
	public InvoiceGUI(ClientController clientController){
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(600,300));		
		pnlTopLeft.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlTopMiddle.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlTopRight.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		
		add(pnlTop, BorderLayout.NORTH);
		pnlTop.add(pnlTopLeft);
		pnlTop.add(pnlTopMiddle);
		pnlTop.add(pnlTopRight);
		
		pnlTopLeft.add(lblInvoice);
		pnlTopLeft.add(lblName);
		pnlTopLeft.add(lblAddress);
		pnlTopLeft.add(lblZipTown);
		pnlTopLeft.add(lblContact);
		pnlTopLeft.add(lblInvoiceRef);
		
		pnlTopMiddle.add(txtInvoice);
		pnlTopMiddle.add(txtName);
		pnlTopMiddle.add(txtAddress);
		pnlTopMiddle.add(pnlZipTown);
		pnlZipTown.add(txtZipCode);
		pnlZipTown.add(txtTown);
		pnlTopMiddle.add(txtContact);
		pnlTopMiddle.add(txtInvoiceRef);
		
		pnlTopRight.add(btnCreate, BorderLayout.SOUTH);
		
		add(pnlProduct, BorderLayout.CENTER);
		pnlProduct.add(pnlAddProduct, BorderLayout.NORTH);
		pnlAddProduct.setBorder(new TitledBorder(""));
		pnlAddProduct.add(lblProductNbr);
		pnlAddProduct.add(txtProductNbr);
		pnlAddProduct.add(lblQuantity);		
		pnlAddProduct.add(txtQuantity);
		pnlAddProduct.add(btnAddProduct);
		
		pnlProduct.add(new JScrollPane(txtPnProducts));
	
		
		addListeners();
	}
	
	/*
	 * Class to enable button functions
	 */
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == btnAddProduct) {			
				txtPnProducts.append("LOL \n");
			}

		}

	}
	/*
	 * Method adds listeners
	 */
	
	private void addListeners() {
		ButtonListener listener = new ButtonListener();
		btnAddProduct.addActionListener(listener);

	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public boolean getRezizable() {
		return true;
	}

}


