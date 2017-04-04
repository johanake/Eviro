import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Floating container for the tools used in the system.
 * 
 * @author Robin Overgaard
 * @version 0.1
 */
public class Toolbox extends JInternalFrame {

	static int globalToolCounter = 0;
	static final int xOffset = 50, yOffset = 50;
	
	//HEJ HEJ
	
	private int toolCounter;
	private JPanel tool;

	/**
	 * Constructs a tool, adds generals and tool specifics.
	 * 
	 * @param title title of the tool
	 * @param tool panel containt toll specifics
	 * @param whether the toolbox should be resizable or not
	 */
	public Toolbox(String title, JPanel tool, boolean resizable) {
		
		super(title, resizable, true, false, true);
		
		this.tool = tool;
		this.toolCounter = ++globalToolCounter;
		
		setup();

	}

	/**
	 * Set up UI using the edt. 
	 */
	private void setup() {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setLocation(xOffset * toolCounter, yOffset * toolCounter);
				setMinimumSize(new Dimension(300,300));
				add(tool);
				pack();
				setVisible(true);
			}
		});
	}

}
