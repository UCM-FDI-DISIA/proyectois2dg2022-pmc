package view.GUIView;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.json.JSONArray;
import org.json.JSONObject;

import view.GUIView.RolitComponents.RolitButton;
import view.GUIView.RolitComponents.RolitPanel;
import view.GUIView.RolitComponents.RolitRadioButton;

public class ChooseTeamFromServerDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private JSONObject gameConfig;
	private JPanel mainPanel;
	private List<String> teamsList = new ArrayList<>();
	private List<RolitRadioButton> listTeamsRadioButtons;
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

	public void initGUI() {
		
		this.setLocation(50, 50);
		
		setTitle("Delete Game");
		setVisible(false);
		
		mainPanel = new RolitPanel();
		
		mainPanel.setLayout(new GridBagLayout());
		
		addComp(mainPanel, new JLabel("Choose team:"), 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);		
		
		RolitPanel buttonsPanel = new RolitPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		
		listTeamsRadioButtons = new ArrayList<RolitRadioButton>();
		G = new ButtonGroup();
		
		for (int i = 0; i < teamsList.size(); ++i) {
			RolitRadioButton aux = new RolitRadioButton();
			aux.setText(teamsList.get(i));
			listTeamsRadioButtons.add(aux);
			G.add(aux);
			buttonsPanel.add(aux);
			
			if (i == 0)
				aux.setSelected(true); //poner uno seleccionado por defecto
		}
		
		addComp(mainPanel, buttonsPanel, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		
		
		RolitButton doneButton = new RolitButton("Done");
		doneButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				status = 1;
				ChooseTeamFromServerDialog.this.setVisible(false);
					
			}
			
		});
		
		addComp(mainPanel, doneButton, 1, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		setContentPane(mainPanel);
		
		setMinimumSize(new Dimension(300, 180));
		
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
	
	/**
	 * This method adds a JComponent in a specified JPanel
	 * @param thePanel Panel in which the component is to be added
	 * @param comp The JComponent to be added
	 * @param xPos The horizontal component of the position in which the component is to be added
	 * @param yPos The vertical component of the position in which the component is to be added
	 * @param compWidth The width of the component to be added
	 * @param compHeight The height of the component to be added
	 * @param place Where in the display area should the component be added
	 * @param stretch Integer that determines whether to resize the component, and if so, how. 
	 */
	private void addComp(JPanel thePanel, JComponent comp, int xPos, int yPos, int compWidth, int compHeight, int place, int stretch){

		GridBagConstraints gridConstraints = new GridBagConstraints();

		gridConstraints.gridx = xPos;
		gridConstraints.gridy = yPos;
		gridConstraints.gridwidth = compWidth;
		gridConstraints.gridheight = compHeight;
		gridConstraints.weightx = 100;
		gridConstraints.weighty = 100;
		gridConstraints.insets = new Insets(5,5,5,5);
		gridConstraints.anchor = place;
		gridConstraints.fill = stretch;

		thePanel.add(comp, gridConstraints);
	}
	
}
