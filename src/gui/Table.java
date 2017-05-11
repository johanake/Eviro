package gui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.swing.JInternalFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import enteties.Entity;

public class Table extends JTable {

	private DefaultTableModel model;

	public Table(Object[] obj, boolean editable) {
		setFillsViewportHeight(true);

		setModel(new DefaultTableModel(obj, 0) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return editable;
			}
		});

		model = (DefaultTableModel) getModel();

	}

	public Table(JInternalFrame frame, Object[] obj, Updatable gui, ArrayList<Entity> list) {
		this(obj, false);
		populate(list);
		addMouseListener(new MouseAdapter() {

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
						frame.setClosed(true);
					} catch (PropertyVetoException e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

	public Table(Object[] obj) {
		this(obj, true);
	}

	public void populate(ArrayList<Entity> objectList) {
		for (int i = 0; i < objectList.size(); i++) {
			model.addRow(objectList.get(i).getData());
		}
	}

	public void populate(Object[] info) {
		model.addRow(info);
	}

}
