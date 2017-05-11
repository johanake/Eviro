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

		ArrayList<Entity> response = clientCtrlr.getAll(entitytype);

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
			popupMessage("Inget svar!");
		} else if (response.size() == 1) {
			popupMessage("Created \"entitytype\" with id: " + response.get(0).getData()[0]);
			tool.setValues(response.get(0).getData());
		} else {
			// TODO Byta ut entitytype mot sträng motsvarande entity, getEntityNamebyId(entitytype) i eviro.java
			popupMessage("A \"entitytype\" with the same values already exists!");

		}

	}

	protected boolean update(Updatable tool, int entitytype) {
		if (clientCtrlr.update(this, tool.getValues(), entitytype)) {

			popupMessage("Update succesfull!");
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
				// setJMenuBar(menu);
				setLayout(new BorderLayout());

				// setPreferredSize(new Dimension(640, 480));

				pnlNorth.setBackground(bgColor);
				pnlSouth.setBackground(bgColor);

				// pnlNorth.setBorder(new EmptyBorder(10, 10, 10, 10));
				pnlSouth.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black),
						BorderFactory.createEmptyBorder(10, 10, 10, 10)));

				add(pnlNorth, BorderLayout.NORTH);
				add(pnlCenter, BorderLayout.CENTER);
				add(pnlSouth, BorderLayout.SOUTH);

				// Fixar bredden
				pnlSouth.setPreferredSize(new Dimension(500, 50));

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

	protected void popupMessage(String txt) {
		JOptionPane.showMessageDialog(this, txt);
	}

	protected JInternalFrame getFrame() {
		return this;
	}

	/**
	 * Customization of JPanel that takes it's name as a parameter in the constructor.
	 * @author Robin Overgaard
	 * @version 1.0
	 */
	public class Tab extends JPanel {

		public Tab(String name, LayoutManager layout) {
			super(layout);
			setName(name);
		}

		public Tab(String name) {
			this(name, new BorderLayout());
		}

	}

	/**
	 * Customization of JPanel that takes it's name as a parameter in the constructor.
	 * @author Robin Overgaard
	 * @version 1.0
	 */
	public class SplitPanel extends JPanel {

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
	 * Customization of JPanel that takes it's name as a parameter in the constructor.
	 * @author Robin Overgaard
	 * @version 1.0
	 */
	public class LabledTextField extends JTextField {

		public LabledTextField(String name, boolean enabled) {
			setTfEditable(this, enabled);
			setName(name);
		}

		public LabledTextField(String name) {
			this(name, true);
		}

	}

	/**
	 * Customization of JButton that takes it's ActionCommand as a parameter in the constructor.
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
