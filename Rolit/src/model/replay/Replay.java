package model.replay;

import utils.StringUtils;
import view.GUIView.ReplayObserver;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import model.logic.Reportable;

/**
 * This class is in charge of Replaying a game
 * @author PMC
 *
 */
public class Replay implements Reportable {

	private static final String PROMPT = "> ";
	private static final String NEXT_ERROR = "Already in the last state";
	private static final String PREVIOUS_ERROR = "Already in the first state";
	private static final String EXIT_MSG = "See ya!";
	private static final String ERROR_MSG = "Not a valid action. Try \"help\".";
	private static final String HELP_MSG = "This are the available actions:" + StringUtils.LINE_SEPARATOR +
	"+: advance to the next state" + StringUtils.LINE_SEPARATOR +
	"-: go to the previous state" + StringUtils.LINE_SEPARATOR +
	"[e]xit: exit the replay" + StringUtils.LINE_SEPARATOR +
	"[h]elp: show this help" + StringUtils.LINE_SEPARATOR ;


	private Scanner input;
	private volatile List<GameState> states;
	private List<ReplayObserver> observers;
	private int currentState;
	private boolean print;
	
	/**
	 * Constructor
	 */
	public Replay() {
		this.input = new Scanner(System.in);
		this.states = new ArrayList<GameState>();
		this.currentState = 0;
		this.observers = new ArrayList<>();
		this.print = true;
	}
	
	/**
	 * This method adds an state to the list of states
	 * @param state GameState to add
	 */
	public void addState(GameState state) {
		states.add(state);
	}
	
	/**
	 *This method starts a console replay
	 */
	public void startConsoleReplay() {
		boolean replaying = true;
		while(replaying) {
			if(print)
				System.out.println(this);
			System.out.print(PROMPT);
			String in = input.nextLine();
			replaying = execute(in);
		}
	}
	
	/**
	 * This method goes back one state. If the state is already the initial it does nothing
	 * @return true if the current state changed and false otherwise
	 */
	public boolean previousState() {
		if(currentState > 0) {
			currentState--;
			onReplayLeftButton();
			print = true;
			onChangeStatusBar(currentState + 1 + " / " + states.size());
			return true;
		}
		else {
			return false;
		}
			
	}
	
	/**
	 * This method goes forward one state. If the state is already the initial it does nothing
	 * @return true if the current state changed and false otherwise
	 */
	public boolean nextState() {
		if(currentState + 1 < states.size()) {
			currentState++;
			onReplayRightButton();
			print = true;
			onChangeStatusBar(currentState + 1 + " / " + states.size());
			return true;
		}
		else {
			return false;

		}
			
	}
	
	/**
	 * In console mode, this method executes an action
	 * @param in Action to execute
	 * @return true if the replay is not finished and false otherwise
	 */
	private boolean execute(String in) {
		boolean replaying = true;
		print = false;
		if(in.equals("+")) {
			if(!nextState()) {System.out.println(NEXT_ERROR);}
		}
		else if(in.equals("-")){
			if(!previousState()) {System.out.println(PREVIOUS_ERROR);}  			
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
	
	/**
	 * It returns the shape name of the current state
	 * @return The shape name of the current state
	 */
	public String getShape() {
		return states.get(currentState).getShape();
	}

	/**
	 * It returns the number of states
	 * @return The number of states
	 */
	public int getNumStates() {
		return states.size();
	}

	/**
	 * It returns the current state
	 * @return The current states
	 */
	public GameState getCurrentState() {
		return states.get(currentState);
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		
		for (GameState s : states) {
			ja.put(s.report());
		}
		
		jo.put("states", ja);
		
		return jo;
	}
	
	@Override
	public String toString() {
		StringBuilder bf = new StringBuilder();
		bf.append("State: " + (currentState  + 1) + "/" + states.size());
		bf.append(StringUtils.LINE_SEPARATOR);
		bf.append(states.get(currentState));
		return bf.toString();

	}

	/**
	 * It adds a ReplayObserver to the list of observers
	 * @param o Observer to add
	 */
	public void addObserver(ReplayObserver o) {
		observers.add(o);
	}
	
	/**
	 * It notifies onReplayLeftButton() to every observer
	 */
	public void onReplayLeftButton() {
		for(ReplayObserver o : observers) {
			o.onReplayLeftButton();
		}
	}

	/**
	 * It notifies onReplayRightButton() to every observer
	 */
	public void onReplayRightButton() {
		for(ReplayObserver o : observers) {
			o.onReplayRightButton();
		}
	}
	
	/**
	 * It notifies onReplayStatusChange() to every observer
	 * @param msg Message to show
	 */
	public void onChangeStatusBar(String msg) {
		for(ReplayObserver o : observers) {
			o.onReplayStatusChange(msg);
		}
	}

}
