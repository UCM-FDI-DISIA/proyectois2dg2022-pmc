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

/**
 * This class is a Swing JDialog in which the user
 * can save the replay of the game
 * @author PMC
 */
public class SaveReplayDialog extends JDialog implements RolitObserver{

	private static final long serialVersionUID = 1L;
	
	private Replay replay;
	private JPanel panel;
	private JLabel askLabel;
	private JButton yesButton;
	private JButton noButton;
	
	/**
	 * Constructor
	 * @param parent The frame of the caller
	 * @param ctrl The controller
	 */
	public SaveReplayDialog(Frame parent, Controller ctrl) {
		super(parent, true);
		ctrl.addObserver(this);
		this.setLocationRelativeTo(null);
	}
	
	/**
	 * This method creates and shows all the components relative to this dialog
	 */
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
		
		//To center the component
		int x = (getParent().getWidth() - this.getWidth()) / 2;
		setLocation(getParent().getX() + x, getParent().getY() + 300);
		
		this.pack();
		this.setVisible(true);
	}

	/**
	 * onTurnPlayed method overridden (RolitObserver interface)
	 */
	@Override
	public void onTurnPlayed(GameState state) {}

	/**
	 * onGameFinished method overridden (RolitObserver interface)
	 * Particularly, it updates the replay, waits one second so that the ranking
	 * can be correctly shown, and calls initGUI
	 */
	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival, Replay replay, GameState state) {
		try {
			Thread.sleep(1000); //we add a wait time so that the ranking is correctly shown
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.replay = replay;
		initGUI();

	}

	/**
	 * onGameExited method overridden (RolitObserver interface)
	 */
	@Override
	public void onGameExited(Replay replay) {
		this.replay = replay;
		initGUI();
	}

	/**
	 * onRegister method overridden (RolitObserver interface)
	 */
	@Override
	public void onRegister(GameState state) {}

	/**
	 * onError method overridden (RolitObserver interface)
	 */
	@Override
	public void onError(String err) {}

	/**
	 * onGameStatusChange method overridden (RolitObserver interface)
	 */
	@Override
	public void onGameStatusChange(GameState state) {}

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
