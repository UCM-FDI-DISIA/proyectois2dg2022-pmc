package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import commands.Command;
import control.Controller;
import logic.Board;
import logic.Game;
import logic.Shape;

public class MainWindow extends JFrame implements RolitObserver, ActionListener {

	private Game game;
	private JPanel welcomePanel;
	private JButton createGameButton;
	private JButton loadGameButton;
	private JButton loadReplayButton;
	JPanel mainPanel;
	JPanel boardPanel;

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
		this.setContentPane(mainPanel); //FIXME No sé yo si así es como se hacen las cosas
		
		
		boardPanel = new JPanel(new BorderLayout());
		
		BoardGUI tablero = new BoardGUI(game);
		tablero.crearTablero(boardPanel);
		//boardPanel.add(new JLabel("Estadï¿½sticas: "), BorderLayout.SOUTH);
		
		this.setContentPane(mainPanel);
		mainPanel.add(new ControlPanel(game), BorderLayout.PAGE_START);
		mainPanel.add(boardPanel, BorderLayout.CENTER);
		mainPanel.add(new StatusBar(game),BorderLayout.PAGE_END);
		
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
				//TODO Mostrar algún tipo de error
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
	public void onGameCreated(Game game, Board board) {
		
		
	}

	@Override
	public void onTurnPlayed(Game game, Board board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayLeftButton(Game game, Board board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayRightButton(Game game, Board board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBoardCreated(Board board) {
		
		
		
	}



}
