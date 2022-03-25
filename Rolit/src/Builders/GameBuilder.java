package Builders;

import java.util.Scanner;

import org.json.JSONObject;

import logic.Board;
import logic.Color;
import logic.GameClassic;
import logic.Shape;

public abstract class GameBuilder {	
	private static final String PROMPT = "> ";
	private static final String ERROR_MODES_MSG = "Game mode must be on of these:";
	private static final String NUMBER_PLAYERS_MSG = "Choose the number of players [2 - " + Color.size() +"]";
	private static final String ERROR_PLAYERS_MSG = "Number of players must be a number between 2 and " + Color.size() + " (inclusive)";
	private static final String BOARD_MSG = "Choose a board shape. ";
	private static final String BOARD_ERROR = "ERROR: Not found such shape. ";
	protected final static Scanner input = new Scanner(System.in);
	
	private static GameBuilder[] builders = {
		new GameClassicBuilder(),
		new GameTeamsBuilder()
	};
	
	private static JSONObject ask() {
		JSONObject o = new JSONObject();
		// elegimos el modo de juego concreto
		// TODO falta el mensaje de los juego disponibles
		String type = input.nextLine();
		while(GameBuilder.parse(type) == null) {
			System.out.println(GameBuilder.ERROR_MODES_MSG);
			type = input.nextLine();
		}
		o.put("type", type);
		// Elegimos el numero de jugadores
		System.out.println(NUMBER_PLAYERS_MSG);
		int nPlayers = input.nextInt();
		while (nPlayers < 2 || nPlayers > Color.size()) {
			System.out.println(ERROR_PLAYERS_MSG);
			nPlayers = input.nextInt();
		}
		// Elegimos el tablero concreto
		System.out.print(BOARD_MSG); //TODO cuando guardemos una partida hay que guardar su forma, ademas de su tamaño
		String board_shape;
		do {
			System.out.print(Shape.availableShapes());
			System.out.print(PROMPT);
			board_shape = input.nextLine();
			if (Shape.valueOfIgnoreCase(board_shape) == null) {
				System.out.print(BOARD_ERROR);
			}
		} while (Shape.valueOfIgnoreCase(board_shape) == null);		
		o.put("Board", new Board(Shape.valueOfIgnoreCase(board_shape)).report());
		// TODO faltan preguntar a los método específicos de cada factoría
		parse(type).whatINeed(nPlayers, o);
		
		return o;
	}
	
	private static GameBuilder parse(String type) {
		for (GameBuilder g : builders) {
			if (g.match(type))
				return g;
		}
		return null;
	}
	
	// FIXME FALTA MANEJAR QUE SEA NULO EL TIPO
	public static GameClassic createGame(JSONObject o) {
		String type = o.getString("type");
		return GameBuilder.parse(type).GenerateGame(o);
	}
	
	public static GameClassic createGame() {
		JSONObject o = GameBuilder.ask();
		return createGame(o); 
	}
	
	private Board chooseBoard() {
		System.out.print(BOARD_MSG); //TODO cuando guardemos una partida hay que guardar su forma, ademas de su tamaño
		String board_shape;
		do {
		System.out.print(Shape.availableShapes());
		System.out.print(PROMPT);
		board_shape = input.nextLine();
		if (Shape.valueOfIgnoreCase(board_shape) == null) {
			System.out.print(BOARD_ERROR);
		}
		} while (Shape.valueOfIgnoreCase(board_shape) == null);
		
		return new Board(Shape.valueOfIgnoreCase(board_shape));
	}
	
	protected abstract void whatINeed(int nPlayers, JSONObject o);
	protected abstract boolean match(String type);
	protected abstract GameClassic GenerateGame(JSONObject o);	
	
}
