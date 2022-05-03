package view.GUIView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import Rolit.Rolit;
import control.Controller;
import logic.Rival;
import view.GUIView.RolitComponents.RolitButton;
import view.GUIView.RolitComponents.RolitPanel;

public class RankingPanel extends RolitPanel {
	private JLabel title;
	private JLabel place1;
	private JLabel place2;
	private JLabel place3;
	private JLabel score1;
	private JLabel score2;
	private JLabel score3;
	private JLabel name1;
	private JLabel name2;
	private JLabel name3;
	private JPanel mainPanel;
	
	private static final Color GOLD = new Color(212, 175, 55);
	private static final Color SILVER = new Color(192, 192, 192);
	private static final Color BRONZE = new Color(205, 127, 50);
	
	private static final int RANKING_SIZE = 60;
	private static final int PLACE_SIZE = 30;
	private static final int NAME_SIZE = 20;
	private static final int SCORE_SIZE = 15;
	
	
	public RankingPanel(List<? extends Rival> rivals) {
		Collections.sort(rivals);
		if (rivals.size() == 2) initGUI(rivals.get(0).getName(), rivals.get(0).getScore(), rivals.get(1).getName(), rivals.get(1).getScore(), null, -1);
		else initGUI(rivals.get(0).getName(), rivals.get(0).getScore(), rivals.get(1).getName(), rivals.get(1).getScore(), rivals.get(2).getName(), rivals.get(2).getScore());
	}
	
	private void initGUI(String name1, int score1, String name2, int score2, String name3, int score3) {
		this.setLayout(new BorderLayout());
		this.setAlignmentX(CENTER_ALIGNMENT);
		title = new RolitLabel("RANKING", RANKING_SIZE);
		this.add(title, BorderLayout.PAGE_START);
		
		mainPanel = new RolitPanel();
		mainPanel.setMinimumSize(new Dimension(500, 600));
		mainPanel.setSize(new Dimension(500, 600));
		mainPanel.setLayout(new GridBagLayout());
		JPanel place1Panel = new RolitPanel();
		place1Panel.setLayout(new BoxLayout(place1Panel, BoxLayout.Y_AXIS));
		place1Panel.setMinimumSize(new Dimension(500, 700));
		place1Panel.setPreferredSize(new Dimension(500, 700));
		place1Panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		place1Panel.setBackground(GOLD);
		
		place1 = new RolitLabel("1st place", PLACE_SIZE);
		place1Panel.add(place1);
		this.name1 = new RolitLabel(name1, NAME_SIZE);
		place1Panel.add(this.name1);
		this.score1 = new RolitLabel(String.valueOf(score1), SCORE_SIZE);
		place1Panel.add(this.score1);
		addComp(mainPanel, place1Panel, 1, 0, 1, 2, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		JPanel place2Panel = new RolitPanel();
		place2Panel.setLayout(new BoxLayout(place2Panel, BoxLayout.Y_AXIS));
		place2Panel.setMinimumSize(new Dimension(500, 700));
		place2Panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		place2Panel.setBackground(SILVER);
		
		place2 = new RolitLabel("2nd place", PLACE_SIZE);
		place2Panel.add(place2);
		this.name2 = new RolitLabel(name2, NAME_SIZE);
		place2Panel.add(this.name2);
		this.score2 = new RolitLabel(String.valueOf(score2), SCORE_SIZE);
		place2Panel.add(this.score2);
		addComp(mainPanel, place2Panel, 0, 1, 1, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE);

		if (name3 != null) {
		JPanel place3Panel = new RolitPanel();
		place3Panel.setLayout(new BoxLayout(place3Panel, BoxLayout.Y_AXIS));
		place3Panel.setMinimumSize(new Dimension(500, 700));
		place3Panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		place3Panel.setBackground(BRONZE);
			
		place3 = new RolitLabel("3rd place", PLACE_SIZE);
		place3Panel.add(place3);
		this.name3 = new RolitLabel(name3, NAME_SIZE);
		place3Panel.add(this.name3);
		this.score3 = new RolitLabel(String.valueOf(score3), SCORE_SIZE);
		place3Panel.add(this.score3);
		addComp(mainPanel, place3Panel, 2, 1, 1, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	}
	
		
		
//		
//		
//		place2 = new RolitLabel("2nd place", PLACE_SIZE);
//		place2.setForeground(SILVER);
//		addComp(mainPanel, place2, 0, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
//		this.name2 = new RolitLabel(name2, NAME_SIZE);
//		addComp(mainPanel, this.name2, 0, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
//		this.score2 = new RolitLabel(String.valueOf(score2), SCORE_SIZE);
//		addComp(mainPanel, this.score2, 0, 4, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
//		
//		if (name3 != null) {
//			place3 = new RolitLabel("3rd place", PLACE_SIZE);
//			place3.setForeground(BRONZE);
//			addComp(mainPanel, place3, 2, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
//			this.name3 = new RolitLabel(name3, NAME_SIZE);
//			addComp(mainPanel, this.name3, 2, 4, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
//			this.score3 = new RolitLabel(String.valueOf(score3), SCORE_SIZE);
//			addComp(mainPanel, this.score3, 2, 5, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
//		}
//		
		this.add(mainPanel, BorderLayout.CENTER);
//		
		
		
		//		this.setLayout(new GridBagLayout());
//		title = new RolitLabel("RANKING", RANKING_SIZE);
//		if (name3 != null) addComp(this, title, 1, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
//		else addComp(this, title, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
//		
//		place1 = new RolitLabel("1st place", PLACE_SIZE);
//		place1.setForeground(GOLD);
//		addComp(this, place1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
//		this.name1 = new RolitLabel(name1, NAME_SIZE);
//		addComp(this, this.name1, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
//		this.score1 = new RolitLabel(String.valueOf(score1), SCORE_SIZE);
//		addComp(this, this.score1, 1, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
//		
//		place2 = new RolitLabel("2nd place", PLACE_SIZE);
//		place2.setForeground(SILVER);
//		addComp(this, place2, 0, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
//		this.name2 = new RolitLabel(name2, NAME_SIZE);
//		addComp(this, this.name2, 0, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
//		this.score2 = new RolitLabel(String.valueOf(score2), SCORE_SIZE);
//		addComp(this, this.score2, 0, 4, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
//		
//		if (name3 != null) {
//			place3 = new RolitLabel("3rd place", PLACE_SIZE);
//			place3.setForeground(BRONZE);
//			addComp(this, place3, 2, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
//			this.name3 = new RolitLabel(name3, NAME_SIZE);
//			addComp(this, this.name3, 2, 4, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
//			this.score3 = new RolitLabel(String.valueOf(score3), SCORE_SIZE);
//			addComp(this, this.score3, 2, 5, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
//		}
	}
	
	private class RolitLabel extends JLabel {
		RolitLabel(String text, int s){
			super(text);
			this.setHorizontalAlignment(JLabel.CENTER);
			this.setAlignmentX(CENTER_ALIGNMENT);
			this.setFont(new Font(this.getFont().getName(), this.getFont().getStyle(), s));
		}
	}
	
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
