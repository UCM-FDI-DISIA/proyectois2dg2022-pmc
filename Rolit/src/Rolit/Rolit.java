package Rolit;

import java.util.Scanner;

import javax.swing.SwingUtilities;

import client.Client;
import control.Controller;
import view.GUIView.MainWindow;
import view.consoleView.ConsoleWindow;
import view.consoleView.MainBashWindow;

public class Rolit {
	private static final String TITLE = "		ROLIT";
	private static final String VERSION = "Sprint Week 5";
	private static final String DEFAULT_MODE = "console";
	private static final String CHOOSE_MODE = "Choose mode: ";
	private static final String CONSOLE_MODE = "Console Mode";
	private static final String GUI_MODE = "GUI Mode";
	private static final String MODES[] = {CONSOLE_MODE, GUI_MODE};
	private static final String INVALID_OPTION = "Invalid option. Try again.";
	private static Scanner input = new Scanner(System.in);
	
	
	public static void main(String[] args) {		
		version();
		System.out.println();
		Controller controller = new Controller();
		Rolit.run(controller);
	}

	private static void version() {
		System.out.println(TITLE);
		System.out.println(VERSION);
	}
	
	private static int mode() {		
		System.out.println(CHOOSE_MODE);		
		for (int i = 0; i < MODES.length; ++i)
			System.out.println(i + 1 + ". " + MODES[i]);

		int respuesta = 1;
		boolean repeat = true;
		while (repeat) {
			respuesta = input.nextInt();
			if (respuesta - 1 >= 0 && respuesta - 1 < MODES.length)
				repeat = false;
			else
				System.out.println(INVALID_OPTION);
		}
		return respuesta;
	}
	
	public static void run(Controller ctr) {
		int selectedMode = mode();
		
		if (GUI_MODE.equals(MODES[selectedMode-1])) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					MainWindow mainWindow = new MainWindow(ctr);
				}
			});
		}
		else if (CONSOLE_MODE.equals(MODES[selectedMode-1])){			
			ConsoleWindow view = new MainBashWindow(ctr);
			view.open();
		}
	}
	
	

}
