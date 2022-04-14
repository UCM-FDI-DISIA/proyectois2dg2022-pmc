package view.consoleView;

import java.util.Scanner;

public abstract interface ConsoleWindow {
	public static final String PROMPT = "Command > ";
	public static Scanner input = new Scanner(System.in);
	// FIXME esto esta puesto como object para que sea generico, pero podria ser JSONObject
	public Object get();
	public boolean open();
}
