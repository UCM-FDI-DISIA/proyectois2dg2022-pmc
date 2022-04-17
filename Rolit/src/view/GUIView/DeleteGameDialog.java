package view.GUIView;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import control.SaveLoadManager;

public class DeleteGameDialog extends JDialog {

	private int status = 0;
	
	private List<String> savedGamesPathList;
	private List<JCheckBox> savedGamesCheckBoxList;
	
	private JPanel mainPanel;
	private JPanel buttonsPanel;
	
	private JLabel errorLabel;
	
	private File file;
	
	public DeleteGameDialog(Frame parent) {
		super(parent, true);
		initGUI();
	}
	
	public void initGUI () {
		
		this.setLocation(50, 50);
		this.setSize(700, 200);
		
		setTitle("Delete Game");
		setVisible(false);
		
		mainPanel = new JPanel();
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		mainPanel.add(new JLabel("Found files from "+SaveLoadManager.FULL_DEFAULT_FILENAME+":"));

		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		mainPanel.add(buttonsPanel);
		updateAndShowListGamesPanel();
		
		
		
		JButton deleteButton = new JButton("Delete selected games");
		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean removedElement = false;
				for (int i = 0; i < savedGamesCheckBoxList.size(); ++i) {
					if (savedGamesCheckBoxList.get(i).isSelected())
						removedElement = SaveLoadManager.removeGame(savedGamesCheckBoxList.get(i).getText());
					
				}
				if (removedElement)
					updateAndShowListGamesPanel();
				else {
					
					if (errorLabel != null)
						remove(errorLabel);
					
					errorLabel = new JLabel("No file has been selected");
					add(errorLabel);
					repaint();
					validate();
					pack();
					
				}
			}
			
		});
		
		mainPanel.add(buttonsPanel);
		mainPanel.add(deleteButton);
		
		if (savedGamesPathList.isEmpty())
			deleteButton.setVisible(false);
		
		
		JButton closeWindowButton = new JButton("Close Window");
		closeWindowButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				status = 1;
				DeleteGameDialog.this.setVisible(false);
			}
			
		});
		
		mainPanel.add(closeWindowButton);
		
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
	
	public File getFile() {
		return file;
	}
	
	public void updateAndShowListGamesPanel() {
		
		savedGamesPathList = SaveLoadManager.getListOfSavedGames();
		
		if (buttonsPanel != null)
			buttonsPanel.removeAll();

		savedGamesCheckBoxList = new ArrayList<JCheckBox>();
		
		for (int i = 0; i < savedGamesPathList.size(); ++i) {
			JCheckBox aux = new JCheckBox();
			aux.setText(savedGamesPathList.get(i));
			savedGamesCheckBoxList.add(aux);
			buttonsPanel.add(aux);
		}
		
		buttonsPanel.repaint();
		mainPanel.repaint();
		pack();
		
		
	}
}
