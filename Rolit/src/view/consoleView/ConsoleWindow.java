	package view.consoleView;

import java.util.Scanner;

/**
 * This interface is implemented by the classes of the console view
 * @author PMC
 *
 */
public abstract interface ConsoleWindow {
	public static final String PROMPT = "Command > ";
	public static Scanner input = new Scanner(System.in);
	
	/**
	 * It is used to access to some information of each window
	 * @return What is needed in each window
	 */
	public Object get();
	
	/**
	 * It notifies the observers that they are being used
	 * @return True if the window opened successfully, false otherwise
	 */
	public boolean open();
	
	/**
	 * It clears the console
	 */
	public default void clear() {
		try {
			Runtime.getRuntime().exec("cls");
		}
		catch(Exception e) {
			
		}
	}
}
