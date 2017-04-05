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


public class InvoiceGUI extends JPanel implements Tool {
	
	JPanel pnlTop = new JPanel(new GridLayout(1,3));
	JPanel pnlTopLeft = new JPanel(new GridLayout(6, 1));
	JPanel pnlTopMiddle = new JPanel(new GridLayout(6, 1));
	JPanel pnlTopRight = new JPanel(new BorderLayout());
	JPanel pnlZipTown = new JPanel(new GridLayout(1, 2));
	JPanel pnlProduct = new JPanel(new BorderLayout());
	JPanel pnlAddProduct = new JPanel(new GridLayout(1,5));
	
	JLabel lblInvoice = new JLabel("Invoice number: ");
	JLabel lblName = new JLabel("Name: ");
	JLabel lblAddress = new JLabel("Address: ");
	JLabel lblZipTown = new JLabel("ZipCode/Town: ");
	JLabel lblContact = new JLabel("Contact: ");
	JLabel lblInvoiceRef = new JLabel("Invoice reference: ");
	JLabel lblProductNbr = new JLabel("Product number: ", SwingConstants.CENTER);
	JLabel lblQuantity = new JLabel("Quantity: ", SwingConstants.CENTER);
	
	JTextField txtInvoice = new JTextField("Invoice number");
	JTextField txtName = new JTextField("Name");
	JTextField txtAddress = new JTextField("Address");
	JTextField txtZipCode = new JTextField("Zip Code");
	JTextField txtTown = new JTextField("Town");
	JTextField txtContact = new JTextField("Contact");
	JTextField txtInvoiceRef = new JTextField("Invoice reference");
	JTextField txtProductNbr = new JTextField();
	JTextField txtQuantity = new JTextField();
	
	JButton btnCreate = new JButton("Create");
	JButton btnAddProduct = new JButton("ADD");

	JTextArea txtPnProducts = new JTextArea();

	
	public InvoiceGUI(){
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
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == btnAddProduct) {			
				txtPnProducts.append("LOL \n");
			}

		}

	}
	
	public void addListeners() {
		ButtonListener listener = new ButtonListener();
		btnAddProduct.addActionListener(listener);

	}

	@Override
	public String getTitle() {
		return null;
	}

}


