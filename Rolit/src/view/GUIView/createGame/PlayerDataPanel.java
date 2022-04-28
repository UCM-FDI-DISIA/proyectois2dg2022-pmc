package view.GUIView.createGame;

import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.json.JSONObject;

import logic.Color;
import view.GUIView.ColorRenderer;
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

	private boolean isTeamMode;

	private static final int MAX_TEXT_LENGTH = 15;

	
	@SuppressWarnings("serial")
	PlayerDataPanel(int number, boolean isTeamMode){
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

		this.add(playerLabel);
		this.add(nameLabel);
		this.add(nameTextArea);
		this.add(colorLabel);
		this.add(colorCombo);
		this.add(teamLabel);
		this.add(teamCombo);
		
		updateMode(isTeamMode);
		
		this.setVisible(true);
	}
	
	public String getPlayerName() {
		return this.nameTextArea.getText();
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
		return player;
	}
}
