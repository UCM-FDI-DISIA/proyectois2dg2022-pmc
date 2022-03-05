package Rolit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import logic.Board;
import logic.Color;
import logic.Cube;
import logic.Game;
import logic.Player;
import logic.Saveable;

public class SaveLoadManager {
	private static final String DEFAULT_FILENAME = "SAVED_GAMES.txt";
	private static final String ERROR_LOAD = "Failed to load the file";
	private static final String ERROR_SAVE = "Failed to save the file";
	private static final String SUCCESS_MSG = "Game saved successfully";
	private static final String CENTINEL = "END";

	/*
	 * Formato de guardado:
	 * 
	 * Número de jugadores (n (int))
	 * Jugador que tiene el turno ("Player" color(char))
	 * Jugador 1 (nombre(String) color(char))
	 * Jugador 2 (nombre(String) color(char))
	 * ...
	 * ...
	 * Jugador n (nombre(String) color(char))
	 * Tamaño del tablero (int)
	 * Cubo(color(char) posx(int) posy(int))
	 * Cubo(color(char) posx(int) posy(int))
	 * Cubo(color(char) posx(int) posy(int))
	 * ...
	 * ...
	 * Cubo(color(char) posx(int) posy(int))
	 * END (centinela)
	 * */
	
	public static void saveGame(Saveable game, String filemane, int boardSize) {	
		try(BufferedWriter save_file = new BufferedWriter(new FileWriter(filemane))) {
			List<Cube> list_cubes = game.saveBoard();
			List<Player> list_players = game.getPlayers();
			save_file.write(String.format("%d%n", game.getPlayers().size()));	//Número de jugadores (n (int))
			save_file.write("Player " + game.getCurrentColor() + String.format("%n"));	//Jugador que tiene el turno ("Player" color(char))
			for (Player i : list_players) {	//Jugadores (nombre(String) color(char))
				save_file.write(i.getName() + " " + i.getColor().toString() + String.format("%n"));
			}
			save_file.write(String.format("%d%n", boardSize));	//Tamaño del tablero (int)
			for (Cube i : list_cubes) {	//Cubos(color(char) posx(int) posy(int))
				save_file.write(i.getColor().toString() + " " + i.getX() + " " + i.getY() + String.format("%n"));
			}
			
			save_file.write(CENTINEL);
			System.out.println(SUCCESS_MSG);
		}
		catch(IOException error_file) {
			System.out.println(ERROR_SAVE);
		}
	}
	
	public static void saveGame(Saveable game, int boardSize) {
		saveGame(game, DEFAULT_FILENAME, boardSize);
	}
	
	public static Game loadGame(String filename) {
		try(BufferedReader save_file = new BufferedReader(new FileReader(filename))) {
			
			int boardSize;
			List<Cube> list_cubes = new ArrayList<Cube>();
			List<Player> list_players = new ArrayList<Player>();
			
			int numPlayers = Integer.parseInt(save_file.readLine());
			
			String[] words = save_file.readLine().split(" ");
			Color turn = Color.valueOfIgnoreCase(words[1].charAt(0));
			
			for (int i = 0; i < numPlayers; i++) {
				words = save_file.readLine().split(" ");
				list_players.add(new Player(Color.valueOfIgnoreCase(words[1].charAt(0)), words[0]));
			}	
			
			boardSize = Integer.parseInt(save_file.readLine());
			Board board = new Board(boardSize);
			
			words = save_file.readLine().split(" ");
			while(!CENTINEL.equals(words[0])) {
				list_cubes.add(new Cube(Integer.parseInt(words[1]), Integer.parseInt(words[2]), Player.getPlayer(Color.valueOfIgnoreCase(words[0].charAt(0)))));
				words = save_file.readLine().split(" ");
			}
								
			return new Game(board, list_cubes, list_players, turn, boardSize);
		}
		catch(IOException error_file) {
			System.out.println(ERROR_LOAD);
		}
		return null;
	}
	
	public static Game loadGame() {
		return loadGame(DEFAULT_FILENAME);
	}
}
