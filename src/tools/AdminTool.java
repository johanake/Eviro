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
 * A GUI tool for administrating users of the system.
 * 
 * @author Peter Sjögren
 */
public class AdminTool extends Tool implements Updatable {

	private ButtonListener buttonListener;
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

	/**
	 * Builds an internal JFrame and creates an interface for managing users and
	 * passwords that access the system.
	 * 
	 * @param clientController
	 *            The ClientController to send to the tool
	 * @param guiController
	 *            The GUIController to send to the tool
	 */

	public AdminTool(ClientController clientController, GUIController guiController) {

		super("Admin", clientController, guiController);
		buttonListener = new ButtonListener();
		setTabs(tabs);
		jpfUserPassword.setName("User Password");
		setContent(0, new JComponent[] { ltfUserID, ltfUserName, jpfUserPassword });
		setButtons(defaultButtons);
	}

	/**
	 * Clears all input fields from text. Resets the tool values and behaviour.
	 */
	private void reset() {

		setTfEditable(ltfAll, true);
		setButtons(defaultButtons);
		setTitle("Admin Tool");
		jpfUserPassword.setText(null);

		for (int i = 0; i < ltfAll.length; i++) {

			ltfAll[i].setText(null);
		}
	}

	/**
	 * ActionListener implementation that listens for gui button clicks.
	 * 
	 * @author Robin Overgaard
	 * @version 1.0
	 */
	private class ButtonListener implements ActionListener {

		public ButtonListener() {
			for (JButton btn : allButtons) {
				btn.addActionListener(this);
			}
		}

		/**
		 * In case "create" the info in the passwordfield get encrypted before
		 * being used by any other methods in the system.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			switch (e.getActionCommand()) {

			case "create":
				ltfUserPassword.setText(
						clientCtrlr.getPassCryptor().encryptPassword(new String(jpfUserPassword.getPassword())));
				ltfUserID.setText(null);
				create(getThis(), Eviro.ENTITY_USER);
				jpfUserPassword.setText(null);
				ltfUserPassword.setText(null);
				break;

			case "edit":
				setButtons(editingButtons);
				setTfEditable(ltfAll, true);
				setTfEditable(ltfUserID, false);
				jpfUserPassword.setText(null);
				ltfUserPassword.setText(null);
				jpfUserPassword.setEnabled(true);
				break;

			case "update":
				ltfUserPassword.setText(
						clientCtrlr.getPassCryptor().encryptPassword(new String(jpfUserPassword.getPassword())));
				if (update(getThis(), Eviro.ENTITY_USER)) {
					setButtons(lookingButtons);
					setTfEditable(ltfAll, false);
				}
				jpfUserPassword.setText(null);
				ltfUserPassword.setText(null);
				jpfUserPassword.setEnabled(false);
				break;

			case "search":
				ltfUserPassword.setText(null);
				jpfUserPassword.setText(null);
				search(getThis(), ltfAll, Eviro.ENTITY_USER);
				jpfUserPassword.setEnabled(false);

				break;

			case "reset":
				reset();
				break;

			default:

				break;
			}
		}
	}

	/**
	 * Sets incoming user info to the text fields
	 */
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

	/**
	 * Gets current user info form the text fields
	 */
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
		return null;
	}
}