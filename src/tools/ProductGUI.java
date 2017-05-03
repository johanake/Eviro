package tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.ClientController;
import client.Eviro;
import enteties.EntityInterface;
import gui.GUIController;
import gui.Tool;
import gui.Updatable;

public class ProductGUI extends JPanel implements Tool, Updatable {

	public final String TOOLNAME = "Search Article";

	private JLabel lblArticleNumber = new JLabel("Art. nr: ");
	private JLabel lblName = new JLabel("Name");
	private JLabel lblDescription = new JLabel("Description");
	private JLabel lblPrice = new JLabel("Price");
	private JLabel lblEan = new JLabel("EAN");
	private JLabel lblSupplier = new JLabel("Supplier");
	private JLabel lblSupplerArticleNumber = new JLabel("Suppler artivle number: ");
	private JLabel lblQuantity = new JLabel("Quantity: ");
	private JLabel lblStockPlace = new JLabel("Stock place: ");

	private JTextField txtArticleNumber = new JTextField();
	private JTextField txtName = new JTextField();
	private JTextField txtDescription = new JTextField();
	private JTextField txtPrice = new JTextField();
	private JTextField txtEan = new JTextField();
	private JTextField txtSupplier = new JTextField();
	private JTextField txtSupplerArticleNumber = new JTextField();
	private JTextField txtBalance = new JTextField();
	private JTextField txtStockPlace = new JTextField();
	private JTextField[] txtAll = { txtArticleNumber, txtName, txtDescription, txtPrice, txtEan, txtSupplier, txtSupplerArticleNumber, txtBalance, txtStockPlace };

	private JPanel pnlNorth = new JPanel(new GridLayout(8, 1));
	private JPanel pnlSouth = new JPanel(new GridLayout(1, 3));
	private JPanel pnlNorthEast = new JPanel(new GridLayout(8, 1));
	private JPanel pnlFinal = new JPanel(new GridLayout(1, 2));
	private JPanel pnlInner = new JPanel(new GridLayout(1, 3));

	private JButton btnSearchArticle = new JButton("Search article");
	private JButton btnAdd = new JButton("Add new aricle");
	private JButton btnEdit = new JButton("Edit article");
	private JButton btnSave = new JButton("Save");

	private boolean show = false;

	private ClientController clientController;
	private GUIController guiController;

	public ProductGUI(ClientController clientController, GUIController guiController) {

		this.clientController = clientController;
		this.guiController = guiController;

		setLayout(new BorderLayout());

		setPreferredSize(new Dimension(600, 275));

		articleWindow();
		addListner();

	}

	public void articleWindow() {

		add(pnlFinal, BorderLayout.NORTH);
		add(pnlSouth, BorderLayout.SOUTH);

		pnlFinal.add(pnlNorth, BorderLayout.WEST);
		pnlFinal.add(pnlNorthEast, BorderLayout.CENTER);

		pnlNorth.add(lblArticleNumber);
		pnlNorth.add(lblName);
		pnlNorth.add(lblPrice);
		pnlNorth.add(lblSupplier);
		pnlNorth.add(lblSupplerArticleNumber);
		pnlNorth.add(lblDescription);
		pnlNorth.add(lblQuantity);
		pnlNorth.add(lblStockPlace);

		pnlNorthEast.add(txtArticleNumber);
		pnlNorthEast.add(txtName);
		pnlNorthEast.add(pnlInner);
		pnlInner.add(txtPrice);
		pnlInner.add(lblEan);
		pnlInner.add(txtEan);
		pnlNorthEast.add(txtSupplier);
		pnlNorthEast.add(txtSupplerArticleNumber);
		pnlNorthEast.add(txtDescription);
		pnlNorthEast.add(txtBalance);
		pnlNorthEast.add(txtStockPlace);

		pnlSouth.add(btnSearchArticle);
		pnlSouth.add(btnAdd);
		pnlSouth.add(btnEdit);
		pnlSouth.add(btnSave);
		btnSave.setEnabled(show);

	}

	public void addListner() {
		ButtonListner listener = new ButtonListner();
		btnSearchArticle.addActionListener(listener);
		btnAdd.addActionListener(listener);
		btnEdit.addActionListener(listener);
		btnSave.addActionListener(listener);

	}

	public void enableButton(boolean show) {
		btnSave.setEnabled(show);
	}

	private String[] getText() {

		String[] text = new String[txtAll.length];

		for (int i = 0; i < txtAll.length; i++) {
			text[i] = txtAll[i].getText();
		}

		System.out.println(Arrays.toString(text));

		return text;

	}

	public void searchArticle() {

		ArrayList<EntityInterface> response = clientController.search(getText(), Eviro.ENTITY_PRODUCT);

		if (response.size() == 0) {

			JOptionPane.showMessageDialog(this, "No matches, try again by changing or adding information in your search.");

		} else if (response.size() == 1) {

			updateGUI(response.get(0).getData());

		} else {

			Object[] searchResultColumns = new Object[] { "Product ID", "Name", "Description", "Price", "Supplier", "SupplierArticleNumber", "EAN", "Stock place", "Balance" };

			guiController.popup(new SearchResults(searchResultColumns, this, response));

		}

	}

	private class ButtonListner implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == btnSearchArticle) {

				searchArticle();

			} else if (e.getSource() == btnAdd) {
				// clientController.addProduct(txtName.getText(), txtPrice.getText(), txtEan.getText(), txtSupplier.getText(), txtSupplerArticleNumber.getText(), txtDescription.getText(), txtStockPlace.getText(), txtBalance.getText());
			} else if (e.getSource() == btnEdit) {
				// Kod som fyller i "låser upp" rutorna så att användaren kan redigera
				btnSave.setEnabled(true);
			} else if (e.getSource() == btnSave) {
				// Kod som sparar dne nya data, därefter så låser sig save knappen.
				btnSave.setEnabled(false);
			}
		}

	}

	@Override
	public String getTitle() {
		return TOOLNAME;

	}

	@Override
	public boolean getRezizable() {
		return false;

	}

	@Override
	public void updateGUI(Object[] values) {

		System.out.println("Update GUI with: " + Arrays.toString(values));

		for (int i = 0; i < txtAll.length; i++) {
			txtAll[i].setText((String) values[i]);
		}

	}

}