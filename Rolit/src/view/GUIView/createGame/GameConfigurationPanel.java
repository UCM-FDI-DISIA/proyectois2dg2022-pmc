package view.GUIView.createGame;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Builders.GameClassicBuilder;
import Builders.GameTeamsBuilder;
import logic.Shape;
import utils.Pair;
import view.GUIView.BoardRenderer;
import view.GUIView.RolitComponents.RolitButton;
import view.GUIView.RolitComponents.RolitComboBox;
import view.GUIView.RolitComponents.RolitPanel;

public class GameConfigurationPanel extends RolitPanel {

	private static final long serialVersionUID = 1L;
	
	private JComboBox<Shape> shapesCombo;
	private JComboBox<String> gameModeCombo;
	private JSpinner playersSpinner;
	private JSpinner teamsSpinner;
	private JLabel numberOfTeamsLabel;
	private JLabel numberOfPlayersLabel;
	private JLabel shapeLabel;
	private JButton cancelButton;
	private JButton okButton;
	private boolean isGameMode;
	
	private void initGUI() {
		this.setLayout(new FlowLayout());
		this.setMinimumSize(new Dimension(700, 20));
		this.setAlignmentX(CENTER_ALIGNMENT);
		
		String[] gameModes = {GameClassicBuilder.TYPE, GameTeamsBuilder.TYPE};

		gameModeCombo = new RolitComboBox<String>(gameModes);
		
		Shape[] shapes = Shape.values();
		shapesCombo = new RolitComboBox<Shape>(shapes);
		shapesCombo.setRenderer(new BoardRenderer());
		
		playersSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 10, 1));
		
		
		teamsSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 10, 0)); //step 0 para bloquear
		//Bloqueamos el jspinner porque por ahora sólo pueden jugar dos equipos
		((JSpinner.DefaultEditor) teamsSpinner.getEditor()).getTextField().setEditable(false);
		teamsSpinner.setVisible(false);
		
		numberOfPlayersLabel = new JLabel("Number of players: ");
		numberOfTeamsLabel = new JLabel("Number of teams: ");
		shapeLabel = new JLabel("Shape: ");
		numberOfTeamsLabel.setVisible(false);
		
		cancelButton = new RolitButton("Cancel", 1);
		okButton = new RolitButton("OK", 1);
		
		this.add(gameModeCombo);
		this.add(shapeLabel);
		this.add(shapesCombo);
		this.add(numberOfPlayersLabel);
		this.add(playersSpinner);
		this.add(numberOfTeamsLabel);
		this.add(teamsSpinner);
		this.add(okButton);
		this.add(cancelButton);
	}
	
	GameConfigurationPanel(CreateGameDialog cgdialog){
		initGUI();
		
		playersSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				isGameMode = getGameMode().equals(GameTeamsBuilder.TYPE);
				cgdialog.update(getNumPlayers(), isGameMode);
				update(isGameMode);			}
			
		});
		
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cgdialog.exit();
			}
			
		});
		
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
								
				Pair<Boolean, String> pair = null;
				pair = cgdialog.checkIfCorrectArguments(isGameMode);
			
				if (pair.getFirst()) {
					cgdialog.okAction();
				}
				else {
					
					JOptionPane.showMessageDialog(cgdialog, pair.getSecond(), "ROLIT_ERROR", JOptionPane.ERROR_MESSAGE);
				}
		}
		});
		
		gameModeCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isGameMode = getGameMode().equals(GameTeamsBuilder.TYPE);
				update(isGameMode);
				cgdialog.update(getNumPlayers(), isGameMode);
			}
		});
	}
	
	void update(boolean isGameMode) {
		numberOfTeamsLabel.setVisible(isGameMode);
		teamsSpinner.setVisible(isGameMode);
	}

	
	public String getGameMode() {
		return (String) gameModeCombo.getSelectedItem();
	}
	

	public Shape getBoardShape() {
		return (Shape) shapesCombo.getSelectedItem();
	}
	

	public int getNumPlayers() {
		return (int) playersSpinner.getValue();
	}
	
	public int getPlayerSpinnerValue() {
		return (int)playersSpinner.getValue();
	}
	
	public int getTeamsSpinnerValue() {
		return (int)teamsSpinner.getValue();
	}
}
