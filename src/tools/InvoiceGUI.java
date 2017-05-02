package tools;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import client.ClientController;
import gui.Tool;

/*
 * Graphical class to create invoice interface
 * @author Johan Åkesson
 */

public class InvoiceGUI extends JPanel implements Tool {

	private JPanel pnlNorth = new JPanel(new BorderLayout());
	private JPanel pnlNorthSouth = new JPanel(new GridLayout(1, 5));
	private JPanel pnlNorthWest = new JPanel(new GridLayout(2, 1));
	private JPanel pnlNorthCenter = new JPanel(new GridLayout(2, 1));
	private JPanel pnlSouth = new JPanel(new BorderLayout());
	private JPanel pnlMain = new JPanel(new BorderLayout());

	private SearchResults searchResults = new SearchResults(new Object[] { "Product", "Name", "Price", "Quantity", "Sum" }, 0);

	private JLabel lblBuyer = new JLabel("Buyer: ");
	private JLabel lblReference = new JLabel("Reference: ");
	private JLabel lblAddProduct = new JLabel("Add product: ");
	private JLabel lblAddQuantity = new JLabel("Add Quantity: ");

	private JTextField txtBuyer = new JTextField();
	private JTextField txtReference = new JTextField();
	private JTextField txtAddProduct = new JTextField();
	private JTextField txtAddQuantity = new JTextField();

	private String customerId;
	private String invoiceId = "00000000"; // Ska hämtas från db i konstruktor

	private JButton btnAdd = new JButton("Add");
	private JButton btnCreate = new JButton("Create");

	private ClientController clientController;

	/*
	 * Constructs the GUI with swing components
	 */
	public InvoiceGUI(ClientController clientController, String customerId) {
		this.customerId = customerId;
		this.clientController = clientController;
		setLayout(new BorderLayout());
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlMain, BorderLayout.CENTER);
		add(pnlSouth, BorderLayout.SOUTH);
		pnlMain.add(searchResults, BorderLayout.CENTER);
		pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlNorth.setBorder(new EmptyBorder(10, 10, 10, 10));
		searchResults.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlSouth.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlNorthSouth.setBorder(new EmptyBorder(10, 10, 10, 10));

		pnlNorth.add(new JLabel("Invoice number: " + invoiceId + " Customer number: " + customerId, JLabel.CENTER), BorderLayout.NORTH);
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

		pnlSouth.add(btnCreate, BorderLayout.EAST);

		addListeners();
	}

	private void addProduct(String[] info) {
		searchResults.addArticle(info);
	}

	private String getTotalPrice() {

		int sum = 0;

		for (int i = 0; i < searchResults.getTable().getRowCount(); i++) {
			sum += Integer.parseInt((String) searchResults.getTable().getValueAt(i, 4));
		}

		return Integer.toString(sum);

	}

	/*
	 * Class to enable button functions
	 */

	private class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == btnAdd) {

				String[] info = new String[5];
				info[0] = txtAddProduct.getText();
				info[1] = "\"KING COCK 7\" VIBRATING COCK";
				info[2] = "579";
				info[3] = txtAddQuantity.getText();
				info[4] = Integer.toString(Integer.parseInt(info[2]) * Integer.parseInt(info[3]));

				addProduct(info);

			}

			if (e.getSource() == btnCreate) {
				System.out.println("Skickas till kontroller: " + Arrays.toString(create()));
				clientController.createInvoice(create());
			}

		}

		private String[] create() {

			String data[] = new String[7];

			data[0] = ""; // Id set by db.
			data[1] = customerId;
			data[2] = txtBuyer.getText();
			data[3] = txtReference.getText();
			data[4] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			data[5] = "30";
			data[6] = getTotalPrice();

			return data;
		}

	}

	/*
	 * Method adds listeners
	 */

	private void addListeners() {
		ButtonListener listener = new ButtonListener();
		btnAdd.addActionListener(listener);
		btnCreate.addActionListener(listener);

	}

	@Override
	public String getTitle() {
		return "New invoice for customer " + customerId;
	}

	@Override
	public boolean getRezizable() {
		return true;
	}

}
