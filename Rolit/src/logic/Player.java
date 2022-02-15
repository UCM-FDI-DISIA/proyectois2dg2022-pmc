package logic;

public class Player {
	private Color color;
	private int score;
	
	public Player(Color c) {
		this.color = c;
		this.score = 0;
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
