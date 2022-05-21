package view.consoleView;

import java.util.Collections;
import java.util.List;
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
	protected static final String MSG_REY = "QUIEN SERA EL REYYYYYY?????? :)";
	private volatile boolean close;
	private Controller ctr;
	private GameState state;
	private volatile boolean waitingForUpdate = false;

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
			
			//Evaluación perezosa: si no se ha acabado el juego, se espera a que haya cosas en el input
			//En cuanto el juego se acaba, no se ejecuta input.hasNext(). Esto nos interesa porque
			//input.hasNext "monopoliza" el uso de System.in. Por tanto, SaveReplay ahora puede
			//acceder al System.in y llegar a tiempo a recibir la y/n.
			
//			while (!this.close && !input.hasNext()) {
//			}
			
			if (!this.close && !this.waitingForUpdate) {
				String s = input.nextLine();
				try {
					String[] args = s.trim().split(" ");
					Command command = Command.getCommand(args);
					ctr.executeCommand(command);
					this.waitingForUpdate = true;
				} catch (Exception e) {
					System.out.println(e.getMessage());
					System.out.println();
				}
			}
				
				
		}
			

		return true;
	}

	@Override
	public void onTurnPlayed(GameState state) {
		this.state = state;
		this.waitingForUpdate = false;
		System.out.println(state.toString());
		System.out.print(PROMPT); // Se pone el PROMPT aquí porque si no sale antes del estado porque el game
									// tarda en mandar la notificación
	}

	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival, Replay replay, GameState state) {
		this.state = state;
		System.out.println(state.toString());
		
		try {
			Thread.sleep(1000); // Esperamos para que de tiempo a ver el tablero
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Collections.sort(rivals);
		this.close = true;
		this.clear();
		System.out.println(StringUtils.LINE_SEPARATOR + MSG_REY + StringUtils.LINE_SEPARATOR);
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
		System.out.println(states.toString());
		System.out.print(PROMPT);
	}

	@Override
	public void onError(String err) {
		System.out.println(err);
		System.out.println(this.state.toString());
	}

	@Override
	public void onGameStatusChange(GameState status) {
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
