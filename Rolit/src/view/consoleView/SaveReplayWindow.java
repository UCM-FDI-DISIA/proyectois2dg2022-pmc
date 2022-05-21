package view.consoleView;

import java.util.List;
import java.util.Scanner;

import controller.Controller;
import model.SaveLoadManager;
import model.logic.Rival;
import model.replay.GameState;
import model.replay.Replay;
import view.RolitObserver;

/**
 * This class displays the necessary information in order to save a replay, it also
 * gathers users intentions
 * It implements ConsoleWindow
 * @author PMC
 *
 */
public class SaveReplayWindow implements ConsoleWindow, RolitObserver{
	private final static String SAVE_QUESTION = "Do you want to save the replay of this game? (y/n)";
	private final static String SUCCESS_MSG = "Replay saved successfully!";
	private final static String HELP_MSG = "Please enter a valid answer: yes/no";
	private final static String[] yesArray = {"y", "yes"};
	private final static String[] noArray = {"n", "no"};
	private Scanner sc = new Scanner(System.in);
	private Replay replay;
	
	/**
	 * Constructor
	 * @param ctr Controller 
	 */
	SaveReplayWindow(Controller ctr){
		ctr.addObserver(this);
	}
	
	@Override
	public Object get() {
		return null;
	}

	@Override
	public boolean open() {
		String ans;
		this.clear();
		System.out.println(String.format("%s %n", SAVE_QUESTION));
		//int prueba = input.nextInt();
		//input.close();
		ans = sc.nextLine();
		int parse =  parse(ans) ;
		while(parse == 0) {
			System.out.println(String.format("%s %n", HELP_MSG));
			ans = sc.nextLine();
		}
		
		//Answer is yes
		if(parse == 1) {
			SaveLoadManager.saveReplay(replay);
			System.out.println(SUCCESS_MSG);
		}
		
		sc.close();
		return false;
	}
	
	/**
	 * It parses the answer of the user
	 * @param ans Answer of the user
	 * @return A positive number if the ans is yes, negative number  if ans is no or a 0 if ans is not valid
	 */
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
	public void onTurnPlayed(GameState state) {
		
	}
	
	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival, Replay replay, GameState state) {
		this.replay = replay;
		this.open();
	}
	
	@Override
	public void onGameExited(Replay replay) {
		this.replay = replay;
		this.open();
	}
	
	@Override
	public void onRegister(GameState state) {
		
	}
	
	@Override
	public void onError(String err) {
		
	}
	
	@Override
	public void onGameStatusChange(GameState state) {
		
	}

}
