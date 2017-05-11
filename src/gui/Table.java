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
import tools.InvoiceTool;

public class Table extends JTable {

	private DefaultTableModel model;
	private InvoiceTool invoice;

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

	// Products constructor
	public Table(InvoiceTool invoice, Object[] obj) {
		this(obj, 100, true);
		this.invoice = invoice;
	}

	@Override
	public void editingStopped(ChangeEvent e) {

		int row = getEditingRow();
		int col = getEditingColumn();
		super.editingStopped(e);

		if (col == 0) {
			invoice.getArticle((String) getValueAt(row, col), row);
		}

		else if (col == 2 || col == 3) {
			int price = 0;
			int quantity = 0;
			try {
				price = Integer.parseInt((String) getValueAt(row, 2));
				quantity = Integer.parseInt((String) getValueAt(row, 3));
			} catch (NumberFormatException nfe) {
				// price = 0;
				// quantity = 1;
			}

			model.setValueAt(Integer.toString(price * quantity), row, 4);
		}

		invoice.setTotalPrice();

	};

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

	public void reset() {
		model.setRowCount(0);
		model.setRowCount(100);
	}

}
