package tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import client.ClientController;
import enteties.Entity;
import gui.GUIController;
import gui.Tool;
import gui.Updatable;
import shared.Eviro;

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
	private JTextField[] txtAll = {
			txtArticleNumber,
			txtName,
			txtDescription,
			txtPrice,
			txtSupplier,
			txtSupplerArticleNumber,
			txtEan,
			txtStockPlace,
			txtBalance };

	private JPanel pnlNorth = new JPanel(new GridLayout(8, 1));
	private JPanel pnlSouth = new JPanel(new GridLayout(1, 3));
	private JPanel pnlNorthEast = new JPanel(new GridLayout(8, 1));
	private JPanel pnlFinal = new JPanel(new GridLayout(1, 2));
	private JPanel pnlInner = new JPanel(new GridLayout(1, 3));

	private JButton btnSearchArticle = new JButton("Search article");
	private JButton btnAdd = new JButton("Add new aricle");
	private JButton btnEdit = new JButton("Edit article");
	private JButton btnSave = new JButton("Save");
	private JButton btnClear = new JButton("Clear fields");

	private Color fieldGray = Color.getHSBColor(0, 0, Float.parseFloat("0.95"));

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

		setBorder(new EmptyBorder(10, 10, 10, 10));
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

		pnlSouth.add(btnEdit);
		pnlSouth.add(btnSave);
		pnlSouth.add(btnSearchArticle);
		pnlSouth.add(btnAdd);
		pnlSouth.add(btnClear);

		btnSave.setEnabled(show);
		btnEdit.setEnabled(false);

	}

	public void addListner() {
		ButtonListner listener = new ButtonListner();
		btnSearchArticle.addActionListener(listener);
		btnAdd.addActionListener(listener);
		btnEdit.addActionListener(listener);
		btnSave.addActionListener(listener);
		btnClear.addActionListener(listener);

	}

	public void enableButton(boolean show) {
		btnSave.setEnabled(show);
	}

	private String[] getText() {

		String[] text = new String[txtAll.length];

		for (int i = 0; i < txtAll.length; i++) {
			text[i] = txtAll[i].getText();
		}

		return text;

	}

	private void displayMessage(String txt) {
		JOptionPane.showMessageDialog(null, txt);
	}

	public String checkFields(Object[] obj, int type) {

		HashMap<String, String> txtList = new HashMap<String, String>();

		txtList.put("Name", obj[1].toString());
		txtList.put("Description", obj[2].toString());
		txtList.put("Price", obj[3].toString());
		txtList.put("Supplier", obj[4].toString());
		txtList.put("supplierArticleNumber", obj[5].toString());
		txtList.put("ean", obj[6].toString());
		txtList.put("stockPlace", obj[7].toString());
		txtList.put("saldo", obj[8].toString());

		for (Map.Entry<String, String> entry : txtList.entrySet()) {

			if (entry.getValue().trim().length() <= 0) {
				return "Please check following data: " + entry.getKey();
			}

			if (entry.getKey().equals("Zip Code") || entry.getKey().equals("Credit Limit")) {
				try {
					Integer.parseInt(entry.getValue());
				} catch (Exception e) {
					return "Please check following data: " + entry.getKey();
				}
			}
		}

		clientController.create(obj, type);
		return "Article added";

	}

	private class ButtonListner implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == btnSearchArticle) {
				searchArticle();
			} else if (e.getSource() == btnAdd) {
				add();
			} else if (e.getSource() == btnEdit) {
				edit();
			} else if (e.getSource() == btnSave) {
				save();
			} else if (e.getSource() == btnClear) {
				clear();
			}
		}
	}

	public void searchArticle() {
		ArrayList<Entity> response = clientController.search(getText(), Eviro.ENTITY_PRODUCT);

		if (response.size() == 0) {
			displayMessage("No matches, try again by changing or adding information in your search.");
		} else if (response.size() == 1) {
			setValues(response.get(0).getData());
		} else {
			Object[] searchResultColumns = new Object[] {
					"Product ID",
					"Name",
					"Description",
					"Price",
					"Supplier",
					"SupplierArticleNumber",
					"EAN",
					"Stock place",
					"Balance" };
			guiController.popup(new SearchResults(searchResultColumns, this, response));
		}
	}

	private void add() {
		txtArticleNumber.setText("");
		displayMessage(checkFields(getText(), Eviro.ENTITY_PRODUCT));
		searchArticle();
	}

	private void edit() {
		btnSave.setEnabled(!btnSave.isEnabled());
		for (JTextField t : txtAll) {
			if (!t.equals(txtArticleNumber)) {
				t.setEditable(!t.isEditable());
				if (t.getBackground() == Color.WHITE) {
					t.setBackground(fieldGray);
				} else
					t.setBackground(Color.WHITE);
			}
		}
	}

	private void save() {
		if (clientController.update(getText(), Eviro.ENTITY_PRODUCT)) {
			for (JTextField t : txtAll) {
				t.setEditable(false);
				t.setBackground(fieldGray);
			}
			btnSave.setEnabled(false);
			displayMessage("Update succesfull!");
		} else {
			setValues(clientController
					.search(new Object[] { txtArticleNumber.getText(), null, null, null, null, null, null, null, null }, Eviro.ENTITY_PRODUCT).get(0)
					.getData());
			displayMessage("Update aborted!");
		}
	}

	private void clear() {
		for (JTextField t : txtAll) {
			t.setText("");
			t.setBackground(Color.WHITE);
			t.setEditable(true);
			btnSave.setEnabled(false);
			btnEdit.setEnabled(false);
			btnAdd.setEnabled(true);
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
	public void setValues(Object[] values) {
		for (int i = 0; i < txtAll.length; i++) {

			if (values[i] instanceof Integer) {
				values[i] = Integer.toString((int) values[i]);
			}

			txtAll[i].setText((String) values[i]);
			txtAll[i].setEditable(false);
			txtAll[i].setBackground(fieldGray);
			btnAdd.setEnabled(false);
			btnEdit.setEnabled(true);

		}

	}

	@Override
	public String[] getValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Updatable getThis() {
		// TODO Auto-generated method stub
		return null;
	}

}