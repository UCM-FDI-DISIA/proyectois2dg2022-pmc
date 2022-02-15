package Rolit;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import logic.Color;
import logic.Player;

public class Rolit {
	private static final String TITLE = "		ROLIT";
	private static final String VERSION = "Sprint Week 1";
	private static final String SELECT_PLAYERS = "Introduce number of players: ";
	private static final String AVAILABLE_BOARDS = "Available boards: ";
	private static final String SELECT_BOARD = "Choose a board";
	private static final String INVALID_NUMBER = "The given entry is not a number.";
	private static final String INVALID_PLAYERS = "The given entry is not a valid number of players.";
	private static final String INVALID_BOARD = "The given entry is not a valid board.";
	private static final String NAME_PLAYERS = "Name the players: ";
	
	private static int numberOfPlayers;
	private static int indexBoardChosen;

	
	private final static String[] boardTypes = {
			"Classical 8x8"
	};
	
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		mainMenu(scanner);
		System.out.println();
		
		selectNumberOfPlayers(scanner);
		System.out.println();
		
		showBoards();
		selectBoard(scanner);
		System.out.println();
		
		List<Player> players = namePlayers(scanner);
		
		//TODO
		//Game game = new Game(...);
		//Controller controller = new Controller(game, scanner);
		//controller.run();
	}

	private static void mainMenu(Scanner scanner) {
		System.out.println(TITLE);
		System.out.println(VERSION);
	}
	
	private static List<Player> namePlayers(Scanner scanner) {
		System.out.println(NAME_PLAYERS);
		System.out.println();
		List<Player> players = new ArrayList<Player>();
		
		
		for (int i = 0; i < numberOfPlayers; ++i) {
			
			Color currentColor = Color.values()[i];
			
			System.out.print("Player "+(i+1)+"("+currentColor.toString()+"): ");
			String name = scanner.nextLine();
			
			Player currentPlayer = new Player(currentColor, name);
			players.add(currentPlayer);
			
		}
		
		return players;
		
	}
	
	
	private static void showBoards() {
		System.out.println(AVAILABLE_BOARDS);
		
		for (int i = 0; i < boardTypes.length; ++i) {
			System.out.println(i+1 + ". " + boardTypes[i]);
		}
		
		System.out.println();
		
		
	}
	
	private static void selectBoard(Scanner scanner) {
		
		int boardNumber = 0;
		boolean repeat = false;
		do {
			
			System.out.print(SELECT_BOARD);
			if (boardTypes.length == 1)
				System.out.print(" (1): ");
			else
				System.out.print(" (1-"+boardTypes.length+"): ");
			
			try {
				repeat = false;
				
				String entrada = scanner.nextLine();
				boardNumber = Integer.parseInt(entrada);
				
				//TODO ¿Convendría hacer un paquete de excepciones, con por ejemplo InvalidBoardNumber?
				if (boardNumber > boardTypes.length || boardNumber < 1) {
					System.out.println(INVALID_BOARD);
					System.out.println();
					repeat = true;
					
				}
				
			}
			catch (NumberFormatException e) {
				System.out.println(INVALID_NUMBER);
				System.out.println();
				repeat = true;
			}
		} while (repeat);
		
		
		
		
		indexBoardChosen = boardNumber - 1;
		
		//TODO ¿Quizá devolver un tipo tablero?
		//Esta función solo muestra un pequeño prototipo visual de cómo se elegiría un tablero.
		//Aún no se ha implementado nada
	}
	
	private static void selectNumberOfPlayers(Scanner scanner) {
		System.out.println(SELECT_PLAYERS);
		System.out.println("For this current version only four players can be selected.");
		numberOfPlayers = 4;
		
		
		/* For a future implementation where any number of players can be chosen:
		 * 
		int numberOfPlayers = 0;
		
		boolean repeat = false;
		do {
			try {
				repeat = false;
				
				String entrada = scanner.nextLine();
				numberOfPlayers = Integer.parseInt(entrada);
				
				//TODO ¿Convendría hacer un paquete de excepciones, con por ejemplo InvalidNumberOfPlayersException?
				if (numberOfPlayers <= 0) { 
					System.out.println(INVALID_NUMBER);
					System.out.println();
					repeat = true;
				}	
			}
			catch (NumberFormatException e) {
				System.out.println(INVALID_NUMBER);
				System.out.println();
				selectNumberOfPlayers();
				repeat = true;
			}
		} while (repeat);

		*/
		
	}
	
}
