import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * The main menu of the main workspace in the system. 
 * @author Robin Overgaard
 * @version 0.1
 */
public class Menu extends JMenuBar implements ActionListener {

	/**
	 * 
	 */
	public Menu() {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setupMenu();
			}
		});

	}

	/**
	 * 
	 */
	private void setupMenu() {
		
		JMenu menu = new JMenu("Menu");

		JMenuItem menuItem = new JMenuItem("Option");
		menuItem.setActionCommand("Option");
		menuItem.addActionListener(this);

		JMenuItem menuItem2 = new JMenuItem("Option 2");
		menuItem2.setActionCommand("Option 2");
		menuItem2.addActionListener(this);

		menu.add(menuItem);
		menu.add(menuItem2);
		add(menu);

		setVisible(true);
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {

		JOptionPane.showMessageDialog(null, "You clicked on: " + e.getActionCommand() + "!");

	}

}
