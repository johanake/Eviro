package tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

import client.ClientController;
import gui.GUIController;
import gui.Tool;
import gui.Updatable;
import shared.Eviro;

public class ArticleTool extends Tool implements Updatable {

	private ButtonListener buttonListener;
	private Tab[] tabs = new Tab[] { new Tab("General"), new Tab("Logistics"), new Tab("Comments") };

	private LabledTextField ltfNo = new LabledTextField("No");
	private LabledTextField ltfName = new LabledTextField("Name");
	private LabledTextField ltfDesc = new LabledTextField("Description");
	private LabledTextField ltfPrice = new LabledTextField("Price");
	private LabledTextField ltfEan = new LabledTextField("EAN");
	private LabledTextField ltfSup = new LabledTextField("Supplier");
	private LabledTextField ltfSupNo = new LabledTextField("Suppler No");
	private LabledTextField ltfQuantity = new LabledTextField("Quantity");
	private LabledTextField ltfStockPlace = new LabledTextField("Stock place");

	private LabledTextField[] ltfAll = { ltfNo, ltfName, ltfDesc, ltfPrice, ltfSup, ltfSupNo, ltfEan, ltfStockPlace, ltfQuantity };

	private ActionButton btnNew = new ActionButton("Create New", "create");
	private ActionButton btnEdit = new ActionButton("Edit", "edit");
	private ActionButton btnUpdate = new ActionButton("Save", "update");
	private ActionButton btnFind = new ActionButton("Find", "search");
	private ActionButton btnReset = new ActionButton("Reset", "reset");

	private JButton[] allButtons = { btnNew, btnEdit, btnUpdate, btnFind, btnReset };
	private JButton[] defaultButtons = { btnNew, btnFind, btnReset };
	private JButton[] lookingButtons = { btnEdit, btnReset };
	private JButton[] editingButtons = { btnUpdate, btnReset };

	public ArticleTool(ClientController clientController, GUIController guiController) {
		super("Article", clientController, guiController);
		buttonListener = new ButtonListener();
		setTabs(tabs);
		setContent(0, new JComponent[] { ltfNo, ltfName, ltfDesc, ltfPrice, ltfEan });
		setContent(1, new JComponent[] { ltfSup, ltfSupNo, ltfQuantity, ltfStockPlace });
		setButtons(defaultButtons);
	}

	private void reset() {

		setTfEditable(ltfAll, true);
		setButtons(defaultButtons);
		setTitle("Article");

		for (int i = 0; i < ltfAll.length; i++) {

			ltfAll[i].setText(null);
		}

	}

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
				create(getThis(), Eviro.ENTITY_PRODUCT);
				break;

			case "edit":
				setButtons(editingButtons);
				setTfEditable(ltfAll, true);
				break;

			case "update":
				if (update(getThis(), Eviro.ENTITY_PRODUCT)) {
					setButtons(lookingButtons);
					setTfEditable(ltfAll, false);
				}
				break;

			case "search":
				search(getThis(), ltfAll, Eviro.ENTITY_PRODUCT);
				break;

			case "reset":
				reset();
				break;

			default:

				break;
			}
		}
	}

	@Override
	public void setValues(Object[] values) {

		setTfEditable(ltfAll, false);
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

		String[] text = new String[ltfAll.length];

		for (int i = 0; i < ltfAll.length; i++) {
			text[i] = ltfAll[i].getText();
		}

		return text;

	}

	@Override
	public Updatable getThis() {
		return this;
	}
}
