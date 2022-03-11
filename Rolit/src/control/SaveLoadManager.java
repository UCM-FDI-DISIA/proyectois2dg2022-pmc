package control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import logic.Board;
import logic.Color;
import logic.Cube;
import logic.Game;
import logic.Player;
import logic.Saveable;
import logic.Shape;
import logic.Reportable;

public class SaveLoadManager {

	private static final String INDEX_FILENAME = "names.txt";
	private static final String DEFAULT_FILENAME = "SAVED_GAMES.txt";
	private static final String ERROR_LOAD = "Failed to load the file";
	private static final String ERROR_LOAD_DEFAULT = "Failed to load the saved games index.";
	private static final String ERROR_SAVE = "Failed to save the file";
	private static final String ERROR_SAVE_DEFAULT = "Failed to update the saved games index";
	private static final String SUCCESS_SAVE_MSG = "Game saved successfully";
	private static final String SUCCESS_DELETE_MSG = "Game deleted successfully";
	private static final String ERROR_DELETE = "Failed to delete the file";
	private static List<String> names;

	/*
	 * Formato de guardado:
	 * 
	 * N�mero de jugadores (n (int))
	 * Jugador que tiene el turno ("Player" color(char))
	 * Jugador 1 (nombre(String) color(char))
	 * Jugador 2 (nombre(String) color(char))
	 * ...
	 * ...
	 * Jugador n (nombre(String) color(char))
	 * Tama�o del tablero (int)
	 * Cubo(color(char) posx(int) posy(int))
	 * Cubo(color(char) posx(int) posy(int))
	 * Cubo(color(char) posx(int) posy(int))
	 * ...
	 * ...
	 * Cubo(color(char) posx(int) posy(int))
	 * END (centinela)
	 * */
	
	private static List<String> getListOfSavedGames() throws IOException {
		return Files.readAllLines(Paths.get(INDEX_FILENAME), StandardCharsets.UTF_8);
		
	}
	public static boolean showSavedGames() {
		
		try(BufferedReader pointer = new BufferedReader(new FileReader(INDEX_FILENAME))) {
			
			names = getListOfSavedGames();
			
			if (names.size() > 0) {
				for (int i = 0; i < names.size(); ++i)
					System.out.println(i+1 + ". " + names.get(i));
			}
			
		} catch(IOException error_file) {
			System.out.println(ERROR_LOAD_DEFAULT);
			return false;
		}
		
		return true;
		
	}
	
	public static void removeGame(int option) throws Exception {
		
		names = getListOfSavedGames();
		
		option--;
		if (option < 0 || option >= names.size())
			throw new Exception();
		
		String filename = names.get(option);
		
		File fileToDelete = new File(filename);
		if (fileToDelete.delete()) {
			
			System.out.println(SUCCESS_DELETE_MSG);

			names.remove(filename);
			
			updateListOfSavedGames();
		}
		else
			System.out.println(ERROR_DELETE);
	
				
	}
	
	private static void updateListOfSavedGames() {
		try(BufferedWriter pointer = new BufferedWriter(new FileWriter(INDEX_FILENAME))) {
			
			for (int i = 0; i < names.size(); ++i) {
				pointer.write(names.get(i));
				if (i != names.size() - 1)
					pointer.write(String.format("%n"));
			}
		} catch (IOException error_file) {
		System.out.println(ERROR_SAVE_DEFAULT);
		}
		
		
	}
	private static void addToListOfSavedGames(String filename) {
		
		try {
			
			names = getListOfSavedGames();
			
			if (!names.contains(filename))
				names.add(filename);
			
			updateListOfSavedGames();
			
		} catch (IOException error_file) {
			System.out.println(ERROR_SAVE_DEFAULT);
		}
		
	}
	
