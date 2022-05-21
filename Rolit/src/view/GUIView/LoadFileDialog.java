package view.GUIView;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import view.GUIView.RolitComponents.RolitButton;
import view.GUIView.RolitComponents.RolitPanel;
import view.GUIView.RolitComponents.RolitRadioButton;

/**
 * This class is a Swing JDialog in which the user
 * can load saved games
 * @author PMC
 */
public class LoadFileDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private int status = 0;
	
	private List<String> savedFilesPathList;
	private List<JRadioButton> savedFilesRadioButtons;
	private ButtonGroup buttonGroup;
	private JPanel mainPanel;
	private JPanel buttonsPanel;
	private JPanel okCancelPanel;
	private JLabel foundFilesMsg;
	private JLabel errorLabel;
	private JButton cancelButton;
	private JButton okButton;
	private JButton loadButton;
	private File file;
	
	/**
	 * Constructor
	 * @param parent The frame of the caller
	 * @param savedGamesPathList List of all saved games
	 */
	public LoadFileDialog(Frame parent, List<String> savedGamesPathList) {
		super(parent, true);
		this.savedFilesPathList = savedGamesPathList;
		initGUI();
	}
	
	/**
	 * This method creates and shows all the components relative to this dialog
	 */
	public void initGUI() {
		
		setTitle("Load File");
		setVisible(false);
		
		mainPanel = new RolitPanel();
		mainPanel.setLayout(new GridBagLayout());
		
		//Create and add components
			//Saved games label
		foundFilesMsg = new JLabel("SAVED FILES: ");
		addComp(mainPanel, foundFilesMsg, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
			//RadioButtons
		buttonsPanel = new RolitPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		savedFilesRadioButtons = new ArrayList<JRadioButton>();
		buttonGroup = new ButtonGroup();
		for (int i = 0; i < savedFilesPathList.size(); ++i) {
			JRadioButton aux = new RolitRadioButton();
			aux.setText(savedFilesPathList.get(i));
			savedFilesRadioButtons.add(aux);
			buttonGroup.add(aux);
			buttonsPanel.add(aux);
		}
		addComp(mainPanel, buttonsPanel, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);		
		
			//OkCancelButtonsPanel
		okCancelPanel = new RolitPanel();
				//CancelButton
		cancelButton = new RolitButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				status = 0;
				LoadFileDialog.this.setVisible(false);
			}
			
		});
				//OkButton
		okButton = new RolitButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				status = 1;
				for (int i = 0; i < savedFilesRadioButtons.size(); ++i) {
					if (savedFilesRadioButtons.get(i).isSelected())
						file = new File(savedFilesRadioButtons.get(i).getText()); 
				}
				if (file != null)
					LoadFileDialog.this.setVisible(false);
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
		
		if (savedFilesPathList.isEmpty())
			okButton.setVisible(false);
		
		okCancelPanel.add(cancelButton);
		
		addComp(mainPanel, okCancelPanel, 0, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
			//LoadButton
		loadButton = new RolitButton("Browse...");
		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int ret = fileChooser.showOpenDialog(loadButton);
				if (ret == JFileChooser.APPROVE_OPTION) {
					status = 1;
					file = fileChooser.getSelectedFile();
				}
					
				LoadFileDialog.this.setVisible(false);
			}
			
		});
		addComp(mainPanel, loadButton, 0, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		setContentPane(mainPanel);
		setMinimumSize(new Dimension(100, 100));
		
		this.pack();
		
		//To center the component
		int x = (getParent().getWidth() - this.getWidth()) / 2;
		setLocation(getParent().getX() + x, getParent().getY() + 50);
	}
	
	/**
	 * This method opens the dialog
	 * @return The status (1-success, 0-failure)
	 */
	int open() {
		setVisible(true);
		return status;
	}
	
	/**
	 * Getter of the selected file
	 * @return The selected file
	 */
	public File getFile() {
		return file;
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
