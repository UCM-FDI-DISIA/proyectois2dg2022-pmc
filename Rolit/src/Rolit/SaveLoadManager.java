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
	
	// Formato para el guardado, todos cubos se guardan como "color posx posy \n" y 
	// al final tenemos "Player  color" para el jugador que tenia el turno.
	
	public static void saveGame(Saveable game, String filemane, int boardSize) {	
		try(BufferedWriter save_file = new BufferedWriter(new FileWriter(filemane))) {
			List<Cube> list_cubes = game.saveBoard();
			List<Player> list_players = game.getPlayers();
			save_file.write(String.format("%d%n", boardSize));
			for (Cube i : list_cubes) {
				save_file.write(i.getColor().toString() + " " + i.getX() + " " + i.getY() + String.format("%n"));
			}
			save_file.write("Player " + game.getCurrentColor() + String.format("%n"));
			for (Player i : list_players) {
				save_file.write(i.getName() + " " + i.getColor().toString() + String.format("%n"));
			}
			System.out.println("Partida guardada exitosamente");
		}
		catch(IOException error_file) {
			System.out.println("ERROR AL GUARDAR ARCHIVO");
		}
	}
	
	public static void saveGame(Saveable game, int boardSize) {
		saveGame(game, DEFAULT_FILENAME, boardSize);
	}
	
	public static Game loadGame(String filename) {
		try(BufferedReader save_file = new BufferedReader(new FileReader(filename))) {
			String[] words = save_file.readLine().split(" ");
			int boardSize;
			List<Cube> list_cubes = new ArrayList<Cube>();
			List<Player> list_players = new ArrayList<Player>();
			
			boardSize = save_file.read();
			Board board = new Board(boardSize);
			
			while(!"Player".equals(words[0])) {
				list_cubes.add(new Cube(Integer.parseInt(words[1]), Integer.parseInt(words[2]), Player.getPlayer(Color.valueOfIgnoreCase(words[0].charAt(0)))));
				words = save_file.readLine().split(" ");
			}
			Color turn = Color.valueOfIgnoreCase(words[1].charAt(0));
			for (int i = 0; i < 4; i++) {
				words = save_file.readLine().split(" ");
				list_players.add(new Player(Color.valueOfIgnoreCase(words[1].charAt(0)), words[0]));
			}						
			return new Game(board, list_cubes, list_players, turn, boardSize);
		}
		catch(IOException error_file) {
			System.out.println("ERROR AL CARGAR ARCHIVO");
		}
		return null;
	}
	
	public static Game loadGame() {
		return loadGame(DEFAULT_FILENAME);
	}
}
