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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import commands.Command;
import control.Controller;
import control.SaveLoadManager;
import logic.Board;
import logic.Color;
import logic.Game;
import logic.Shape;
import replay.Replay;

public class MainWindow extends JFrame implements RolitObserver, ActionListener {

	private Game game;
	private Replay replay;
	private Shape boardShape;
	private JPanel welcomePanel;
	private JButton createGameButton;
	private JButton loadGameButton;
	private JButton loadReplayButton;
	private JFileChooser fileChooser;
	private JPanel mainPanel;
	private JPanel centerPanel;
	private JPanel boardPanel;
	private JPanel gamePanel;
	private JPanel rankingPanel;

	public MainWindow() {
		super("Rolit");
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
		loadReplayButton = new JButton("Load replay");
		loadReplayButton.setActionCommand("Load replay");
		loadReplayButton.addActionListener(this);

		welcomePanel.add(new JLabel("Choose an option"));
		welcomePanel.add(createGameButton);
		welcomePanel.add(loadGameButton);
		welcomePanel.add(loadReplayButton);
		
		
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	public void initGame() {

		this.remove(welcomePanel);
		
		this.repaint();
		
		mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel); //FIXME No s� yo si as� es como se hacen las cosas
		
		centerPanel = new JPanel(new GridLayout(1, 2));
		boardPanel = new JPanel();
		gamePanel = new JPanel(new BorderLayout());	//Contiene el turnBar (arriba) y el boardPanel (abajo)
		//rankingPanel = new JPanel(new GridLayout());
		
		centerPanel.add(gamePanel);
		//centerPanel.add(rankingPanel);
		
		BoardGUI tablero = new BoardGUI(game);
		tablero.crearTablero(boardPanel);
		
		TurnBar turnBar = new TurnBar(game);
		
		gamePanel.add(turnBar, BorderLayout.PAGE_START);
		gamePanel.add(boardPanel, BorderLayout.CENTER);
		
		this.setContentPane(mainPanel);
		mainPanel.add(new ControlPanel(game), BorderLayout.PAGE_START);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(new StatusBar(game),BorderLayout.PAGE_END);
		
		
		
		this.pack();
		
	}
	
	public void initReplay() {

		this.remove(welcomePanel);
		
		this.repaint();
		
		mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel); //FIXME No s� yo si as� es como se hacen las cosas
		
		centerPanel = new JPanel(new GridLayout(1, 2));
		boardPanel = new JPanel();
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
		//mainPanel.add(new StatusBar(game),BorderLayout.PAGE_END);
		
		
		
		this.pack();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Create new game")) {
			CreateGameDialog dialog = new CreateGameDialog(MainWindow.this);
			int status = dialog.open();
			
			if (status == 1) { //e.d. se ha presionado OK
				game = dialog.getNewGame();
				initGame();
			}
			else {
				//TODO Mostrar alg�n tipo de error
			}
		}
		else if(e.getActionCommand().equals("Load game")) {
			fileChooser = new JFileChooser();
			int ret = fileChooser.showOpenDialog(loadGameButton);
			if (ret == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				game = SaveLoadManager.loadGame(file.getPath());
				initGame();
			} 
			else {
				//TODO Mostrar algún mensaje
			}
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

}
