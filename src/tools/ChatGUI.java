package tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import client.ClientController;
import gui.GUIController;
import gui.Tool;

public class ChatGUI extends JPanel implements Tool, ActionListener {

	private ClientController clientController;
	private Object GUIController;
	private JTextArea txtArea = new JTextArea();
	private JTextField txtMessage = new JTextField("Your message");
	private JTextField txtName = new JTextField("Your name");
	private JButton btnSend = new JButton("Send");
	private JPanel pnlSouth = new JPanel(new BorderLayout());
	private JScrollPane sp = new JScrollPane(txtArea);

	public ChatGUI(ClientController clientController, GUIController guiController) {
		this.clientController = clientController;
		this.GUIController = GUIController;
		txtName.setPreferredSize(new Dimension(100, 0));
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(400, 275));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		add(sp, BorderLayout.CENTER);
		add(pnlSouth, BorderLayout.SOUTH);
		pnlSouth.add(txtName, BorderLayout.WEST);
		pnlSouth.add(txtMessage, BorderLayout.CENTER);
		pnlSouth.add(btnSend, BorderLayout.EAST);
		btnSend.addActionListener(this);
		String[] res = clientController.getChatMessages();
		for (int i = 0; i < res.length; i++) {
			txtArea.append(res[i]);
		}
	}

	public void actionPerformed(ActionEvent e) {
		String res = txtName.getText() + ": " + txtMessage.getText();
		clientController.addChatMessage(res);
		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		txtArea.append(timestamp + "\t" + res);
	}

	public String getTitle() {

		return "Chatwindow";
	}

	public boolean getRezizable() {

		return true;
	}

}
