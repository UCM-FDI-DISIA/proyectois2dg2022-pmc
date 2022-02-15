package logic;

public enum Color {
	YELLOW('Y'), RED('R'), GREEN('G'), BLUE('B');
	
	private char shortcut;
	
	Color(char shortcut) {
		this.shortcut = shortcut;
	}
	
	@Override
	public String toString() {
		return String.valueOf(shortcut);
	}
}
