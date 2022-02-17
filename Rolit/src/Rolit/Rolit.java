package Rolit;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import logic.Board;
import logic.Color;
import logic.Game;
import logic.Player;

public class Rolit {
	private static final String TITLE = "		ROLIT";
	private static final String VERSION = "Sprint Week 1";
	private static final String NAME_PLAYERS = "Name the players: ";
	
	private static final int numberOfPlayers = 4;
	private static final int boardSize = 8;

	
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		version();
		System.out.println();
		
		
		List<Player> players = namePlayers(scanner);
		
		
		Game game = new Game(players, new Board(boardSize));
		
		Controller controller = new Controller(game);
		
		controller.run();
		
	}

	private static void version() {
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

}
