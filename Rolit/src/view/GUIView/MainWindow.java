package view.GUIView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import org.json.JSONObject;

import client.Client;
import control.Controller;
import control.SaveLoadManager;
import logic.Rival;
import replay.Replay;
import replay.GameState;
import server.Server;
import view.GUIView.RolitComponents.RolitButton;
import view.GUIView.createGame.CreateGameDialog;
import view.GUIView.createGame.CreateGameWithPlayersDialog;

// FIXME creo que no tiene mucho sentido que esta clase sea observadora
public class MainWindow extends JFrame implements RolitObserver, ActionListener {
	
	private static final long serialVersionUID = 1L;
	private static final String BUTTONS[] = { "NG", "LG", "DG", "LR", "CS", "JS" };
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
	private JFileChooser fileChooser;
	private JPanel mainPanel;
	private JPanel centerPanel;
	private JPanel gamePanel;
	private JLabel rolitLogo;
	private JLabel optionMessage;
	
	private volatile boolean onlineGameStarted = false;	
	
	public MainWindow(Controller ctrl) {
		super("Rolit");		
		this.clientRolit = new Client(this);		
		this.ctrl = ctrl;
		this.ctrl.setClient(clientRolit);		
		initGUI();
	}
	
	private void initGUI() {
		//Icono de la ventana
		this.setIconImage(new ImageIcon(ICONS_PATH + "\\rolitIcon.png").getImage());
		
		//Panel de bienvenida
		welcomePanel = new JPanel();
		welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
		welcomePanel.setBackground(Color.WHITE);
		this.setContentPane(welcomePanel);
		
		//Logo
		rolitLogo = new JLabel(resizeIcon(new ImageIcon(ICONS_PATH + "\\logoRolit.png"), 400, 150));
		rolitLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
		welcomePanel.add(rolitLogo);
		
		//Botones
		
		// Boton de juego nuevo
		createGameButton = new RolitButton("Create game");
		createGameButton.setActionCommand(BUTTONS[0]);
		createGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		createGameButton.addActionListener(this);
		// Boton cargar juego
		loadGameButton = new RolitButton("Load game");
		loadGameButton.setActionCommand(BUTTONS[1]);
		loadGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		loadGameButton.addActionListener(this);
		// Boton borrar juego
		deleteGameButton = new RolitButton("Delete game");
		deleteGameButton.setActionCommand(BUTTONS[2]);
		deleteGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		deleteGameButton.addActionListener(this);
		// Boton cargar replay
		loadReplayButton = new RolitButton("Load replay");
		loadReplayButton.setActionCommand(BUTTONS[3]);
		loadReplayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		loadReplayButton.addActionListener(this);
		// Boton cargar server
		createServerButton = new RolitButton("Create Server");
		createServerButton.setActionCommand(BUTTONS[4]);
		createServerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		createServerButton.addActionListener(this);
		// Boton unirse a server
		joinServerButton = new RolitButton("Join Server");
		joinServerButton.setActionCommand(BUTTONS[5]);
		joinServerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		joinServerButton.addActionListener(this);
		
		//Labels
		optionMessage = new JLabel("Choose an option:");
		optionMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//Separador del final (para que haya un hueco entre el ultimo compente y el final del jframe)
		Box.Filler glue = (Filler) Box.createVerticalGlue();
	    glue.changeShape(glue.getMinimumSize(),  new Dimension(0, 10), glue.getMaximumSize());

				
		// Colocar los botones en el panel
	  
		welcomePanel.add(optionMessage);
		welcomePanel.add(Box.createRigidArea(new Dimension(1, 5))); //hueco en blanco
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
		
		
	    welcomePanel.add(glue);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {		
		switch(e.getActionCommand()) {
		case "NG":
			CreateGameDialog dialogNew = new CreateGameWithPlayersDialog(MainWindow.this, ctrl);
			int status1 = dialogNew.open();		
			if (status1 == 1) { // se ha presionado OK
				this.state = dialogNew.getState();
				ctrl.createGame(dialogNew.createJSONObjectGame());
				ctrl.startGame();
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
			CreateGameDialog dialogCS = new CreateGameDialog(MainWindow.this, ctrl);
			int statuscs = dialogCS.open();
			if(statuscs == 1) {
				new Server(dialogCS.createJSONObjectGame());
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
			break;
		}
	}
	
	private void initGame() {		
		ctrl.addObserver(this);
		// Por si acaso, para que siempre se limpie pantalla
		if (welcomePanel != null)
			this.remove(welcomePanel);
		
		this.repaint();

		mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
	
		gamePanel = new JPanel(new BorderLayout());	//Contiene el turnBar (arriba) y el boardPanel (abajo)
		//centerPanel.add(gamePanel);
		
		BoardGUI tablero = new BoardGUI(ctrl);
		
		TurnAndRankingBar turnAndRankingBar = new TurnAndRankingBar(ctrl, state);
		
		gamePanel.add(turnAndRankingBar, BorderLayout.PAGE_START);
		gamePanel.add(tablero, BorderLayout.CENTER);
		
		this.setContentPane(mainPanel);
		mainPanel.add(new ControlPanel(ctrl), BorderLayout.PAGE_START);
		mainPanel.add(gamePanel, BorderLayout.CENTER);
		mainPanel.add(new StatusBar(ctrl), BorderLayout.PAGE_END);
						
		this.pack();
		this.setSize(new Dimension(this.getWidth() + 50, this.getHeight())); //Para que no se salga la lista de puntuaciones si los nombres son demasiado largos
		this.setLocationRelativeTo(null);
		
	}
	
	private void initReplay() {

		this.remove(welcomePanel);
		
		this.repaint();
		
		mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel); //FIXME No s� yo si as� es como se hacen las cosas
		
		centerPanel = new JPanel(new GridLayout(1, 2));
		
		gamePanel = new JPanel(new BorderLayout());	//Contiene el turnBar (arriba) y el boardPanel (abajo)
		//rankingPanel = new JPanel(new GridLayout());
		
		centerPanel.add(gamePanel);
		//centerPanel.add(rankingPanel);
		
		BoardGUI tablero = new BoardGUI(replay);		
		//TurnBar turnBar = new TurnBar(game);
		
		//gamePanel.add(turnBar, BorderLayout.PAGE_START);
		gamePanel.add(tablero, BorderLayout.CENTER);

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
	
	public class WaitWorker extends SwingWorker<Void, Void> {

		// Método obligatorio
		@Override
		protected Void doInBackground() {
			try {
				wait(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	public void chooseTeamFromServer(JSONObject JSONJuegoNuevo) {
		ChooseTeamFromServerDialog ctfsd = new ChooseTeamFromServerDialog(this, JSONJuegoNuevo);
		int statusjs = ctfsd.open();
		if(statusjs == 1) {
			clientRolit.sendChosenTeamToServer(ctfsd.getSelectedTeamJSON());
			
		}
	}

	
	private Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
	    Image img = icon.getImage();  
	    Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);  
	    return new ImageIcon(resizedImage);
	}

	@Override
	public void onGameExited(Replay replay) {
		this.initGUI();
	}

}
