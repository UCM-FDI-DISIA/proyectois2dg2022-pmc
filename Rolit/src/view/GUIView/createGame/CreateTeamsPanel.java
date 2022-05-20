package view.GUIView.createGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.BoxLayout;

import org.json.JSONObject;

import view.GUIView.RolitComponents.RolitPanel;

/**
 * This panel contains TeamDataPanels displayed vertically
 * @author PMC
 *
 */
public class CreateTeamsPanel extends RolitPanel {
	private static final long serialVersionUID = 1L;
	
	private static final int INITIAL_NUMBER_TEAMS = 2;
	
	List<TeamDataPanel> listTeamPanels;
	
	/**
	 * Constructor
	 */
	CreateTeamsPanel(){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setAlignmentX(CENTER_ALIGNMENT);
		
		listTeamPanels = new ArrayList<>();

		for (int i = 0; i < INITIAL_NUMBER_TEAMS; i++) {
			TeamDataPanel t = new TeamDataPanel(i);
			listTeamPanels.add(t);
			this.add(t);
		}
		this.setVisible(false);
	}
	
	/**
	 * It shows/hides the panel if the game mode is GameTeams or not.
	 * @param isTeamMode  true if playing GameTeams and false otherwise
	 */
	void update(boolean isTeamMode){
		if(isTeamMode)
			this.setVisible(true);
		else
			this.setVisible(false);
	}
	
	/**
	 * Returns the number of teams
	 * @return The number of teams
	 */
	int numTeams() {
		return INITIAL_NUMBER_TEAMS;
	}
	
	/**
	 * It returns the report of a team in JSONObject format. IMPORTANT: it doesn't contain the playerList.
	 * @param team Team number
	 * @return The report of a team in JSONObject format. IMPORTANT: it doesn't contain the playerList.
	 */
	JSONObject getTeamReport(int team) {
		return listTeamPanels.get(team).getTeamReport();
	}
	
	/**
	 * This function returns the text in the name fields
	 * @return A set containing the text in the name fields
	 */
	public HashSet<String> getTeamNames(){
		HashSet<String> teamNames = new HashSet<String>();
		for(int i = 0; i < listTeamPanels.size(); i++) {
			String name = listTeamPanels.get(i).getTeamName();
			teamNames.add(name);
		}
		return teamNames;
	}
	
}