	public static Game loadGame(String filename) {
		
		try(BufferedReader save_file = new BufferedReader(new FileReader(filename))) {
			
			String jsonString;
			jsonString = save_file.readLine(); //FIXME asumo, por ahora, que el .json contiene una �nica linea
			
			JSONObject gameJSONObject = new JSONObject(jsonString);
			
			int boardSize;
			List<Cube> list_cubes = new ArrayList<Cube>();
			List<Player> list_players = new ArrayList<Player>();
			
			Color turn = Color.valueOfIgnoreCase(gameJSONObject.getString("turn").charAt(0));
			
			JSONArray playersJSONArray = gameJSONObject.getJSONArray("players");
			
			for (int i = 0; i < playersJSONArray.length(); ++i) {
				
				String auxPlayerName = playersJSONArray.getJSONObject(i).getString("name");
				String auxPlayerColor = playersJSONArray.getJSONObject(i).getString("color");
				
				list_players.add(new Player(Color.valueOfIgnoreCase(auxPlayerColor.charAt(0)), auxPlayerName));
			}	
			
			JSONObject boardJSONObject = gameJSONObject.getJSONObject("board");
			String boardShape = boardJSONObject.getString("shape");
			Board board = new Board(boardShape); //FIXME asumo que el constructor de Board se ve as�
			
			JSONArray cubesJSONArray = boardJSONObject.getJSONArray("cubes");
			
			for (int i = 0; i < cubesJSONArray.length(); ++i) {
				
				String auxCubeColor = playersJSONArray.getJSONObject(i).getString("name");
				
				//TODO Comprobar si as� se pueden parsear los arrays de enteros
				List<Integer> posList = new ArrayList<>();
				JSONArray auxCubePos = playersJSONArray.getJSONObject(i).getJSONArray("pos");
				for(Object s: auxCubePos) {
					posList.add((Integer)s);
				}
				
				list_cubes.add(new Cube(posList.get(0), posList.get(1), Player.getPlayer(Color.valueOfIgnoreCase(auxCubeColor.charAt(0)))));
				
				
			}
								
			return new Game(board, list_cubes, list_players, turn, boardSize);
		}
		catch(IOException error_file) {
			System.out.println(ERROR_LOAD);
		}
		return null;
	}
	
	public static int[][] loadShape(String filename) {
		try(BufferedReader shape_file = new BufferedReader(new FileReader(filename))) {
			
			int size = Integer.parseInt(shape_file.readLine());
			int array_int[][] = new int[size][size];
			
			for (int i = 0; i < size + 2; i++) {
				for (int j = 0; j < size + 2; j++) {
					if (shape_file.read() == 'X') array_int[i][j] = 0;
					else if (shape_file.read() == 'D') array_int[i][j] = 1;
					else array_int[i][j] = 2;
					shape_file.read(); //lee el espacio
				}
			}
								
			return array_int;
		}
		catch(IOException error_file) {
			System.out.println(ERROR_LOAD);
		}
		return null;
	}
	
	public static Game loadGame() {
		return loadGame(DEFAULT_FILENAME);
	}
	
	public static Game loadGame(int option) throws Exception {
		option--;
		if (option < 0 || option >= names.size())
			throw new Exception();
		else {
			return loadGame(names.get(option));
		}
		
	}
	
	public static void saveGame(Reportable game, String filename) {
		
		filename += ".json";
		try(BufferedWriter save_file = new BufferedWriter(new FileWriter(filename))) {
			
			save_file.write(game.report().toString());
			
			System.out.println(SUCCESS_SAVE_MSG);
			
			addToListOfSavedGames(filename);
			
		}
		catch(IOException error_file) {
			System.out.println(ERROR_SAVE);
		}
	}
	
	public static void saveGame(Reportable game) {
		saveGame(game, DEFAULT_FILENAME);
	}	

	public static void loadReplay(String filename) {
		// TODO Auto-generated method stub
		
	}
	
	public static void saveReplay() {
		// TODO Auto-generated method stub
		
	}
}
