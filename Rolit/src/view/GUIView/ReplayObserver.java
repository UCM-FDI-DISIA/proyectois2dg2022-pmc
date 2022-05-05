package view.GUIView;

public interface ReplayObserver {
	void onReplayLeftButton();
	void onReplayRightButton();
	void onReplayStatusChange(String msg);
}
