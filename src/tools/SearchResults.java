package tools;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import enteties.Customer;
import gui.Tool;

public class SearchResults extends JPanel implements Tool {
	private JTable table;
	private DefaultTableModel model;
	private JScrollPane scrollPane;

	public SearchResults(Object[] obj, CustomerGUI gui, ArrayList<Customer> list) {

		table = new JTable();

		table.setModel(new DefaultTableModel(obj, 0) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}

		});

		// table = new JTable(new DefaultTableModel(obj, rows));
		model = (DefaultTableModel) table.getModel();
		scrollPane = new JScrollPane(table);

		setLayout(new BorderLayout());
		add(scrollPane);
		setVisible(true);
		table.setFillsViewportHeight(true);
		addCustomer(list);

		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				if (me.getClickCount() == 2) {

					Object[] values = new Object[obj.length];

					for (int i = 0; i < obj.length; i++) {
						values[i] = table.getValueAt(row, i);
					}
					// System.out.println(Arrays.toString(values));
					gui.setText(values);

				}

			}
		});

	}

	public SearchResults(Object[] obj, int rows) {
		table = new JTable(new DefaultTableModel(obj, rows));
		model = (DefaultTableModel) table.getModel();
		scrollPane = new JScrollPane(table);
		setLayout(new BorderLayout());
		add(scrollPane);
		setVisible(true);
		table.setFillsViewportHeight(true);

	}

	public void addCustomer(ArrayList<Customer> objectList) {
		for (int i = 0; i < objectList.size(); i++) {
			model.addRow(objectList.get(i).getAllInObjects());
		}

	}

	public void addArticle(Object[] info) {
		model.addRow(info);

	}

	public JTable getTable() {
		return table;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Search Results";
	}

	@Override
	public boolean getRezizable() {
		// TODO Auto-generated method stub
		return true;
	}
}