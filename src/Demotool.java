import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Demotool extends Tool {
	
	public Demotool() {
		
		setPreferredSize(new Dimension(400,400));
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
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
		});
		
	}

}
