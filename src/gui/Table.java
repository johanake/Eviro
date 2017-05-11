package gui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.swing.JInternalFrame;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import enteties.Entity;
import tools.temp_invoice;

public class Table extends JTable {

	private DefaultTableModel model;
	private temp_invoice invoice;

	// General
	public Table(Object[] obj, int rows, boolean editable) {
		setFillsViewportHeight(true);

		setModel(new DefaultTableModel(obj, rows) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return editable;
			}
		});

		model = (DefaultTableModel) getModel();

	}

	// Search result
	public Table(JInternalFrame frame, Object[] obj, Updatable gui, ArrayList<Entity> list) {
		this(obj, 0, false);
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(getModel());
		setRowSorter(sorter);
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

	@Override
	public void editingStopped(ChangeEvent e) {
		// getting these values before calling super.editingStopped(e); because they get erased.
		int row = getEditingRow();
		int col = getEditingColumn();
		super.editingStopped(e); // must call the super code to have a working edition

		if (col == 0) {
			// invoice.getArticle(getValueAt(row, col));
			// String value = (String) getValueAt(getSelectedRow(), 0);
			invoice.getArticle((String) getValueAt(row, col), row);
		}
	};

	// Products
	public Table(temp_invoice invoice, Object[] obj) {
		this(obj, 10, true);
		this.invoice = invoice;
		// model.addTableModelListener(new TableModelListener() {
		// @Override
		// public void tableChanged(TableModelEvent tableModelEvent) {
		//
		// // getCellEditor().stopCellEditing();
		// if (isEditing()) {
		// String value = (String) getValueAt(getSelectedRow(), 0);
		// invoice.getArticle(value);
		//
		// }
		// }
		// });
	}

	public void populate(ArrayList<Entity> objectList) {
		for (int i = 0; i < objectList.size(); i++) {
			model.addRow(objectList.get(i).getData());
		}

	}

	public void populate(Object[] info, int row) {

		for (int i = 0; i < info.length; i++) {
			model.setValueAt(info[i], row, i);
		}

	}

}
