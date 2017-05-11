package tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
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
		table = new Table(this, obj, gui, list);
		scrollPane = new JScrollPane(table);
		add(scrollPane);
		setPreferredSize(new Dimension(800, 400));
		setVisible(true);
		pack();

	}

}
