package tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;

import client.ClientController;
import enteties.Entity;
import gui.GUIController;
import gui.Table;
import gui.Tool;
import gui.Updatable;
import shared.Eviro;

public class ArticleTool extends Tool implements Updatable {

	// private ButtonListener buttonListener;
	private Table tblComments;
	private Table tblSales = new Table(new Object[] { "Transaction No", "Invoice No", "Quantity", "Sum" }, false);

	private Tab tabComments = new Tab("Comments");
	private Tab tabSales = new Tab("Sales");
	private Tab[] tabs = new Tab[] { new Tab("General"), new Tab("Logistics"), tabSales, tabComments };

	private LabledTextField ltfNo = new LabledTextField("No");
	private LabledTextField ltfName = new LabledTextField("Name");
	private LabledTextField ltfDesc = new LabledTextField("Description");
	private LabledTextField ltfPrice = new LabledTextField("Price", Eviro.VALIDATOR_DOUBLE);
	private LabledTextField ltfEan = new LabledTextField("EAN");
	private LabledTextField ltfSup = new LabledTextField("Supplier");
	private LabledTextField ltfSupNo = new LabledTextField("Suppler No");
	private LabledTextField ltfStockPlace = new LabledTextField("Stock place");
	private LabledTextField ltfQuantity = new LabledTextField("Quantity", Eviro.VALIDATOR_INTEGER);
	private LabledTextField ltfSalesSum = new LabledTextField("Sum", false);
	private LabledTextField ltfSalesQuantity = new LabledTextField("Sold", false);
	private LabledTextField ltfReturnedQuantity = new LabledTextField("Returned", false);
	private LabledTextField ltfPriceHigh = new LabledTextField("H)", false);
	private LabledTextField ltfPriceLow = new LabledTextField("Price Range (L", false);

	private LabledTextField[] ltfAll = {
			ltfNo,
			ltfName,
			ltfDesc,
			ltfPrice,
			ltfSup,
			ltfSupNo,
			ltfEan,
			ltfStockPlace,
			ltfQuantity };
	private LabledTextField[] ltfRequired = { ltfName, ltfDesc, ltfPrice, ltfSup, ltfQuantity };

	private ActionButton btnNew = new ActionButton("Create New", "create");
	private ActionButton btnEdit = new ActionButton("Edit", "edit");
	private ActionButton btnUpdate = new ActionButton("Save", "update");
	private ActionButton btnFind = new ActionButton("Find", "search");
	private ActionButton btnReset = new ActionButton("Reset", "reset");
	private ActionButton btnAdd = new ActionButton("Add to invoice", "add");

	private JButton[] allButtons = { btnNew, btnEdit, btnUpdate, btnFind, btnReset, btnAdd };
	private JButton[] defaultButtons = { btnReset, btnFind, btnNew };
	private JButton[] lookingButtons = { btnReset, btnEdit };
	private JButton[] editingButtons = { btnReset, btnUpdate };
	private JButton[] openedFromInvoiceButtons = { btnFind };
	private JButton[] sendToInvoiceButtons = { btnAdd };

	private JScrollPane scrollPane;

	private boolean sendingToInvoice;
	private InvoiceTool invoiceGUI;

	// private KeyPress keyListener = new KeyPress();

