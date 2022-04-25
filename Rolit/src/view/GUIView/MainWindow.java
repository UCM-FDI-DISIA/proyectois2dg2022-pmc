package view.GUIView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.json.JSONObject;

import CPU.CPUPlayerView;
import CPU.PlayerView;
import client.Client;
import control.Controller;
import control.SaveLoadManager;
import logic.Color;
import logic.Rival;
import replay.Replay;
import replay.GameState;
import server.Server;
import utils.Pair;

// FIXME creo que no tiene mucho sentido que esta clase sea observadora
public class MainWindow extends JFrame implements RolitObserver, ActionListener {
	
	private static final long serialVersionUID = 1L;
	private static final String BUTTONS[] = { "NG", "LG", "DG", "LR", "CS", "JS" };
	
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
	private JFileChooser fileChooser;
	private JPanel centerPanel;
	private JPanel mainPanel;
	private JPanel boardPanel;
	private JPanel gamePanel;
	
	private volatile boolean onlineGameStarted = false;	

	public MainWindow(Controller ctrl) {
		super("Rolit");		
		this.clientRolit = new Client(this);		
		this.ctrl = ctrl;
		this.ctrl.setClient(clientRolit);		
		initGUI();
	}
	
	private void initGUI() {
		
		//Panel de bienvenida
		welcomePanel = new JPanel(new FlowLayout());
		this.setContentPane(welcomePanel);
		
		//Botones		
		// Boton de juego nuevo
		createGameButton = new JButton("Create new game");
		createGameButton.setActionCommand(BUTTONS[0]);
		createGameButton.addActionListener(this);
		// Boton cargar juego
		loadGameButton = new JButton("Load game");
		loadGameButton.setActionCommand(BUTTONS[1]);
		loadGameButton.addActionListener(this);
		// Boton borrar juego
		deleteGameButton = new JButton("Delete game");
		deleteGameButton.setActionCommand(BUTTONS[2]);
		deleteGameButton.addActionListener(this);
		// Boton cargar replay
		loadReplayButton = new JButton("Load replay");
		loadReplayButton.setActionCommand(BUTTONS[3]);
		loadReplayButton.addActionListener(this);
		
		createServerButton = new JButton("Create Server");
		createServerButton.setActionCommand(BUTTONS[4]);
		createServerButton.addActionListener(this);
		
		joinServerButton = new JButton("Join Server");
		joinServerButton.setActionCommand(BUTTONS[5]);
		joinServerButton.addActionListener(this);

		// Colocar los botones en el panel
		welcomePanel.add(new JLabel("Choose an option"));
		welcomePanel.add(createGameButton);
		welcomePanel.add(loadGameButton);
		welcomePanel.add(deleteGameButton);
		welcomePanel.add(loadReplayButton);
		welcomePanel.add(createServerButton);
		welcomePanel.add(joinServerButton);
				
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {		
		switch(e.getActionCommand()) {
		case "NG":
			CreateGameDialog dialogNew = new CreateGameDialog(MainWindow.this, false, ctrl);
			int status1 = dialogNew.open();		
			if (status1 == 1) { // se ha presionado OK
				this.state = dialogNew.getState();
				List<Pair<Color, Pair<Boolean, Integer>>> data = dialogNew.getPlayersData();
				for(int i = 0; i < data.size(); i++) {	//TODO Cambiar para que sea como una clase Command y no tener que diferenciar casos
					if(data.get(i).getSecond().getFirst())
						new CPUPlayerView(data.get(i).getFirst(), this.ctrl, data.get(i).getSecond().getSecond());
					else
						new PlayerView(data.get(i).getFirst(), this.ctrl);
				}
				this.initGame();
			}
			else {
				//TODO Mostrar alg�n tipo de error
			}			
			break;
		case "LG":
			LoadGameDialog dialogLoad = new LoadGameDialog(MainWindow.this);
			int status2 = dialogLoad.open();
			if (status2 == 1) {
				File file = dialogLoad.getFile();
				state = ctrl.loadGame(file.getPath());
				initGame();			
			}
			break;
		case "DG":
			DeleteGameDialog dialogDelete = new DeleteGameDialog(MainWindow.this);
			dialogDelete.open();
			break;
		case "LR":
			fileChooser = new JFileChooser();
			int ret = fileChooser.showOpenDialog(loadGameButton);
			if (ret == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				replay = SaveLoadManager.loadReplay(file.getPath());
				initReplay();
			} 
			else {
				//TODO Mostrar algún mensaje
			}
			break;
		case "CS":
			CreateGameDialog dialogNewOnline = new CreateGameDialog(MainWindow.this, true, ctrl);
			int statuscs = dialogNewOnline.open();
			if(statuscs == 1) {
				JOptionPane.showMessageDialog(mainPanel,  "If a server is created, the full application will be blocked until a match is created.", "Warning", JOptionPane.WARNING_MESSAGE);
				new Server(dialogNewOnline.createJSONObjectGame());
			}
				
			break;
		case "JS":
			JoinServerDialog jsd = new JoinServerDialog(this);
			int statusjs = jsd.open();
			if(statusjs == 1) {
				ctrl.setOnlineMode(true);
				clientRolit.empezarPartida(jsd.getIp(), Integer.parseInt(jsd.getPort()));
				clientRolit.join(jsd.getPlayerName(), jsd.getPlayerColor());
			}
		}
	}
	
	private void initGame() {		
		ctrl.addObserver(this);
		// Por si acaso, para que siempre se limpie pantalla
		if (welcomePanel != null)
			this.remove(welcomePanel);
		
		mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
	
		gamePanel = new JPanel(new BorderLayout());	//Contiene el turnBar (arriba) y el boardPanel (abajo)
		boardPanel = new JPanel();
		
		BoardGUI tablero = new BoardGUI(ctrl);
		
		// TODO cambiar a ingles el cambiar tablero
		tablero.crearTablero(boardPanel);
		
		TurnAndRankingBar turnAndRankingBar = new TurnAndRankingBar(ctrl, state);
		
		gamePanel.add(turnAndRankingBar, BorderLayout.PAGE_START);
		gamePanel.add(boardPanel, BorderLayout.CENTER);
		
		this.setContentPane(mainPanel);
		mainPanel.add(new ControlPanel(ctrl), BorderLayout.PAGE_START);
		mainPanel.add(gamePanel, BorderLayout.CENTER);
		mainPanel.add(new StatusBar(ctrl),BorderLayout.PAGE_END);
		
		this.pack();
		this.setSize(new Dimension(this.getWidth() + 50, this.getHeight())); //Para que no se salga la lista de puntuaciones si los nombres son demasiado largos
		
		this.ctrl.startGame();
		
	}
	
	private void initReplay() {

		this.remove(welcomePanel);
		
		this.repaint();
		
		mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel); //FIXME No s� yo si as� es como se hacen las cosas
		
		centerPanel = new JPanel(new GridLayout(1, 2));
		boardPanel = new JPanel();
		boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.Y_AXIS));
		boardPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		gamePanel = new JPanel(new BorderLayout());	//Contiene el turnBar (arriba) y el boardPanel (abajo)
		//rankingPanel = new JPanel(new GridLayout());
		
		centerPanel.add(gamePanel);
		//centerPanel.add(rankingPanel);
		
		BoardGUI tablero = new BoardGUI(replay);
		tablero.crearTablero(boardPanel);
		
		//TurnBar turnBar = new TurnBar(game);
		
		//gamePanel.add(turnBar, BorderLayout.PAGE_START);
		gamePanel.add(boardPanel, BorderLayout.CENTER);

		this.setContentPane(mainPanel);
		mainPanel.add(new ControlPanel(replay), BorderLayout.PAGE_START);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(new StatusBar(replay),BorderLayout.PAGE_END);		
		
		this.pack();
		this.setSize(new Dimension(this.getWidth() + 50, this.getHeight())); //Para que no se salga la lista de puntuaciones si los nombres son demasiado largos
		
	}
	
	@Override
	public void onError(String err) {
		
	}

	@Override
	public void onFirstPlay(GameState state) {}
	
	@Override
	public void onTurnPlayed(GameState state) {
		this.state = state;
		this.revalidate();
		this.repaint();
		// FIXME dice Leo que hay que quitarlo
		this.pack();
	}

	public JSONObject getGameReport() {
		return state.report().getJSONObject("game");
	}

	public void updateGameFromServer(JSONObject JSONnewState) {
		if(!onlineGameStarted) {
			onlineGameStarted = true;
			state = ctrl.createGame(JSONnewState);
			initGame();
		}			
		ctrl.updateGameFromServer(JSONnewState);
	}

	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onRegister(GameState state) {
		this.state = state;
	}

	@Override
	public void onGameStatusChange(GameState state) {
		
	}

}
