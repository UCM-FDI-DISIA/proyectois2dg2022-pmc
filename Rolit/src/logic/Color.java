package logic;

public enum Color {
	YELLOW('Y'), RED('R'), GREEN('G'), BLUE('B');
	
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
