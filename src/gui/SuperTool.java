package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;

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

public class SuperTool extends JInternalFrame {

	private Color bgColor = new Color(233, 236, 242);

	private Menu menu = new Menu();

	private JPanel pnlNorth = new JPanel(new BorderLayout());
	private JPanel pnlCenter = new JPanel(new BorderLayout());
	private JPanel pnlSouth = new JPanel(new BorderLayout());

	private JTabbedPane tabbedPane = new JTabbedPane();

	static int openFrameCount = 0;

	public ClientController clientCtrlr;
	public GUIController guiCtrlr;

	public SuperTool(String title, ClientController clientController, GUIController guiController) {
		super(title, true, true, false, true);
		this.clientCtrlr = clientController;
		this.guiCtrlr = guiController;
		setup();
		openFrameCount++;
	}

	public void setTabs(JPanel[] tabs) {

		for (JPanel t : tabs) {

			tabbedPane.addTab(t.getName(), t);
		}

		pnlCenter.setBackground(bgColor);
		pnlCenter.add(tabbedPane);

	}

	public void setButtons(JButton[] buttons) {

		JPanel pnlButtons = new JPanel(new GridLayout(1, buttons.length));
		pnlButtons.setBackground(bgColor);
		for (JButton b : buttons) {

			pnlButtons.add(b);
		}

		pnlSouth.add(pnlButtons, BorderLayout.EAST);

	}

	public void setContent(JComponent[] content) {

		if (tabbedPane.getTabCount() > 0) {
			setContent(0, content);
		}

		else {
			setContent(pnlCenter, content);
		}

	}

	public void setContent(int tabIndex, JComponent[] content) {

		if (tabbedPane.getTabCount() <= 0) {
			setContent(pnlCenter, content);
		}

		else {
			JPanel pnl = (JPanel) tabbedPane.getComponentAt(tabIndex);
			setContent(pnl, content);
		}
	}

	public void setContent(JPanel pnl, JComponent[] content) {

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
		pnl.add(pnlContent, BorderLayout.NORTH);

	}

	private void setup() {

		// pnlCenter = new JPanel(new GridLayout(components.length, 1));
		//
		// for (JComponent c : components) {
		// pnlCenter.add(c);
		// }

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// setJMenuBar(menu);
				setLayout(new BorderLayout());

				// setPreferredSize(new Dimension(640, 480));

				pnlNorth.setBackground(bgColor);
				pnlSouth.setBackground(bgColor);

				pnlNorth.setBorder(new EmptyBorder(10, 10, 10, 10));
				pnlSouth.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black),
						BorderFactory.createEmptyBorder(10, 10, 10, 10)));

				add(pnlNorth, BorderLayout.NORTH);
				add(pnlCenter, BorderLayout.CENTER);
				add(pnlSouth, BorderLayout.SOUTH);

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
			setEnabled(enabled);
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
