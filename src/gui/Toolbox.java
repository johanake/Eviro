package gui;
import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Floating container for the tools used in the system.
 * @author Robin Overgaard
 * @version 1.0
 */
public class Toolbox extends JInternalFrame {

	private JPanel tool;

	/**
	 * Constructs a tool, adds generals and tool specifics.
	 * @param tool panel containt toll specifics
	 * @param whether the toolbox should be resizable or not
	 */
	public Toolbox(Tool tool) {

		super(tool.getTitle(), tool.getRezizable(), true, false, true);
		this.tool = (JPanel) tool;
		
		setup();
	
	}

	/**
	 * Set up UI using the EDT. 
	 */
	private void setup() {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setMinimumSize(new Dimension(300,300));
				add(tool);
				pack();
				setVisible(true);
			}
		});
	}

}
