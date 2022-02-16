package Rolit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import logic.Game;

public class SaveLoadManager {
	private Game game;
	private static final String FILENAME = "SAVED_GAMES";
	
	// Formato para el guardado, todos cubos se guardan como "color posx posy \n" y 
	// al final tenemos "Player: color" para el jugador que tenia el turno.	
	public void saveGame() {	
		try(BufferedWriter save_file = new BufferedWriter(new FileWriter(SaveLoadManager.FILENAME))) {			
			save_file.write(this.game.save());
		}
		catch(IOException error_file) {
			System.out.println("ERROR AL GUARDAR ARCHIVO");
		}
	}
	
	public void loadGame() {
		try(BufferedReader save_file = new BufferedReader(new FileReader(SaveLoadManager.FILENAME))) {
			
			save_file.readLine();
		}
		catch(IOException error_file) {
			System.out.println("ERROR AL CARGAR ARCHIVO");
		}
	}
}
