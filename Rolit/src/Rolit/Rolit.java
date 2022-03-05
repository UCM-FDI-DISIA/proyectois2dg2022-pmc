package Rolit;

public class Rolit {
	private static final String TITLE = "		ROLIT";
	private static final String VERSION = "Sprint Week 2";
	
	public static void main(String[] args) {
		
		version();
		System.out.println();
		
		Controller controller = new Controller();
		
		controller.run();
		
	}

	private static void version() {
		System.out.println(TITLE);
		System.out.println(VERSION);
	}
	
	

}
