package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.text.JTextComponent;

import client.ClientController;
import enteties.Entity;
import shared.Eviro;
import tools.SearchResult;

/**
 * A class that defines base functionality and ensures a uniform layout for all the tools used in the system.
 * @author Robin Overgaard
 * @version 1.0
 */

public class Tool extends JInternalFrame {

	protected JPanel pnlNorth = new JPanel(new BorderLayout());
	protected JPanel pnlCenter = new JPanel(new BorderLayout());
	protected JPanel pnlSouth = new JPanel(new BorderLayout());
	protected JTabbedPane tabbedPane = new JTabbedPane();
	protected ClientController clientCtrlr;
	protected GUIController guiCtrlr;

	static int openFrameCount = 0;

	private Color bgColor = new Color(233, 236, 242);

	/**
	 * Constructor, creates the tool.
	 * @param title the title of the tool
	 * @param clientController a clientcontroller instance
	 * @param guiController a guiController instance
	 */
	protected Tool(String title, ClientController clientController, GUIController guiController) {
		super(title, true, true, false, true);
		this.clientCtrlr = clientController;
		this.guiCtrlr = guiController;
		setup();
		openFrameCount++;
	}

	/**
	 * Searches for entities of the specified type.
	 * @param values the values to search with
	 * @param entitytype the type of entity
	 * @return a list containg the search result
	 */
	protected ArrayList<Entity> search(String[] values, int entitytype) {

		ArrayList<Entity> resultList = clientCtrlr.search(values, entitytype);

		if (resultList.size() == 0) {
			popupMessage("No matches, try again by changing or adding information in your search.");
			return null;
		} else
			return resultList;
	}

	/**
	 * Searches for entities of the specified type.
	 * @param tool the updatable implementation to get and set values from/to
	 * @param ltfAll the textfields containing all the search values
	 * @param entitytype the type of entity
	 */
	protected void search(Updatable tool, LabledTextField[] ltfAll, int entitytype) {

		ArrayList<Entity> response = clientCtrlr.search(tool.getValues(), entitytype);

		if (response.size() == 0) {
			popupMessage("No matches, try again by changing or adding information in your search.");
		} else if (response.size() == 1) {
			tool.setValues(response.get(0).getData());
		} else {

			Object[] searchLabels = new Object[tool.getValues().length];

			for (int i = 0; i < tool.getValues().length; i++) {
				searchLabels[i] = ltfAll[i].getName();
			}

			guiCtrlr.add(new SearchResult(searchLabels, tool, response));
		}
	}

	/**
	 * Creates a new entity of the specified type.
	 * @param tool the updatable implementation to get and set values from/to
	 * @param entitytype the type of entity
	 */
	protected void create(Updatable tool, int entitytype) {

		ArrayList<Entity> response = clientCtrlr.create(tool.getValues(), entitytype, true, false);

		if (response.size() == 0) {
			popupMessage("Server returned 0 items!");
		} else if (response.size() == 1) {
			popupMessage(Eviro.getEntityNameByNumber(entitytype).substring(0, 1).toUpperCase() + Eviro.getEntityNameByNumber(entitytype).substring(1)
					+ " created with id no: " + response.get(0).getData()[0]);
			tool.setValues(response.get(0).getData());
		} else {
			popupMessage("A " + Eviro.getEntityNameByNumber(entitytype) + " with the same values already exists!");

		}

	}

	/**
	 * Updates a entity.
	 * @param tool the updatable implementation to get and set values from/to
	 * @param entitytype the type of entity
	 * @param isSilent
	 * @return whether the update was succesfull or not
	 */
	protected boolean update(Updatable tool, int entitytype, boolean isSilent) {
		if (clientCtrlr.update(this, tool.getValues(), entitytype, isSilent)) {
			if (!isSilent)
				popupMessage("The " + Eviro.getEntityNameByNumber(entitytype) + " has been succesfully updated!");
			return true;
		} else {
			Object[] test = new Object[tool.getValues().length];
			test[0] = tool.getValues()[0];
			tool.setValues(clientCtrlr.search(test, entitytype).get(0).getData());
			if (!isSilent)
				popupMessage("No changes made, update aborted!");
			return false;
		}

	}

