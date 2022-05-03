package view.GUIView.createGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import org.json.JSONArray;
import logic.Color;
import view.GUIView.RolitComponents.RolitPanel;

public class CreatePlayersPanel extends RolitPanel {
	private static final long serialVersionUID = 1L;
	
	private static final int INITIAL_NUMBER_PLAYERS = 2;
	
	private List<PlayerDataPanel> listPlayerPanels;
	private boolean isTeamMode;
	
	
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
	
	public HashSet<String> getPlayersNames(){
		HashSet<String> playerNames = new HashSet<String>();
		for(int i = 0; i < listPlayerPanels.size(); i++) {
			String name = listPlayerPanels.get(i).getPlayerName();
			playerNames.add(name);
		}
		return playerNames;
	}
	
	public HashSet<Color> getPlayersColors(){
		HashSet<Color> playerColors = new HashSet<Color>();
		for(int i = 0; i < listPlayerPanels.size(); i++) {
			Color color = listPlayerPanels.get(i).getPlayerColor();
			playerColors.add(color);
		}
		return playerColors;
	}
	
	public JSONArray getPlayersReport() {
		JSONArray playersArray = new JSONArray();
		
		for (PlayerDataPanel playerDataPanel : listPlayerPanels) {
			playersArray.put(playerDataPanel.getPlayerReport());
		}
		
		return playersArray;
		
	}

	public void update(int numPlayers, boolean isTeamMode) {
		updatePlayers(numPlayers);
		updateMode(isTeamMode);
		this.revalidate();
		this.repaint();
	}
	
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
	
	private void updateMode(boolean isTeamMode) {
		for (PlayerDataPanel playerDataPanel : listPlayerPanels) {
			playerDataPanel.updateMode(isTeamMode);
		}
	}
	
	JSONArray getPlayersFromTeam(int teamNumber) {
		JSONArray players = new JSONArray();
		for (PlayerDataPanel playerDataPanel : listPlayerPanels) {
			if(playerDataPanel.isAtTeam(teamNumber))
				players.put(playerDataPanel.getPlayerReport());
		}
		return players;
		
	}
	
	boolean existPlayerAtTeam(int teamNumber) {
		for (PlayerDataPanel playerDataPanel : listPlayerPanels) {
			if(playerDataPanel.isAtTeam(teamNumber))
				return true;
		}
		return false;
	}
	
}
