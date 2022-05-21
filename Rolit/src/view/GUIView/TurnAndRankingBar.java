package view.GUIView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import controller.Controller;
import model.logic.Color;
import model.logic.Rival;
import model.replay.GameState;
import model.replay.Replay;
import view.RolitObserver;
import view.GUIView.RolitComponents.RolitPanel;

/**
 * This class is a RolitPanel that gathers the real-time ranking as well
 * as the current turn in the game or replay.
 * @author PMC
 */
public class TurnAndRankingBar extends RolitPanel implements RolitObserver, ReplayObserver {

	private static final long serialVersionUID = 1L;
	
	private Controller ctrl;
	private GameState state;
	private JLabel msgLabel;
	private JLabel colorLabel;
	private JPanel rankingPanel;
	private String colorPath;
	private Replay replay;
	public static final String TURN_MSG = "Turn for ";
	public static final String FINISHED_GAME_MSG = "Game has finished!";
	public static final int ROW_HEIGHT = 20;
	public static final int COLUMN_WIDTH = 50;
	
	/**
	 * Constructor
	 * @param ctrl The controller
	 * @param state A game state
	 */
	public TurnAndRankingBar(Controller ctrl, GameState state) {
		this.state = state;
		this.ctrl = ctrl;
		initGUI();
		ctrl.addObserver(this);
	}
	
	/**
	 * Constructor
	 * @param replay The replay
	 */
	public TurnAndRankingBar(Replay replay) {
		this.replay = replay;
		this.state = replay.getCurrentState();
		initGUIReplay();
		update(state.getTurnName(), Color.valueOfIgnoreCase(state.getTurnColorShortcut()));
		replay.addObserver(this);
	}
	
	/**
	 * This method creates and shows all the components relative to this panel
	 */
	public void initGUI() {
		
		msgLabel = new JLabel();
		msgLabel.setMinimumSize(new Dimension(CellGUI.SIDE_LENGTH, 10));
		
		colorLabel = new JLabel();
		colorLabel.setMinimumSize(new Dimension(CellGUI.SIDE_LENGTH, CellGUI.SIDE_LENGTH));
		
		JTable rankingTable = new JTable(new RankingTableModel(ctrl, state));
		rankingTable.setTableHeader(null);
		rankingTable.setSize(new Dimension(300, ROW_HEIGHT * 3));
		
		rankingPanel = createViewPanel(rankingTable);
		JScrollPane scrollPane = new JScrollPane(rankingPanel);
		scrollPane.setMinimumSize(new Dimension(300, ROW_HEIGHT * 2));
		scrollPane.setPreferredSize(new Dimension(300, ROW_HEIGHT * 2));
		scrollPane.setMaximumSize(new Dimension(300, ROW_HEIGHT * 2));
		this.add(msgLabel);
		this.add(colorLabel);
		this.add(rankingTable);
		this.setVisible(true);
	}
	
	/**
	 * This method creates and shows all the components relative to the replay
	 */
	public void initGUIReplay() {
		
		msgLabel = new JLabel();
		msgLabel.setMinimumSize(new Dimension(CellGUI.SIDE_LENGTH, 10));
		
		colorLabel = new JLabel();
		colorLabel.setMinimumSize(new Dimension(CellGUI.SIDE_LENGTH, CellGUI.SIDE_LENGTH));
		
		JTable rankingTable = new JTable(new RankingTableModel(replay));
		rankingTable.setTableHeader(null);
		rankingTable.setSize(new Dimension(300, ROW_HEIGHT * 3));
		
		rankingPanel = createViewPanel(rankingTable);
		JScrollPane scrollPane = new JScrollPane(rankingPanel);
		scrollPane.setMinimumSize(new Dimension(300, ROW_HEIGHT * 2));
		scrollPane.setPreferredSize(new Dimension(300, ROW_HEIGHT * 2));
		scrollPane.setMaximumSize(new Dimension(300, ROW_HEIGHT * 2));
		this.add(msgLabel);
		this.add(colorLabel);
		this.add(rankingTable);
		this.setVisible(true);
	}

	/**
	 * This method creates a panel with possibility of scrolling
	 */
	private JPanel createViewPanel(JComponent c) {
		JPanel p = new JPanel( new BorderLayout() );
		p.add(new JScrollPane(c));
		return p;
	}
	
	/**
	 * This method updates the panel, showing the new turn
	 * @param name The name of the player that now has the turn
	 * @param color The color of the player that now has the turn
	 */
	private void update(String name, Color color) {
		this.msgLabel.setText(String.format("%s %s", TURN_MSG, name));
		this.colorPath = color.getPath();
		ImageIcon colorIcon = new ImageIcon(colorPath);
		Image colorImage = colorIcon.getImage();
		Image scaledColorImage = colorImage.getScaledInstance(CellGUI.SIDE_LENGTH, CellGUI.SIDE_LENGTH, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledColorImage);
		this.colorLabel.setIcon(scaledIcon);
	}
	
	/**
	 * onTurnPlayed method overridden (RolitObserver interface)
	 */
	@Override
	public void onTurnPlayed(GameState state) {
		this.state = state;
		update(state.getTurnName(), Color.valueOfIgnoreCase(state.getTurnColorShortcut()));
	}

	/**
	 * onRegister method overridden (RolitObserver interface)
	 */
	@Override
	public void onRegister(GameState state) {
		this.state = state;
		update(state.getTurnName(), Color.valueOfIgnoreCase(state.getTurnColorShortcut()));
		
	}

	/**
	 * onError method overridden (RolitObserver interface)
	 */
	@Override
	public void onError(String err) {}

	/**
	 * onGameStatusChange method overridden (RolitObserver interface)
	 */
	@Override
	public void onGameStatusChange(GameState status) {}

	/**
	 * onGameFinished method overridden (RolitObserver interface)
	 */
	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival, Replay replay, GameState state) {
		onTurnPlayed(state);
		
		this.remove(colorLabel);
		this.remove(msgLabel);
		this.revalidate();

	}

	/**
	 * onGameExited method overridden (RolitObserver interface)
	 */
	@Override
	public void onGameExited(Replay replay) {}

	/**
	 * onReplayLeftButton method overridden (ReplayObserver interface)
	 */
	@Override
	public void onReplayLeftButton() {
		this.state = replay.getCurrentState();
		update(state.getTurnName(), Color.valueOfIgnoreCase(state.getTurnColorShortcut()));
	}

	/**
	 * onReplayRightButton method overridden (ReplayObserver interface)
	 */
	@Override
	public void onReplayRightButton() {
		this.state = replay.getCurrentState();
		update(state.getTurnName(), Color.valueOfIgnoreCase(state.getTurnColorShortcut()));
	}

	/**
	 * onReplayStatusChange method overridden (ReplayObserver interface)
	 */
	@Override
	public void onReplayStatusChange(String msg) {}

}