	/**
	 * Updates a entity.
	 * @param tool the updatable implementation to get and set values from/to
	 * @param entitytype the type of entity
	 * @return whether the update was succesfull or not
	 */
	protected boolean update(Updatable tool, int entitytype) {
		return update(tool, entitytype, false);
	}

	/**
	 * Creates tabbed pages in the tool.
	 * @param tabs the panels to be used as tabs
	 */
	protected void setTabs(JPanel[] tabs) {

		tabbedPane.setBorder(new EmptyBorder(20, 0, 0, 0));

		for (int i = 0; i < tabs.length; i++) {

			tabbedPane.addTab(tabs[i].getName() + " (" + (i + 1) + ")", tabs[i]);

			switch (i) {
			case 0:
				tabbedPane.setMnemonicAt(i, KeyEvent.VK_1);
				break;
			case 1:
				tabbedPane.setMnemonicAt(i, KeyEvent.VK_2);
				break;
			case 2:
				tabbedPane.setMnemonicAt(i, KeyEvent.VK_3);
				break;
			case 3:
				tabbedPane.setMnemonicAt(i, KeyEvent.VK_4);
				break;
			case 4:
				tabbedPane.setMnemonicAt(i, KeyEvent.VK_5);
				break;
			}
		}

		pnlCenter.setBackground(bgColor);
		pnlCenter.add(tabbedPane);

	}

	/**
	 * Sets what buttons to display in the tool.
	 * @param buttons the buttons to display
	 */
	protected void setButtons(JButton[] buttons) {

		setFocusable(true);
		requestFocusInWindow();
		pnlSouth.removeAll();

		JPanel pnlButtons = new JPanel(new GridLayout(1, buttons.length));
		pnlButtons.setBackground(bgColor);

		for (JButton b : buttons) {
			pnlButtons.add(b);
		}

		pnlSouth.add(pnlButtons, BorderLayout.EAST);
		revalidate();
		repaint();
	}

	/**
	 * Add content to either the first tab of the tool or if there's no tabs the center panel.
	 * @param content the content
	 */
	protected void setContent(JComponent[] content) {

		if (tabbedPane.getTabCount() > 0) {
			setContent(0, content);
		}

		else {
			setContent(pnlCenter, content);
		}

	}

	/**
	 * Add content to either the specified tab of the tool or if there's no tabs the center panel.
	 * @param tabIndex the tab to add the content to
	 * @param content the content
	 */
	protected void setContent(int tabIndex, JComponent[] content) {

		if (tabbedPane.getTabCount() <= 0) {
			setContent(pnlCenter, content);
		}

		else {
			JPanel pnl = (JPanel) tabbedPane.getComponentAt(tabIndex);
			setContent(pnl, content);
		}
	}

	/**
	 * Add content to the specified panel.
	 * @param pnl the panel to add the content to
	 * @param content the content
	 */
	protected void setContent(JPanel pnl, JComponent[] content) {
		setContent(pnl, BorderLayout.NORTH, content);
	}

	/**
	 * Add content to the specified panel.
	 * @param pnl the panel to add the content to
	 * @param constraints the layout constraint of where to add the content
	 * @param content the content
	 */
	protected void setContent(JPanel pnl, Object constraints, JComponent[] content) {
		JPanel pnlContent = new JPanel(new BorderLayout());
		JPanel pnlContentLeft = new JPanel(new GridLayout(content.length, 1));
		JPanel pnlContentRight = new JPanel(new GridLayout(content.length, 1));

		pnlContent.setBorder(new EmptyBorder(15, 15, 15, 15));

		pnlContentRight.setBorder(new EmptyBorder(0, 10, 0, 0));
		pnlContentLeft.setBorder(new EmptyBorder(0, 0, 0, 10));

		for (JComponent c : content) {
			pnlContentLeft.add(new JLabel(c.getName() + ":"));
			pnlContentRight.add(c);
		}

		pnlContent.add(pnlContentLeft, BorderLayout.WEST);
		pnlContent.add(pnlContentRight, BorderLayout.CENTER);
		pnl.add(pnlContent, constraints);

	}

