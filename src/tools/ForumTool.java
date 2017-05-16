package tools;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import client.ClientController;
import gui.GUIController;
import gui.Table;
import gui.Tool;
import gui.Updatable;
import gui.Tool.Tab;
import shared.Eviro;

/**
 * A forum for Eviro users.
 * @author Mattias Sundquist
 */
public class ForumTool extends Tool implements Updatable {

	private ButtonListener buttonListener;

	private Tab tab1 = new Tab("Tab 1");
	private Tab[] tabs = new Tab[] { tab1, new Tab("General") };
	
	private ActionButton btnOpen = new ActionButton("Open Message", "open");
	private ActionButton btnUpdate = new ActionButton("Update", "update");
	private ActionButton btnNew = new ActionButton("New Message", "new");

	private JButton[] allButtons = { btnOpen, btnUpdate, btnNew };
	private JButton[] defaultButtons = { btnOpen, btnUpdate, btnNew };

	private ArrayList<Object[]> messageList = new ArrayList<Object[]>();

	private Table posts = new Table(new Object[] { "Date", "User", "Topic" }, false);

	public ForumTool(ClientController clientController, GUIController guiController) {
		super("Eviro Forum", clientController, guiController);
		setButtons(defaultButtons);
		setTabs(tabs);
		tab1.add(new JScrollPane(posts), BorderLayout.CENTER);
		posts.getColumnModel().getColumn(0).setMinWidth(150);
		posts.getColumnModel().getColumn(0).setMaxWidth(150);
		posts.getColumnModel().getColumn(1).setMinWidth(150);
		posts.getColumnModel().getColumn(1).setMaxWidth(150);
		buttonListener = new ButtonListener();
		get(this, Eviro.ENTITY_FORUMMESSAGE);

		posts.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				if (me.getClickCount() == 2 && row >= 0) {

					if (posts.getValueAt(posts.getSelectedRow(), 0) != null) {
						guiCtrlr.add(new ReadWriteMessage("Read/Write Message", messageList.get(posts.getSelectedRow())));
					} else {
						JOptionPane.showMessageDialog(null, "No message is selected.");
					}

				}
			}
		});

	}

	@Override
	public void setValues(Object[] values) {
		messageList.clear();
		for (int i = 0; i < values.length; i++) {
			messageList.add((Object[]) values[i]);
			posts.populate((Object[]) values[i], i);
		}
	}

	@Override
	public String[] getValues() {
		return null;
	}

	@Override
	public String[] getValues(boolean getNames) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Updatable getThis() {
		return this;
	}

	private class ButtonListener implements ActionListener {

		public ButtonListener() {
			for (JButton btn : allButtons) {
				btn.addActionListener(this);
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			switch (e.getActionCommand()) {

			case "update":
				get(getThis(), Eviro.ENTITY_FORUMMESSAGE);
				break;

			case "new":
				guiCtrlr.add(new ReadWriteMessage("Read/Write Message"));
				break;

			case "open":
				if (posts.getValueAt(posts.getSelectedRow(), 0) != null) {
					guiCtrlr.add(new ReadWriteMessage("Read/Write Message", messageList.get(posts.getSelectedRow())));
				} else {
					JOptionPane.showMessageDialog(null, "No message is selected.");
				}
				break;

			}
		}
	}

	private class ReadWriteMessage extends Tool implements Updatable {

		private ButtonListener buttonListener;

		private ActionButton btnSend = new ActionButton("Send", "send");
		private ActionButton btnDelete = new ActionButton("Delete", "delete");

		private JButton[] allButtons = { btnSend, btnDelete };
		private JButton[] defaultButtons = { btnSend };
		private JButton[] lookingButtons = { btnDelete };

		private LabledTextField ltfTopic = new LabledTextField("Topic");
		private LabledTextField ltfMsg = new LabledTextField("Message");

		public ReadWriteMessage(String title) {
			super(title, ForumTool.this.clientCtrlr, ForumTool.this.guiCtrlr);
			setup();
			setTfEditable(new LabledTextField[] { ltfTopic, ltfMsg }, true);
			setButtons(defaultButtons);
		}

		public ReadWriteMessage(String title, Object[] values) {
			super(title, ForumTool.this.clientCtrlr, ForumTool.this.guiCtrlr);
			setValues(values);
			setup();
			setTfEditable(new LabledTextField[] { ltfTopic, ltfMsg }, false);
			btnDelete.setEnabled(false);
			setButtons(lookingButtons);
		}

		public void setup() {
			setContent(new JComponent[] { ltfTopic, ltfMsg });
			buttonListener = new ButtonListener();
		}

		@Override
		public void setValues(Object[] values) {
			ltfTopic.setText((String) values[2]);
			ltfMsg.setText((String) values[3]);

		}

		@Override
		public String[] getValues() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Updatable getThis() {
			return ForumTool.this.getThis();
		}

		private class ButtonListener implements ActionListener {

			public ButtonListener() {
				for (JButton btn : allButtons) {
					btn.addActionListener(this);
				}
			}

			@Override
			public void actionPerformed(ActionEvent e) {

				switch (e.getActionCommand()) {

				case "send":
					if (ltfTopic.getText().trim().length() > 0 && ltfMsg.getText().trim().length() > 0) {
						Object[] obj = { null, "Anon", ltfTopic.getText(), ltfMsg.getText() };
						ForumTool.this.clientCtrlr.create(obj, Eviro.ENTITY_FORUMMESSAGE);

						try {
							getFrame().setClosed(true);
						} catch (PropertyVetoException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						get(getThis(), Eviro.ENTITY_FORUMMESSAGE);

					} else
						JOptionPane.showMessageDialog(null, "You must enter a topic and a message");
					break;

				}
			}
		}

		@Override
		public String[] getValues(boolean getNames) {
			// TODO Auto-generated method stub
			return null;
		}
	}

}
