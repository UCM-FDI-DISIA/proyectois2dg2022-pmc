package view.consoleView;

import java.util.Collections;
import java.util.List;

import org.json.JSONArray;

import controller.Controller;
import model.commands.Command;
import model.logic.Rival;
import model.replay.GameState;
import model.replay.Replay;
import utils.StringUtils;
import view.RolitObserver;

/**
 * This class displays the necessary information in order play a Rolit game, 
 * it also gathers users intentions 
 * It implements ConsoleWindow
 * @author PMC
 *
 */
public class PlayWindow extends Thread implements ConsoleWindow, RolitObserver {
	protected static final String WINNER_MSG = "Final Ranking:";
	private volatile boolean close;
	private Controller ctr;
	private GameState state;
	private volatile boolean waitingForUpdate = true;
	private boolean firstPrint = true;
	private boolean needsUpdates = false;
	
	/**
	 * Constructor
	 * @param ctr Controller
	 */
	PlayWindow(Controller ctr) {
		this.ctr = ctr;
		ctr.addObserver(this);
	}
	
	@Override
	public boolean open() {
		this.clear();
		while (!this.close) {			
			if (!this.close && !this.waitingForUpdate && this.needsUpdates) {
				String s = input.nextLine();
				try {
					this.waitingForUpdate = true;
					String[] args = s.trim().split(" ");
					Command command = Command.getCommand(args);
					ctr.executeCommand(command);					
				} catch (Exception e) {
					System.out.println(e.getMessage());
					System.out.print(PROMPT);
					this.waitingForUpdate = false;
				}
				
			}				
		}
		return true;
	}
	
	@Override
	public void onTurnPlayed(GameState state) {
		this.waitingForUpdate = false;
		this.state = state;
		System.out.println(state.toString());
		System.out.print(PROMPT); //We put the PROMPT here because if not, it exits before the state, in account of the delay of the game sending the notification
	}
	
	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival, Replay replay, GameState state) {
		this.state = state;
		System.out.println(state.toString());
		
		try {
			Thread.sleep(1000); //We wait so that we have enough time to watch the board
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Collections.sort(rivals);
		this.close = true;
		this.clear();
		System.out.println(StringUtils.LINE_SEPARATOR + WINNER_MSG + StringUtils.LINE_SEPARATOR);
		for (int i = 0; i < rivals.size(); i++)
			System.out.println(
					String.format("%d. %s: %s points", i + 1, rivals.get(i).getName(), rivals.get(i).getScore()));
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onRegister(GameState states) {
		this.state = states;
		if(this.firstPrint) {
			this.firstPrint = false;
			System.out.println(states.toString());
			System.out.print(PROMPT);
		}
		JSONArray players = state.report().getJSONObject("game").getJSONArray("players");
		for(int i = 0; i < players.length(); i++) {
			if(!players.getJSONObject(i).has("strategy")) {
				this.waitingForUpdate = true;
				this.needsUpdates = true;
				break;
			}
		}
	}
	
	@Override
	public void onError(String err) {
		this.waitingForUpdate = false;
		System.out.println(err);
		System.out.println(this.state.toString());
		System.out.print(PROMPT);
	}
	
	@Override
	public void onGameStatusChange(GameState status) {
		this.waitingForUpdate = false;
		this.state = status;
		System.out.println(status.toString());
		System.out.print(PROMPT);		
	}
	
	@Override
	public Object get() {
		return null;
	}
	
	@Override
	public void onGameExited(Replay replay) {
		this.close = true;
		System.out.println("See you!");
	}

}
