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
	
	private int toolCounter;
	private JPanel tool;

	/**
	 * Constructs a tool, adds generals and tool specifics.
	 * 
	 * @param title title of the tool
	 * @param tool panel containt toll specifics
	 */
	public Toolbox(String title, JPanel tool) {
		
		super(title, true, true, true, true);
		
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
				setSize(600,500);
				setLocation(xOffset * toolCounter, yOffset * toolCounter);
				setVisible(true);
//				add(tool);
			}
		});
	}

}
