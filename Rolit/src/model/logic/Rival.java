package model.logic;
/**
 * This interface is an abstraction of the participants in a Rolit game: players, teams, etc.
 * @author PMC
 *
 */
public interface Rival extends Comparable<Rival>{
	public String getName();
	public int getScore();
	
	@Override
	public default int compareTo(Rival r) {
		return -(this.getScore() - r.getScore());
	}
}
