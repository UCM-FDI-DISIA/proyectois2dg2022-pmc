package view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.json.JSONArray;
import org.json.JSONObject;

import Builders.GameBuilder;
import Builders.GameClassicBuilder;
import Builders.GameTeamsBuilder;
import client.Client;
import commands.Command;
import logic.Board;
import logic.Color;
import logic.Cube;
import logic.Game;
import logic.GameTransfer;
import logic.Player;
import logic.Shape;
import server.Server;
import utils.Pair;

public class CreateGameDialog extends JDialog {
	
	private GameTransfer gameTransfer;
	private Frame parent;
	private int status;
	
	private Map<String, ImageIcon> colorMap;
	private Map<String, ImageIcon> boardMap;
	
	private JComboBox<Shape> shapesCombo;
	private JComboBox<String> gameModeCombo;
	private JSpinner playersSpinner;
	private JSpinner teamsSpinner;
	private JLabel numberOfTeamsLabel;
	
	private JPanel mainPanel;
	private JPanel classicPanel;
	private JPanel teamsPanel;
	private JPanel nameTeamsMainPanel;

	
	private List<JPanel> listPlayerPanels;
	private List<JTextArea> listPlayerTextAreas;
	
	private List<JComboBox<Color>> listPlayerComboColors;
	
	private List<JComboBox<Integer>> listComboNumberTeams;
	private List<JComboBox<String>> listComboNamesTeams;
	
	private List<JPanel> listNameTeamsPanel;
	private List<JTextArea> listTextAreasTeamsPanels;
	
	private JLabel errorLabel;
	
	private final int MAX_TEXT_LENGTH = 15;
	
	private boolean onlineMode;
	
	public CreateGameDialog(Frame parent, Client clientRolit, boolean onlineMode) {
		super(parent, true);
		this.parent = parent;
		this.onlineMode = onlineMode;
		this.gameTransfer = new GameTransfer(clientRolit);
		initGUI();
	}
	
