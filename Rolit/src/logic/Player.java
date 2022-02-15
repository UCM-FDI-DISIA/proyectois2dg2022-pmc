package logic;

public class Player {
	private Color color;
	private int score;
	private String name;
	
	public Player(Color c, String name) {
		this.color = c;
		this.score = 0;
		this.name = name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getScore() {
		return score;
	}
	
	public void addScore(int score) {
		this.score += score;
	}
	
}
