package view.GUIView.createGame;

import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.json.JSONArray;
import org.json.JSONObject;

import controller.Controller;
import model.builders.GameTeamsBuilder;
import model.replay.GameState;
import utils.Pair;

/**
 * This dialog allows to configure a game, without having to specify the players' name and color
 * @author PMC
 *
 */
public class CreateGameDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	protected Controller ctrl;
	protected GameState state;
	protected int status;

	GameConfigurationPanel gameConfig;
	CreateTeamsPanel teamsPanel;

	protected JPanel mainPanel;

	/**
	 * Contructor
	 * @param parent Parent component
	 * @param ctrl Game Controller
	 */
	public CreateGameDialog(Frame parent, Controller ctrl) {
		super(parent, true);
		this.ctrl = ctrl;
		initGUI();
	}

	/**
	 * It initializates the GUI
	 */
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
		setMinimumSize(new Dimension(725, 80));

		this.pack();
	}

	/**
	 * It opens the dialog
	 * @return Dialog status
	 */
	public int open() {
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		setVisible(true);
		return status;
	}

	/**
	 * It gets the information from the dialog an generates a JSONObject with the Game information (according to the report)
	 * @return JSONObject with the Game information (according to the report)
	 */
	public JSONObject createJSONObjectGame() {
		JSONObject o = new JSONObject();
		o.put("type", gameConfig.getGameMode());

		JSONObject boardJSONObject = new JSONObject();
		boardJSONObject.put("shape", gameConfig.getBoardShape().toString());
		boardJSONObject.put("cubes", new JSONArray());

		o.put("board", boardJSONObject);
		
		JSONArray players = new JSONArray();
		for (int i = 0; i < gameConfig.getNumPlayers(); i++) {
			players.put("");
		}
		o.put("players", players);
		
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

	/**
	 * This method shows or hides the team panel depending on the selected option
	 * @param numberPlayers Number of players
	 * @param isTeamMode true if it is TeamMode
	 */
	void update(int numberPlayers, boolean isTeamMode) {
		teamsPanel.update(isTeamMode);
		mainPanel.repaint();

		this.pack();
	}

	/**
	 * It returns the generated state
	 * @return The generated GameState
	 */
	public GameState getState() {
		return this.state;
	}

	/**
	 * It exits the dialog
	 */
	void exit() {
		this.status = 0;
		this.setVisible(false);
	}

	void okAction() {
		status = 1;
		this.setVisible(false);
	}
	
	Pair<Boolean, String> checkIfCorrectArguments(boolean isTeamMode) {
		
		return new Pair<Boolean, String>(true, null);
	}
	

}
