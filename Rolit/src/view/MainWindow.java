package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.json.JSONObject;

import client.ClientController;
import client.Client;
import commands.Command;
import control.Controller;
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
	private GameTransfer gameTransfer;
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

	public MainWindow(Client clientRolit) {
		super("Rolit");
		this.clientRolit = clientRolit;
		initGUI();
	}
	
	private void initGUI() {
		
		//Panel de bienvenida
		welcomePanel = new JPanel();
		this.setContentPane(welcomePanel);
		
		//Botones
		
		createGameButton = new JButton("Create new game");
		createGameButton.setActionCommand("Create new game");
		createGameButton.addActionListener(this);
		
		loadGameButton = new JButton("Load game");
		loadGameButton.setActionCommand("Load game");
		loadGameButton.addActionListener(this);
		
		deleteGameButton = new JButton("Delete game");
		deleteGameButton.setActionCommand("Delete game");
		deleteGameButton.addActionListener(this);
		
		loadReplayButton = new JButton("Load replay");
		loadReplayButton.setActionCommand("Load replay");
		loadReplayButton.addActionListener(this);
		
		createServerButton = new JButton("Create Server");
		createServerButton.setActionCommand("Create Server");
		createServerButton.addActionListener(this);
		
		joinServerButton = new JButton("Join Server");
		joinServerButton.setActionCommand("Join Server");
		joinServerButton.addActionListener(this);

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

	public void initGame() {
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
	
	public void initReplay() {

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
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Create new game")) {
			CreateGameDialog dialog = new CreateGameDialog(MainWindow.this, clientRolit, false);
			int status = dialog.open();
			
			if (status == 1) { //e.d. se ha presionado OK
				gameTransfer = dialog.getNewGame(); //FIXME creo que esto está mal
				initGame();
			}
			else {
				//TODO Mostrar alg�n tipo de error
			}
		}
		else if(e.getActionCommand().equals("Load game")) {
			LoadGameDialog dialog = new LoadGameDialog(MainWindow.this);
			int status = dialog.open();
			if (status == 1) {
				File file = dialog.getFile();
				gameTransfer.loadGame(file.getPath());
				initGame();
				
			}
			
		}
		else if(e.getActionCommand().equals("Delete game")) {
			DeleteGameDialog dialog = new DeleteGameDialog(MainWindow.this);
			dialog.open();
			
		}
		else if(e.getActionCommand().contentEquals("Load replay")) {
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
		else if(e.getActionCommand().equals("Create Server")) {
			
			CreateGameDialog dialog = new CreateGameDialog(MainWindow.this, clientRolit, true);
			int status = dialog.open();
			
			if (status == 1) { //e.d. se ha presionado OK
				JSONObject json = dialog.createJSONObjectGame();
				new Server(json);
			}
			
			
		}
		else if(e.getActionCommand().equals("Join Server")) {
			JoinServerDialog dialog = new JoinServerDialog(MainWindow.this);
			int status = dialog.open();
			if (status == 1) {
				onlineMode = true;
				clientRolit.empezarPartida(dialog.getIp(), Integer.parseInt(dialog.getPort()));
				clientRolit.setPlayer(new Player(dialog.getPlayerColor(), dialog.getPlayerName()));
				
			}
		}
	}
	
	@Override
	public void onCommandIntroduced(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(Game game, Board board, Command command) {
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
	public void onGameStatusChange(String msg) {
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
