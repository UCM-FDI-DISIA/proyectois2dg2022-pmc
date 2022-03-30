package view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.json.JSONArray;
import org.json.JSONObject;

import Builders.GameBuilder;
import Builders.GameClassicBuilder;
import Builders.GameTeamsBuilder;
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
	
	private ChoosePlayersDialog choosePlayersDialog;
	
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
		
		JPanel mainPanel = new JPanel();
		mainPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		String[] gameModes = {GameClassicBuilder.TYPE, GameTeamsBuilder.TYPE};
		gameModeCombo = new JComboBox<String>(gameModes);
		
		Shape[] shapes = Shape.values();
		shapesCombo = new JComboBox<Shape>(shapes);
		
		playersSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
		
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
				choosePlayersDialog = new ChoosePlayersDialog(parent, (int)playersSpinner.getValue());
				int status = choosePlayersDialog.open();
				if(status == 1) {
					//TODO Aquí va a haber que diferenciar el modo de juego
					List<Player> players = choosePlayersDialog.getPlayersList();
					Shape shape = (Shape) shapesCombo.getSelectedItem();
					Board board = new Board(shape);
					List<Cube> cubes = new ArrayList<>();	//Vacía, para pasársela al constructor de game
					Color currentColor = players.get(0).getColor();
					
					game = GameBuilder.createGame(createJSONObjectGame());
					status = 1;
					CreateGameDialog.this.setVisible(false);
				}
				else {
					//TODO Mostrar algún mensaje de error
				}
			}
			
		});
		
		buttonsPanel.add(gameModeCombo);
		buttonsPanel.add(shapesCombo);
		buttonsPanel.add(playersSpinner);
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		
		mainPanel.add(buttonsPanel);
		
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
	
	//TODO Revisar cuáles de los métodos siguientes se usan
	
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
	
	protected JSONObject createJSONObjectGame() {
		JSONObject o = new JSONObject();
		o.put("type", this.getGameMode());
		
		JSONObject boardJSONObject = new JSONObject();
		boardJSONObject.put("shape", this.getBoardShape().toString());
		boardJSONObject.put("cubes", new JSONArray());
		
		o.put("board", boardJSONObject);
		
		List<Player> players = choosePlayersDialog.getPlayersList();
		
		o.put("turn", players.get(0).getColor().toString());
		
		if (this.getGameMode().equals(GameClassicBuilder.TYPE)) {
			
			JSONArray playerJSONArray = new JSONArray();		 
			for (int i = 0; i < players.size(); ++i)
				playerJSONArray.put(players.get(i).report());
						
			o.put("players", playerJSONArray);
			
		}
		else if (this.getGameMode().equals(GameTeamsBuilder.TYPE)) {
			
			JSONArray teamsJSONArray = new JSONArray();
			
			//...
			
			o.put("teams", teamsJSONArray);
			
		}
		
			
		return o;
	}
}
