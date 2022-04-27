package replay;

import logic.Board;
import logic.Color;
import logic.Cube;
import logic.Game;
import logic.GameClassic;
import logic.Player;
import logic.Replayable;
import logic.Reportable;
import logic.Shape;
import utils.StringUtils;
import view.GUIView.Observable;
import view.GUIView.ReplayObserver;
import view.GUIView.RolitObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

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
	private List<GameState> states;
	private List<ReplayObserver> observers;
	private int currentState;
	private boolean print;
	
	public Replay() {
		this.input = new Scanner(System.in);
		this.states = new ArrayList<GameState>();
		this.currentState = 0;
		this.observers = new ArrayList<>();
		this.print = true;
	}
	
	public void addState(String commandName, Replayable game) {
		states.add(new GameState(commandName, game));
	}
	
	public void startReplay() {
		boolean replaying = true;
		while(replaying) {
			if(print)
				// FIXME esto no debería estar aquí
				System.out.println(this);
			System.out.print(PROMPT);
			String in = input.nextLine();
			replaying = execute(in);
		}
	}
	
	public void previousState() {
		if(currentState > 0) {
			currentState--;
			onReplayLeftButton();
			print = true;
		}
		else {
			System.out.println(PREVIOUS_ERROR);
			onChangeStatusBar(PREVIOUS_ERROR);
		}
			
	}
	
	public void nextState() {
		if(currentState + 1 < states.size()) {
			currentState++;
			onReplayRightButton();
			print = true;
		}
		else {
			System.out.println(NEXT_ERROR);
			onChangeStatusBar(NEXT_ERROR);
		}
			
	}
	
	private boolean execute(String in) {
		boolean replaying = true;
		print = false;
		if(in.equals("+")) {
			nextState();
		}
		else if(in.equals("-")){
			previousState();
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

	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		
		for (GameState s : states) {
			ja.put(s.report());
		}
		
		jo.put("states", ja);
		
		return jo;
	}
	
	public String getShape() {
		return states.get(currentState).getShape();
	}
	
	public JSONObject currentStateReport() {
		return states.get(currentState).report();
	}
	
	@Override
	public String toString() {
		StringBuilder bf = new StringBuilder();
		bf.append("State: " + (currentState  + 1) + "/" + states.size());
		bf.append(StringUtils.LINE_SEPARATOR);
		bf.append(states.get(currentState));
		return bf.toString();

	}

	public void addObserver(ReplayObserver o) {
		observers.add(o);
	}

	
	public void removeObserver(ReplayObserver o) {
		observers.remove(o);
	}
	
	public void onReplayLeftButton() {
		for(ReplayObserver o : observers) {
			o.onReplayLeftButton();
		}
	}

	public void onReplayRightButton() {
		for(ReplayObserver o : observers) {
			o.onReplayRightButton();
		}
	}
	
	public void onChangeStatusBar(String msg) {
		for(ReplayObserver o : observers) {
			o.onReplayStatusChange(msg);
		}
	}
}