	public ArticleTool(ClientController clientController, GUIController guiController) {
		super("Article", clientController, guiController);
		new ButtonListener();
		setTabs(tabs);
		setContent(0, new JComponent[] { ltfNo, ltfName, ltfDesc, ltfPrice, ltfEan });
		setContent(1, new JComponent[] { ltfSup, ltfSupNo, ltfQuantity, ltfStockPlace });
		setContent(2,
				new JComponent[] {
						new SplitPanel(ltfSalesQuantity, ltfReturnedQuantity),
						ltfSalesSum,
						new SplitPanel(ltfPriceLow, ltfPriceHigh),
				});
		setButtons(defaultButtons);

		scrollPane = new JScrollPane(tblSales);
		scrollPane.setPreferredSize(new Dimension(1, 150));
		tabSales.add(scrollPane, BorderLayout.CENTER);

		tblSales.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				if (me.getClickCount() == 2 && row >= 0) {

					if (tblSales.getModel().getValueAt(tblSales.getSelectedRow(), 1) != null) {
						InvoiceTool invoiceTool = new InvoiceTool(clientCtrlr, guiCtrlr);
						invoiceTool.search((String) tblSales.getModel().getValueAt(tblSales.getSelectedRow(), 1));
						guiCtrlr.add(invoiceTool);
					}
				}
			}
		});
		// addKeyListener(keyListener);
		createCommentsTable(false);
		// setFocusable(true);
		btnNew.setMnemonic(KeyEvent.VK_N);
		btnEdit.setMnemonic(KeyEvent.VK_E);
		btnUpdate.setMnemonic(KeyEvent.VK_S);
		btnFind.setMnemonic(KeyEvent.VK_F);
		btnReset.setMnemonic(KeyEvent.VK_R);
	}

	private void getSales(String articleNo) {

		ArrayList<Entity> sales = clientCtrlr.search(new Object[] { null, null, articleNo, null, null }, Eviro.ENTITY_TRANSACTION);

		int soldQuantity = 0;
		int returnedQuantity = 0;
		int returnedPercentage = 0;
		double soldSum = 0.00;

		double priceLow = Double.MAX_VALUE;
		double priceHigh = Double.MIN_VALUE;

		for (int i = 0; i < sales.size(); i++) {

			int rowQuantity = 0;
			double rowSum = 0.00;

			Object[] data = new Object[4];

			data[0] = sales.get(i).getData()[0];
			data[1] = sales.get(i).getData()[1];
			data[2] = sales.get(i).getData()[3];
			data[3] = sales.get(i).getData()[4];

			tblSales.populate(data, i);

			rowQuantity = Integer.parseInt((String) data[2]);
			rowSum = Double.parseDouble((String) data[3]);

			if (rowSum > 0) {

				soldSum += rowSum;
				soldQuantity += rowQuantity;

				if ((rowSum / rowQuantity) > priceHigh) {
					priceHigh = rowSum / rowQuantity;
				}

				if ((rowSum / rowQuantity) < priceLow) {
					priceLow = rowSum / rowQuantity;
				}
			}

			else {
				returnedQuantity += rowQuantity;
			}
		}

		try {
			returnedPercentage = (returnedQuantity * 100) / soldQuantity;
			ltfPriceLow.setText(Double.toString(priceLow));
			ltfPriceHigh.setText(Double.toString(priceHigh));
			ltfSalesSum.setText(Double.toString(soldSum));
			ltfSalesQuantity.setText(Integer.toString(soldQuantity));
			ltfReturnedQuantity.setText(Integer.toString(returnedQuantity) + " (" + returnedPercentage + "%)");
		} catch (ArithmeticException e) {
			returnedPercentage = 0;
			ltfPriceLow.setText("0");
			ltfPriceHigh.setText("0");
			ltfSalesSum.setText("0");
			ltfSalesQuantity.setText("0");
			ltfReturnedQuantity.setText("0 (" + returnedPercentage + "%)");
		}

	}

	public ArticleTool(InvoiceTool invoiceGUI, ClientController clientCtrlr, GUIController guiCtrlr) {
		this(clientCtrlr, guiCtrlr);
		this.invoiceGUI = invoiceGUI;
		setButtons(openedFromInvoiceButtons);
		sendingToInvoice = true;
	}

	private void createCommentsTable(boolean editable) {

		tabComments.removeAll();

		tblComments = new Table(new Object[] { "Date", "Comment" }, editable) {

			@Override
			public void editingStopped(ChangeEvent e) {
				int row = getEditingRow();
				int col = getEditingColumn();
				super.editingStopped(e);

				String comment = (String) tblComments.getModel().getValueAt(tblComments.getSelectedRow(), 1);

				tblComments.getModel().setValueAt(new SimpleDateFormat("yy-MM-dd").format(new Date()), tblComments.getSelectedRow(), 0);

				if (comment != null && comment.trim().length() > 0) {

					clientCtrlr.create(
							new Object[] {
									ltfNo.getText(),
									"product",
									getValueAt(tblComments.getSelectedRow(), 1),
									getValueAt(tblComments.getSelectedRow(), 0) },
							Eviro.ENTITY_COMMENT, false, true);
				}

			}

		};

		tblComments.getColumnModel().getColumn(0).setWidth(100);
		tblComments.getColumnModel().getColumn(0).setMaxWidth(100);
		tabComments.setPreferredSize(new Dimension(1, 150));
		tabComments.add(new JScrollPane(tblComments));

	}

	public void getComments(String customerNo) {

		ArrayList<Entity> comments = clientCtrlr.search(new Object[] { customerNo, "product", null },
				Eviro.ENTITY_COMMENT);

		for (int i = 0; i < comments.size(); i++) {

			Object[] commentData = new Object[2];

			commentData[0] = comments.get(i).getData()[2];
			commentData[1] = comments.get(i).getData()[1];

			tblComments.populate(commentData, i);
		}

	}

	private void reset() {

		if (tblComments != null)
			tblComments.reset();

		if (tblSales != null)
			tblSales.reset();

		setTfEditable(ltfAll, true);
		setButtons(defaultButtons);
		setTitle("Article");

		ltfPriceLow.setText(null);
		ltfPriceHigh.setText(null);
		ltfSalesSum.setText(null);
		ltfSalesQuantity.setText(null);
		ltfReturnedQuantity.setText(null);

		for (int i = 0; i < ltfAll.length; i++) {

			ltfAll[i].setText(null);
		}

	}

	// private class KeyPress implements KeyListener {
	// // Set<Character> pressed = new HashSet<Character>();
	//
	// @Override
	// public synchronized void keyPressed(KeyEvent e) {
	// int keyCode = e.getKeyCode();
	//
	// // pressed.add((char) e.getKeyCode());
	// // ClientController clientController = new ClientController();
	// // GUIController gController;
	// switch (e.getKeyCode()) {
	// // case KeyEvent.VK_F1: {
	// // System.out.println("1. Du tryckte på F1 från ArticleTool");
	// // break;
	// // }
	// case KeyEvent.VK_F2: // Trycker på SÖK knappen på articleTool
	// search(getThis(), ltfAll, Eviro.ENTITY_PRODUCT);
	// System.out.println("2. Du tryckte på F2 från ArticleTool");
	// break;
	//
	// case KeyEvent.VK_F3: // Fnkar ej. Trycker på SAVE knappen på articleTool
	// setButtons(lookingButtons);
	// setTfEditable(ltfAll, false);
	// System.out.println("3. Du tryckte på F3 från ArticleTool");
	// break;
	//
	// default:
	// System.out.println("ArtcleTool. Annan knapp " + KeyEvent.getKeyText(keyCode));
	// e.consume();
	// break;
	//
	// }
	// }
	//
	// @Override
	// public synchronized void keyReleased(KeyEvent e) {
	// e.consume();
	//
	// }
	//
	// @Override
	// public void keyTyped(KeyEvent e) {
	// e.consume();
	// }
	// }

	private class ButtonListener implements ActionListener {

		public ButtonListener() {
			for (JButton btn : allButtons) {
				btn.addActionListener(this);
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			switch (e.getActionCommand()) {

			case "create":
				if (validate(ltfRequired)) {
					ltfNo.setText(null);
					create(getThis(), Eviro.ENTITY_PRODUCT);
				}
				break;

			case "edit":
				setButtons(editingButtons);
				setTfEditable(ltfAll, true);
				break;

			case "update":
				if (validate(ltfRequired)) {
					if (update(getThis(), Eviro.ENTITY_PRODUCT)) {
						setButtons(lookingButtons);
						setTfEditable(ltfAll, false);
					}
				}
				break;

			case "search":
				search(getThis(), ltfAll, Eviro.ENTITY_PRODUCT);
				break;

			case "reset":
				reset();
				break;

			case "add":
				String[] allValues = getValues();
				String[] returnValues = { allValues[0], allValues[1], allValues[3], "1", null };
				invoiceGUI.addArticle(returnValues);

				try {
					setClosed(true);
				} catch (PropertyVetoException e1) {
					e1.printStackTrace();
				}

				break;

			default:

				break;
			}
		}
	}

	@Override
	public void setValues(Object[] values) {

		createCommentsTable(true);
		getComments((String) values[0]);
		getSales((String) values[0]);
		setTfEditable(ltfAll, false);

		if (sendingToInvoice)
			setButtons(sendToInvoiceButtons);
		else
			setButtons(lookingButtons);

		setTitle(values[0] + " - " + values[1]);

		for (int i = 0; i < ltfAll.length; i++) {

			if (values[i] instanceof Integer) {
				values[i] = Integer.toString((int) values[i]);
			}

			ltfAll[i].setText((String) values[i]);
		}

	}

	@Override
	public String[] getValues() {

		return getValues(false);
	}

	@Override
	public String[] getValues(boolean getNames) {

		String[] text = new String[ltfAll.length];

		for (int i = 0; i < ltfAll.length; i++) {

			if (getNames)
				text[i] = ltfAll[i].getName();
			else {
				text[i] = ltfAll[i].getText();
			}

		}

		return text;

	}

	@Override
	public Updatable getThis() {

		return this;
	}

}
