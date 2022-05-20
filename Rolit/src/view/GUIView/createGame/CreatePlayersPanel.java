package view.GUIView.createGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import org.json.JSONArray;

import model.logic.Color;
import view.GUIView.RolitComponents.RolitPanel;

/**
 * This panel contains PlayerDataPanels displayed vertically
 * @author PMC
 *
 */
public class CreatePlayersPanel extends RolitPanel {
	private static final long serialVersionUID = 1L;
	
	private static final int INITIAL_NUMBER_PLAYERS = 2;
	
	private List<PlayerDataPanel> listPlayerPanels;
	private boolean isTeamMode;
	
	/**
	 * Constructor
	 * @param isTeamMode true if playing GameTeams and false otherwise
	 */
	CreatePlayersPanel(boolean isTeamMode){
		this.isTeamMode = isTeamMode;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setAlignmentX(CENTER_ALIGNMENT);
		
		listPlayerPanels = new ArrayList<PlayerDataPanel>();
		
		for(int i = 0; i < INITIAL_NUMBER_PLAYERS; i++) {
			listPlayerPanels.add(new PlayerDataPanel(i, isTeamMode));
		}
		
		for(JPanel panel : listPlayerPanels) {
			this.add(panel);
		}
		
		JPanel buttonsPanel = new RolitPanel();
		this.add(buttonsPanel);
		
		this.setVisible(true);
	}
	
	/**
	 * This function returns the text in the name fields
	 * @return A set containing the text in the name fields
	 */
	public HashSet<String> getPlayersNames(){
		HashSet<String> playerNames = new HashSet<String>();
		for(int i = 0; i < listPlayerPanels.size(); i++) {
			String name = listPlayerPanels.get(i).getPlayerName();
			playerNames.add(name);
		}
		return playerNames;
	}
	
	/**
	 * This function returns the chosen colors
	 * @return A set containing the chosen colors
	 */
	public HashSet<Color> getPlayersColors(){
		HashSet<Color> playerColors = new HashSet<Color>();
		for(int i = 0; i < listPlayerPanels.size(); i++) {
			Color color = listPlayerPanels.get(i).getPlayerColor();
			playerColors.add(color);
		}
		return playerColors;
	}
	
	/**
	 * It returns the report of the players according to the fields
	 * @return A JSONArray of JSONObject, each of them containing a player report
	 */
	public JSONArray getPlayersReport() {
		JSONArray playersArray = new JSONArray();
		
		for (PlayerDataPanel playerDataPanel : listPlayerPanels) {
			playersArray.put(playerDataPanel.getPlayerReport());
		}
		
		return playersArray;
		
	}

	/**
	 * This function updates the panel graphics when the user changes the game mode
	 * @param numPlayers Number of players
	 * @param isTeamMode true if playing GameTeams and false otherwise
	 */
	public void update(int numPlayers, boolean isTeamMode) {
		updatePlayers(numPlayers);
		updateMode(isTeamMode);
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * It updates the number of PlayerDataPanels in the dialog
	 * @param numPlayers Number of players
	 */
	private void updatePlayers(int numPlayers) {
		while(numPlayers < listPlayerPanels.size()) {
			PlayerDataPanel p = listPlayerPanels.get(listPlayerPanels.size() - 1);
			this.remove(p);
			listPlayerPanels.remove(p);
			
		}
		while(numPlayers > listPlayerPanels.size()) {
			PlayerDataPanel p = new PlayerDataPanel(listPlayerPanels.size(), isTeamMode);
			listPlayerPanels.add(p);
			this.add(p);

		}
	}
	
	/**
	 * It shows/hides the team configuration of each player
	 * @param numPlayers Number of players
	 */
	private void updateMode(boolean isTeamMode) {
		for (PlayerDataPanel playerDataPanel : listPlayerPanels) {
			playerDataPanel.updateMode(isTeamMode);
		}
	}
	
	/**
	 * Given the team number, this function returns the players that joined the team.
	 * @param teamNumber Team number
	 * @return A JSONArray with the report of the players that joined the team.
	 */
	JSONArray getPlayersFromTeam(int teamNumber) {
		JSONArray players = new JSONArray();
		for (PlayerDataPanel playerDataPanel : listPlayerPanels) {
			if(playerDataPanel.isAtTeam(teamNumber))
				players.put(playerDataPanel.getPlayerReport());
		}
		return players;
		
	}
	
	/**
	 * It checks whether a team has players or not
	 * @param teamNumber Team number
	 * @return true if there is at least one player at team number teamNumber and false otherwise
	 */
	boolean existPlayerAtTeam(int teamNumber) {
		for (PlayerDataPanel playerDataPanel : listPlayerPanels) {
			if(playerDataPanel.isAtTeam(teamNumber))
				return true;
		}
		return false;
	}
	
}
