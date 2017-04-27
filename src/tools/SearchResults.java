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
	private JTable table;
	private DefaultTableModel model;
	private JScrollPane scrollPane;
	
	
	public SearchResults(Object[] obj, int rows, ArrayList<Customer> list){
		table = new JTable(new DefaultTableModel(obj, rows));
		model = (DefaultTableModel) table.getModel();
		scrollPane = new JScrollPane(table);
		System.out.println("4");
		setLayout(new BorderLayout());
		add(scrollPane);		
		setVisible(true);
		table.setFillsViewportHeight(true);		//Så syns alla cols
		addCustomer(list);

	}
	
	public SearchResults(Object[] obj, int rows){
		table = new JTable(new DefaultTableModel(obj, rows));
		model = (DefaultTableModel) table.getModel();
		scrollPane = new JScrollPane(table);
		setLayout(new BorderLayout());
		add(scrollPane);		
		setVisible(true);
		
	}
	
	public void addCustomer(ArrayList<Customer> objectList){
		System.out.println("5");		
		for(int i=0; i<objectList.size(); i++){
			model.addRow(objectList.get(i).getAllInObjects());			
		}

	}
	
	public void addArticle(String[] info){
		model.addRow(info);
		
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