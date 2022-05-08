package model.logic;

/**
 * This enum class represents each player color
 * @author PMC
 *
 */
public enum Color {
	YELLOW('Y', "resources/icons/yellowCube.png"), RED('R',"resources/icons/redCube.png"), GREEN('G',"resources/icons/greenCube.png"), BLUE('L', "resources/icons/blueCube.png"), 
	ORANGE('O', "resources/icons/orangeCube.png"), PINK('K', "resources/icons/pinkCube.png"), PURPLE('P', "resources/icons/purpleCube.png"), 
	BLACK('B', "resources/icons/blackCube.png"), BROWN('W', "resources/icons/brownCube.png"), BEIGE('E', "resources/icons/beigeCube.png");
	
	private char shortcut;
	private String path;
	
	/**
	 * Constructor
	 * @param shortcut The color shortcut
	 * @param path Visual image path
	 */
	Color(char shortcut, String path) {
		this.shortcut = shortcut;
		this.path = path;
	}
	
	/**
	 * This function receives a shortcut and returns its color. It is not case sensitive. If not match was found it returns null
	 * @param shortcut The color shortcut
	 * @return Color associated to the shortcut
	 */
	public static Color valueOfIgnoreCase(char shortcut) {
		for (Color color : Color.values()) {
			if (color.shortcut == Character.toUpperCase(shortcut)) {
				return color;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return String.valueOf(shortcut);
	}
	
	/**
	 * Given a color returns its complete name, not the sortcut
	 * @return The color name
	 */
	public String toFullNameString() {
		switch(this.shortcut) {
		case 'Y':
			return "Yellow";
		case 'O':
			return "Orange";
		case 'B':
			return "Black";
		case 'R':
			return "Red";
		case 'K':
			return "Pink";
		case 'W':
			return "Brown";
		case 'G':
			return "Green";
		case 'P':
			return "Purple";
		case 'E':
			return "Beige";
		case 'L':
			return "Blue";
		}
		return null;
		
	}
	
	/**
	 * This method returns of the image associated to the color
	 * @return Image associated to the color
	 */
	public String getPath() {
		return this.path;
	}
	
	/**
	 * This method returns the number of colors
	 * @return Number of colors
	 */
	public static int size() {
		return Color.values().length;
	}
}
