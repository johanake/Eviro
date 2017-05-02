package tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.ClientController;
import gui.Tool;

public class SearchArticle extends JPanel implements Tool {

	public final String TOOLNAME = "Search Article";

	private JLabel lblArticleNumber = new JLabel("Art. nr: ");
	private JLabel lblName = new JLabel("Name");
	private JLabel lblDescription = new JLabel("Description");
	private JLabel lblPrice = new JLabel("Price");
	private JLabel lblEan = new JLabel("EAN");
	private JLabel lblSupplier = new JLabel("Supplier");
	private JLabel lblSupplerArticleNumber = new JLabel("Suppler artivle number: ");
	private JLabel lblQuantity = new JLabel("Quantity: ");

	private JTextField txtArticleNumber = new JTextField("Art. nr");
	private JTextField txtName = new JTextField("Name");
	private JTextField txtDescription = new JTextField("Description");
	private JTextField txtPrice = new JTextField("Price");
	private JTextField txtEan = new JTextField("EAN");
	private JTextField txtSupplier = new JTextField("Supplier");
	private JTextField txtSupplerArticleNumber = new JTextField("Supplier article numver");
	private JTextField txtQuantity = new JTextField("Quantity // Kolla upp");

	private JPanel pnlNorth = new JPanel();
	private JPanel pnlSouth = new JPanel();
	// private JPanel pnlFinal = new JPanel();

	private JButton btnSearchArticle = new JButton("Search article");
	private JButton btnAdd = new JButton("Add new aricle");
	private JButton btnEdit = new JButton("Edit article");

	private ClientController clientController;

	public SearchArticle(ClientController clientController) {
		this.clientController = clientController;
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(600, 275));
		articleWindow();
	}

	public void articleWindow() {
		setLayout(new BorderLayout());

		add(pnlNorth, BorderLayout.NORTH);
		add(pnlSouth, BorderLayout.SOUTH);

		pnlNorth.add(lblArticleNumber);
		pnlNorth.add(txtArticleNumber);
		pnlNorth.add(lblName);
		pnlNorth.add(txtName);
		pnlNorth.add(lblName);
		pnlNorth.add(lblPrice);
		pnlNorth.add(txtPrice);
		pnlNorth.add(lblEan);
		pnlNorth.add(txtEan);
		pnlNorth.add(lblSupplier);
		pnlNorth.add(txtSupplier);
		pnlNorth.add(lblSupplerArticleNumber);
		pnlNorth.add(txtSupplerArticleNumber);
		pnlNorth.add(lblDescription);
		pnlNorth.add(txtDescription);
		pnlNorth.add(lblQuantity);
		pnlNorth.add(txtQuantity);

		pnlSouth.add(btnSearchArticle);
		pnlSouth.add(btnAdd);
		pnlSouth.add(btnEdit);

	}

	public void addListner() {
		ButtonListner listener = new ButtonListner();
		btnSearchArticle.addActionListener(listener);
		btnAdd.addActionListener(listener);
		btnEdit.addActionListener(listener);

	}

	private class ButtonListner implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnSearchArticle) {

			} else if (e.getSource() == btnAdd) {

			} else if (e.getSource() == btnEdit) {

			}

		}

	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return TOOLNAME;
	}

	@Override
	public boolean getRezizable() {
		// TODO Auto-generated method stub
		return false;
	}

}
