package logic;

public class Player implements Comparable<Player> {
	private Color color;
	private int score;
	private String name;

	public Player(Color c, String name) {
		this.color = c;
		this.score = 0;
		this.name = name;
	}

	public Color getColor() {
		return this.color;
	}

	public int getScore() {
		return this.score;
	}

	public void addScore(int score) {
		this.score += score;
	}

	@Override
	public int compareTo(Player p) {
		// FIXME SI FALLA EL ORDEN ES AQU�
		if (score < p.getScore()) {
			return -1;
		} else if (score == p.getScore()) {
			return 0;
		}
		return 1;

	}

	public boolean isColorAvailable(Color color) {
		return true;
	}
	
	public String getName() {
		return this.name;
	}
}
