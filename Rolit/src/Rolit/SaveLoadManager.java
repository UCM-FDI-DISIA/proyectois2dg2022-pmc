package Rolit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import logic.Color;
import logic.Cube;
import logic.Game;
import logic.Player;

public class SaveLoadManager {
	private Game game;
	private String saving_file;
	
	public SaveLoadManager(Game game, String file) {
		this.game = game;
		this.saving_file = file;
	}
	
	// Formato para el guardado, todos cubos se guardan como "color posx posy \n" y 
	// al final tenemos "Player  color" para el jugador que tenia el turno.
	
	public void saveGame() {	
		try(BufferedWriter save_file = new BufferedWriter(new FileWriter(this.saving_file))) {
			List<Cube> list_cubes = this.game.saveBoard();
			List<Player> list_players = this.game.getPlayers();
			for (Cube i : list_cubes) {
				save_file.write(i.getColor().toString() + " " + i.getX() + " " + i.getY() + String.format("%n"));
			}
			save_file.write("Player " + this.game.getCurrentColor() + String.format("%n"));
			for (Player i : list_players) {
				save_file.write(i.getName() + " " + i.getColor().toString() + String.format("%n"));
			}
			System.out.println("Partida guardada exitosamente");
		}
		catch(IOException error_file) {
			System.out.println("ERROR AL GUARDAR ARCHIVO");
		}
	}
	
	public void loadGame() {
		try(BufferedReader save_file = new BufferedReader(new FileReader(this.saving_file))) {
			String[] words = save_file.readLine().split(" ");
			List<Cube> list_cubes = new ArrayList<Cube>();
			List<Player> list_players = new ArrayList<Player>();
			
			while(!"Player".equals(words[0])) {
				list_cubes.add(new Cube(Integer.parseInt(words[1]), Integer.parseInt(words[2]), Color.valueOfIgnoreCase(words[0].charAt(0))));
				words = save_file.readLine().split(" ");
			}
			Color turn = Color.valueOfIgnoreCase(words[1].charAt(0));
			for (int i = 0; i < 4; i++) {
				words = save_file.readLine().split(" ");
				list_players.add(new Player(Color.valueOfIgnoreCase(words[1].charAt(0)), words[0]));
			}						
			this.game.loadGame(list_cubes, list_players, turn);
		}
		catch(IOException error_file) {
			System.out.println("ERROR AL CARGAR ARCHIVO");
		}
	}
}
