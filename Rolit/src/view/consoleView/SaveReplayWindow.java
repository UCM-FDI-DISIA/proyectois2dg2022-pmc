package view.consoleView;

import java.util.List;

import control.Controller;
import control.SaveLoadManager;
import logic.Rival;
import replay.GameState;
import replay.Replay;
import view.GUIView.RolitObserver;

public class SaveReplayWindow implements ConsoleWindow, RolitObserver{
	private final static String SAVE_QUESTION = "Do you want to save the replay of this game? (y/n)";
	private final static String SUCCESS_MSG = "Replay saved successfully!";
	private final static String HELP_MSG = "Please enter a valid answer: yes/no";
	private final static String[] yesArray = {"y", "yes"};
	private final static String[] noArray = {"n", "no"};
	private Replay replay;
	
	SaveReplayWindow(Controller ctr){
		ctr.addObserver(this);
	}
	
	@Override
	public Object get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean open() {
		// TODO Auto-generated method stub
		String ans;
		this.clear();
		System.out.println(String.format("%s %n", SAVE_QUESTION));
		ans = ConsoleWindow.input.next();
		
		int parse =  parse(ans) ;
		while(parse < 0) {
			System.out.println(String.format("%s %n", HELP_MSG));
			ans = ConsoleWindow.input.next();
		}
		
		//Answer is yes
		if(parse == 1) {
			SaveLoadManager.saveReplay(replay);
			System.out.println(SUCCESS_MSG);
		}
		
		return false;
	}
	
	//Returns a positive number if the ans is yes, 0 if ans is no or a negative number if ans is not valid
	private int parse(String ans) {
		for (int i = 0; i < yesArray.length; i++) {
			if(yesArray[i].equals(ans.toLowerCase()))
				return 1;
		}
		
		for (int i = 0; i < noArray.length; i++) {
			if(noArray[i].equals(ans.toLowerCase()))
				return -1;
		}
		return 0;
	}

	@Override
	public void onFirstPlay(GameState state) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTurnPlayed(GameState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameExited(Replay replay) {
		// TODO Auto-generated method stub
		this.replay = replay;
		this.open();
	}

	@Override
	public void onRegister(GameState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameStatusChange(GameState state) {
		// TODO Auto-generated method stub
		
	}

}
