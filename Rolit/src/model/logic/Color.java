package model.logic;

public enum Color {
	YELLOW('Y', "resources/icons/yellowCube.png"), RED('R',"resources/icons/redCube.png"), GREEN('G',"resources/icons/greenCube.png"), BLUE('L', "resources/icons/blueCube.png"), 
	ORANGE('O', "resources/icons/orangeCube.png"), PINK('K', "resources/icons/pinkCube.png"), PURPLE('P', "resources/icons/purpleCube.png"), 
	BLACK('B', "resources/icons/blackCube.png"), BROWN('W', "resources/icons/brownCube.png"), BEIGE('E', "resources/icons/beigeCube.png");
	
	private char shortcut;
	private String path;
	
	Color(char shortcut, String path) {
		this.shortcut = shortcut;
		this.path = path;
	}
	
	public static Color valueOfIgnoreCase(char inputString) {
		for (Color color : Color.values()) {
			if (color.shortcut == Character.toUpperCase(inputString)) {
				return color;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return String.valueOf(shortcut);
	}
	
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
	
	public String getPath() {
		return this.path;
	}
	
	public static int size() {
		return Color.values().length;
	}
}
