package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
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

import client.ClientController;
import enteties.Entity;
import shared.Eviro;
import tools.SearchResult;

public class Tool extends JInternalFrame {

	private Color bgColor = new Color(233, 236, 242);

	private Menu menu = new Menu();

	public JPanel pnlNorth = new JPanel(new BorderLayout());
	public JPanel pnlCenter = new JPanel(new BorderLayout());
	public JPanel pnlSouth = new JPanel(new BorderLayout());

	private JTabbedPane tabbedPane = new JTabbedPane();

	static int openFrameCount = 0;

	public ClientController clientCtrlr;
	public GUIController guiCtrlr;

	protected Tool(String title, ClientController clientController, GUIController guiController) {
		super(title, true, true, false, true);
		this.clientCtrlr = clientController;
		this.guiCtrlr = guiController;
		setup();
		openFrameCount++;
	}

	protected ArrayList<Entity> search(String[] values, int entitytype) {

		ArrayList<Entity> resultList = clientCtrlr.search(values, entitytype);

		if (resultList.size() == 0) {
			popupMessage("No matches, try again by changing or adding information in your search.");
			return null;
		} else
			return resultList;
	}

	protected void get(Updatable tool, int entitytype) {

		ArrayList<Entity> response = clientCtrlr.getAllbyType(entitytype);

		Object[][] results = new Object[response.size()][4];

		for (int i = 0; i < results.length; i++) {
			results[i] = response.get(i).getData();

		}

		tool.setValues(results);

	}

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

	protected void create(Updatable tool, int entitytype) {

		ArrayList<Entity> response = clientCtrlr.create(tool.getValues(), entitytype, true);

		if (response.size() == 0) {
			popupMessage("Server returned 0 items!");
		} else if (response.size() == 1) {
			popupMessage("A " + Eviro.getEntityNameByNumber(entitytype) + " has been created with id no: " + response.get(0).getData()[0]);
			tool.setValues(response.get(0).getData());
		} else {
			// TODO Byta ut entitytype mot sträng motsvarande entity, getEntityNamebyId(entitytype) i eviro.java
			popupMessage("A " + Eviro.getEntityNameByNumber(entitytype) + " with the same values already exists!");

		}

	}

	protected boolean update(Updatable tool, int entitytype) {
		if (clientCtrlr.update(this, tool.getValues(), entitytype)) {

			popupMessage("The " + Eviro.getEntityNameByNumber(entitytype) + " has been succesfully updated!");
			return true;
		} else {
			Object[] test = new Object[tool.getValues().length];
			test[0] = tool.getValues()[0];
			tool.setValues(clientCtrlr.search(test, entitytype).get(0).getData());
			popupMessage("No changes made, update aborted!");
			return false;
		}

	}

	protected void setTabs(JPanel[] tabs) {

		tabbedPane.setBorder(new EmptyBorder(20, 0, 0, 0));

		for (JPanel t : tabs) {
			tabbedPane.addTab(t.getName(), t);
		}

		pnlCenter.setBackground(bgColor);
		pnlCenter.add(tabbedPane);

	}

	protected void setButtons(JButton[] buttons) {

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

	protected void setContent(JComponent[] content) {

		if (tabbedPane.getTabCount() > 0) {
			setContent(0, content);
		}

		else {
			setContent(pnlCenter, content);
		}

	}

	protected void setContent(int tabIndex, JComponent[] content) {

		if (tabbedPane.getTabCount() <= 0) {
			setContent(pnlCenter, content);
		}

		else {
			JPanel pnl = (JPanel) tabbedPane.getComponentAt(tabIndex);
			setContent(pnl, content);
		}
	}

	protected void setContent(JPanel pnl, JComponent[] content) {
		setContent(pnl, BorderLayout.NORTH, content);
	}

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

	protected void setTfEditable(LabledTextField[] textFields, Boolean enabled) {

		Color fieldColor;

		if (enabled) {
			fieldColor = Color.WHITE;
		}

		else {
			fieldColor = new Color(214, 217, 223);
		}

		for (JTextField c : textFields) {
			c.setEditable(enabled);
			c.setBackground(fieldColor);

		}

	}

	protected void setTfEditable(LabledTextField textField, Boolean enabled) {
		setTfEditable(new LabledTextField[] { textField }, enabled);
	}

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
		JOptionPane.showMessageDialog(this, txt);
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
		// Color errColor = new Color(169, 46, 34); // Röd?
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

			JOptionPane.showMessageDialog(this, "Please check the following fields before continuing:" + check, "Required fields missing",
					JOptionPane.INFORMATION_MESSAGE);
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
