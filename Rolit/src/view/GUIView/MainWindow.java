package view.GUIView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONObject;

import client.ClientController;
import client.Client;
import commands.Command;
import control.SaveLoadManager;
import logic.Board;
import logic.Color;
import logic.Game;
import logic.GameTransfer;
import logic.Player;
import logic.Shape;
import replay.Replay;
import server.Server;

public class MainWindow extends JFrame implements RolitObserver, ActionListener {
	
	private Client clientRolit;
	private Controller ctrl;
	private Replay replay;
	private JPanel welcomePanel;
	private JButton createGameButton;
	private JButton loadGameButton;
	private JButton deleteGameButton;
	private JButton loadReplayButton;
	private JButton createServerButton;
	private JButton joinServerButton;
	private JFileChooser fileChooser;
	private JPanel mainPanel;
	private JPanel centerPanel;
	private JPanel boardPanel;
	private JPanel gamePanel;
	
	private boolean onlineMode = false;
	private static final String BUTTONS[] = { "NG", "LG", "DG", "LR" };

	public MainWindow(Client clientRolit) {
		super("Rolit");
		this.clientRolit = clientRolit;
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
		createServerButton.setActionCommand("Create Server");
		createServerButton.addActionListener(this);
		
		joinServerButton = new JButton("Join Server");
		joinServerButton.setActionCommand("Join Server");
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
			CreateGameDialog dialogNew = new CreateGameDialog(MainWindow.this);
			int status1 = dialogNew.open();		
			if (status1 == 1) { // se ha presionado OK
				// FIXME no puede haber un game aquí
				game = dialogNew.getNewGame();
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
				game = SaveLoadManager.loadGame(file.getPath());
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
		}
	}
	
	private void initGame() {

		if (welcomePanel != null)
			this.remove(welcomePanel);
		
		this.repaint();
		
		mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel); //FIXME No s� yo si as� es como se hacen las cosas
		
		centerPanel = new JPanel(new GridLayout(1, 1));
		gamePanel = new JPanel(new BorderLayout());	//Contiene el turnBar (arriba) y el boardPanel (abajo)
		boardPanel = new JPanel();
		
		centerPanel.add(gamePanel);
		
		BoardGUI tablero = new BoardGUI(gameTransfer);
		
		tablero.crearTablero(boardPanel);
		
		TurnAndRankingBar turnAndRankingBar = new TurnAndRankingBar(gameTransfer);
		
		//gamePanel.add(turnAndRankingBar, BorderLayout.PAGE_START);
		gamePanel.add(turnAndRankingBar, BorderLayout.PAGE_START);
		gamePanel.add(boardPanel, BorderLayout.CENTER);
		
		this.setContentPane(mainPanel);
		mainPanel.add(new ControlPanel(gameTransfer), BorderLayout.PAGE_START);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(new StatusBar(gameTransfer),BorderLayout.PAGE_END);
		
		
		gameTransfer.onFirstPlay();
		
		this.pack();
		this.setSize(new Dimension(this.getWidth() + 50, this.getHeight())); //Para que no se salga la lista de puntuaciones si los nombres son demasiado largos

		
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
	public void onCommandIntroduced(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(State status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameFinished() {
		// TODO Completar
		
	}

	@Override
	public void onTurnPlayed(String name, Color color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameStatusChange(State status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFirstPlay(String name, Color color) {
		// TODO Auto-generated method stub
		
	}

	public JSONObject getGameReport() {
		return gameTransfer.getGameReport();
		
	}

	public void updateGameFromServer(JSONObject JSONJuegoNuevo) {
		if (gameTransfer == null) {
			gameTransfer = new GameTransfer(clientRolit, onlineMode);
			gameTransfer.updateGameFromServer(JSONJuegoNuevo);
			initGame();
		}
		else
			gameTransfer.updateGameFromServer(JSONJuegoNuevo);
			
		
		
	}


}
