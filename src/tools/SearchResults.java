package tools;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import enteties.Customer;
import gui.Tool;

public class SearchResults extends JPanel implements Tool{
	JTable table = new JTable(new DefaultTableModel(new Object[]{"Customer ID", "Name", "Address", "Zip Code", "City", "Phone Number", "Email", "VAT Number"}, 0));
	DefaultTableModel model = (DefaultTableModel) table.getModel();
	JScrollPane scrollPane = new JScrollPane(table);
	
	
	public SearchResults(ArrayList<Customer> list){
		System.out.println("4");
		setLayout(new BorderLayout());
		add(scrollPane);		
		setVisible(true);
		table.setFillsViewportHeight(true);		//Så syns alla cols
		addRow(list);

	}
	
	private void addRow(ArrayList<Customer> objectList){
		System.out.println("5");		
		for(int i=0; i<objectList.size(); i++){
			model.addRow(objectList.get(i).getAllInObjects());
		}

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