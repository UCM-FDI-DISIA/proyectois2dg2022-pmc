package model;

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
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONTokener;

import model.builders.GameBuilder;
import model.logic.Game;
import model.logic.Reportable;
import model.logic.Shape;
import model.replay.GameState;
import model.replay.Replay;

/**
 * This class is in charge of saving and loading information from files
 * @author PMC
 *
 */
public class SaveLoadManager {
	private static final String INDEX_GAME_FILENAME = "SAVED_GAMES.txt";
	private static final String INDEX_REPLAY_FILENAME = "SAVED_REPLAYS.txt";
	private static final String DEFAULT_GAME_FILENAME = "DEFAULT_SAVED_GAME.json";
	private static final String DEFAULT_REPLAY_FILENAME = "DEFAULT_SAVED_REPLAY.json";
	public static final String FULL_DEFAULT_FILENAME = "SAVED_GAMES.txt";
	private static final String ERROR_LOAD = "Failed to load the file";
	private static final String ERROR_SAVE_DEFAULT = "Failed to update the saved games index";
	private static final String SUCCESS_DELETE_MSG = "Game deleted successfully";
	private static final String ERROR_DELETE = "Failed to delete the file";
	private static List<String> names;
	private static List<String> namesWithDefaultName;

	/**
	 * This method saves a game in the default file
	 * @param game Game report
	 * @return true if the game was saved successfully and false otherwise
	 */
	public static boolean saveGame(Reportable game) {
		return saveGame(game, DEFAULT_GAME_FILENAME);
	}
	
	/**
	 * This method saves the game in the path filename
	 * @param game Game report
	 * @param filename File's path
	 * @return true if the game was saved successfully and false otherwise
	 */
	public static boolean saveGame(Reportable game, String filename) {
		if(!filename.endsWith(".json")) filename += ".json";
		try (BufferedWriter save_file = new BufferedWriter(new FileWriter(filename))) {
			save_file.write(game.report().toString());
			if(!filename.equals(DEFAULT_GAME_FILENAME)) addToListOfSavedFiles(filename, DEFAULT_GAME_FILENAME, INDEX_GAME_FILENAME);			
			return true;
			
		} catch (IOException error_file) {			
			return false;
		}
	}
	
	/**
	 * This method loads the game from the path filename
	 * @param filename File's path
	 * @return true if the game was loaded successfully and false otherwise
	 */
	public static JSONObject loadGame(String filename) {
		try (BufferedReader save_file = new BufferedReader(new FileReader(filename))) {
			addToListOfSavedFiles(filename, DEFAULT_GAME_FILENAME, INDEX_GAME_FILENAME);
			JSONObject gameJSONObject = new JSONObject(new JSONTokener(save_file));
			return gameJSONObject;
		} catch (IOException error_file) {
			// FIXME esta excepcion no debería estar aqui
			System.out.println(ERROR_LOAD);
		}
		return null;
	}	

	/**
	 * It loads the game at index option from the list of saved games
	 * @param option Index of the game to load
	 * @return true if the game was loaded successfully and false otherwise
	 */
	public static JSONObject loadGame(int option) throws Exception {
		if(option == 1)
			return loadGame(DEFAULT_GAME_FILENAME);

		option -= 2;
		if (option < 0 || option >= names.size())
			throw new Exception();
		else {
			return loadGame(names.get(option));
		}
	}
	
	/**
	 * This method saves a replay in the default file
	 * @param replay Replay report
	 * @return true if the replay was saved successfully and false otherwise
	 */
	public static void saveReplay(Reportable replay) {
		SaveLoadManager.saveReplay(DEFAULT_REPLAY_FILENAME, replay);
	}

