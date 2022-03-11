package replay;

import logic.Replayable;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Replay {

	private static final String PROMPT = "> ";
	private static final String NEXT_ERROR = "You are in the last state";
	private static final String PREVIOUS_ERROR = "You are in the first state";
	private static final String EXIT_MSG = "See ya!";
	private static final String ERROR_MSG = "Not a valid action. Try \"help\".";
	private static final String HELP_MSG = "This are the available actions:" + StringUtils.LINE_SEPARATOR +
	"+: advance to the next state" + StringUtils.LINE_SEPARATOR +
	"-: go to the previous state" + StringUtils.LINE_SEPARATOR +
	"[e]xit: exit the replay" + StringUtils.LINE_SEPARATOR +
	"[h]elp: show this help" + StringUtils.LINE_SEPARATOR ;


	private Scanner input;
	private List<State> states;
	private int currentState;
	
	public Replay() {
		this.input = new Scanner(System.in);
		this.states = new ArrayList<State>();
		currentState = 0;
	}
	
	public void addState(String playerName, String colorShortcut, String commandName, Replayable board) {
		states.add(new State(playerName, colorShortcut, commandName, board));
	}
	
	public void startReplay() {
		boolean replaying = true;
		while(replaying) {
			System.out.println(states.get(currentState));
			System.out.println(PROMPT);
			String in = input.nextLine();
			replaying = execute(in);
		}
	}
	
	private boolean execute(String in) {
		boolean replaying = true;
		if(in.equals("+")) {
			if(currentState + 1 < states.size())
				currentState++;
			else
				System.out.println(NEXT_ERROR);
		}
		else if(in.equals("-")){
			if(currentState > 0)
				currentState--;
			else
				System.out.println(PREVIOUS_ERROR);
		}
		else if(in.equals("exit") || in.equals("e")) {
			replaying = false;
			System.out.println(EXIT_MSG);
		}
		else if(in.equals("help") || in.equals("h")) {
			System.out.println(HELP_MSG);
		}
		else {
			System.out.println(ERROR_MSG);
		}
		return replaying;
	}

	public JSONArray report() {
		JSONArray ja = new JSONArray();
		
		for (State s : states) {
			ja.put(s.report());
		}
		
		return null;
	}
	
}
