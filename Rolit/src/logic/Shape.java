package logic;

import utils.StringUtils;

public enum Shape {
	
	SS("square_small.txt", "square 9x9"), SM("square_medium.txt", "square 13x13"), 
	SL("square_large.txt", "square 17x17"), CS("circle_small.txt", "circle 9x9"), 
	CM("circle_medium.txt", "circle 13x13"), CL("circle_large.txt", "circle 17x17"),
	DS("diamond_small.txt", "diamond 9x9"), DM("diamond_medium.txt", "diamond 13 x 13"),
	DL("diamond_large.txt", "diamond 17x17");
	
	private String filename;
	private String info;
	private String directory = "./resources/shapes/";

	Shape(String filename, String info) {
		this.filename =  directory + filename;
		this.info = info;
	}
	
	public static Shape valueOfIgnoreCase(String inputString) {
		for (Shape forma : Shape.values()) {
			if (forma.toString().equals(inputString.toUpperCase())) {
				return forma;
			}
		}
		return null;
	}
	
	public static String availableShapes() {
		StringBuilder buffer = new StringBuilder("Available shapes: ");
		buffer.append(StringUtils.LINE_SEPARATOR);
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