	/**
	 * This method saves a replay in the path filename
	 * @param replay Replay report
	 * @param filename File's path
	 * @return true if the replay was saved successfully and false otherwise
	 */
	public static void saveReplay(String filename, Reportable replay) {
		if(!filename.endsWith(".json")) filename += ".json";
		try (BufferedWriter save_file = new BufferedWriter(new FileWriter(filename))) {
			save_file.write(replay.report().toString());
			if(!filename.equals(DEFAULT_REPLAY_FILENAME)) addToListOfSavedFiles(filename, DEFAULT_REPLAY_FILENAME, INDEX_REPLAY_FILENAME);			
		} catch (IOException error_file) {
			error_file.printStackTrace();
		}
	}	
	
	/**
	 * This function loads a replay from the path filename
	 * @param filename File's path
	 * @return true if the replay was loaded successfully and false otherwise
	 */
	public static Replay loadReplay(String filename) {
		Replay replay = null;
		try (BufferedReader save_file = new BufferedReader(new FileReader(filename))) {
			replay = new Replay();
			// Guardamos el .txt en un JSONObject
			JSONObject jo = new JSONObject(new JSONTokener(save_file));
			// Cogemos el array de estados
			JSONArray states = jo.getJSONArray("states");
			for (int i = 0; i < states.length(); i++) {
				JSONObject state = states.getJSONObject(i);
				String commandName = state.getString("command");
				Game game = GameBuilder.createGame(state.getJSONObject("game"));
				replay.addState(new GameState(commandName, game));
			}
		} catch (IOException | JSONException error_file) {
			System.out.println(ERROR_LOAD + ": " + error_file);
		}
		return replay;
	}
	
	/**
	 * It loads the game at index option from the list of saved games
	 * @param option Index of the game to load
	 * @return true if the game was loaded successfully and false otherwise
	 */
	public static Replay loadReplay(int option) throws Exception {
		if(option == 1)
			return loadReplay(DEFAULT_REPLAY_FILENAME);

		option -= 2;
		if (option < 0 || option >= names.size())
			throw new Exception();
		else {
			return loadReplay(names.get(option));
		}
	}

	/**
	 * This method returns the size of the shape saved at the path filename
	 * @param filename File's path
	 * @return The size of the shape saved at the path filename
	 */
	public static int getShapeSize(String filename) {
		try (BufferedReader shape_file = new BufferedReader(new FileReader(filename))) {
			int size = Integer.parseInt(shape_file.readLine());
			return size;
		} catch (IOException error_file) {
			System.out.println(ERROR_LOAD + error_file);
		}
		return 0;
	}

	/**
	 * It loads a shape
	 * @param shape Shape to load
	 * @return Shape matrix
	 */
	public static boolean[][] loadShape(Shape shape) {
		return loadShape(shape.getFilename());
	}
	
