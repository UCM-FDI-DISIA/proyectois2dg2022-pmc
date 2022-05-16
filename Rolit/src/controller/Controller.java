package controller;

import org.json.JSONObject;

import model.SaveLoadManager;
import model.builders.GameBuilder;
import model.commands.Command;
import model.logic.Game;
import model.online.Client;
import model.replay.GameState;
import model.replay.Replay;
import view.RolitObserver;

/**
 * This class is in charge of controlling the logic of the game by creating and starting it
 * as well as creating the commands. It is also the link between the model and the view GUI in MVC
 * @author PMC
 *
 */
public class Controller {
	/**
	 * The Rolit game
	 */
	private volatile Game game;
	
	/**
	 * The client. It is used for network game
	 */
	private volatile Client clientRolit;
	
	/**
	 * True when game is online
	 */
	private boolean onlineMode = false;
	
	/**
	 * Constructor
	 */
	public Controller() {
		
	}
	
	/**
	 * Method that creates the game given a JSONObject
	 * @param o the JSONObject that contains the information to create the game
	 * @return a GameState with the created Game
	 */
	public GameState createGame(JSONObject o) {
		this.game = GameBuilder.createGame(o);
		return new GameState(game);
	}
	
	/**
	 * Method that starts the game
	 */
	public void startGame() {
		this.game.start();
	}
	
	/**
	 * Method that starts the Replay of a game
	 * @param r Replay to be started
	 */
	public void startReplay(Replay r) {
		r.startConsoleReplay();
	}
	
	/**
	 * Adds an observer to the list for MVC
	 * @param o the RolitObserver to be added
	 */
	public void addObserver(RolitObserver o) {
		game.addObserver(o);
	}
	
	/**
	 * Method in charge of executing a Command given
	 * @param c Command to be executed
	 * @throws Exception Exception thrown if an error occurs while executing a command
	 */
	public void executeCommand(Command c) throws Exception {
		if (onlineMode) {
			if (game.getCurrentPlayer().getColor().equals(clientRolit.getPlayer().getColor())) {
				c.execute(game);
				while(!game.executedTurn()) {
					//No se hace nada para esperar a que el modelo haga lo que le haga falta
				}
				clientRolit.updateGameToServer();
			}			
		}
		else {	//FIXME hay que asegurar que el turno sea del jugador actual
			c.execute(game);
		}
	}
	
	/**
	 * Method used in network game to update the game from the server
	 * @param o JSONObject which contains the information of the game
	 */
	public void updateGameFromServer(JSONObject o) {
		this.game.interrupt();
		Game newGame = GameBuilder.createGame(o);
		newGame.updateGameFromServer(game.getObserverList());		
		game = newGame;
		game.start();
	}
	
	/**
	 * Method that loads a game given its filePath
	 * @param filePath String that contains the path of the file in wich a game is saved
	 * @return GameState from the loaded game
	 */
	public GameState loadGame(String filePath) {
		game = GameBuilder.createGame(SaveLoadManager.loadGame(filePath));
		return new GameState(game);
	}
	
	/**
	 * Setter of the online mode
	 * @param onlineMode true if the game is online and false if it is not
	 */
	public void setOnlineMode(boolean onlineMode) {
		this.onlineMode = onlineMode;
	}
	
	/**
	 * Setter of the client for network game
	 * @param clientRolit Client
	 */
	public void setClient(Client clientRolit) {
		this.clientRolit = clientRolit;
	}
}