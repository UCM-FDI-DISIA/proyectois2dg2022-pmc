package logic;

import utils.StringUtils;

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
	
	public static int BIG_SHAPE_BUTTON_LENGTH = 28;
	public static int MEDIUM_SHAPE_BUTTON_LENGTH = 36;
	public static int SMALL_SHAPE_BUTTON_LENGTH = 48;

	Shape(String filename, String info, String path) {
		this.filename =  directory + filename;
		this.info = info;
		this.path = path;
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
	
	public String getPath() {
		return path;
	}

	public int shapeToSideButtonLength() {
		
		switch(this.name().charAt(1)) {
		case 'S':
			return SMALL_SHAPE_BUTTON_LENGTH;
		case 'M':
			return MEDIUM_SHAPE_BUTTON_LENGTH;
		case 'L':
			return BIG_SHAPE_BUTTON_LENGTH;
		
		}
		
		return 0;
	}
	
	/*
	@Override
	public String toString() {
		
		this.
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
	 * */
	
	
}
