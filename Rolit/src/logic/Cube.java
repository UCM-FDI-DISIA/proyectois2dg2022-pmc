package logic;

public class Cube {
	private int x;
	private int y;
	Color color;
	
	public Cube(int x, int y, Color c) {
		this.x = x;
		this.y = y;
		this.color = c;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	public String toString() {
		return color.toString();
	}
}
