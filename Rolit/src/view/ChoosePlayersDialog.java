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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import logic.Color;
import logic.Player;

public class ChoosePlayersDialog extends JDialog {

	private int status;
	private List<Player> players;
	private List<Color> colors;
	private List<String> names;
	private List<JPanel> panels;
	private List<JTextArea> textAreas;
	private List<JComboBox<Color>> combos;
	private int numPlayers;
	
	public ChoosePlayersDialog(Frame parent, int numPlayers) {
		super(parent, true);
		this.players = new ArrayList<>();
		this.numPlayers = numPlayers;
		initGUI();
	}
	
	public void initGUI() {
		
		this.setLocation(50, 50);
		this.setSize(700, 200);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setAlignmentX(CENTER_ALIGNMENT);
		setContentPane(mainPanel);
		
		panels = new ArrayList<>();
		textAreas = new ArrayList<>();
		combos = new ArrayList<>();
		
		for(int i = 0; i < numPlayers; i++) {
			panels.add(new JPanel());
			textAreas.add(new JTextArea());
			textAreas.get(textAreas.size() - 1).setEditable(true);
			textAreas.get(textAreas.size() - 1).setLineWrap(true);
			textAreas.get(textAreas.size() - 1).setWrapStyleWord(true);
			combos.add(new JComboBox<Color>());
			panels.get(i).add(new JLabel(String.format("Player %d: ", i + 1)));
			panels.get(i).add(new JLabel("Name: "));
			panels.get(i).add(textAreas.get(i));
			panels.get(i).add(new JLabel("Color: "));
			for(Color color : Color.values()) {
				combos.get(i).addItem(color);
			}
			panels.get(i).add(combos.get(i));
		}
		
		for(JPanel panel : panels) {
			mainPanel.add(panel);
		}
		
		JPanel buttonsPanel = new JPanel();
		mainPanel.add(buttonsPanel);
		
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(generatePlayersList()) {
					status = 1;
					ChoosePlayersDialog.this.setVisible(false);
				}
				else {
					//TODO Mostrar algún tipo de error
				}
			}
			
		});
		buttonsPanel.add(okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				status = 0;
				ChoosePlayersDialog.this.setVisible(false);
			}
			
		});
		buttonsPanel.add(cancelButton);
		
		
		pack();
		setResizable(false);
		setVisible(false);
	}
	
	private boolean generatePlayersList() {
		Set<Color> s = new HashSet<>();
		boolean ok = true;
		for(JComboBox<Color> combo : combos) {
			if(s.contains((Color) combo.getSelectedItem())) {
				//TODO Lanzar un mensaje de error
				ok = false;
			}
		}
		if(ok) {
			for(int i = 0; i < numPlayers; i++) {
				players.add(new Player((Color) combos.get(i).getSelectedItem(), (String) textAreas.get(i).getText()));
			}
			return true;
		}
		return false;
	}
	
	public List<Player> getPlayersList() {
		return this.players;
	}
	
	public int open() {
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		setVisible(true);
		return status;
	}
}
