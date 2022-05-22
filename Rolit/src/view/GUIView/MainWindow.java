package view.GUIView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import javax.swing.Box;
import javax.swing.Box.Filler;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.json.JSONObject;

import controller.Controller;
import model.SaveLoadManager;
import model.logic.Rival;
import model.online.Client;
import model.online.Server;
import model.replay.GameState;
import model.replay.Replay;
import view.RolitObserver;
import view.GUIView.RolitComponents.RolitButton;
import view.GUIView.createGame.CreateGameDialog;
import view.GUIView.createGame.CreateGameWithPlayersDialog;

/**
 * This class represents the main window in which Rolit is played
 * in GUI Mode
 * @author PMC
 */
public class MainWindow extends JFrame implements RolitObserver, ActionListener {
	
	private static final long serialVersionUID = 1L;
	private static final String BUTTONS[] = { "NG", "LG", "DG", "LR", "CS", "JS", "T" };
	private static final String ICONS_PATH = "resources\\icons\\";
	
	private Client clientRolit;
	private Controller ctrl;
	private Replay replay;
	private volatile GameState state;
	private JPanel welcomePanel;
	private JButton createGameButton;
	private JButton loadGameButton;
	private JButton deleteGameButton;
	private JButton loadReplayButton;
	private JButton createServerButton;
	private JButton joinServerButton;
	private JButton tutorialButton;
	private JPanel mainPanel;
	private JPanel gamePanel;
	private JLabel rolitLogo;
	private JLabel optionMessage;
	private JPanel boardPanel;
	private ControlPanel controlPanel;
	private JPanel statusBar;
	
	JoinServerDialog jsd;
	
	private volatile boolean onlineGameStarted = false;	
	
	/**
	 * Constructor
	 * @param ctrl The controller
	 */
	public MainWindow(Controller ctrl) {
		super("Rolit");		
		this.clientRolit = new Client(this);		
		this.ctrl = ctrl;
		this.ctrl.setClient(clientRolit);		
		initGUI();
	}
	
