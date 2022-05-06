package model.logic;

public interface Rival extends Comparable<Rival>{
	public String getName();
	public int getScore();
	
	@Override
	public default int compareTo(Rival r) {
		return -(this.getScore() - r.getScore());
	}
}
