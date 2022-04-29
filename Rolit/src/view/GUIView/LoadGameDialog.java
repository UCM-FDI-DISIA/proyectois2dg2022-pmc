package view.GUIView;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


import control.SaveLoadManager;
import view.GUIView.RolitComponents.RolitButton;
import view.GUIView.RolitComponents.RolitIconButton;
import view.GUIView.RolitComponents.RolitPanel;
import view.GUIView.RolitComponents.RolitRadioButton;

public class LoadGameDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private int status = 0;
	
	private List<String> savedGamesPathList;
	private List<JRadioButton> savedGamesRadioButtons;
	private ButtonGroup G;
	private JPanel mainPanel;
	private JPanel buttonsPanel;
	private JPanel okCancelPanel;
	private JLabel foundGamesMsg;
	private JLabel loadFileMsg;
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
		
		mainPanel = new RolitPanel();
		
		mainPanel.setLayout(new GridBagLayout());

		//mainPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		foundGamesMsg = new JLabel("Found files from "+SaveLoadManager.FULL_DEFAULT_FILENAME+":");
		addComp(mainPanel, foundGamesMsg, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		savedGamesPathList = SaveLoadManager.getListOfSavedGames();
		
		buttonsPanel = new RolitPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		
		savedGamesRadioButtons = new ArrayList<JRadioButton>();
		G = new ButtonGroup();
		
		for (int i = 0; i < savedGamesPathList.size(); ++i) {
			JRadioButton aux = new RolitRadioButton();
			aux.setText(savedGamesPathList.get(i));
			savedGamesRadioButtons.add(aux);
			G.add(aux);
			buttonsPanel.add(aux);
		}
		
		addComp(mainPanel, buttonsPanel, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);		
		
		okCancelPanel = new RolitPanel();
		
		JButton cancelButton = new RolitButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				status = 0;
				LoadGameDialog.this.setVisible(false);
			}
			
		});
		JButton okButton = new RolitButton("OK");
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
		
		addComp(mainPanel, okCancelPanel, 0, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		//loadFileMsg = new JLabel("Alternatively, load from external file: ");
		//addComp(mainPanel, loadFileMsg, 0, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);

		
		JButton loadButton = new RolitIconButton(new ImageIcon("resources/icons/open.png"));
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
		addComp(mainPanel, loadButton, 0, 4, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
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
