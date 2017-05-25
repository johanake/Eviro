package tools;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

import client.ClientController;
import enteties.Entity;
import gui.GUIController;
import gui.Table;
import gui.Tool;
import gui.Updatable;
import shared.Eviro;

/**
 * A GUI tool for Eviro users to communicate with other Eviro users.
 * @author Mattias Sundquist
 */
public class SocialTool extends Tool implements Updatable {

	private ButtonListener buttonListener;
	private ActionButton btnOpen = new ActionButton("Open Message", "open");
	private ActionButton btnUpdate = new ActionButton("Update", "update");
	private ActionButton btnNew = new ActionButton("New Message", "new");
	private JButton[] allButtons = { btnOpen, btnUpdate, btnNew };
	private ArrayList<Object[]> messageList = new ArrayList<Object[]>();
	private Table posts = new Table(new Object[] { "Date", "User", "Topic" }, false);

	/**
	 * Builds an internal JFrame and creates a "wall" where Eviro users can communicate with each other.
	 * @param clientController The ClientController to send to the tool
	 * @param guiController The GUIController to send to the tool
	 */
	public SocialTool(ClientController clientController, GUIController guiController) {
		super("Social", clientController, guiController);
		setButtons(allButtons);
		pnlCenter.add(new JScrollPane(posts), BorderLayout.CENTER);
		buttonListener = new ButtonListener();
		get(this, Eviro.ENTITY_FORUMMESSAGE);
		posts.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent me) {

				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				if (me.getClickCount() == 2 && row >= 0) {

					if (posts.getModel().getValueAt(posts.getSelectedRow(), 0) != null) {
						Object[] obj = messageList.get(posts.getSelectedRow());
						guiCtrlr.add(new ReadWriteMessage("Read message from " + obj[1].toString(), obj));
					} else {
						JOptionPane.showMessageDialog(null, "No message is selected.");
					}

				}
			}
		});

		btnOpen.setMnemonic(KeyEvent.VK_O);
		btnUpdate.setMnemonic(KeyEvent.VK_U);
		btnNew.setMnemonic(KeyEvent.VK_N);
	}

	protected void get(Updatable tool, int entitytype) {

		ArrayList<Entity> response = clientCtrlr.getAllbyType(entitytype);

		Object[][] results = new Object[response.size()][4];

		for (int i = 0; i < results.length; i++) {
			results[i] = response.get(i).getData();

		}

		tool.setValues(results);

	}

	/**
	 * NOT USED
	 */
	@Override
	public void setValues(Object[] values) {

		messageList.clear();
		for (int i = 0; i < values.length; i++) {
			messageList.add((Object[]) values[i]);
			posts.populate((Object[]) values[i], i);
		}
	}

	/**
	 * NOT USED
	 */
	@Override
	public String[] getValues() {

		return null;
	}

	/**
	 * NOT USED
	 */
	@Override
	public String[] getValues(boolean getNames) {

		return null;
	}

	/**
	 * NOT USED
	 */
	@Override
	public Updatable getThis() {

		return this;
	}

	/**
	 * An ActionListener class that handles button clicks.
	 * @author Mattias Sundquist
	 */
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
				guiCtrlr.add(new ReadWriteMessage("Write new Message"));
				break;

			case "open":
				if (posts.getModel().getValueAt(posts.getSelectedRow(), 0) != null) {
					Object[] obj = messageList.get(posts.getSelectedRow());
					guiCtrlr.add(new ReadWriteMessage("Read message from " + obj[1].toString(), obj));
				} else {
					JOptionPane.showMessageDialog(null, "No message is selected.");
				}
				break;
			}
		}
	}

	/**
	 * A GUI tool used by the SocialTool class to show messages.
	 * @author Mattias Sundquist
	 */
	private class ReadWriteMessage extends Tool implements Updatable {

		private ButtonListener buttonListener;
		private ActionButton btnSend = new ActionButton("Send", "send");
		private ActionButton btnDelete = new ActionButton("Delete", "delete");
		private JButton[] allButtons = { btnSend, btnDelete };
		private JButton[] defaultButtons = { btnSend };
		private JButton[] lookingButtons = { btnDelete };
		private LabledTextField ltfTopic = new LabledTextField("Topic");
		private JTextArea txtMessage = new JTextArea();

		/**
		 * Constructor for writing a new message
		 * @param title The windows title.
		 */
		public ReadWriteMessage(String title) {
			super(title, SocialTool.this.clientCtrlr, SocialTool.this.guiCtrlr);
			setup();
			setTfEditable(new JTextComponent[] { ltfTopic, txtMessage }, true);
			setButtons(defaultButtons);
		}

		/**
		 * Constructor for reading a message.
		 * @param title The windows title.
		 * @param values The topic and message to be read.
		 */
		public ReadWriteMessage(String title, Object[] values) {
			super(title, SocialTool.this.clientCtrlr, SocialTool.this.guiCtrlr);
			setValues(values);
			setup();
			setTfEditable(new JTextComponent[] { ltfTopic, txtMessage }, false);
			btnDelete.setEnabled(false);
			setButtons(lookingButtons);
			btnSend.setMnemonic(KeyEvent.VK_ENTER);
		}

		/**
		 * Sets up the layout and button listeners.
		 */
		public void setup() {

			JPanel pnlMessage = new JPanel(new BorderLayout());
			txtMessage.setLineWrap(true);
			txtMessage.setRows(5);
			pnlMessage.add(new JScrollPane(txtMessage), BorderLayout.CENTER);
			pnlMessage.setBorder(new EmptyBorder(0, 15, 15, 15));

			setContent(new JComponent[] { ltfTopic });
			pnlCenter.add(pnlMessage, BorderLayout.CENTER);
			buttonListener = new ButtonListener();
		}

		/**
		 * NOT USED
		 */
		@Override
		public void setValues(Object[] values) {

			ltfTopic.setText((String) values[2]);
			txtMessage.setText((String) values[3]);
		}

		/**
		 * NOT USED
		 */
		@Override
		public String[] getValues() {

			return null;
		}

		/**
		 * NOT USED
		 */
		@Override
		public Updatable getThis() {

			return SocialTool.this.getThis();
		}

		/**
		 * NOT USED
		 */
		@Override
		public String[] getValues(boolean getNames) {

			return null;
		}

		/**
		 * An ActionListener class that handles button clicks.
		 * @author Mattias Sundquist
		 */
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
					if (ltfTopic.getText().trim().length() > 0 && txtMessage.getText().trim().length() > 0) {
						Object[] obj = {
								null,
								clientCtrlr.getActiveUser().getData()[1],
								ltfTopic.getText(),
								txtMessage.getText() };
						SocialTool.this.clientCtrlr.create(obj, Eviro.ENTITY_FORUMMESSAGE);

						try {
							getFrame().setClosed(true);
						} catch (PropertyVetoException e1) {
							e1.printStackTrace();
						}

						get(getThis(), Eviro.ENTITY_FORUMMESSAGE);

					} else
						JOptionPane.showMessageDialog(null, "You must enter a topic and a message");
					break;
				}
			}
		}
	}
}
