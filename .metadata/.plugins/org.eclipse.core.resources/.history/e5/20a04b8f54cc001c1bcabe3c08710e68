package logic;

public interface Rival extends Comparable<Rival>{
	public String getName();
	public int getScore();
	public String getType();
	
	@Override
	public default int compareTo(Rival r) {
		return -(this.getScore() - r.getScore());
	}
}
