package view.GUIView.createGame;

import java.awt.Frame;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONObject;

import Builders.GameTeamsBuilder;
import control.Controller;
import logic.Color;
import utils.Pair;

public class CreateGameWithPlayersDialog extends CreateGameDialog {

	CreatePlayersPanel playersPanel;
	
	public CreateGameWithPlayersDialog(Frame parent, Controller ctrl) {
		super(parent, ctrl);
		
		playersPanel = new  CreatePlayersPanel(false);
		mainPanel.add(playersPanel); // por defecto se muestra para elegir jugadores en el modo GameClassic
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
			//o.put("turn", players.getJSONObject(0).getString("color")); //FIXME LO COMENTO PORQUE CREO QUE NO HACE FALTA
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

		if (playerNames.size() < gameConfig.getPlayerSpinnerValue()) // e.d hay un elemento repetido
		{
			pair = new Pair<Boolean, String>(false, "ERROR: At least one player has a repeated name.");
			return pair;
		}
		if (playerColors.size() < gameConfig.getPlayerSpinnerValue()) // e.d hay un elemento repetido
		{
			pair = new Pair<Boolean, String>(false, "ERROR: At least one player has a repeated color.");
			return pair;
		}
		if (isTeamMode) { // e.d estamos en el modo por equipos
			if (teamNames.size() < teamsPanel.numTeams()) // e.d hay un elemento repetido
			{
				pair = new Pair<Boolean, String>(false, "ERROR: At least one team has a repeated name.");
				return pair;
			}

			// comprobamos que en todos los equipos hay por
			// lo menos un jugador

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
	void generateGame() {
		status = 1;
		state = ctrl.createGame(createJSONObjectGame());
		this.setVisible(false);
	}
}
