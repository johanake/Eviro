package tools;
import java.awt.BorderLayout;



import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
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
	
	private JPanel pnlNorth = new JPanel(new BorderLayout());
	private JPanel pnlNorthSouth = new JPanel(new GridLayout(2,5));
	private JPanel pnlNorthWest = new JPanel(new GridLayout(2,1));
	private JPanel pnlNorthCenter = new JPanel(new GridLayout(2,1));	
	private JPanel pnlSouth = new JPanel(new BorderLayout());
	private JPanel pnlMain = new JPanel(new BorderLayout());
	
	private JTextArea txtMain = new JTextArea();
	
	private JLabel lblBuyer = new JLabel("Buyer: ");
	private JLabel lblReference = new JLabel("Reference: ");
	private JLabel lblAddProduct = new JLabel("Add product: ");
	private JLabel lblAddQuantity = new JLabel("Add Quantity: ");
	private JLabel lblProduct = new JLabel("Product");
	private JLabel lblName = new JLabel("Name");
	private JLabel lblPrice = new JLabel("Price");
	private JLabel lblQuantity = new JLabel("Quantity");
	private JLabel lblSum = new JLabel("Sum");
	
	private JTextField txtBuyer = new JTextField();
	private JTextField txtReference = new JTextField();
	private JTextField txtAddProduct = new JTextField();
	private JTextField txtAddQuantity = new JTextField();
	
	private JButton btnAdd = new JButton("Add");
	private JButton btnCreate = new JButton("Create");
	



	/*
	 * Constructs the GUI with swing components
	 */
	public InvoiceGUI(ClientController clientController){
		setLayout(new BorderLayout());
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlMain, BorderLayout.CENTER);
		add(pnlSouth, BorderLayout.SOUTH);
		pnlMain.add(txtMain, BorderLayout.CENTER);
		pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlNorth.setBorder(new EmptyBorder(10, 10, 10, 10));
		txtMain.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlSouth.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		pnlNorth.add(pnlNorthSouth, BorderLayout.SOUTH);
		pnlNorth.add(pnlNorthWest, BorderLayout.WEST);
		pnlNorth.add(pnlNorthCenter, BorderLayout.CENTER);
		
		pnlNorthWest.add(lblBuyer);
		pnlNorthWest.add(lblReference);
		
		pnlNorthCenter.add(txtBuyer);
		pnlNorthCenter.add(txtReference);
		
		pnlNorthSouth.add(lblAddProduct);
		pnlNorthSouth.add(txtAddProduct);
		pnlNorthSouth.add(lblAddQuantity);
		pnlNorthSouth.add(txtAddQuantity);
		pnlNorthSouth.add(btnAdd);
		pnlNorthSouth.add(lblProduct);
		pnlNorthSouth.add(lblName);
		pnlNorthSouth.add(lblPrice);
		pnlNorthSouth.add(lblQuantity);
		pnlNorthSouth.add(lblSum);
		
		pnlSouth.add(btnCreate, BorderLayout.EAST);
		

//		addListeners();
	}
	
	/*
	 * Class to enable button functions
	 */
	
//	private class ButtonListener implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//
//			if (e.getSource() ==  ) {			
//			}
//
//		}
//
//	}
//	/*
//	 * Method adds listeners
//	 */
//	
//	private void addListeners() {
//		ButtonListener listener = new ButtonListener();
//		btnAddProduct.addActionListener(listener);
//
//	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public boolean getRezizable() {
		return true;
	}

}


