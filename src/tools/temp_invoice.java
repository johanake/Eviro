package tools;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import client.ClientController;
import enteties.Entity;
import gui.GUIController;
import gui.SuperTool;
import gui.Table;
import gui.Updatable;
import shared.Eviro;

public class temp_invoice extends SuperTool implements Updatable {

	private ButtonListener buttonListener;

	private LabledTextField ltfCustNo = new LabledTextField("Customer-");
	private LabledTextField ltfInvNo = new LabledTextField("Invoice-No");
	private LabledTextField ltfBuyer = new LabledTextField("Buyer");
	private LabledTextField ltfRef = new LabledTextField("Reference");

	// private LabledTextField ltfPrice = new LabledTextField("Add Quantity");

	private LabledTextField[] ltfAll = { ltfBuyer, ltfRef };

	private ActionButton btnNew = new ActionButton("Create", "create");
	private ActionButton btnReset = new ActionButton("Reset", "reset");

	private JButton[] allButtons = { btnNew, btnReset };
	private JButton[] defaultButtons = { btnNew, btnReset };

	private Table articles = new Table(this, new Object[] { "Article No", "Name", "Price", "Quantity", "Sum" });

	public temp_invoice(ClientController clientController, GUIController guiController, String customer) {
		super("Invoice", clientController, guiController);
		buttonListener = new ButtonListener();
		setContent(new JComponent[] { new SplitPanel(ltfCustNo, ltfInvNo), ltfBuyer, ltfRef });
		setButtons(defaultButtons);
		setTfEditable(ltfCustNo, false);
		setTfEditable(ltfInvNo, false);
		ltfCustNo.setText(customer);
		pnlCenter.add(new JScrollPane(articles), BorderLayout.CENTER);
	}

	private void reset() {

		setTfEditable(ltfAll, true);
		setButtons(defaultButtons);
		setTitle("Invoice");

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
				// create(getThis(), Eviro.ENTITY_);
				break;

			case "reset":
				reset();
				break;

			default:

				break;
			}
		}
	}

	public void getArticle(String articleno, int row) {

		System.out.println("ddd");

		ArrayList<Entity> response = clientCtrlr.search(new String[] { articleno, null, null, null, null, null, null, null, null },
				Eviro.ENTITY_PRODUCT);

		if (response.size() == 1) {
			Object[] article = response.get(0).getData();

			Object[] info = new Object[5];
			info[0] = article[0];
			info[1] = article[1];
			info[2] = article[3];
			info[3] = "1";
			info[4] = Integer.toString(Integer.parseInt((String) info[2]) * Integer.parseInt((String) info[3]));

			articles.populate(info, row);
		}

		else {
			articles.populate(new Object[] { null, null, null, null, null }, row);
		}

	}

	@Override
	public void setValues(Object[] values) {

		// setTfEditable(ltfAll, false);
		// setButtons(lookingButtons);
		// setTitle(values[0] + " - " + values[1]);
		//
		// for (int i = 0; i < ltfAll.length; i++) {
		//
		// if (values[i] instanceof Integer) {
		// values[i] = Integer.toString((int) values[i]);
		// }
		//
		// ltfAll[i].setText((String) values[i]);
		// }

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
