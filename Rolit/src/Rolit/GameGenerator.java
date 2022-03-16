package Rolit;

import java.util.Scanner;

import logic.Board;
import logic.Color;
import logic.Shape;
import logic.Game;

public class GameGenerator {
	private static final String PROMPT = "> ";
	private static final String NAME_PLAYERS = "Name the players: ";
	private static final String CHOOSE_COLOR = "Choose a color shortcut: ";
	private static final String NUMBER_PLAYERS_MSG = "Choose the number of players [2 - " + Color.size() +"]";
	private static final String ERROR_PLAYERS_MSG = "Number of players must be a number between 2 and " + Color.size() + " (inclusive)";
	private static final String BOARD_MSG = "Choose a board shape. ";
	private static final String BOARD_ERROR = "ERROR: Not found such shape. ";
	
	private static Scanner input = new Scanner(System.in);
	
	public static Game createGame() {
		Game game = new Game(chooseBoard());
		chooseColor(numPlayers(), game);
		return game;
	}
	
	private static int numPlayers() {
		System.out.println(NUMBER_PLAYERS_MSG);
		int nPlayers = input.nextInt();
		while (nPlayers < 2 || nPlayers > Color.size()) {
			System.out.println(ERROR_PLAYERS_MSG);
			nPlayers = input.nextInt();
		}
		return nPlayers;
	}
	
	private static Board chooseBoard() {
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

	private static void chooseColor(int numPlayers, Game game) {
		System.out.println(NAME_PLAYERS);
		System.out.println();
		for (int i = 0; i < numPlayers; ++i) {
			boolean added = false;

			System.out.print("Player " + (i + 1) + ": ");
			input.nextLine();
			String name = input.nextLine();
			 while (!added) {
				try {
					System.out.println(game.availableColors());
					System.out.print(CHOOSE_COLOR);
					char c = input.next().charAt(0); // HAGO QUE SEA UN STRING POR SI EL USUARIO INTRODUCE MAS DE UN CARACTER
					game.tryToAddPlayer(name, c);
					added = true;
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
					added = false;
				}
			}
		}
	}

	
	
}
