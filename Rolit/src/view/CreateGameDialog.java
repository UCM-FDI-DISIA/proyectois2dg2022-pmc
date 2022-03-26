package view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import logic.Game;
import logic.Shape;

public class CreateGameDialog extends JDialog {
	
	private Game game;
	private Frame parent;
	
	public CreateGameDialog(Game game, Frame parent) {
		super(parent, true);
		this.parent = parent;
		this.game = game;
		initGUI();
	}
	
	public void initGUI () {
		
		this.setLocation(50, 50);
		this.setSize(700, 200);
		
		setTitle("Create Game");
		setVisible(false);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		String[] gameModes = {"Classic"};
		JComboBox gameModeCombo = new JComboBox(gameModes);
		
		Shape[] shapes = Shape.values();
		JComboBox shapesCombo = new JComboBox(shapes);
		
		JSpinner playersSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CreateGameDialog.this.setVisible(false);
			}
			
		});
		
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
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
	
	public void open() {
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		setVisible(true);
	}

	
}
