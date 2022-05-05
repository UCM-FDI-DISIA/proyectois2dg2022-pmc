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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import control.SaveLoadManager;
import view.GUIView.RolitComponents.RolitButton;
import view.GUIView.RolitComponents.RolitCheckBox;
import view.GUIView.RolitComponents.RolitPanel;

public class DeleteGameDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private int status = 0;

	private List<String> savedGamesPathList;
	private List<JCheckBox> savedGamesCheckBoxList;

	private JPanel mainPanel;
	private JPanel buttonsPanel;

	private JLabel errorLabel;
	private JLabel foundFilesMsg;
	private JPanel okCancelPanel;
	private JButton cancelButton;
	private JButton okButton;

	public DeleteGameDialog(Frame parent) {
		super(parent, true);
		initGUI();
	}

	public void initGUI() {

		this.setLocation(50, 50);
		this.setSize(700, 200);

		setTitle("Delete Game");
		setVisible(false);

		mainPanel = new RolitPanel();
		mainPanel.setLayout(new GridBagLayout());

		foundFilesMsg = new JLabel("SAVED GAMES: ");
		addComp(mainPanel, foundFilesMsg, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);

		buttonsPanel = new RolitPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		savedGamesPathList = SaveLoadManager.getListOfSavedGames();
		updateAndShowListGamesPanel();
		addComp(mainPanel, buttonsPanel, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);

		// OkCancelButtonsPanel
		okCancelPanel = new RolitPanel();
		// CancelButton
		cancelButton = new RolitButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				status = 0;
				DeleteGameDialog.this.setVisible(false);
			}

		});
		// OkButton
		okButton = new RolitButton("Delete");
		okButton.addActionListener(new ActionListener() {

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
				DeleteGameDialog.this.setVisible(false);
			}

		});

		okCancelPanel.add(okButton);

		if (savedGamesPathList.isEmpty())
			okButton.setVisible(false);

		okCancelPanel.add(cancelButton);

		addComp(mainPanel, okCancelPanel, 0, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);



		if (savedGamesPathList.isEmpty())
			okButton.setVisible(false);

		setContentPane(mainPanel);
		setMinimumSize(new Dimension(100, 100));

		this.pack();

	}

	int open() {
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		setVisible(true);
		return status;
	}

	public void updateAndShowListGamesPanel() {

		savedGamesPathList = SaveLoadManager.getListOfSavedGames();

		if (buttonsPanel != null)
			buttonsPanel.removeAll();

		savedGamesCheckBoxList = new ArrayList<JCheckBox>();

		for (int i = 0; i < savedGamesPathList.size(); ++i) {
			JCheckBox aux = new RolitCheckBox();
			aux.setText(savedGamesPathList.get(i));
			savedGamesCheckBoxList.add(aux);
			buttonsPanel.add(aux);
		}

		buttonsPanel.repaint();
		mainPanel.repaint();
		pack();

	}

	private void addComp(JPanel thePanel, JComponent comp, int xPos, int yPos, int compWidth, int compHeight, int place,
			int stretch) {

		GridBagConstraints gridConstraints = new GridBagConstraints();

		gridConstraints.gridx = xPos;
		gridConstraints.gridy = yPos;
		gridConstraints.gridwidth = compWidth;
		gridConstraints.gridheight = compHeight;
		gridConstraints.weightx = 100;
		gridConstraints.weighty = 100;
		gridConstraints.insets = new Insets(5, 5, 5, 5);
		gridConstraints.anchor = place;
		gridConstraints.fill = stretch;

		thePanel.add(comp, gridConstraints);
	}
}
