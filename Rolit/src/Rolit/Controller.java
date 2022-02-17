package Rolit;

import java.util.Scanner;

import logic.Game;

public class Controller {
	private Scanner input;
	private Game game;
	
	public Controller(Game game, ) {
		
	}
	
	private void parse(String command) {
		
	}
	
	public void run() {
		while(!game.isFinish()) {
			String command;
			this.parse(command);
			input.get()
			game.play(x,y);
			
		}
	}
}
