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
	

	private static final int boardSize = 8;	
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		version();
		System.out.println();
		
		
		List<String> players = namePlayers(scanner);
		
		
		Game game = new Game(players, new Board(boardSize));
		
		Controller controller = new Controller(game);
		
		controller.run();
		
	}

	private static void version() {
		System.out.println(TITLE);
		System.out.println(VERSION);
	}
	
	

}
