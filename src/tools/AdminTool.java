package tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPasswordField;

import client.ClientController;
import gui.GUIController;
import gui.Tool;
import gui.Updatable;
import shared.Eviro;

/**
 * 
 * @author peter
 *
 */

public class AdminTool extends Tool implements Updatable {

	private Tab[] tabs = new Tab[] { new Tab("Users"), new Tab("Network") };

	private LabledTextField ltfUserID = new LabledTextField("User ID");
	private LabledTextField ltfUserName = new LabledTextField("User Name");
	private JPasswordField jpfUserPassword = new JPasswordField();
	private LabledTextField ltfUserPassword = new LabledTextField("User Password");
	private LabledTextField[] ltfAll = { ltfUserID, ltfUserName, ltfUserPassword };

	private ActionButton btnNew = new ActionButton("Create New", "create");
	private ActionButton btnEdit = new ActionButton("Edit", "edit");
	private ActionButton btnUpdate = new ActionButton("Save", "update");
	private ActionButton btnFind = new ActionButton("Find", "search");
	private ActionButton btnReset = new ActionButton("Reset", "reset");

	private JButton[] allButtons = { btnNew, btnEdit, btnUpdate, btnFind, btnReset };
	private JButton[] defaultButtons = { btnReset, btnFind, btnNew };
	private JButton[] lookingButtons = { btnReset, btnEdit };
	private JButton[] editingButtons = { btnReset, btnUpdate };
	private ButtonListener buttonListener = new ButtonListener();

	
	public AdminTool(ClientController clientController, GUIController guiController) {
		super("Admin", clientController, guiController);
		setTabs(tabs);
		jpfUserPassword.setName("User Password");
		setContent(0, new JComponent[] { ltfUserID, ltfUserName, jpfUserPassword });
		setContent(1, new JComponent[] {});
		setButtons(defaultButtons);
	}

	private void reset() {

		setTfEditable(ltfAll, true);
		setButtons(defaultButtons);
		setTitle("Admin");
		jpfUserPassword.setText(null);

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
				ltfUserPassword.setText(
						clientCtrlr.getPassCryptor().encryptPassword(new String(jpfUserPassword.getPassword())));
				ltfUserID.setText(null);
				create(getThis(), Eviro.ENTITY_USER);
				break;

			case "edit":
				setButtons(editingButtons);
				setTfEditable(ltfAll, true);
				setTfEditable(ltfUserID, false);
				jpfUserPassword.setText(null);
				break;

			case "update":
				if (update(getThis(), Eviro.ENTITY_USER)) {
					setButtons(lookingButtons);
					setTfEditable(ltfAll, false);
				}
				break;

			case "search":
				ltfUserPassword.setText(null);
				search(getThis(), ltfAll, Eviro.ENTITY_USER);
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

		for (int i = 0; i < ltfAll.length - 1; i++) {

			if (values[i] instanceof Integer) {
				values[i] = Integer.toString((int) values[i]);
			}

			ltfAll[i].setText((String) values[i]);
		}
		ltfAll[2].setText("******************");

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

	@Override
	public String[] getValues(boolean getNames) {
		// TODO Auto-generated method stub
		return null;
	}
}
