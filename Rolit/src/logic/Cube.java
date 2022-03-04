package logic;

public class Cube {
	private int x;
	private int y;
	private Player player;
	private int value = 1;
	
	public Cube(int x, int y, Player player) {
		this.x = x;
		this.y = y;
		this.player = player;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public Color getColor() {
		return player.getColor();
	}
	
	public void setColor(Color color) {
		this.player = player.getPlayer(color);
	}
	
	
	@Override
	public String toString() {
		return this.getColor().toString();
	}

	public String serialize() {
		return (this.toString() + " " + x + " " + y);
	}
	
	public void addScore() {//FIXME ARREGLAR CUANDO SE RESTAN LOS PUNTOS, HACERLO TODO INTERNAMENTE CUANDO SE CAMBIA DE COLOR
		player.addScore(value);
	}
}
