package view.GUIView.createGame;

import java.awt.Frame;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.Controller;
import model.builders.GameTeamsBuilder;
import model.logic.Color;
import utils.Pair;

/**
 * This dialog allows to configure a game, including fields for the players' name and color
 * @author PMC
 *
 */
public class CreateGameWithPlayersDialog extends CreateGameDialog {

	CreatePlayersPanel playersPanel;
	
	public CreateGameWithPlayersDialog(Frame parent, Controller ctrl) {
		super(parent, ctrl);
		
		playersPanel = new CreatePlayersPanel(false);
		mainPanel.add(playersPanel); // by default it shows the players in the GameClassic mode
		this.pack();
	}

	private static final long serialVersionUID = 1L;

	@Override
	public JSONObject createJSONObjectGame() {
		JSONObject o = new JSONObject();
		o.put("type", gameConfig.getGameMode());

		JSONObject boardJSONObject = new JSONObject();
		boardJSONObject.put("shape", gameConfig.getBoardShape().toString());
		boardJSONObject.put("cubes", new JSONArray());

		o.put("board", boardJSONObject);

		JSONArray players = playersPanel.getPlayersReport();
		o.put("players", players);
		o.put("turn", players.getJSONObject(0).getString("color"));

		if (gameConfig.getGameMode().equals(GameTeamsBuilder.TYPE)) {

			JSONArray teamsJSONArray = new JSONArray();

			for (int i = 0; i < teamsPanel.numTeams(); ++i) {
				JSONObject aux = teamsPanel.getTeamReport(i);
				aux.put("players", playersPanel.getPlayersFromTeam(i));
				teamsJSONArray.put(aux);
			}

			o.put("players", players);
			o.put("teams", teamsJSONArray);

		}

		return o;
	}
	
	@Override
	void update(int numberPlayers, boolean isGameMode) {
		playersPanel.update(numberPlayers, isGameMode);
		teamsPanel.update(isGameMode);
		mainPanel.repaint();

		this.pack();
	}
	
	Pair<Boolean, String> checkIfCorrectArguments(boolean isTeamMode) {
		HashSet<String> playerNames = playersPanel.getPlayersNames();
		HashSet<Color> playerColors = playersPanel.getPlayersColors();
		HashSet<String> teamNames = teamsPanel.getTeamNames();

		Pair<Boolean, String> pair;

		if (playerNames.size() < gameConfig.getNumPlayers()) // i.e there is a repeated element
		{
			pair = new Pair<Boolean, String>(false, "ERROR: At least one player has a repeated name.");
			return pair;
		}
		if (playerColors.size() < gameConfig.getNumPlayers()) // i.e there is a repeated element
		{
			pair = new Pair<Boolean, String>(false, "ERROR: At least one player has a repeated color.");
			return pair;
		}
		if (isTeamMode) { // e.d we are in the GameTeams mode
			if (teamNames.size() < teamsPanel.numTeams()) // i.e there is a repeated element
			{
				pair = new Pair<Boolean, String>(false, "ERROR: At least one team has a repeated name.");
				return pair;
			}

			// We check that in every team there is
			// at least one player

			boolean encontrado = false;
			for (int j = 0; j < teamsPanel.numTeams() && !encontrado; ++j) {
				if (!playersPanel.existPlayerAtTeam(j))
					encontrado = true;
			}
			if (encontrado) {
				pair = new Pair<Boolean, String>(false, "ERROR: At least one team is empty.");
				return pair;
			}
		}

		pair = new Pair<Boolean, String>(true, "");
		return pair;

	}
	
	@Override
	void okAction() {
		status = 1;
		state = ctrl.createGame(createJSONObjectGame());
		this.setVisible(false);
	}
}
