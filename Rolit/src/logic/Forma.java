package logic;

import utils.StringUtils;

public enum Forma {
	SS("square_small.txt", "cuadrado 9x9"), SM("square_medium.txt", "cuadrado 13x13"), 
	SL("square_large.txt", "cuadrado 17x17"), CS("circle_small.txt", "circulo 9x9"), 
	CM("circle_medium.txt", "circulo 13x13"), CL("circle_large.txt", "circulo 17x17"),
	DS("diamond_small.txt", "rombo 9x9"), DM("diamond_medium.txt", "romo 13 x 13"),
	DL("diamond_large.txt", "rombo 17x17");
	
	private String filename;
	private String info;
	
	Forma(String filename, String info) {
		this.filename = filename;
		this.info = info;
	}
	
	public static Forma valueOfIgnoreCase(String inputString) {
		for (Forma forma : Forma.values()) {
			if (forma.toString() == inputString.toUpperCase()) {
				return forma;
			}
		}
		return null;
	}

	public String getInfo() {
		return info;
	}
}
