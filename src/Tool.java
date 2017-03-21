import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Tool extends JPanel {

	JPanel pnlBtns = new JPanel(new GridLayout(1,5));
	JPanel pnlMain = new JPanel(new GridLayout(1,2));
	JPanel pnlMainLeft = new JPanel(new GridLayout(10,2));
	JPanel pnlMainRight = new JPanel(new GridLayout(10,2));
	
	public Tool() {
		setLayout(new BorderLayout());
		add(pnlMain, BorderLayout.CENTER);
		add(pnlBtns, BorderLayout.SOUTH);
		
		pnlMain.add(pnlMainLeft);
		pnlMain.add(pnlMainRight);
		
		pnlBtns.setBorder(new EmptyBorder(0, 10, 10, 10));
		pnlMainLeft.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlMainRight.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		for(int i = 1; i < 6; i++) {
			pnlBtns.add(new JButton("Demobutton " + i));
		}
		
		for(int i = 1; i < 11; i++) {
			pnlMainLeft.add(new JLabel("Demolabel "  + i));
			pnlMainLeft.add(new JTextField("Demovalue " + i));
			pnlMainRight.add(new JLabel("Demolabel " + i));
			pnlMainRight.add(new JTextField("Demovalue "  + i));
		}
	}
}
