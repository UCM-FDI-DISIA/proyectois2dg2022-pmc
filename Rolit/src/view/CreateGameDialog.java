package view;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import logic.Game;
import logic.Shape;

public class CreateGameDialog extends JDialog {
	
	private Game game;
	
	public CreateGameDialog(Frame parent) {
		super(parent, true);
		initGUI();
	}
	
	public void initGUI () {
		setTitle("Create Game");
		setVisible(false);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		String[] gameModes = {"Classic"};
		JComboBox gameModeCombo = new JComboBox(gameModes);
		
		Shape[] shapes = Shape.values();
		JComboBox shapesCombo = new JComboBox(shapes);
		
		JSpinner playersSpinner = new JSpinner(new SpinnerNumberModel(0, 1, 10, 1));
		
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
		buttonsPanel.add(cancelButton);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
			
		});
		buttonsPanel.add(okButton);
		
		//TODO Antes de pasar a crear el Game hay que crear los players
		//Hay que pedir los nombres y colores para crearlos
	}
	
	public void open() {
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		setVisible(true);
	}

	
}
