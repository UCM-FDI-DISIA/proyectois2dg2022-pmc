package view.GUIView.createGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.BoxLayout;

import org.json.JSONObject;

import logic.Color;
import view.GUIView.RolitComponents.RolitPanel;

public class CreateTeamsPanel extends RolitPanel {
	private static final long serialVersionUID = 1L;
	
	private static final int INITIAL_NUMBER_TEAMS = 2;
	
	List<TeamDataPanel> listTeamPanels;
	
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
	
	void update(boolean isTeamMode){
		if(isTeamMode)
			this.setVisible(true);
		else
			this.setVisible(false);
	}
	
	int numTeams() {
		return INITIAL_NUMBER_TEAMS;
	}
	
	//This JSONObject doesn't contain the playerList
	JSONObject getTeamReport(int team) {
		return listTeamPanels.get(team).getTeamReport();
	}
	
	public HashSet<String> getTeamNames(){
		HashSet<String> teamNames = new HashSet<String>();
		for(int i = 0; i < listTeamPanels.size(); i++) {
			String name = listTeamPanels.get(i).getTeamName();
			teamNames.add(name);
		}
		return teamNames;
	}
	
}
