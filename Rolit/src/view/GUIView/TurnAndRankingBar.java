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
import control.Controller;
import logic.Color;
import logic.Rival;
import replay.GameState;

public class TurnAndRankingBar extends JPanel implements RolitObserver {

	private static final long serialVersionUID = 1L;
	
	private Controller ctrl;
	private GameState state;
	private JLabel msgLabel;
	private JLabel colorLabel;
	private JPanel rankingPanel;
	private String colorPath;
	public static final String TURN_MSG = "Turn for ";
	public static final String FINISHED_GAME_MSG = "Game has finished!";
	public static final int ROW_HEIGHT = 20;
	public static final int COLUMN_WIDTH = 50;
	
	public TurnAndRankingBar(Controller ctrl, GameState state) {
		this.state = state;
		this.ctrl = ctrl;
		initGUI();
		ctrl.addObserver(this);
	}
	
	public void initGUI() {
		
		msgLabel = new JLabel();
		msgLabel.setMinimumSize(new Dimension(CeldaGUI.SIDE_LENGTH, 10));
		
		colorLabel = new JLabel();
		colorLabel.setMinimumSize(new Dimension(CeldaGUI.SIDE_LENGTH, CeldaGUI.SIDE_LENGTH));
		
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

	private JPanel createViewPanel(JComponent c) {
		JPanel p = new JPanel( new BorderLayout() );
		p.add(new JScrollPane(c));
		return p;
	}
	
	private void update(String name, Color color) {
		this.msgLabel.setText(String.format("%s %s", TURN_MSG, name));
		this.colorPath = color.getPath();
		ImageIcon colorIcon = new ImageIcon(colorPath);
		Image colorImage = colorIcon.getImage();
		Image scaledColorImage = colorImage.getScaledInstance(CeldaGUI.SIDE_LENGTH, CeldaGUI.SIDE_LENGTH, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledColorImage);
		this.colorLabel.setIcon(scaledIcon);
	}
	
	@Override
	public void onTurnPlayed(GameState state) {	//TODO Si esto funciona dios existe
		this.state = state;
		update(state.getTurnName(), Color.valueOfIgnoreCase(state.getTurnColorShorcut()));
	}

	@Override
	public void onRegister(GameState state) {
		this.state = state;
		update(state.getFirstPlayerName(), Color.valueOfIgnoreCase(state.getFirstPlayerColorShortcut()));
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameStatusChange(GameState status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFirstPlay(GameState state) {}

}
