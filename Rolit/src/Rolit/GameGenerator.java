package Rolit;

import java.util.Scanner;

import logic.Board;
import logic.Color;
import logic.Game;

public class GameGenerator {
	
	private static final String NAME_PLAYERS = "Name the players: ";
	private static final String CHOOSE_COLOR = "Choose a color shortcut: ";
	private static final String NUMBER_PLAYERS_MSG = "Choose the number of players [2 - " + Color.size() +"]";
	private static final String ERROR_PLAYERS_MSG = "Number of players must be a number between 2 and " + Color.size() + " (inclusive)";
	private static final String BOARD_MSG = "Choose your board size [8 - "+ Board.MAX_SIZE + "]";
	private static final String BOARD_ERROR = "Board size must be a number between 8 and 15 (inclusive)";
	
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
		System.out.println(BOARD_MSG);
		int board_size = input.nextInt();
		while (board_size < 8 || board_size > Board.MAX_SIZE) {
			System.out.println(BOARD_ERROR);
			board_size = input.nextInt();
		}
		return new Board(board_size);
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
