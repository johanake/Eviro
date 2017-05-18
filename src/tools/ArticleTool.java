package tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import client.ClientController;
import enteties.Entity;
import gui.GUIController;
import gui.Table;
import gui.Tool;
import gui.Updatable;
import shared.Eviro;

public class ArticleTool extends Tool implements Updatable {

	private ButtonListener buttonListener;

	private Tab tabSales = new Tab("Sales");
	private Tab[] tabs = new Tab[] { new Tab("General"), new Tab("Logistics"), tabSales, new Tab("Comments") };

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
	private LabledTextField ltfSalesQuantity = new LabledTextField("Sales Quantity", false);
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
	private JButton[] defaultButtons = { btnNew, btnFind, btnReset };
	private JButton[] lookingButtons = { btnEdit, btnReset };
	private JButton[] editingButtons = { btnUpdate, btnReset };
	private JButton[] openedFromInvoiceButtons = { btnFind };
	private JButton[] sendToInvoiceButtons = { btnAdd };

	private JScrollPane scrollPane;
	private Table tblSales = new Table(new Object[] { "Transaction No", "Invoice No", "Quantity", "Sum" }, false);

	private boolean sendingToInvoice;
	private InvoiceTool invoiceGUI;

	// private KeyPress keyListener = new KeyPress();

	public ArticleTool(ClientController clientController, GUIController guiController) {
		super("Article", clientController, guiController);
		buttonListener = new ButtonListener();
		setTabs(tabs);
		setContent(0, new JComponent[] { ltfNo, ltfName, ltfDesc, ltfPrice, ltfEan });
		setContent(1, new JComponent[] { ltfSup, ltfSupNo, ltfQuantity, ltfStockPlace });
		setContent(2, new JComponent[] { new SplitPanel(ltfSalesQuantity, ltfSalesSum) });
		setButtons(defaultButtons);

		scrollPane = new JScrollPane(tblSales);
		scrollPane.setPreferredSize(new Dimension(1, 150));
		tabSales.add(scrollPane, BorderLayout.CENTER);

		// addKeyListener(keyListener);
		// setFocusable(true);
		btnNew.setMnemonic(KeyEvent.VK_N);
		btnEdit.setMnemonic(KeyEvent.VK_E);
		btnUpdate.setMnemonic(KeyEvent.VK_S);
		btnFind.setMnemonic(KeyEvent.VK_F);
		btnReset.setMnemonic(KeyEvent.VK_R);
	}

	private void getSales(String articleNo) {

		ArrayList<Entity> sales = clientCtrlr.search(new Object[] { null, null, articleNo, null, null }, Eviro.ENTITY_TRANSACTION);

		int quantity = 0;
		double sum = 0.00;

		for (int i = 0; i < sales.size(); i++) {

			Object[] data = new Object[4];

			data[0] = sales.get(i).getData()[0];
			data[1] = sales.get(i).getData()[1];
			data[2] = sales.get(i).getData()[3];
			data[3] = sales.get(i).getData()[4];

			tblSales.populate(data, i);

			quantity += Integer.parseInt((String) data[2]);
			sum += Double.parseDouble((String) data[3]);

		}

		ltfSalesSum.setText(Double.toString(sum));
		ltfSalesQuantity.setText(Integer.toString(quantity));

	}

	public ArticleTool(InvoiceTool invoiceGUI, ClientController clientCtrlr, GUIController guiCtrlr) {
		this(clientCtrlr, guiCtrlr);
		this.invoiceGUI = invoiceGUI;
		setButtons(openedFromInvoiceButtons);
		sendingToInvoice = true;
	}

	private void reset() {

		setTfEditable(ltfAll, true);
		setButtons(defaultButtons);
		setTitle("Article");

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
