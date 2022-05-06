package view.GUIView;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import model.SaveLoadManager;
import model.logic.Rival;
import model.replay.GameState;
import model.replay.Replay;
import view.RolitObserver;
import view.GUIView.RolitComponents.RolitButton;
import view.GUIView.RolitComponents.RolitPanel;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;

public class SaveReplayDialog extends JDialog implements RolitObserver{

	private static final long serialVersionUID = 1L;
	
	private Replay replay;
	private JPanel panel;
	private JLabel askLabel;
	private JButton yesButton;
	private JButton noButton;
	
	public SaveReplayDialog(Frame parent, Controller ctrl) {
		super(parent, true);
		ctrl.addObserver(this);
		this.setLocationRelativeTo(null);
	}
	
	
	private void initGUI() {
		this.setTitle("Save Replay");
		
		setSize(300,400);

		panel = new RolitPanel();
		panel.setLayout(new GridBagLayout());
		
		askLabel = new JLabel("Do you want to save the replay of this game?");
		addComp(panel, askLabel, 0, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
		
		yesButton = new RolitButton("Yes");
		addComp(panel, yesButton, 0, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
		yesButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser saveDialog = new JFileChooser();
				setVisible(false);
				int ans = saveDialog.showSaveDialog(null);
				if(ans == JFileChooser.APPROVE_OPTION) {
					SaveLoadManager.saveReplay(saveDialog.getSelectedFile().getAbsolutePath(), replay);
				}
				
			}
			
		});
		
		noButton = new RolitButton("No");
		addComp(panel, noButton, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
		noButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SaveReplayDialog.this.setVisible(false);
			}
			
		});
		
		this.add(panel);
		
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}

	@Override
	public void onTurnPlayed(GameState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival, Replay replay) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(1000); //añadimos un tiempo de espera para que se vea bien el ranking
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.replay = replay;
		initGUI();

	}

	@Override
	public void onGameExited(Replay replay) {
		this.replay = replay;
		initGUI();

		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(GameState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameStatusChange(GameState state) {
		// TODO Auto-generated method stub
		
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