	/**
	 * Sets whether an array of specified textcomponents shoud be editable or not.
	 * @param fields the array of textcomponents
	 * @param enabled whether the textcomponents should be editable or not
	 */
	protected void setTfEditable(JTextComponent[] fields, Boolean enabled) {

		Color fieldColor;

		if (enabled) {
			fieldColor = Color.WHITE;
		}

		else {
			fieldColor = new Color(214, 217, 223);
		}

		for (JTextComponent c : fields) {
			c.setEditable(enabled);
			c.setBackground(fieldColor);

		}

	}

	/**
	 * Sets whether a specified textcomponent shoud be editable or not.
	 * @param field the textcomponent
	 * @param enabled whether the textcomponent should be editable or not
	 */
	protected void setTfEditable(JTextComponent field, Boolean enabled) {
		setTfEditable(new JTextComponent[] { field }, enabled);
	}

	/**
	 * Set up GUI using the EDT.
	 */
	private void setup() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				setLayout(new BorderLayout());

				pnlNorth.setBackground(bgColor);
				pnlSouth.setBackground(bgColor);

				pnlSouth.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black),
						BorderFactory.createEmptyBorder(10, 10, 10, 10)));

				add(pnlNorth, BorderLayout.NORTH);
				add(pnlCenter, BorderLayout.CENTER);
				add(pnlSouth, BorderLayout.SOUTH);

				pnlSouth.setPreferredSize(new Dimension(500, 50)); // Fixar bredden

				setLocation(15 * openFrameCount, 15 * openFrameCount);
				setVisible(true);
				setFocusable(true);
				pack();
				setMinimumSize(getSize());
				addInternalFrameListener(new InternalFrameAdapter() {
					@Override
					public void internalFrameClosing(InternalFrameEvent e) {
						openFrameCount--;
					}
				});

			}
		});
	}

	/**
	 * Displays a popup message positioned relative to this frame.
	 * @param txt the text to display on the popup
	 */
	protected void popupMessage(String txt) {
		popupMessage(txt, "Message", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Displays a popup message positioned relative to this frame.
	 * @param txt the text to display on the popup
	 */
	protected void popupMessage(String message, String title, int messageType) {
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}

	/**
	 * Validates input and displays appropriate warning messages.
	 * @param fields array of fields to validate
	 * @return whether all fields were successfully validated or not
	 */
	protected boolean validate(LabledTextField[] fields) {

		String check = "";
		String[] values = new String[fields.length];
		String[] names = new String[fields.length];
		Color bgColor = Color.WHITE;
		Color errColor = new Color(255, 220, 35);

		for (int i = 0; i < values.length; i++) {

			values[i] = fields[i].getText();
			names[i] = fields[i].getName();
			fields[i].setBackground(bgColor);

			if (fields[i].isInteger()) {

				values[i] = values[i].replaceAll(" ", "");
				fields[i].setText(values[i]);

				try {
					Integer.parseInt(values[i]);
				} catch (NumberFormatException e) {
					check += "\n" + names[i] + " (number)";
					fields[i].setBackground(errColor);
				}
			}

			else if (fields[i].isDouble()) {

				values[i] = values[i].replaceAll(",", ".");
				fields[i].setText(values[i]);

				try {
					Double.parseDouble(values[i]);
				} catch (NumberFormatException e) {
					check += "\n" + names[i] + " (decimal number)";
					fields[i].setBackground(errColor);
				}
			}

			else if (values[i] == null || values[i].trim().length() <= 0) {
				check += "\n" + names[i];
				fields[i].setBackground(errColor);
			}

		}

		if (check.length() > 0) {

			popupMessage("Please check the following fields before continuing:" + check, "Required fields missing", JOptionPane.INFORMATION_MESSAGE);

			return false;
		}

		return true;

	}

	/**
	 * Returns a instance of this JInternalFrame.
	 * @return a instance of this JInternalFrame
	 */
	protected JInternalFrame getFrame() {
		return this;
	}

	/**
	 * Customization of JPanel that takes its name and LayoutManager as parameters in the constructor.
	 * @author Robin Overgaard
	 * @version 1.0
	 */
	public class Tab extends JPanel {

		/**
		 * Creates a tab with a name and a specified LayoutManager.
		 * @param name the name for the tab
		 * @param layout the LayoutManager to use for the tab
		 */
		public Tab(String name, LayoutManager layout) {
			super(layout);
			setName(name);
		}

		/**
		 * Creates a tab with a name and a BorderLayout LayoutManager.
		 * @param name the name for the tab
		 */
		public Tab(String name) {
			this(name, new BorderLayout());
		}

	}

	/**
	 * Customization of JPanel that by default is split in two.
	 * @author Robin Overgaard
	 * @version 1.0
	 */
	public class SplitPanel extends JPanel {

		/**
		 * Creates the splitted panel and sets its name to a combination of reght and left.
		 * @param left the left panel
		 * @param right the right panel
		 */
		public SplitPanel(JComponent left, JComponent right) {

			setLayout(new GridLayout(1, 2));

			String name = "";

			if (left.getName().trim().length() > 0) {
				name += left.getName();
				if (right.getName().trim().length() > 0) {
					name += "/" + right.getName();
				}

				setName(name);

			}

			else if (right.getName().trim().length() > 0) {
				setName(right.getName());
			}

			add(left);
			add(right);

		}

	}

	/**
	 * Customization of JTextField that takes its name, its enabled state and its validation type as parameters in the constructor.
	 * @author Robin Overgaard
	 * @version 1.0
	 */
	public class LabledTextField extends JTextField {

		private boolean isInteger = false; // Used while validating the field value.
		private boolean isDouble = false; // Used while validating the field value.

		/**
		 * Constructor.
		 * @param name the name of this text field
		 * @param enabled whether the input field should be enabled or not
		 * @param fieldValueType type of data to validate the input for
		 */
		public LabledTextField(String name, boolean enabled, int fieldValueType) {
			setTfEditable(this, enabled);
			setName(name);

			switch (fieldValueType) {

			case Eviro.VALIDATOR_DOUBLE:
				isDouble = true;
				break;

			case Eviro.VALIDATOR_INTEGER:
				isInteger = true;
				break;

			}

		}

		/**
		 * Constructor.
		 * @param name the name of this text field
		 * @param enabled whether the input field should be enabled or not
		 */
		public LabledTextField(String name, boolean enabled) {
			this(name, enabled, 0);
		}

		/**
		 * Constructor.
		 * @param name the name of this text field
		 * @param fieldValueType type of data to validate the input for
		 */
		public LabledTextField(String name, int fieldValueType) {
			this(name, true, fieldValueType);
		}

		/**
		 * Constructor.
		 * @param name the name of this text field
		 */
		public LabledTextField(String name) {
			this(name, true);
		}

		/**
		 * Returns whether the textfields text should convert to integer.
		 * @return whether the textfields text should convert to integer
		 */
		public boolean isInteger() {
			return isInteger;
		}

		/**
		 * Returns whether the textfields text should convert to double.
		 * @return whether the textfields text should convert to double
		 */
		public boolean isDouble() {
			return isDouble;
		}

	}

	/**
	 * Customization of JButton that takes its ActionCommand as a parameter in the constructor.
	 * @author Robin Overgaard
	 * @version 1.0
	 */
	public class ActionButton extends JButton {

		/**
		 * Constructor.
		 * @param text the text to display on this button
		 */
		public ActionButton(String text) {
			super(text);
		}

		/**
		 * Constructor.
		 * @param text the text to display on this button
		 * @param action the action command for this button
		 */
		public ActionButton(String text, String action) {
			this(text);
			this.setActionCommand(action);
		}

	}

}
