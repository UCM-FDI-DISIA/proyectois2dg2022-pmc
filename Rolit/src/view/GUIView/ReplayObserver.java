package view.GUIView;

/**
 * This class is a Observer (Observer design pattern) used when functionalities
 * of replays are done and must notify observers
 * @author PMC
 */
public interface ReplayObserver {
	/**
	 * This method is fired when going left in a replay
	 */
	void onReplayLeftButton();
	/**
	 * This method is fired when going right in a replay
	 */
	void onReplayRightButton();
	/**
	 * This method is the status of the replay is changed
	 * @param msg Message of the new status
	 */
	void onReplayStatusChange(String msg);
}
