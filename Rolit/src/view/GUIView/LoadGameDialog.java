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
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


import control.SaveLoadManager;

public class LoadGameDialog extends JDialog {

	private int status = 0;
	
	private List<String> savedGamesPathList;
	private List<JRadioButton> savedGamesRadioButtons;
	private ButtonGroup G;
	
	private JPanel mainPanel;
	private JPanel buttonsPanel;
	private JPanel okCancelPanel;
	
	private JLabel errorLabel;
	
	private File file;
	
	public LoadGameDialog(Frame parent) {
		super(parent, true);
		initGUI();
	}
	
	public void initGUI () {
		
		this.setLocation(50, 50);
		this.setSize(700, 200);
		
		setTitle("Load Game");
		setVisible(false);
		
		mainPanel = new JPanel();
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		mainPanel.add(new JLabel("Found files from "+SaveLoadManager.FULL_DEFAULT_FILENAME+":"));
		
		savedGamesPathList = SaveLoadManager.getListOfSavedGames();
		
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		
		savedGamesRadioButtons = new ArrayList<JRadioButton>();
		G = new ButtonGroup();
		
		for (int i = 0; i < savedGamesPathList.size(); ++i) {
			JRadioButton aux = new JRadioButton();
			aux.setText(savedGamesPathList.get(i));
			savedGamesRadioButtons.add(aux);
			G.add(aux);
			buttonsPanel.add(aux);
		}
		
		mainPanel.add(buttonsPanel);
		
		
		okCancelPanel = new JPanel();
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				status = 0;
				LoadGameDialog.this.setVisible(false);
			}
			
		});
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				status = 1;
				for (int i = 0; i < savedGamesRadioButtons.size(); ++i) {
					if (savedGamesRadioButtons.get(i).isSelected())
						file = new File(savedGamesRadioButtons.get(i).getText()); 
				}
				if (file != null)
					LoadGameDialog.this.setVisible(false);
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
		
		
		okCancelPanel.add(okButton);
		if (savedGamesPathList.isEmpty())
			okButton.setVisible(false);
		
		okCancelPanel.add(cancelButton);
		
		mainPanel.add(okCancelPanel);
		
		mainPanel.add(new JLabel("Alternatively, load from external file: "));
		
		JButton loadButton = new JButton();
		loadButton.setIcon(new ImageIcon("resources/icons/open.png"));
		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int ret = fileChooser.showOpenDialog(loadButton);
				if (ret == JFileChooser.APPROVE_OPTION) {
					status = 1;
					file = fileChooser.getSelectedFile();
				}
					
				LoadGameDialog.this.setVisible(false);
			}
			
		});
		loadButton.setMinimumSize(new Dimension(75, 20));
		mainPanel.add(loadButton);
		
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
}
