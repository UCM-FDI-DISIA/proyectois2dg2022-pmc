package Rolit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import logic.Game;
import view.GamePrinter;

public class Controller {
	private Scanner input;
	private Game game;
	private GamePrinter printer;
	private SaveLoadManager saveLoadManager;
	private static final String DEFAULT_FILENAME = "SAVED_GAMES.txt";
	
	private final String NUEVA_PARTIDA = "Nueva partida";
	private final String CARGAR_PARTIDA = "Cargar partida";
	private final String OPCION_INCORRECTA = "Opcion incorrecta. Introduzca de nuevo.";
	
	private final String[] arrayOpciones = {
			NUEVA_PARTIDA,
			CARGAR_PARTIDA
	};
	
	public Controller(Game game) {
		this.game = game;
		this.printer = new GamePrinter(game);
		input = new Scanner(System.in);
		this.saveLoadManager = new SaveLoadManager(game, DEFAULT_FILENAME);
	} 
	
	private void printGame() {
		System.out.println(this.printer);
	}
	
	public void run() {

		System.out.println();
		System.out.println("¿Que desea?");
		System.out.println();
		for (int i = 0; i < arrayOpciones.length; ++i)
			System.out.println((i+1) + ". " + arrayOpciones[i]);
		
		int respuesta = 1;
		boolean repeat = true;
		
		
		while (repeat) {
			respuesta = input.nextInt();
			if (respuesta-1 >= 0 && respuesta-1 < arrayOpciones.length)
				repeat = false;
			else
				System.out.println(OPCION_INCORRECTA);
		}
		
		if (CARGAR_PARTIDA.equals(arrayOpciones[respuesta-1])) {
			saveLoadManager.loadGame();
		}
		

		while(!game.isFinished()) {
			String command;
			int posx, posy;
			boolean valido = false;
			
			printGame();
			
			while (!valido) {
				System.out.println(printer.showTurn());
				System.out.println("Introduce un comando:");
				System.out.println("c : Poner un cubo");
				System.out.println("s : Guardar partida");
				command = input.next();
				if ("c".equals(command)) {
					System.out.println("Introduce la posicion x: ");
					posx = input.nextInt();
					System.out.println("Introduce la posicion y: ");
					posy = input.nextInt();
					valido = game.play(posx, posy);
				}					
				else if("s".equals(command))
					saveLoadManager.saveGame();
				else
					System.out.println("Invalid Command");										
			}
			
		}
		
		printGame();
		
		System.out.println(this.printer.showRanking());
	}
}