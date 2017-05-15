package gui;

import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import enteties.Entity;

public class Table extends JTable {

	private DefaultTableModel model;

	/**
	 * Creates a table with 100 empty rows.
	 * @param columns the columns to display in the table
	 * @param editable whether the table is editable or not
	 */
	public Table(Object[] columns, boolean editable) {
		setFillsViewportHeight(true);

		setModel(new DefaultTableModel(columns, 100) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return editable;
			}
		});

		this.model = (DefaultTableModel) getModel();

	}

	/**
	 * Populates the table with data.
	 * @param data list of entities to populate the table with
	 */
	public void populate(ArrayList<Entity> data) {
		model.setRowCount(0);
		for (int i = 0; i < data.size(); i++) {
			model.addRow(data.get(i).getData());
		}

	}

	/**
	 * Populates the table with data.
	 * @param data array of objects to populate the table with
	 * @param row the row to add the data to
	 */
	public void populate(Object[] data, int row) {

		for (int i = 0; i < model.getColumnCount(); i++) {
			model.setValueAt(data[i], row, i);
		}

	}

	/**
	 * Resets the table.
	 */
	public void reset() {
		model.setRowCount(0);
		model.setRowCount(100);
	}

}
