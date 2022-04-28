package view.GUIView;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChooseTeamFromServerDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private JSONObject gameConfig;
	private JPanel mainPanel;
	private List<String> teamsList = new ArrayList<>();
	private List<JRadioButton> listTeamsRadioButtons;
	private ButtonGroup G;
	private int status = 0;
	
	public ChooseTeamFromServerDialog(Frame parent, JSONObject gameConfig) {
		super(parent, true);
		this.gameConfig = gameConfig;
		buildTeamsList();
		initGUI();
	}
	
	private void buildTeamsList() {
		JSONArray teamsArray = gameConfig.getJSONArray("teams");
		for (int i = 0; i < teamsArray.length(); ++i) {
			teamsList.add(teamsArray.getJSONObject(i).getString("name"));
		}
	}

	public void initGUI () {
		
		this.setLocation(50, 50);
		this.setSize(700, 200);
		
		setTitle("Delete Game");
		setVisible(false);
		
		mainPanel = new JPanel();
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		mainPanel.add(new JLabel("Choose team:"));
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		
		listTeamsRadioButtons = new ArrayList<JRadioButton>();
		G = new ButtonGroup();
		
		for (int i = 0; i < teamsList.size(); ++i) {
			JRadioButton aux = new JRadioButton();
			aux.setText(teamsList.get(i));
			listTeamsRadioButtons.add(aux);
			G.add(aux);
			buttonsPanel.add(aux);
			
			if (i == 0)
				aux.setSelected(true); //poner uno seleccionado por defecto
		}
		
		mainPanel.add(buttonsPanel);
		
		
		
		JButton doneButton = new JButton("Done");
		doneButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				status = 1;
				ChooseTeamFromServerDialog.this.setVisible(false);
					
			}
			
		});
		
		mainPanel.add(doneButton);
		
		setContentPane(mainPanel);
		setMinimumSize(new Dimension(100, 100));
		
		this.pack();
		
		//TODO Antes de pasar a crear el Game hay que crear los players
		//Hay que pedir los nombres y colores para crearlos
	}
	

	int open() {
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		setVisible(true);
		return status;
	}
	
	public JSONObject getSelectedTeamJSON() {
		JSONObject o = new JSONObject();
		String selectedTeam = null;
		
		for (int i = 0; i < listTeamsRadioButtons.size(); ++i) {
			if (listTeamsRadioButtons.get(i).isSelected())
				selectedTeam = listTeamsRadioButtons.get(i).getText();
			
		}
		o.put("team", selectedTeam);
		
		return o;
		
	}
	
}
