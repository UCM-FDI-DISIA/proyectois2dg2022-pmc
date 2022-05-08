package model.logic;

import utils.StringUtils;

/**
 * This class represents the shape of a Board
 * @author PMC
 *
 */
public enum Shape {
	
	SS("square_small.txt", "square 9x9", "resources/icons/squareShape.png"), SM("square_medium.txt", "square 13x13", "resources/icons/squareShape.png"), 
	SL("square_large.txt", "square 17x17", "resources/icons/squareShape.png"), CS("circle_small.txt", "circle 9x9", "resources/icons/circleShape.png"), 
	CM("circle_medium.txt", "circle 13x13", "resources/icons/circleShape.png"), CL("circle_large.txt", "circle 17x17", "resources/icons/circleShape.png"),
	DS("diamond_small.txt", "diamond 9x9", "resources/icons/diamondShape.png"), DM("diamond_medium.txt", "diamond 13 x 13", "resources/icons/diamondShape.png"),
	DL("diamond_large.txt", "diamond 17x17", "resources/icons/diamondShape.png");
	
	private String filename;
	private String info;
	private String path;
	private String directory = "./resources/shapes/";

	/**
	 * Constructor
	 * @param filename Name of the file that contains the shape
	 * @param info Shape description
	 * @param path Path of the shape icon
	 */
	Shape(String filename, String info, String path) {
		this.filename =  directory + filename;
		this.info = info;
		this.path = path;
	}
	
	/**
	 * This function receives a shape name and returns its shape. It is not case sensitive. If not match was found it returns null
	 * @param inputString Shape name
	 * @return Shape associated to the name
	 */
	public static Shape valueOfIgnoreCase(String inputString) {
		for (Shape forma : Shape.values()) {
			if (forma.toString().equals(inputString.toUpperCase())) {
				return forma;
			}
		}
		return null;
	}
	
	/**
	 * This method returns a String listing the available shapes
	 * @return List of available shapes
	 */
	public static String availableShapes() {
		StringBuilder buffer = new StringBuilder("Available shapes: ");
		buffer.append(StringUtils.LINE_SEPARATOR);
		for (Shape forma : Shape.values()) {
			buffer.append(forma.name()).append(": ").append(forma.getInfo()).append(StringUtils.LINE_SEPARATOR);
		}
		return buffer.toString();
	}

	/**
	 * It returns a brief description of the shape
	 * @return Brief description of the shape
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * It returns the filename where the shape is saved
	 * @return The filename where the shape is saved
	 */
	public String getFilename() {
		return filename;
	}
	
	/**
	 * It returns the path of the shape icon
	 * @return The path of the shape icon
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Returns the size name in natural language
	 * @return The size name
	 */
	public String toSizeString() {
		
		switch(this.name().charAt(1)) {
		case 'S':
			return "Small";
		case 'M':
			return "Medium";
		case 'L':
			return "Large";
		
		}
		return null;
	}
	
}
