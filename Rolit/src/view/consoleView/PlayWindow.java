package view.consoleView;

import java.util.List;

import commands.Command;
import control.Controller;
import logic.Rival;
import replay.GameState;
import utils.StringUtils;
import view.GUIView.RolitObserver;

public class PlayWindow implements ConsoleWindow, RolitObserver {
	protected static final String MSG_REY = "QUIEN SERA EL REYYYYYY?????? :)";
	private boolean close;
	private Controller ctr;
	
	PlayWindow(Controller ctr){
		this.ctr = ctr;
		ctr.addObserver(this);
	}
	
	@Override
	public boolean open() {
		this.clear();
		while (!this.close) {
			System.out.print(PROMPT);
			String s = input.nextLine();
			try {
				String[] args = s.trim().split(" ");
				Command command = Command.getCommand(args);
				ctr.executeCommand(command);
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
				System.out.println();
			}			
		}
		return true;
	}
	
	@Override
	public void onTurnPlayed(GameState state) {
		System.out.println(state.toString());				
	}
	
	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival) {
		this.close = true;
		this.clear();
		System.out.println(StringUtils.LINE_SEPARATOR + MSG_REY + StringUtils.LINE_SEPARATOR);
		for (int i = 0; i < rivals.size(); i++)
			System.out.println(String.format("%d. %s: %s points", i+1, rivals.get(i).getName(), rivals.get(i).getScore()));
	}
	
	@Override
	public void onRegister(GameState states) {
		System.out.println(states.toString());		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameStatusChange(GameState status) {
		System.out.println(status.toString());		
	}

	@Override
	public Object get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onFirstPlay(GameState state) {
		// TODO Auto-generated method stub
		
	}

}
