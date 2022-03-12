package logic;

import utils.StringUtils;

public enum Shape {
	SS("square_small.txt", "cuadrado 9x9"), SM("square_medium.txt", "cuadrado 13x13"), 
	SL("square_large.txt", "cuadrado 17x17"), CS("circle_small.txt", "circulo 9x9"), 
	CM("circle_medium.txt", "circulo 13x13"), CL("circle_large.txt", "circulo 17x17"),
	DS("diamond_small.txt", "rombo 9x9"), DM("diamond_medium.txt", "romo 13 x 13"),
	DL("diamond_large.txt", "rombo 17x17");
	
	private String filename;
	private String info;
	
	Shape(String filename, String info) {
		this.filename = filename;
		this.info = info;
	}
	
	public static Shape valueOfIgnoreCase(String inputString) {
		for (Shape forma : Shape.values()) {
			if (forma.toString() == inputString.toUpperCase()) {
				return forma;
			}
		}
		return null;
	}
	
	public static String availableShapes() {
		StringBuilder buffer = new StringBuilder("Elija entre uno de los siguinetes: ");
		for (Shape forma : Shape.values()) {
			buffer.append(forma.name()).append(": ").append(forma.getInfo()).append(StringUtils.LINE_SEPARATOR);
		}
		return buffer.toString();
	}

	public String getInfo() {
		return info;
	}

	public String getFilename() {
		return filename;
	}
}