	/**
	 * This method loads the shape from the path filename
	 * @param filename File's path
	 * @return Shape matrix
	 */
	public static boolean[][] loadShape(String filename) {
		try (BufferedReader shape_file = new BufferedReader(new FileReader(filename))) {
			int size = Integer.parseInt(shape_file.readLine());
			boolean array_pos[][] = new boolean[size][size];			
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					int temp = shape_file.read();
					if (temp == 'X') array_pos[i][j] = true;
					else array_pos[i][j] = false;
					shape_file.read(); // lee el espacio
				}
				shape_file.readLine(); // lee el intro
			}
			return array_pos;
		} catch (IOException error_file) {
			System.out.println(ERROR_LOAD + error_file);
		}
		return null;
	}

	/**
	 * This function loads and updates the list of saved files
	 * @param path List of saved games path
	 * @param defaultFile Default file - file to ignore when updating the list
	 * @throws IOException
	 */
	private static void loadAndUpdateListOfSavedFiles(String path, String defaultFile) throws IOException {		
		//Comprobamos si la lista está desactualizada, y los archivos
		//no encontrados se borran de la lista.
		names = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
		
		for (int i = 0; i < names.size(); ++i) {
			File fileAux = new File(names.get(i));
			if (!fileAux.canRead()) {
				names.remove(i);
				i--;
			}
				
		}		
		saveListOfSavedGamesToFile(path, defaultFile);
	}
	
	/**
	 * It removes the game from the path filename
	 * @param filename File's path
	 * @return true if the game was deleted successfully and false otherwise
	 */
	public static boolean removeGame(String filename) {
		return removeFile(filename, INDEX_GAME_FILENAME, DEFAULT_GAME_FILENAME);
	}
	
	/**
	 * It removes the file from the computer and the list
	 * @param filename File's path
	 * @param path List of saved games
	 * @param defaultFile  Default file - file to ignore when updating the list
	 * @return true if the game was loaded successfully and false otherwise
	 */
	public static boolean removeFile(String filename, String path, String defaultFile) {
		File fileToDelete = new File(filename);
		boolean exito = fileToDelete.delete();
		if (exito)
		{
			if (names == null) {
				try {
					loadAndUpdateListOfSavedFiles(path, defaultFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			names.remove(filename);
			saveListOfSavedGamesToFile(path, defaultFile);
		}
		return exito;
		
	}
	
	/**
	 * It removes the game at index option from the list of saved games
	 * @param option Index of the game to load
	 * @return true if the game was deleted successfully and false otherwise
	 * @throws IOException Could not delete the game, maybe option was out of bounds
	 */
	public static void removeGame(int option) throws IOException {
		loadAndUpdateListOfSavedFiles(INDEX_GAME_FILENAME, DEFAULT_GAME_FILENAME);
		option--;
		if (option < 0 || option >= names.size())
			throw new IllegalArgumentException();
		String filename = names.get(option);
		if (removeGame(filename)){
			System.out.println(SUCCESS_DELETE_MSG);
		} else
			System.out.println(ERROR_DELETE);
	}
	
	/**
	 * This method saves the list of saved files
	 * @param path List's path
	 * @param defaultFile Default file - file to ignore when updating the list
	 */
	private static void saveListOfSavedGamesToFile(String path, String defaultFile) {
		try (BufferedWriter pointer = new BufferedWriter(new FileWriter(path))) {
			for (int i = 0; i < names.size(); ++i) {
				if(!names.get(i).equals(defaultFile)) {
					pointer.write(names.get(i));
					if (i != names.size() - 1)
						pointer.write(String.format("%n"));	
				}
			}
		} catch (IOException error_file) {
			System.out.println(ERROR_SAVE_DEFAULT);
		}
	}

	/**
	 * This function adds a path to the list of saved files
	 * @param filename File's path
	 * @param defaultFile Default file - file to ignore when updating the list
	 * @param path List's path
	 */
	private static void addToListOfSavedFiles(String filename, String defaultFile, String path) {
		try {
			loadAndUpdateListOfSavedFiles(path, defaultFile);
			if (!names.contains(filename))
				names.add(filename);
			saveListOfSavedGamesToFile(path, defaultFile);

		} catch (IOException error_file) {
			System.out.println(ERROR_SAVE_DEFAULT);
		}
	}
	
	/**
	 * This function returns the list of saved files, including the default file
	 * @param path List's path
	 * @param defaultFile defaultFile Default file - file to ignore when updating the list
	 * @return The list of saved files
	 */
	private static List<String> getListOfSavedFiles(String path, String defaultFile) {
		try {
			loadAndUpdateListOfSavedFiles(path, defaultFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		namesWithDefaultName = new ArrayList<String>();
		namesWithDefaultName.add(defaultFile);
		namesWithDefaultName.addAll(names);
		return Collections.unmodifiableList(namesWithDefaultName);
	}
	
	/**
	 *  This function returns the list of saved games, including the default game
	 * @return The list of saved games
	 */
	public static List<String> getListOfSavedGames() {
		return getListOfSavedFiles(INDEX_GAME_FILENAME, DEFAULT_GAME_FILENAME);
	}
	
	/**
	 *  This function returns the list of saved replays, including the default replay
	 * @return The list of saved replays
	 */
	public static List<String> getListOfSavedReplays() {
		return getListOfSavedFiles(INDEX_REPLAY_FILENAME, DEFAULT_REPLAY_FILENAME);
	}
	
}
