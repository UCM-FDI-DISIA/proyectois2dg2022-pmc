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
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONTokener;

import Builders.GameBuilder;
import logic.Game;
import logic.Reportable;
import logic.Shape;
import replay.GameState;
import replay.Replay;

public class SaveLoadManager {
	private static final String INDEX_GAME_FILENAME = "SAVED_GAMES.txt";
	private static final String INDEX_REPLAY_FILENAME = "SAVED_REPLAYS.txt";
	private static final String DEFAULT_GAME_FILENAME = "DEFAULT_SAVED_GAME";
	private static final String DEFAULT_REPLAY_FILENAME = "DEFAULT_SAVED_REPLAY.json";
	public static final String FULL_DEFAULT_FILENAME = "SAVED_GAMES.txt";
	private static final String ERROR_LOAD = "Failed to load the file";
	private static final String ERROR_LOAD_DEFAULT = "Failed to load the saved games index.";
	private static final String ERROR_SAVE = "Failed to save the file";
	private static final String ERROR_SAVE_DEFAULT = "Failed to update the saved games index";
	private static final String SUCCESS_SAVE_MSG = "Game saved successfully";
	private static final String SUCCESS_DELETE_MSG = "Game deleted successfully";
	private static final String ERROR_DELETE = "Failed to delete the file";
	private static List<String> names;
	
	public static boolean saveGame(Reportable game) {
		return saveGame(game, DEFAULT_GAME_FILENAME);
	}
	
	public static boolean saveGame(Reportable game, String filename) {
		filename += ".json";
		try (BufferedWriter save_file = new BufferedWriter(new FileWriter(filename))) {
			save_file.write(game.report().toString());
			addToListOfSavedFiles(filename, INDEX_GAME_FILENAME);			
			return true;
			
		} catch (IOException error_file) {			
			return false;
		}
	}
	
	public static JSONObject loadGame() {
		return loadGame(DEFAULT_GAME_FILENAME);
	}
	
	public static JSONObject loadGame(String filename) {
		try (BufferedReader save_file = new BufferedReader(new FileReader(filename))) {
			addToListOfSavedFiles(filename, INDEX_GAME_FILENAME);
			JSONObject gameJSONObject = new JSONObject(new JSONTokener(save_file));
			return gameJSONObject;
		} catch (IOException error_file) {
			// FIXME esta excepcion no debería estar aqui
			System.out.println(ERROR_LOAD);
		}
		return null;
	}	

	// FIXME esto es chapuza
	public static JSONObject loadGame(int option) throws Exception {
		option--;
		if (option < 0 || option >= names.size())
			throw new Exception();
		else {
			return loadGame(names.get(option));
		}
	}
	
	public static void saveReplay(Reportable replay) {
		SaveLoadManager.saveReplay(DEFAULT_REPLAY_FILENAME, replay);
	}

	public static void saveReplay(String filename, Reportable replay) {
		try (BufferedWriter save_file = new BufferedWriter(new FileWriter(filename))) {
			save_file.write(replay.report().toString());
			addToListOfSavedFiles(filename, INDEX_REPLAY_FILENAME);			
		} catch (IOException error_file) {
			error_file.printStackTrace();
		}
	}	
	
	public static Replay loadReplay() {
		return SaveLoadManager.loadReplay(DEFAULT_REPLAY_FILENAME);
	}
	
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
	
	public static Replay loadReplay(int option) throws Exception {
		option--;
		if (option < 0 || option >= names.size())
			throw new Exception();
		else {
			return loadReplay(names.get(option));
		}
	}

	
	public static int getShapeSize(String filename) {
		try (BufferedReader shape_file = new BufferedReader(new FileReader(filename))) {
			int size = Integer.parseInt(shape_file.readLine());
			return size;
		} catch (IOException error_file) {
			System.out.println(ERROR_LOAD + error_file);
		}
		return 0;
	}

	public static boolean[][] loadShape(Shape shape) {
		return loadShape(shape.getFilename());
	}
	
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

	
	
	public static void loadAndUpdateListOfSavedFiles(String path) throws IOException {		
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
		saveListOfSavedGamesToFile(path);
	}
	
	public static boolean removeGame(String filename) {
		return removeFile(filename, INDEX_GAME_FILENAME);
	}
	
	public static boolean removeFile(String filename, String path) {
		File fileToDelete = new File(filename);
		boolean exito = fileToDelete.delete();
		if (exito)
		{
			if (names == null) {
				try {
					loadAndUpdateListOfSavedFiles(path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			names.remove(filename);
			saveListOfSavedGamesToFile(path);
		}
		return exito;
		
	}
	public static void removeGame(int option) throws Exception {
		loadAndUpdateListOfSavedFiles(INDEX_GAME_FILENAME);
		option--;
		if (option < 0 || option >= names.size())
			throw new Exception();
		String filename = names.get(option);
		if (removeGame(filename)){
			System.out.println(SUCCESS_DELETE_MSG);
		} else
			System.out.println(ERROR_DELETE);
	}
	
	private static void saveListOfSavedGamesToFile(String path) {
		try (BufferedWriter pointer = new BufferedWriter(new FileWriter(path))) {
			for (int i = 0; i < names.size(); ++i) {
				pointer.write(names.get(i));
				if (i != names.size() - 1)
					pointer.write(String.format("%n"));
			}
		} catch (IOException error_file) {
			System.out.println(ERROR_SAVE_DEFAULT);
		}
	}

	private static void addToListOfSavedFiles(String filename, String path) {
		try {
			loadAndUpdateListOfSavedFiles(path);
			if (!names.contains(filename))
				names.add(filename);
			saveListOfSavedGamesToFile(path);

		} catch (IOException error_file) {
			System.out.println(ERROR_SAVE_DEFAULT);
		}
	}
	
	
	private static List<String> getListOfSavedFiles(String path, String defaultFile) {
		try {
			loadAndUpdateListOfSavedFiles(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		names.add(defaultFile);
		return Collections.unmodifiableList(names);
	}
	
	public static List<String> getListOfSavedGames() {
		return getListOfSavedFiles(INDEX_GAME_FILENAME, DEFAULT_GAME_FILENAME);
	}
	
	public static List<String> getListOfSavedReplays() {
		return getListOfSavedFiles(INDEX_REPLAY_FILENAME, DEFAULT_REPLAY_FILENAME);
	}
	
}
