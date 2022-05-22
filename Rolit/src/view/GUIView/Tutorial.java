package view.GUIView;

import javax.swing.SwingUtilities;

/**
 * This class represents the Rolit tutorial.
 * @author PMC
 *
 */
public class Tutorial {
	
	/**
	 * Path for the video file
	 */
	private static final String TUTORIAL_PATH = "resources/tutorial.mp4";
	
	/**
	 * Name for the window that pops up when the tutorial starts playing
	 */
	private static final String TUTORIAL_WINDOW_NAME = "ROLIT Tutorial";
	
	/**
	 * Method that opens the video
	 */
	public void open() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				@SuppressWarnings("unused")
				Video videoFrame = new Video(TUTORIAL_PATH, TUTORIAL_WINDOW_NAME);
			}
		});
	}
	
}