package client;
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

//	static int globalToolCounter = 0;
//	static final int xOffset = 50, yOffset = 50;
//	private int toolCounter;
	
	private JPanel tool;

	/**
	 * Constructs a tool, adds generals and tool specifics.
	 * @param tool panel containt toll specifics
	 * @param whether the toolbox should be resizable or not
	 */
	public Toolbox(Tool tool) {

		super(tool.getTitle(), tool.getRezizable(), true, false, true);
		this.tool = (JPanel) tool;
//		this.toolCounter = ++globalToolCounter;
		
		setup();
	
	}

	/**
	 * Set up UI using the EDT. 
	 */
	private void setup() {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
//				setLocation(xOffset * toolCounter, yOffset * toolCounter);
				setMinimumSize(new Dimension(300,300));
				add(tool);
				pack();
				setVisible(true);
			}
		});
	}

}