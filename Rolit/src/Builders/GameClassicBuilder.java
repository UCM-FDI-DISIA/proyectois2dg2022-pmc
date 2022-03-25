package Builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import logic.Board;
import logic.Color;
import logic.Cube;
import logic.GameClassic;
import logic.Player;
import logic.Shape;

public class GameClassicBuilder extends GameBuilder {
	private static final String TYPE = "GameClassic";
	private static final String NAME_PLAYERS = "Name the players: ";
	private static final String CHOOSE_COLOR = "Choose a color shortcut: ";

	GameClassicBuilder(){
		
	};
	
	@Override
	protected boolean match(String type) {
		return TYPE.equals(type);
	}
	

	@Override
	protected GameClassic GenerateGame(JSONObject o) {
		List<Cube> list_cubes = new ArrayList<Cube>();
		List<Player> list_players = new ArrayList<Player>();

		Color turn = Color.valueOf(o.getString("turn"));

		JSONArray playersJSONArray = o.getJSONArray("players");

		for (int i = 0; i < playersJSONArray.length(); ++i) {

			String auxPlayerName = playersJSONArray.getJSONObject(i).getString("name");
			String auxPlayerColor = playersJSONArray.getJSONObject(i).getString("color");

			list_players.add(new Player(Color.valueOfIgnoreCase(auxPlayerColor.charAt(0)), auxPlayerName));
		}

		JSONObject boardJSONObject = o.getJSONObject("board");
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

		return new GameClassic(board, list_cubes, list_players, turn);
	}	
	
	// TODO faltan las cosas que tiene que preguntar cada factoria en concreto
	@Override
	private void whatINeed(int nPlayers) {
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
