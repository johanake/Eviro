package tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import client.ClientController;
import enteties.ForumMessage;
import gui.Tool;

public class ForumGUI extends JPanel implements Tool, ActionListener {

	private JTextField txtMessage = new JTextField("Your message");
	private JTextField txtName = new JTextField("Your name");
	private JButton btnSend = new JButton("Send");
	private JPanel pnlSouth = new JPanel(new BorderLayout());

	private ClientController clientController;
	private DefaultTableModel model;
	private JTable table = new JTable();
	private JScrollPane sp = new JScrollPane(table);
	private Timer timer = new Timer();
	private TimerTask task = new TimerTask() {

		public void run() {

			updateChat();
		}
	};

	public ForumGUI(ClientController clientController) {
		this.clientController = clientController;
		setupTable();
		txtName.setPreferredSize(new Dimension(100, 0));
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(600, 400));
		add(sp, BorderLayout.CENTER);
		add(pnlSouth, BorderLayout.SOUTH);
		pnlSouth.add(txtName, BorderLayout.WEST);
		pnlSouth.add(txtMessage, BorderLayout.CENTER);
		pnlSouth.add(btnSend, BorderLayout.EAST);
		btnSend.addActionListener(this);
		timer.scheduleAtFixedRate(task, 5000, 5000);
	}

	public void updateChat() {
		ArrayList<ForumMessage> list = clientController.getForumMessages();
		for (int i = model.getRowCount(); i < list.size(); i++) {
			model.addRow(list.get(i).getData());
		}
	}

	private void setupTable() {

		Object[] obj = { "Date", "User", "Message" };
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
		ArrayList<ForumMessage> list = clientController.getForumMessages();
		for (int i = 0; i < list.size(); i++) {
			model.addRow(list.get(i).getData());
		}
	}

	public void actionPerformed(ActionEvent e) {

		ForumMessage fm = new ForumMessage(new Object[] { null, txtName.getText(), txtMessage.getText() });
		clientController.addForumMessage(fm);
	}

	public String getTitle() {

		return "Eviro forum";
	}

	public boolean getRezizable() {

		return true;
	}

}
