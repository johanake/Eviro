import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class Tool extends JPanel {

	// Basic layout example
	JPanel pnlBtns = new JPanel(new GridLayout(1, 5));
	JPanel pnlMain = new JPanel(new GridLayout(1, 2));
	JPanel pnlMainLeft = new JPanel(new GridLayout(10, 2));
	JPanel pnlMainRight = new JPanel(new GridLayout(10, 2));

	public Tool() {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setLayout(new BorderLayout());
				add(pnlMain, BorderLayout.CENTER);
				add(pnlBtns, BorderLayout.SOUTH);

				pnlMain.add(pnlMainLeft);
				pnlMain.add(pnlMainRight);

				pnlBtns.setBorder(new EmptyBorder(0, 10, 10, 10));
				pnlMainLeft.setBorder(new EmptyBorder(10, 10, 10, 10));
				pnlMainRight.setBorder(new EmptyBorder(10, 10, 10, 10));
			}
		});
		
	}
	
}
