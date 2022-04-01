package view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
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

import org.json.JSONArray;
import org.json.JSONObject;

import Builders.GameBuilder;
import Builders.GameClassicBuilder;
import Builders.GameTeamsBuilder;
import commands.Command;
import logic.Board;
import logic.Color;
import logic.Cube;
import logic.Game;
import logic.Player;
import logic.Shape;

public class CreateGameDialog extends JDialog {
	
	private Game game;
	private Frame parent;
	private int status;
	
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
	
	public CreateGameDialog(Frame parent) {
		super(parent, true);
		this.parent = parent;
		initGUI();
	}
	
	public void initGUI () {
		
		this.setLocation(50, 50);
		this.setSize(700, 200);
		
		setTitle("Create Game");
		setVisible(false);
		
		mainPanel = new JPanel();
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		String[] gameModes = {GameClassicBuilder.TYPE, GameTeamsBuilder.TYPE};
		gameModeCombo = new JComboBox<String>(gameModes);
		
		Shape[] shapes = Shape.values();
		shapesCombo = new JComboBox<Shape>(shapes);
		
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
		
		teamsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
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
				
				game = GameBuilder.createGame(createJSONObjectGame());
				status = 1;
				CreateGameDialog.this.setVisible(false);
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
	
	public Game getNewGame() {
		return this.game;
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
		
		for (int i = 0; i < listPlayerComboColors.size(); ++i) {
			Player playerAux = new Player((Color) listPlayerComboColors.get(i).getSelectedItem(), (String) listPlayerTextAreas.get(i).getText());
			if (i == 0)
				o.put("turn", playerAux.getColor().toString());
			playerJSONArray.put(playerAux.report());
			
		}
					
		o.put("players", playerJSONArray);
		
		if (this.getGameMode().equals(GameTeamsBuilder.TYPE)) {
			
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
			listPlayerTextAreas.add(new JTextArea());
			listPlayerTextAreas.get(listPlayerTextAreas.size() - 1).setEditable(true);
			listPlayerTextAreas.get(listPlayerTextAreas.size() - 1).setLineWrap(true);
			listPlayerTextAreas.get(listPlayerTextAreas.size() - 1).setWrapStyleWord(true);
			listPlayerComboColors.add(new JComboBox<Color>());
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
			listTextAreasTeamsPanels.add(new JTextArea());
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
			
			listPlayerComboColors.add(new JComboBox<Color>());
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
		classicPanel.setVisible(true);
		mainPanel.repaint();
		pack();
		
	}
	
	private void turnToTeamsGameMode() {
		if (classicPanel.getParent() == mainPanel)
			mainPanel.remove(classicPanel);
		else if (teamsPanel.getParent() == mainPanel)
			mainPanel.remove(teamsPanel);
		
		buildTeamsPanel();
		mainPanel.add(teamsPanel);
		teamsPanel.setVisible(true);
		numberOfTeamsLabel.setVisible(true);
		teamsSpinner.setVisible(true);
		mainPanel.repaint();
		pack();
	}
}
