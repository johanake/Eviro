package tools;

import java.awt.BorderLayout;
import java.awt.Dimension;


import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import gui.Tool;

public class SearchResults extends JPanel implements Tool{
	JTable table = new JTable(new DefaultTableModel(new Object[]{"Customer ID", "Name", "Address", "Zip Code", "City", "Phone Number", "Email", "VAT Number"}, 0));
	DefaultTableModel model = (DefaultTableModel) table.getModel();
	JScrollPane scrollPane = new JScrollPane(table);
	
	
	public SearchResults(){
		setLayout(new BorderLayout());
		add(scrollPane);		
		setVisible(true);
		table.setFillsViewportHeight(true);		//Så syns alla cols
		model.addRow(new Object[]{"hej", "test"});

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