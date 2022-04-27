package view.GUIView.createGame;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.json.JSONArray;
import org.json.JSONObject;
import Builders.GameTeamsBuilder;
import control.Controller;
import logic.Color;
import logic.Player;
import replay.State;
import utils.Pair;

public class CreateGameDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private Controller ctrl;
	private State state;
	private int status;

	GameConfigurationPanel gameConfig;
	CreatePlayersPanel playersPanel;
	CreateTeamsPanel teamsPanel;

	private JPanel mainPanel;

	private boolean onlineMode;

	public CreateGameDialog(Frame parent, boolean onlineMode, Controller ctrl) {
		super(parent, true);
		this.onlineMode = onlineMode;
		this.ctrl = ctrl;
		initGUI();
	}

	public void initGUI() {

		this.setLocation(50, 50);
		this.setSize(700, 200);

		setTitle("Create Game");
		setVisible(false);

		mainPanel = new JPanel();

		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.setAlignmentX(CENTER_ALIGNMENT);

		gameConfig = new GameConfigurationPanel(this);
		playersPanel = new CreatePlayersPanel(false);
		teamsPanel = new CreateTeamsPanel();

		mainPanel.add(gameConfig);
		mainPanel.add(teamsPanel);
		mainPanel.add(playersPanel); // por defecto se muestra para elegir jugadores en el modo GameClassic

		setContentPane(mainPanel);
		setMinimumSize(new Dimension(700, 150));

		this.pack();
	}

	public int open() {
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		setVisible(true);
		return status;
	}

	public JSONObject createJSONObjectGame() {
		JSONObject o = new JSONObject();
		o.put("type", gameConfig.getGameMode());

		JSONObject boardJSONObject = new JSONObject();
		boardJSONObject.put("shape", gameConfig.getBoardShape().toString());
		boardJSONObject.put("cubes", new JSONArray());

		o.put("board", boardJSONObject);

		if (!onlineMode) {
			JSONArray players = playersPanel.getPlayersReport();
			o.put("players", players);
			o.put("turn", players.getJSONObject(0).getString("color"));
		}

		if (gameConfig.getGameMode().equals(GameTeamsBuilder.TYPE)) {

			JSONArray teamsJSONArray = new JSONArray();

			for (int i = 0; i < teamsPanel.numTeams(); ++i) {
				JSONObject aux = teamsPanel.getTeamReport(i);
				aux.put("players", playersPanel.getPlayersFromTeam(i));
				teamsJSONArray.put(aux);
			}

			if (!onlineMode) {
				JSONArray players = playersPanel.getPlayersReport();
				o.put("players", players);
				o.put("turn", players.getJSONObject(0).getString("color"));
			}

			o.put("teams", teamsJSONArray);

		}

		return o;
	}

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

	public State getState() {
		return this.state;
	}

	void exit() {
		this.status = 0;
		this.setVisible(false);
	}

	void generateGame() {
		status = 1;
		state = ctrl.createGame(createJSONObjectGame());
		this.setVisible(false);
	}

	int getPlayerSpinnerValue() {
		return gameConfig.getPlayerSpinnerValue();
	}

}