	/**
	 * This method creates and shows all the components relative to this dialog
	 */
	private void initGUI() {
		//Icon of the window
		this.setIconImage(new ImageIcon(ICONS_PATH + "\\rolitIcon.png").getImage());
		
		//Welcome Panel
		welcomePanel = new JPanel();
		welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
		welcomePanel.setBackground(Color.WHITE);
		this.setContentPane(welcomePanel);
		
		//Logo
		rolitLogo = new JLabel(resizeIcon(new ImageIcon(ICONS_PATH + "\\logoRolit.png"), 400, 150));
		rolitLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
		welcomePanel.add(rolitLogo);
		
		//Buttons
		
		//Button of new game
		createGameButton = new RolitButton("Create game");
		createGameButton.setActionCommand(BUTTONS[0]);
		createGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		createGameButton.addActionListener(this);
		
		//Button of load game
		loadGameButton = new RolitButton("Load game");
		loadGameButton.setActionCommand(BUTTONS[1]);
		loadGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		loadGameButton.addActionListener(this);
		
		//Button of delete game
		deleteGameButton = new RolitButton("Delete game");
		deleteGameButton.setActionCommand(BUTTONS[2]);
		deleteGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		deleteGameButton.addActionListener(this);
		
		//Button of load replay
		loadReplayButton = new RolitButton("Load replay");
		loadReplayButton.setActionCommand(BUTTONS[3]);
		loadReplayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		loadReplayButton.addActionListener(this);
		
		//Button of creating server
		createServerButton = new RolitButton("Create Server");
		createServerButton.setActionCommand(BUTTONS[4]);
		createServerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		createServerButton.addActionListener(this);
		
		//Button of joining a server
		joinServerButton = new RolitButton("Join Server");
		joinServerButton.setActionCommand(BUTTONS[5]);
		joinServerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		joinServerButton.addActionListener(this);
		
		//Button to open the tutorial
		tutorialButton = new RolitButton("Tutorial");
		tutorialButton.setActionCommand(BUTTONS[6]);
		tutorialButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		tutorialButton.addActionListener(this);
		
		//Labels
		optionMessage = new JLabel("Choose an option:");
		optionMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//Separator at the end (so that there is a gap between the last component and the end of the JFrame)
		Box.Filler glue = (Filler) Box.createVerticalGlue();
	    glue.changeShape(glue.getMinimumSize(),  new Dimension(0, 10), glue.getMaximumSize());

				
		// Putting the buttons in the panel
	  
		welcomePanel.add(optionMessage);
		welcomePanel.add(Box.createRigidArea(new Dimension(1, 5))); //Gap
		welcomePanel.add(createGameButton);
		welcomePanel.add(Box.createRigidArea(new Dimension(1, 5)));
		welcomePanel.add(loadGameButton);
		welcomePanel.add(Box.createRigidArea(new Dimension(1, 5)));
		welcomePanel.add(deleteGameButton);
		welcomePanel.add(Box.createRigidArea(new Dimension(1, 5)));
		welcomePanel.add(loadReplayButton);
		welcomePanel.add(Box.createRigidArea(new Dimension(1, 5)));
		welcomePanel.add(createServerButton);
		welcomePanel.add(Box.createRigidArea(new Dimension(1, 5)));
		welcomePanel.add(joinServerButton);
		welcomePanel.add(Box.createRigidArea(new Dimension(1, 5)));
		welcomePanel.add(tutorialButton);
		welcomePanel.add(Box.createRigidArea(new Dimension(1, 5)));
		
	    welcomePanel.add(glue);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
	
	/**
	 * This method, overridden from ActionListener, manages all the options when clicked
	 */
	@Override
	public void actionPerformed(ActionEvent e) {		
		switch(e.getActionCommand()) {
		case "NG":
			CreateGameDialog dialogNew = new CreateGameWithPlayersDialog(MainWindow.this, ctrl);
			int statusng = dialogNew.open();		
			if (statusng == 1) { // OK has been clicked
				this.state = dialogNew.getState();
				ctrl.createGame(dialogNew.createJSONObjectGame());
				this.initGame();
				ctrl.startGame();
				new SaveReplayDialog(this, ctrl);
			}		
			break;
		case "LG":
			LoadFileDialog dialogLoad = new LoadFileDialog(MainWindow.this, SaveLoadManager.getListOfSavedGames());
			int statuslg = dialogLoad.open();
			if (statuslg == 1) {
				File file = dialogLoad.getFile();
				state = ctrl.loadGame(file.getPath());
				initGame();
				ctrl.startGame();
				new SaveReplayDialog(this, ctrl);
			}
			break;
		case "DG":
			DeleteGameDialog dialogDelete = new DeleteGameDialog(MainWindow.this);
			dialogDelete.open();
			break;
		case "LR":
			LoadFileDialog dialogLoadReplay = new LoadFileDialog(MainWindow.this, SaveLoadManager.getListOfSavedReplays());
			int statuslr = dialogLoadReplay.open();
			
			if (statuslr == 1) {
				File file = dialogLoadReplay.getFile();
				replay = SaveLoadManager.loadReplay(file.getPath());
				initReplay();
			}
		
			break;
		case "CS":
			CreateGameDialog dialogCS = new CreateGameDialog(MainWindow.this, ctrl);
			int statuscs = dialogCS.open();
			if(statuscs == 1) {
				new Server(dialogCS.createJSONObjectGame());
			}
			break;
		case "JS":
			jsd = new JoinServerDialog(this);
			int statusjs = jsd.open();
			if(statusjs == 1) {
				ctrl.setOnlineMode(true);
				try {
					clientRolit.startMatch(jsd.getIp(), jsd.getPort());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(jsd, "Connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
				clientRolit.join(jsd.getPlayerName(), jsd.getPlayerColor());
				jsd.showWaitingDialog();
			}
			break;
		case "T":
			Tutorial tutorial = new Tutorial();
			tutorial.open();
			break;
		}
	}
	
	/**
	 * This method creates and shows all the components relative to the game
	 */
	private void initGame() {		
		// Just in case so that the screen is always cleaned
		if (welcomePanel != null)
			this.remove(welcomePanel);
		
		this.repaint();

		mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
	
		gamePanel = new JPanel(new BorderLayout());	//It contains the turnBar (up) and the boardPanel (down)
		
		boardPanel = new BoardGUI(ctrl);
		
		TurnAndRankingBar turnAndRankingBar = new TurnAndRankingBar(ctrl, state);
		
		gamePanel.add(turnAndRankingBar, BorderLayout.PAGE_START);
		gamePanel.add(boardPanel, BorderLayout.CENTER);
		
		controlPanel = new ControlPanel(ctrl);
		statusBar = new StatusBar(ctrl);
		this.setContentPane(mainPanel);
		mainPanel.add(controlPanel, BorderLayout.PAGE_START);
		mainPanel.add(gamePanel, BorderLayout.CENTER);
		mainPanel.add(statusBar, BorderLayout.PAGE_END);
						
		this.pack();
		this.setSize(new Dimension(this.getWidth() + 50, this.getHeight())); //So that the score list does fit if the names are long enough
		this.setMinimumSize(this.getSize());
		this.setLocationRelativeTo(null);
		ctrl.addObserver(this);

		
	}
	
	/**
	 * This method creates and shows all the components relative to the replay
	 */
	private void initReplay() {

		this.remove(welcomePanel);
		
		this.repaint();
		
		mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
			
		gamePanel = new JPanel(new BorderLayout());	//It contains the turnBar (up) and the boardPanel (down)
				
		boardPanel = new BoardGUI(replay);		
		TurnAndRankingBar trbar = new TurnAndRankingBar(replay);

		gamePanel.add(trbar, BorderLayout.PAGE_START);
		gamePanel.add(boardPanel, BorderLayout.CENTER);

		this.setContentPane(mainPanel);
		mainPanel.add(new ControlPanel(replay), BorderLayout.PAGE_START);
		mainPanel.add(gamePanel, BorderLayout.CENTER);
		mainPanel.add(new StatusBar(replay),BorderLayout.PAGE_END);		
		
		this.repaint();
		this.pack();
		this.setSize(new Dimension(this.getWidth() + 50, this.getHeight())); //So that the score list does fit if the names are long enough
		this.setMinimumSize(this.getSize());
		this.setLocationRelativeTo(null);
	}
	
	/**
	 * onError method overridden (RolitObserver interface)
	 */
	@Override
	public void onError(String err) {
	}

	/**
	 * onTurnPlayed method overridden (RolitObserver interface).
	 * Particularly, the game state is updated and the whole GUI
	 * is repainted
	 */
	@Override
	public void onTurnPlayed(GameState state) {
		this.state = state;
		this.revalidate();
		this.repaint();
	}

	/**
	 * Getter of the game report from the GameState
	 * @return The report of the game
	 */
	public JSONObject getGameReport() {
		return state.report().getJSONObject("game");
	}

	/**
	 * This method is used to update the GUI as well as the controller
	 * with the JSONObject report of the game that has been sent by the server from another client
	 * @param JSONnewState JSONObject that represents the report of the game sent by server
	 */
	public void updateGameFromServer(JSONObject JSONnewState) {
		if(!onlineGameStarted) {
			onlineGameStarted = true;
			jsd.closeWaitingDialog();
			state = ctrl.createGame(JSONnewState);
			initGame();
		}			
		ctrl.updateGameFromServer(JSONnewState);
	}

	/**
	 * onGameFinished method overridden (RolitObserver interface).
	 * Particularly, when the game is finished, this method erases unnecessary JComponents
	 * and shows the ranking panel.
	 */
	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival, Replay replay, GameState state) {
		onTurnPlayed(state);
		
		try {
			Thread.sleep(1000); //Wait 1 second to see the last cube
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		gamePanel.remove(boardPanel);
		RankingPanel r_panel = new RankingPanel(rivals);
		gamePanel.add(r_panel);
		this.remove(controlPanel);
		this.remove(statusBar);
		this.revalidate();
	}

	/**
	 * onRegister method overridden (RolitObserver interface)
	 * Particularly, the state attribute is updated with the new one
	 */
	@Override
	public void onRegister(GameState state) {
		this.state = state;
	}

	/**
	 * onGameFinished method overridden (RolitObserver interface)
	 * Particularly, when the game is finished, this method erases unnecessary JComponents
	 * and shows the ranking panel.
	 */
	@Override
	public void onGameStatusChange(GameState state) {
		
	}

	/**
	 * This method is used to create a ChooseTeamFromServerDialog that allows the client
	 * to choose a team between the teams stored in newGameJSON
	 * @param newGameJSON Part of the game JSON that should be completed by client
	 * with the team chosen before returning it to server
	 */
	public void chooseTeamFromServer(JSONObject newGameJSON) {
		ChooseTeamFromServerDialog ctfsd = new ChooseTeamFromServerDialog(this, newGameJSON);
		int statusjs = ctfsd.open();
		if(statusjs == 1) {
			clientRolit.sendChosenTeamToServer(ctfsd.getSelectedTeamJSON());
			
		}
	}

	/**
	 * This method is used to resize an ImageIcon to the desired size
	 * @param icon ImageIcon to be resized
	 * @param resizedWidth The final width that the icon must have
	 * @param resizedHeight The final height that the icon must have
	 */
	private Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
	    Image img = icon.getImage();  
	    Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);  
	    return new ImageIcon(resizedImage);
	}

	/**
	 * onGameExited method overridden (RolitObserver interface).
	 * Particularly, when the game is exited, initGUI is called
	 */
	@Override
	public void onGameExited(Replay replay) {
		this.initGUI();
	}

}
