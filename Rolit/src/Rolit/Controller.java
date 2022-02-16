package Rolit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import logic.Game;

public class Controller {
	private Scanner input;
	private Game game;
	private GamePrinter printer;
		
	public Controller(Game game) {
		this.game = game;
	} 
	
	private void printGame() {
		System.out.println(this.printer);
	}
	
	private void split(String data) {
		
	}
	
	public void run() {
		while(!game.isFinished()) {
			String command;
			int posx, posy;
			boolean valido = false;
			
			while (!valido) {
				command = input.next();
				if ("c".equals(command)) {
					System.out.println("Introduce la posicion x: ");
					posx = input.nextInt();
					System.out.println("Introduce la posicion y: ");
					posy = input.nextInt();
					valido = game.play(posx, posy);
				}					
				else if("s".equals(command))
					this.saveGame();
				else
					System.out.println("Invalid Command");										
			}
			
		}
		
		System.out.println(this.game.showRanking());
	}
}
