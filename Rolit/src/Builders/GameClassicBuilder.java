package Builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import logic.Board;
import logic.Color;
import logic.Cube;
import logic.Game;
import logic.GameClassic;
import logic.Player;
import logic.Shape;

public class GameClassicBuilder extends GameBuilder {
	public static final String TYPE = "GameClassic";
	private static final String NAME_PLAYERS = "Name the players: ";
	private static final String CHOOSE_COLOR = "Choose a color shortcut: ";

	GameClassicBuilder(){
		
	};
	
	String getName(){
		return TYPE;
	}
	
	@Override
	protected boolean match(String type) {
		return TYPE.equals(type);
	}
	
	@Override
	protected Game GenerateGame(JSONObject o) {
		Color turn = Color.valueOfIgnoreCase(o.getString("turn").charAt(0));
		
		List<Player> list_players = new ArrayList<Player>();
		JSONArray playersJSONArray = o.getJSONArray("players");
		for (int i = 0; i < playersJSONArray.length(); ++i)
			list_players.add(new Player(Color.valueOfIgnoreCase(playersJSONArray.getJSONObject(i).getString("color").charAt(0)),
					playersJSONArray.getJSONObject(i).getString("name")));

		JSONObject boardJSONObject = o.getJSONObject("board");
		Board board = new Board(Shape.valueOfIgnoreCase(boardJSONObject.getString("shape"))); // FIXME asumo que el constructor de Board se ve
		
		List<Cube> list_cubes = new ArrayList<Cube>();
		JSONArray cubesJSONArray = boardJSONObject.getJSONArray("cubes");
		for (int i = 0; i < cubesJSONArray.length(); ++i)
			list_cubes.add(new Cube(cubesJSONArray.getJSONObject(i).getJSONArray("pos").getInt(0), cubesJSONArray.getJSONObject(i).getJSONArray("pos").getInt(1),
					Player.getPlayer(Color.valueOfIgnoreCase(cubesJSONArray.getJSONObject(i).getString("color").charAt(0)))));
		return new GameClassic(board, list_cubes, list_players, turn);
	}
	
	@Override
	protected void whatINeed(int nPlayers, JSONObject o) {
		JSONArray jPlayers = new JSONArray();	
		
		System.out.println(NAME_PLAYERS);
		System.out.println();
		
		for (int i = 0; i < nPlayers; ++i) {
			boolean added = false;
			System.out.print("Player " + (i + 1) + ": ");
			String name = input.nextLine();
			 while (!added) {
				 System.out.println(this.availableColors(jPlayers));
				 System.out.print(CHOOSE_COLOR);
				 char c = input.next().charAt(0); // HAGO QUE SEA UN STRING POR SI EL USUARIO INTRODUCE MAS DE UN CARACTER
				 input.nextLine();
				 Color color = Color.valueOfIgnoreCase(c);
				 try {
					 // FIXME no se si al dar excepcion antes de hacer el put realmente no se hace put de nada
					 jPlayers.put(this.validatePlayer(jPlayers, name, color));
					 added = true;
				 }				 
				 catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
					added = false;
				}
			}
		}
		o.put("players", jPlayers);
	}
	
}
