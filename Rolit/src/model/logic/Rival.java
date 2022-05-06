package model.logic;
/**
 * This interface is an abstraction of the participants in a Rolit game: players, teams, etc.
 * @author PMC
 *
 */
public interface Rival extends Comparable<Rival>{
	
	/**
	 * This method returns the rival name
	 * @return The rival name
	 */
	public String getName();
	
	/**
	 * It returns the rival score
	 * @return The rival score
	 */
	public int getScore();
	
	@Override
	public default int compareTo(Rival r) {
		return -(this.getScore() - r.getScore());
	}
}
