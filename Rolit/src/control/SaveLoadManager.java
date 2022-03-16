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
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONTokener;
import logic.Board;
import logic.Color;
import logic.Cube;
import logic.Game;
import logic.Player;
import logic.Shape;
import logic.Reportable;
import replay.Replay;

public class SaveLoadManager {

	private static final String INDEX_FILENAME = "names.txt";
	private static final String DEFAULT_FILENAME = "SAVED_GAMES";
	private static final String REPLAY_FILE_NAME = "replay.txt";
	private static final String ERROR_LOAD = "Failed to load the file";
	private static final String ERROR_LOAD_DEFAULT = "Failed to load the saved games index.";
	private static final String ERROR_SAVE = "Failed to save the file";
	private static final String ERROR_SAVE_DEFAULT = "Failed to update the saved games index";
	private static final String SUCCESS_SAVE_MSG = "Game saved successfully";
	private static final String SUCCESS_DELETE_MSG = "Game deleted successfully";
	private static final String ERROR_DELETE = "Failed to delete the file";
	private static List<String> names;

	private static List<String> getListOfSavedGames() throws IOException {
		return Files.readAllLines(Paths.get(INDEX_FILENAME), StandardCharsets.UTF_8);

	}

	public static boolean showSavedGames() {

		try (BufferedReader pointer = new BufferedReader(new FileReader(INDEX_FILENAME))) {

			names = getListOfSavedGames();

			if (names.size() > 0) {
				for (int i = 0; i < names.size(); ++i)
					System.out.println(i + 1 + ". " + names.get(i));
			}

		} catch (IOException error_file) {
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
		} else
			System.out.println(ERROR_DELETE);

	}

	private static void updateListOfSavedGames() {
		try (BufferedWriter pointer = new BufferedWriter(new FileWriter(INDEX_FILENAME))) {

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

		try (BufferedReader save_file = new BufferedReader(new FileReader(filename))) {
			JSONObject gameJSONObject = new JSONObject(new JSONTokener(save_file));
			return loadGame(gameJSONObject);
		} catch (IOException error_file) {
			System.out.println(ERROR_LOAD);
		}
		return null;
	}

	private static Game loadGame(JSONObject gameJSONObject) {

		List<Cube> list_cubes = new ArrayList<Cube>();
		List<Player> list_players = new ArrayList<Player>();

		Color turn = Color.valueOf(gameJSONObject.getString("turn"));

		JSONArray playersJSONArray = gameJSONObject.getJSONArray("players");

		for (int i = 0; i < playersJSONArray.length(); ++i) {

			String auxPlayerName = playersJSONArray.getJSONObject(i).getString("name");
			String auxPlayerColor = playersJSONArray.getJSONObject(i).getString("color");

			list_players.add(new Player(Color.valueOfIgnoreCase(auxPlayerColor.charAt(0)), auxPlayerName));
		}

		JSONObject boardJSONObject = gameJSONObject.getJSONObject("board");
		String boardShape = boardJSONObject.getString("shape");
		Board board = new Board(Shape.valueOfIgnoreCase(boardShape)); // FIXME asumo que el constructor de Board se ve
																		// as�

		JSONArray cubesJSONArray = boardJSONObject.getJSONArray("cubes");

		for (int i = 0; i < cubesJSONArray.length(); ++i) {

			String auxCubeColor = cubesJSONArray.getJSONObject(i).getString("color");

			// TODO Comprobar si as� se pueden parsear los arrays de enteros
			List<Integer> posList = new ArrayList<>();
			JSONArray auxCubePos = cubesJSONArray.getJSONObject(i).getJSONArray("pos");
			for (Object s : auxCubePos) {
				posList.add((Integer) s);
			}

			list_cubes.add(new Cube(posList.get(0), posList.get(1),
					Player.getPlayer(Color.valueOfIgnoreCase(auxCubeColor.charAt(0)))));
		}

		return new Game(board, list_cubes, list_players, turn);

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
		try (BufferedWriter save_file = new BufferedWriter(new FileWriter(filename))) {

			save_file.write(game.report().toString());

			System.out.println(SUCCESS_SAVE_MSG);

			addToListOfSavedGames(filename);

		} catch (IOException error_file) {
			System.out.println(ERROR_SAVE);
		}
	}

	public static void saveGame(Reportable game) {
		saveGame(game, DEFAULT_FILENAME);
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
				Game game = loadGame(state.getJSONObject("game"));
				replay.addState(commandName, game);
			}

		} catch (IOException | JSONException error_file) {
			System.out.println(ERROR_LOAD + ": " + error_file);
		}
		return replay;
	}
	
	public static Replay loadReplay() {
		return SaveLoadManager.loadReplay(REPLAY_FILE_NAME);
	}

	public static void saveReplay(String filename, Reportable replay) {
		try (BufferedWriter save_file = new BufferedWriter(new FileWriter(filename))) {
			save_file.write(replay.report().toString());
			System.out.println(SUCCESS_SAVE_MSG);
		} catch (IOException error_file) {
			System.out.println(ERROR_SAVE);
		}
	}
	
	public static void saveReplay(Reportable replay) {
		SaveLoadManager.saveReplay(REPLAY_FILE_NAME, replay);
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
}
