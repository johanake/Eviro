package tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import enteties.Entity;
import gui.Table;
import gui.Updatable;
		/**
		 * SearchResult represents a search result that happens in the program. 
		 * @author Johan Åkersson
		 * @author Robin Overgaard
		 *
		 */
public class SearchResult extends JInternalFrame {

	private Table table;
	private DefaultTableModel model;
	private JScrollPane scrollPane;

	/**
	 * Creats a search results.
	 * @param obj containing column headers
	 * @param gui sends the data to the gui
	 * @param list list of search results
	 */
	public SearchResult(Object[] obj, Updatable gui, ArrayList<Entity> list) {
		super("Search Result", true, true, false, true);
		setLayout(new BorderLayout());
		table = new Table(obj, false);
		table.populate(list);
		scrollPane = new JScrollPane(table);
		add(scrollPane);
		setPreferredSize(new Dimension(800, 400));
		setVisible(true);
		pack();

		table.setToolTipText("Double click to open");

		table.addMouseListener(new MouseAdapter() {

			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
			 */
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				if (me.getClickCount() == 2 && row >= 0) {

					Object[] values = new Object[obj.length];

					for (int i = 0; i < obj.length; i++) {
						values[i] = table.getModel().getValueAt(row, i);
					}
					gui.setValues(values);
					try {
						setClosed(true);
					} catch (PropertyVetoException e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

}
