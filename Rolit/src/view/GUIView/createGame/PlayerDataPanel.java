package view.GUIView.createGame;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.json.JSONObject;

import model.logic.Color;
import model.strategy.Strategy;
import view.GUIView.ColorRenderer;
import view.GUIView.RolitComponents.RolitCheckBox;
import view.GUIView.RolitComponents.RolitComboBox;
import view.GUIView.RolitComponents.RolitPanel;
import view.GUIView.RolitComponents.RolitTextArea;

public class PlayerDataPanel extends RolitPanel {

	private static final long serialVersionUID = 1L;
	
	private JTextArea nameTextArea;
	private JComboBox<Color> colorCombo;
	private JLabel playerLabel;
	private JLabel nameLabel;
	private JLabel colorLabel;
	
	private JLabel teamLabel;
	private JComboBox<String> teamCombo;

	private JLabel AILabel;
	private JCheckBox AICheckBox;
	private JComboBox<Strategy> AICombo;
	
	private boolean isTeamMode;

	private static final int MAX_TEXT_LENGTH = 15;

	@SuppressWarnings("serial")
	PlayerDataPanel(int number, boolean isTeamMode) {
		this.setLayout(new FlowLayout());
		this.isTeamMode = isTeamMode;
		
		//TextArea with the name
		nameTextArea = new RolitTextArea();
		nameTextArea.setDocument(new PlainDocument() {
		    @Override
		    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException{
		        if (str == null || nameTextArea.getText().length() >= MAX_TEXT_LENGTH) {
		            return;
		        }
		 
		        super.insertString(offs, str, a);
		    }
		});
		nameTextArea.setEditable(true);
		nameTextArea.setLineWrap(true);
		nameTextArea.setWrapStyleWord(true);

		//ComboBox with the colors
		colorCombo = new RolitComboBox<Color>();
		colorCombo.setRenderer(new ColorRenderer());
		for(Color color : Color.values()) {
			colorCombo.addItem(color);
		}
		
		//ComboBox with the teams
		teamCombo = new RolitComboBox<String>();
		teamCombo.addItem("Team 1");
		teamCombo.addItem("Team 2");
		
		playerLabel = new JLabel(String.format("Player %d: ", number + 1));	//Label con el numero de jugador
		nameLabel = new JLabel("Name: ");
		colorLabel = new JLabel("Color: ");
		teamLabel = new JLabel("Team: ");
		
		//AI
		AILabel = new JLabel("AI: ");
		AICheckBox = new RolitCheckBox();
		AICheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(AICheckBox.isSelected())
					AICombo.setVisible(true);
				else
					AICombo.setVisible(false);
			}
			
		});
		AICombo = new RolitComboBox<Strategy>(Strategy.strategies);
		AICombo.setVisible(false);
		
		this.add(playerLabel);
		this.add(nameLabel);
		this.add(nameTextArea);
		this.add(colorLabel);
		this.add(colorCombo);
		this.add(teamLabel);
		this.add(teamCombo);
		this.add(AILabel);
		this.add(AICheckBox);
		this.add(AICombo);
		
		updateMode(isTeamMode);
		
		this.setVisible(true);
	}
	
	public String getPlayerName() {
		return this.nameTextArea.getText();
	}
	
	public Strategy getPlayerStrategy() {
		return (Strategy) AICombo.getSelectedItem();
	}
	
	public Color getPlayerColor() {
		return (Color) colorCombo.getSelectedItem();
	}

	public void updateMode(boolean isTeamMode) {
		this.isTeamMode = isTeamMode;
		if(isTeamMode) {
			teamLabel.setVisible(true);
			teamCombo.setVisible(true);
		}
		else {
			teamLabel.setVisible(false);
			teamCombo.setVisible(false);

		}
	}
	
	public boolean isAtTeam(int teamNumber) {
		if(isTeamMode) {
			return teamNumber == (int) teamCombo.getSelectedIndex();
		}
		return false;
	}
	
	public JSONObject getPlayerReport() {
		JSONObject player = new JSONObject();
		player.put("name", getPlayerName());
		player.put("color",  getPlayerColor().toString());
		player.put("score", 0);
		if(AICheckBox.isSelected() && getPlayerStrategy() != null)
			player.put("strategy", getPlayerStrategy().toString());
		return player;
	}
}
