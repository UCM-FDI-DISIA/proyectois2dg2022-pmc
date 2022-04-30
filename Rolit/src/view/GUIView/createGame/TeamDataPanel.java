package view.GUIView.createGame;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.json.JSONObject;

import view.GUIView.RolitComponents.RolitPanel;
import view.GUIView.RolitComponents.RolitTextArea;

public class TeamDataPanel extends RolitPanel {
	private static final long serialVersionUID = 1L;

	private static final int MAX_TEXT_LENGTH = 15;

	private JTextArea nameTextArea;
	private JLabel nameLabel;
	
	@SuppressWarnings("serial")
	TeamDataPanel(int number){
		nameLabel = new JLabel(String.format("Team %d: ", number + 1));

		
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

		this.add(nameLabel);
		this.add(nameTextArea);
	}

	public String getTeamName() {
		return nameTextArea.getText();
	}
	
	//This JSONObject doesn't contain the playerList
	public JSONObject getTeamReport() {
		JSONObject jo = new JSONObject();
		jo.put("name", nameTextArea.getText());
		jo.put("players", new JSONObject());
		jo.put("score", 0);
		return jo;
	}
}