	public void initGUI () {
		
		this.setLocation(50, 50);
		this.setSize(700, 200);
		
		colorMap = new HashMap<String, ImageIcon>();
		boardMap = new HashMap<String, ImageIcon>();

		
		setTitle("Create Game");
		setVisible(false);
		
		mainPanel = new JPanel();
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		String[] gameModes = {GameClassicBuilder.TYPE, GameTeamsBuilder.TYPE};
		gameModeCombo = new JComboBox<String>(gameModes);
		
		Shape[] shapes = Shape.values();
		shapesCombo = new JComboBox<Shape>(shapes);
		shapesCombo.setRenderer(new BoardRenderer());
		
		playersSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 10, 1));
		playersSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (getGameMode().equals(GameClassicBuilder.TYPE)) {
					turnToClassicGameMode(); //esto es para que se actualicen las componentes
					
				}
				if (getGameMode().equals(GameTeamsBuilder.TYPE)) {
					turnToTeamsGameMode(); //esto es para que se actualicen las componentes
					
				}
			}
			
		});
		
		
		//FIXME bloqueamos el jspinner porque por ahora sólo pueden jugar dos equipos
		teamsSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 10, 0)); //step 0 para bloquear
		((JSpinner.DefaultEditor) teamsSpinner.getEditor()).getTextField().setEditable(false);
		
		teamsSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (getGameMode().equals(GameClassicBuilder.TYPE)) {
					turnToClassicGameMode(); //esto es para que se actualicen las componentes
					
				}
				if (getGameMode().equals(GameTeamsBuilder.TYPE)) {
					turnToTeamsGameMode(); //esto es para que se actualicen las componentes
					
				}
			}
			
		});
		
		teamsSpinner.setVisible(false);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				status = 0;
				CreateGameDialog.this.setVisible(false);
			}
			
		});
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				Pair<Boolean, String> pair = checkIfCorrectArguments(); 
				if (onlineMode) {
					status = 1;
					CreateGameDialog.this.setVisible(false);
					
				}
				else if (pair.getFirst()) {
					status = 1;
					gameTransfer.createGame(createJSONObjectGame());
					CreateGameDialog.this.setVisible(false);	
					
				}
				else {
					
					if (errorLabel != null)
						remove(errorLabel);
					
					errorLabel = new JLabel(pair.getSecond());
					add(errorLabel);
					repaint();
					validate();
					pack();
				}
				
			}
			
		});
		
		buttonsPanel.add(gameModeCombo);
		buttonsPanel.add(shapesCombo);
		buttonsPanel.add(new JLabel("Number of players: "));
		buttonsPanel.add(playersSpinner);
		
		numberOfTeamsLabel = new JLabel("Number of teams: ");
		numberOfTeamsLabel.setVisible(false);
		buttonsPanel.add(numberOfTeamsLabel);
		buttonsPanel.add(teamsSpinner);
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		
		
		mainPanel.add(buttonsPanel);
		
		this.buildClassicPanel();
		//this.buildTeamsPanel();
		mainPanel.add(classicPanel); //por defecto se muestra para elegir jugadores en el modo GameClassic
		
		
		gameModeCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (getGameMode().equals(GameClassicBuilder.TYPE)) {
					turnToClassicGameMode();
				}
				
				if (getGameMode().equals(GameTeamsBuilder.TYPE)) {
					turnToTeamsGameMode();
				}
				
			}
		});
		
		setContentPane(mainPanel);
		setMinimumSize(new Dimension(100, 100));
		
		this.pack();
		
		//TODO Antes de pasar a crear el Game hay que crear los players
		//Hay que pedir los nombres y colores para crearlos
	}
	
	public int open() {
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		setVisible(true);
		return status;
	}
	
	//TODO Revisar cu�les de los m�todos siguientes se usan
	
	public String getGameMode() {
		return (String) gameModeCombo.getSelectedItem();
	}
	
	public Shape getBoardShape() {
		return (Shape) shapesCombo.getSelectedItem();
	}

	public int getNumPlayers() {
		return (int) playersSpinner.getValue();
	}
	
	public GameTransfer getNewGame() {
		return this.gameTransfer;
	}
	
	public Shape getNewShape() {
		return this.getBoardShape();
	}
	
	protected JSONObject createJSONObjectGame() {
		JSONObject o = new JSONObject();
		o.put("type", this.getGameMode());
		
		JSONObject boardJSONObject = new JSONObject();
		boardJSONObject.put("shape", this.getBoardShape().toString());
		boardJSONObject.put("cubes", new JSONArray());
		
		o.put("board", boardJSONObject);
		
		
		JSONArray playerJSONArray = new JSONArray();		
		
		if (!onlineMode) {
			for (int i = 0; i < listPlayerComboColors.size(); ++i) {
				Player playerAux = new Player((Color) listPlayerComboColors.get(i).getSelectedItem(), (String) listPlayerTextAreas.get(i).getText());
				if (i == 0)
					o.put("turn", playerAux.getColor().toString());
				playerJSONArray.put(playerAux.report());
				
			}
			
			o.put("players", playerJSONArray);
		}
		
					
		
		if (!onlineMode && this.getGameMode().equals(GameTeamsBuilder.TYPE)) {
			
			JSONArray teamsJSONArray = new JSONArray();
			
			for (int i = 0; i < listTextAreasTeamsPanels.size(); ++i) {
				JSONObject aux = new JSONObject();
				aux.put("name", listTextAreasTeamsPanels.get(i).getText());
				aux.put("players", new JSONArray());
				teamsJSONArray.put(aux);
			}
			
			for(int i = 0; i < (int)playersSpinner.getValue(); i++) {
				Player playerAux = new Player((Color) listPlayerComboColors.get(i).getSelectedItem(), (String) listPlayerTextAreas.get(i).getText());
				if (i == 0)
					o.put("turn", playerAux.getColor().toString());
				JSONObject JSONObjectaux = (JSONObject) teamsJSONArray.get((int)listComboNumberTeams.get(i).getSelectedItem()-1);
				JSONObjectaux.getJSONArray("players").put(playerAux.report());
			}
			
			o.put("teams", teamsJSONArray);
			
		}
		
			
		return o;
	}
	
	private void buildClassicPanel() {
		
		classicPanel = new JPanel();
		
		classicPanel.setLayout(new BoxLayout(classicPanel, BoxLayout.Y_AXIS));
		classicPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		listPlayerPanels = new ArrayList<>();
		listPlayerTextAreas = new ArrayList<>();
		listPlayerComboColors = new ArrayList<>();
		
		for(int i = 0; i < (int)playersSpinner.getValue(); i++) {
			listPlayerPanels.add(new JPanel());
			JTextArea auxTextArea = new JTextArea();
			auxTextArea.setDocument(new PlainDocument() {
			    @Override
			    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException{
			        if (str == null || auxTextArea.getText().length() >= MAX_TEXT_LENGTH) {
			            return;
			        }
			 
			        super.insertString(offs, str, a);
			    }
			});
			listPlayerTextAreas.add(auxTextArea);
			listPlayerTextAreas.get(listPlayerTextAreas.size() - 1).setEditable(true);
			listPlayerTextAreas.get(listPlayerTextAreas.size() - 1).setLineWrap(true);
			listPlayerTextAreas.get(listPlayerTextAreas.size() - 1).setWrapStyleWord(true);
			JComboBox<Color> aux = new JComboBox<Color>();
			aux.setRenderer(new ColorRenderer());
			listPlayerComboColors.add(aux);
			listPlayerPanels.get(i).add(new JLabel(String.format("Player %d: ", i + 1)));
			listPlayerPanels.get(i).add(new JLabel("Name: "));
			listPlayerPanels.get(i).add(listPlayerTextAreas.get(i));
			listPlayerPanels.get(i).add(new JLabel("Color: "));
			for(Color color : Color.values()) {
				listPlayerComboColors.get(i).addItem(color);
			}
			listPlayerPanels.get(i).add(listPlayerComboColors.get(i));
		}
		
		for(JPanel panel : listPlayerPanels) {
			classicPanel.add(panel);
		}
		
		JPanel buttonsPanel = new JPanel();
		classicPanel.add(buttonsPanel);
		
		classicPanel.setVisible(!onlineMode);
	
		
	}
	
	private void buildTeamsPanel() {
		
		teamsPanel = new JPanel();
		
		teamsPanel.setLayout(new BoxLayout(teamsPanel, BoxLayout.Y_AXIS));
		teamsPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		listPlayerPanels = new ArrayList<>();
		listPlayerTextAreas = new ArrayList<>();
		listPlayerComboColors = new ArrayList<>();
		
		listComboNumberTeams = new ArrayList<>();
		
		listNameTeamsPanel = new ArrayList<>();
		
		nameTeamsMainPanel = new JPanel();
		nameTeamsMainPanel.setLayout(new BoxLayout(nameTeamsMainPanel, BoxLayout.Y_AXIS));
		
		listTextAreasTeamsPanels = new ArrayList<>();
		listComboNamesTeams = new ArrayList<>();
		
		for (int i = 0; i < (int)teamsSpinner.getValue(); i++) {
			listNameTeamsPanel.add(new JPanel());
			JTextArea auxTextArea = new JTextArea();
			auxTextArea.setDocument(new PlainDocument() {
			    @Override
			    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException{
			        if (str == null || auxTextArea.getText().length() >= MAX_TEXT_LENGTH) {
			            return;
			        }
			 
			        super.insertString(offs, str, a);
			    }
			});
			listTextAreasTeamsPanels.add(auxTextArea);
			listTextAreasTeamsPanels.get(listTextAreasTeamsPanels.size() - 1).setEditable(true);
			listTextAreasTeamsPanels.get(listTextAreasTeamsPanels.size() - 1).setLineWrap(true);
			listTextAreasTeamsPanels.get(listTextAreasTeamsPanels.size() - 1).setWrapStyleWord(true);
			listComboNamesTeams.add(new JComboBox<String>());
			listNameTeamsPanel.get(i).add(new JLabel(String.format("Team %d: ", i + 1)));
			listNameTeamsPanel.get(i).add(new JLabel("Name: "));
			listNameTeamsPanel.get(i).add(listTextAreasTeamsPanels.get(i));
			
		}
		
		for(int i = 0; i < (int)playersSpinner.getValue(); i++) {
			listPlayerPanels.add(new JPanel());
			listPlayerTextAreas.add(new JTextArea());
			listPlayerTextAreas.get(listPlayerTextAreas.size() - 1).setEditable(true);
			listPlayerTextAreas.get(listPlayerTextAreas.size() - 1).setLineWrap(true);
			listPlayerTextAreas.get(listPlayerTextAreas.size() - 1).setWrapStyleWord(true);
			
			listPlayerPanels.get(i).add(new JLabel(String.format("Player %d: ", i + 1)));
			listPlayerPanels.get(i).add(new JLabel("Name: "));
			listPlayerPanels.get(i).add(listPlayerTextAreas.get(i));
			JComboBox<Color> aux = new JComboBox<Color>();
			aux.setRenderer(new ColorRenderer());
			listPlayerComboColors.add(aux);
			listPlayerPanels.get(i).add(new JLabel("Color: "));
			for(Color color : Color.values()) {
				listPlayerComboColors.get(i).addItem(color);
			}
			listPlayerPanels.get(i).add(listPlayerComboColors.get(i));
			
			
			listComboNumberTeams.add(new JComboBox<Integer>());
			
			listPlayerPanels.get(i).add(new JLabel("Team: "));
			for (int j = 0; j < (int)teamsSpinner.getValue(); j++)
					listComboNumberTeams.get(i).addItem((Integer)j+1);
			listPlayerPanels.get(i).add(listComboNumberTeams.get(i));
			}

		
		for(JPanel panel : listNameTeamsPanel) {
			teamsPanel.add(panel);
		}
		for(JPanel panel : listPlayerPanels) {
			teamsPanel.add(panel);
		}
		
		JPanel buttonsPanel = new JPanel();
		teamsPanel.add(buttonsPanel);
	
		
	}
	

	private void turnToClassicGameMode() {
		if (classicPanel.getParent() == mainPanel)
			mainPanel.remove(classicPanel);
		else if (teamsPanel.getParent() == mainPanel) {
			mainPanel.remove(teamsPanel);
			numberOfTeamsLabel.setVisible(false);
			teamsSpinner.setVisible(false);
		}
			
		
		buildClassicPanel();
		mainPanel.add(classicPanel);
		classicPanel.setVisible(true && !onlineMode);
		mainPanel.repaint();
		
		if (errorLabel != null) //Queremos que si hay un mensaje de error, desaparezca
			remove(errorLabel);
			
		repaint();
		validate();
		pack();
		
	}
	
	private void turnToTeamsGameMode() {
		if (classicPanel.getParent() == mainPanel)
			mainPanel.remove(classicPanel);
		else if (teamsPanel.getParent() == mainPanel)
			mainPanel.remove(teamsPanel);
		
		buildTeamsPanel();
		mainPanel.add(teamsPanel);
		teamsPanel.setVisible(true && !onlineMode);
		numberOfTeamsLabel.setVisible(true);
		teamsSpinner.setVisible(true);
		mainPanel.repaint();
		
		if (errorLabel != null) //Queremos que si hay un mensaje de error, desaparezca
			remove(errorLabel);
			
		repaint();
		validate();
		pack();
	}
	
	private Pair<Boolean, String> checkIfCorrectArguments() {
		HashSet<String> playerNames = new HashSet<String>();
		HashSet<Color> playerColors = new HashSet<Color>();
		
		Pair<Boolean, String> pair;
		
		
		for(int i = 0; i < (int)playersSpinner.getValue(); i++) {
			playerNames.add((String) listPlayerTextAreas.get(i).getText());
			playerColors.add((Color) listPlayerComboColors.get(i).getSelectedItem());
		}
		
		if (playerNames.size() < (int)playersSpinner.getValue()) //e.d hay un elemento repetido
		{
			pair = new Pair<Boolean, String>(false, "ERROR: At least one player has a repeated name.");
			return pair;
		}
		if (playerColors.size() < (int)playersSpinner.getValue()) //e.d hay un elemento repetido
		{
			pair = new Pair<Boolean, String>(false, "ERROR: At least one player has a repeated color.");
			return pair;
		}
		 if (teamsPanel != null && teamsPanel.getParent() == mainPanel) { //e.d estamos en el modo por equipos

			 HashSet<String> teamNames = new HashSet<String>();
			 
			 for (int i = 0; i < listTextAreasTeamsPanels.size(); ++i)			 
				 teamNames.add(listTextAreasTeamsPanels.get(i).getText());
			 if (teamNames.size() < listTextAreasTeamsPanels.size()) //e.d hay un elemento repetido
			 {
				pair = new Pair<Boolean, String>(false, "ERROR: At least one team has a repeated name.");
				return pair;
			 }
			 
			 
			 //comprobamos que en todos los equipos hay por
			 //lo menos un jugador
			 
			 for (int i = 0; i < (int)teamsSpinner.getValue(); ++i) {
				 boolean encontrado = false;
				 for (int j = 0; j < (int)listComboNumberTeams.size() && !encontrado; ++j) {
					 if (i == (int)listComboNumberTeams.get(j).getSelectedItem()-1)
						 encontrado = true;
				 }
				 if (!encontrado)
				 {
					pair = new Pair<Boolean, String>(false, "ERROR: At least one team is empty.");
					return pair;
				}
			 }
		 }
		 
		pair = new Pair<Boolean, String>(true, "");
		return pair;

		
	}
}
