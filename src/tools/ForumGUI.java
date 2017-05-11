package tools;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import client.ClientController;
import enteties.ForumMessage;
import gui.GUIController;
import gui.Tool;

/**
 * A forum for Eviro users.
 * 
 * @author Mattias Sundquist
 *
 */
public class ForumGUI extends JPanel implements Tool, ActionListener {

	private JButton btnOpen = new JButton("Open Message");
	private JButton btnUpdate = new JButton("Update");
	private JButton btnNew = new JButton("New Message");
	private JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));

	private ClientController clientController;
	private GUIController guiController;
	private DefaultTableModel model;
	private JTable table = new JTable();
	private JScrollPane sp = new JScrollPane(table);
	private ArrayList<ForumMessage> messageList;

	public ForumGUI(ClientController clientController, GUIController guiController) {
		this.guiController = guiController;
		this.clientController = clientController;
		setupTable();
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(600, 400));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		add(sp, BorderLayout.CENTER);
		add(pnlSouth, BorderLayout.SOUTH);
		pnlSouth.add(btnOpen);
		pnlSouth.add(btnUpdate);
		pnlSouth.add(btnNew);
		btnUpdate.addActionListener(this);
		btnNew.addActionListener(this);
		btnOpen.addActionListener(this);
		scrollToBottom();
	}

	private void scrollToBottom() {

		JScrollBar verticalBar = sp.getVerticalScrollBar();
		AdjustmentListener downScroller = new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {

				Adjustable adjustable = e.getAdjustable();
				adjustable.setValue(adjustable.getMaximum());
				verticalBar.removeAdjustmentListener(this);
			}
		};
		verticalBar.addAdjustmentListener(downScroller);
	}

	public void updateChat() {

		ArrayList<ForumMessage> list = clientController.getForumMessages();
		for (int i = model.getRowCount(); i < list.size(); i++) {
			model.addRow(list.get(i).getData());
		}
		scrollToBottom();
	}

	private void setupTable() {

		Object[] obj = { "Date", "User", "Topic" };
		table.setModel(new DefaultTableModel(obj, 0) {

			public boolean isCellEditable(int row, int col) {

				return false;
			}
		});
		model = (DefaultTableModel) table.getModel();
		table.getColumnModel().getColumn(0).setMinWidth(150);
		table.getColumnModel().getColumn(0).setMaxWidth(150);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(1).setMaxWidth(150);
		messageList = clientController.getForumMessages();
		for (int i = 0; i < messageList.size(); i++) {
			model.addRow(messageList.get(i).getData());
		}

		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent me) {

				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				int length = messageList.get(row).getData().length;
				if (me.getClickCount() == 2 && row >= 0) {
					Object[] values = messageList.get(row).getData();
					guiController.popup(new ReadWriteMessage(values));
				}
			}
		});
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnUpdate)
			updateChat();
		if (e.getSource() == btnNew)
			guiController.popup(new ReadWriteMessage());
		if (e.getSource() == btnOpen) {
			int row = table.getSelectedRow();
			if (row != -1) {
				Object[] values = messageList.get(row).getData();
				guiController.popup(new ReadWriteMessage(values));
			} else
				JOptionPane.showMessageDialog(null, "No message is selected.");
		}
	}

	public String getTitle() {

		return "Eviro forum";
	}

	public boolean getRezizable() {

		return true;
	}

	private class ReadWriteMessage extends JPanel implements Tool, ActionListener {

		private JPanel pnlLabels = new JPanel();
		private JPanel pnlText = new JPanel();
		private JPanel pnlSouth = new JPanel();
		private JButton btnSend = new JButton("Send");
		private JTextField txtTopic = new JTextField();
		private JTextArea txtArea = new JTextArea();
		private JScrollPane sp = new JScrollPane(txtArea);
		private JLabel lblTopic = new JLabel("Topic");
		private JLabel lblMessage = new JLabel("Message");

		public ReadWriteMessage() {
			setPreferredSize(new Dimension(400, 200));
			setBorder(new EmptyBorder(10, 10, 10, 10));
			setLayout(new BorderLayout());
			lblTopic.setBorder(new EmptyBorder(0, 0, 10, 0));
			pnlLabels.setLayout(new BoxLayout(pnlLabels, BoxLayout.Y_AXIS));
			pnlText.setLayout(new BorderLayout());
			pnlSouth.setLayout(new FlowLayout(FlowLayout.RIGHT));
			pnlLabels.add(lblTopic);
			pnlLabels.add(lblMessage);
			pnlText.add(txtTopic, BorderLayout.NORTH);
			pnlText.add(sp);
			pnlSouth.add(btnSend);
			txtArea.setLineWrap(true);
			txtArea.setWrapStyleWord(true);
			add(pnlLabels, BorderLayout.WEST);
			add(pnlText);
			add(pnlSouth, BorderLayout.SOUTH);
			btnSend.addActionListener(this);
		}

		public ReadWriteMessage(Object[] values) {
			this();
			txtTopic.setText(values[2].toString());
			txtArea.append(values[3].toString());
			btnSend.setEnabled(false);
			txtTopic.setEditable(false);
			txtArea.setEditable(false);
		}

		@Override
		public boolean getRezizable() {

			return true;
		}

		@Override
		public String getTitle() {

			return "Read/Write Message";
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == btnSend) {
				if (!txtArea.getText().equals("") && !txtTopic.getText().equals("")) {
					Object[] obj = { null, "Anon", txtTopic.getText(), txtArea.getText() };
					ForumMessage fm = new ForumMessage(obj);
					messageList.add(fm);
					clientController.addForumMessage(fm);
				} else
					JOptionPane.showMessageDialog(null, "You must enter a topic and a message");
			}
		}
	}
}
