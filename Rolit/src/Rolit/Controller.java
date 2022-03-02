package Rolit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import commands.Command;
import logic.Game;
import view.GamePrinter;

public class Controller {
	private Scanner input;
	private Game game;
	private GamePrinter printer;
	private static final String PROMPT = "Command > ";
	
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
			SaveLoadManager.loadGame(game);
		}
		
		boolean refreshDisplay = true;
		while(!game.isFinished()) {
			if(refreshDisplay) printGame();
			System.out.println(PROMPT);
			String s = input.nextLine();
			String[] parameters = s.toLowerCase().trim().split(" ");
			Command command = null;
			try {
				command = Command.getCommand(parameters);
				refreshDisplay = command.execute(game);
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		if(refreshDisplay) printGame();
		
		if(!game.exited()) System.out.println(this.printer.showRanking());
	}
}