package view.GUIView;

import javax.swing.SwingUtilities;

public class Tutorial {
	
	private static final String TUTORIAL_PATH = "resources/tutorial.mp4";
	private static final String TUTORIAL_WINDOW_NAME = "ROLIT Tutorial";
	
	public void open() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Video videoFrame = new Video(TUTORIAL_PATH, TUTORIAL_WINDOW_NAME);
			}
		});
	}
	
}