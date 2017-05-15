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

public class SearchResult extends JInternalFrame {

	private Table table;
	private DefaultTableModel model;
	private JScrollPane scrollPane;

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

			@Override
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				if (me.getClickCount() == 2 && row >= 0) {

					Object[] values = new Object[obj.length];

					for (int i = 0; i < obj.length; i++) {
						values[i] = table.getValueAt(row, i);
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
