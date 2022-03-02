package logic;

public enum Color {
	YELLOW('Y'), RED('R'), GREEN('G'), BLUE('L'), ORANGE('O'), 
	PINK('K'), PURPLE('P'), BLACK('B'), BROWN('W'), BEIGE('E');
	
	private char shortcut;
	
	Color(char shortcut) {
		this.shortcut = shortcut;
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
}
