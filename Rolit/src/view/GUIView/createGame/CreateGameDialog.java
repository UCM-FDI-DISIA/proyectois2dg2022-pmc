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

	protected Controller ctrl;
	protected State state;
	protected int status;

	GameConfigurationPanel gameConfig;
	CreateTeamsPanel teamsPanel;

	protected JPanel mainPanel;

	public CreateGameDialog(Frame parent, Controller ctrl) {
		super(parent, true);
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
		teamsPanel = new CreateTeamsPanel();

		mainPanel.add(gameConfig);
		mainPanel.add(teamsPanel);

		setContentPane(mainPanel);
		setMinimumSize(new Dimension(700, 80));

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
		
		if (gameConfig.getGameMode().equals(GameTeamsBuilder.TYPE)) {

			JSONArray teamsJSONArray = new JSONArray();

			for (int i = 0; i < teamsPanel.numTeams(); ++i) {
				JSONObject aux = teamsPanel.getTeamReport(i);
				teamsJSONArray.put(aux);
			}

			o.put("teams", teamsJSONArray);

		}

		return o;
	}

	void update(int numberPlayers, boolean isGameMode) {
		teamsPanel.update(isGameMode);
		mainPanel.repaint();

		this.pack();
	}

	public State getState() {
		return this.state;
	}

	void exit() {
		this.status = 0;
		this.setVisible(false);
	}

	void okAction() {
		status = 1;
		this.setVisible(false);
	}

	int getPlayerSpinnerValue() {
		return gameConfig.getPlayerSpinnerValue();
	}
	
	Pair<Boolean, String> checkIfCorrectArguments(boolean isTeamMode) {
		
		return new Pair<Boolean, String>(true, null);
	}
	

}
