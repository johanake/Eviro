package gui;

import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 * Floating container for the tools used in the system.
 * @author Robin Overgaard
 * @version 1.0
 */
public class Toolbox extends JInternalFrame {

	private JPanel tool;
	static int openFrameCount = 0;

	/**
	 * Constructs a tool, adds generals and tool specifics.
	 * @param tool panel containt toll specifics
	 * @param whether the toolbox should be resizable or not
	 */
	public Toolbox(Tool tool) {

		super(tool.getTitle(), tool.getRezizable(), true, false, true);
		this.tool = (JPanel) tool;
		openFrameCount++;
		setup();

		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				openFrameCount--;
			}
		});

	}

	/**
	 * Set up UI using the EDT.
	 */
	private void setup() {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setMinimumSize(new Dimension(300, 300));
				add(tool);
				pack();
				setVisible(true);
				setLocation(15 * openFrameCount, 15 * openFrameCount);
			}
		});
	}

}
