package view.consoleView;

import java.util.List;

import org.json.JSONObject;

import controller.Controller;
import model.logic.Rival;
import model.replay.GameState;
import model.replay.Replay;
import view.RolitObserver;

/**
 * Main console window, it is in charge of managing which window is opened at any given moment 
 * It implements ConsoleWindow and RolitObserver
 * @author PMC
 *
 */
public class MainBashWindow implements ConsoleWindow, RolitObserver {
	// Hay que hacer una clase contenedora de todas estas constantes
	private final static String NEW_GAME = "New Game";
	private final static String LOAD_GAME = "Load Game";
	private final static String DELETE_GAME = "Delete Game";
	private final static String REPLAY_GAME = "Replay Game";
	private static final String OPTIONS[] = {NEW_GAME, LOAD_GAME, DELETE_GAME, REPLAY_GAME};	
	protected static final String RANKING = "RANKING DEL ROLIT";
	private static final String INITIAL_MESSAGE = "Choose an option:";
	private static final String INVALID_OPTION = "Invalid option. Try again.";
	protected static final String MSG_POS = "En la posicion numero ";
	protected static final String MSG_GOOD_LUCK = "Suerte para la siguiente :)";
	protected static final String NAME_PLAYERS = "Name the players: ";
	protected static final String CHOOSE_COLOR = "Choose a color shortcut: ";
	
	private Controller ctr;
	
	/**
	 * Constructor
	 * @param ctr Controller
	 */
	public MainBashWindow(Controller ctr) {
		this.ctr = ctr;		
	}
	
	/**
	 * It shows all the available options in Rolit (new game, load game, delete game, replay game)
	 * @return The chosen option
	 */
	private int menu() {
		int option;
		boolean repeat = true;
		System.out.println(INITIAL_MESSAGE);
		for (int i = 0; i < OPTIONS.length; i++)
			System.out.println(String.format("%d. %s", i + 1, OPTIONS[i]));		
		do {
			option = Integer.valueOf(ConsoleWindow.input.nextInt());
			input.nextLine();
			if (option - 1 >= 0 && option - 1 < OPTIONS.length)
				repeat = false;
			else
				System.out.println(INVALID_OPTION);
		} while (repeat);
		return option;
	}

	@Override
	public Object get() {
		return null;
	}

	@Override
	public boolean open() {
		boolean repeatMenu = false;
		do {
			this.clear();
			int option = this.menu();
			ConsoleWindow nextWindow;
			// Se ha seleccionado jugar a un juego nuevo
			if (NEW_GAME.equals(OPTIONS[option - 1])) {
				nextWindow = new NewGameWindow();
				repeatMenu = nextWindow.open();
				ctr.createGame((JSONObject) nextWindow.get());
				this.initGame();
			}				
			// Se ha seleccionado cargar un juego antiguo
			else if (LOAD_GAME.equals(OPTIONS[option - 1])) {
				nextWindow = new LoadGameWindow();
				repeatMenu = nextWindow.open();
				JSONObject game = (JSONObject) nextWindow.get();
				if (game != null) {
					ctr.createGame(game);	
				}
				this.initGame();

			}
			// Se ha seleccionado borrar un juego
			else if (DELETE_GAME.equals(OPTIONS[option - 1])) {
				nextWindow = new DeleteGameWindow();
				repeatMenu = nextWindow.open();
			}
			// Se ha seleccionado ver una repeticion
			else if(REPLAY_GAME.equals(OPTIONS[option - 1])) {
				nextWindow = new LoadReplayWindow();
				repeatMenu = nextWindow.open();
				Replay replay = (Replay) nextWindow.get();
				if (replay != null) {
					this.ctr.startReplay(replay);
				}
			}			
		} while (repeatMenu);
	
		return true;
	}

	
	private void initGame() {
		ctr.startGame();
		ctr.addObserver(this);
		ConsoleWindow gameWindow = new PlayWindow(this.ctr);
		new SaveReplayWindow(ctr);
		gameWindow.open();
	}
	
	@Override
	public void onTurnPlayed(GameState state) {}

	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival, Replay replay) {}

	@Override
	public void onGameExited(Replay replay) {
		this.clear();
	}

	@Override
	public void onRegister(GameState state) {}

	@Override
	public void onError(String err) {}

	@Override
	public void onGameStatusChange(GameState state) {}
}
