package view.GUIView;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.json.JSONArray;

import controller.Controller;
import model.logic.Rival;
import model.replay.GameState;
import model.replay.Replay;
import view.RolitObserver;

/**
 * This table model shows a table of each of the rivals'
 * name and score when playing a game 
 * @author PMC
 */
public class RankingTableModel extends AbstractTableModel implements RolitObserver, ReplayObserver {

	private static final long serialVersionUID = 1L;
	
	private String[] _colNames;
	private JSONArray rivals;
	private Replay replay;
	public static final int NUM_ROWS = 2;
	
	/**
	 * Constructor
	 * @param ctrl The controller
	 * @param ctrl A game state
	 */
	public RankingTableModel(Controller ctrl, GameState state) {
		this.rivals = state.getRivals();
		this._colNames = new String[this.rivals.length() + 1];
		this._colNames[0] = state.getType();
		for(int i = 1; i <= this.rivals.length(); i++) {
			this._colNames[i] = this.rivals.getJSONObject(i - 1).getString("name");
		}
		ctrl.addObserver(this);
	}
	
	/**
	 * Constructor
	 * @param replay The replay
	 */
	public RankingTableModel(Replay replay) {
		this.replay = replay;
		GameState state = replay.getCurrentState();
		this.rivals = state.getRivals();
		this._colNames = new String[this.rivals.length() + 1];
		this._colNames[0] = state.getType();
		for(int i = 1; i <= this.rivals.length(); i++) {
			this._colNames[i] = this.rivals.getJSONObject(i - 1).getString("name");
		}
		update();
		this.rivals = replay.getCurrentState().getRivals();
		replay.addObserver(this);
	}
	
	/**
	 * This method updates the table
	 */
	public void update() {
		fireTableDataChanged();
	}
	
	/**Method overridden from AbstractTableModel.
	 * It returns the name of the column specified by the parameter
	 * @param col The column in which the name wants to be known
	 * @return The name of the specified column
	 */
	@Override
	public String getColumnName(int col) {
		return _colNames[col];
	}

	/**
	 * Method overridden from AbstractTableModel.
	 * It returns the number of columns in the table
	 * @return The number of columns in the table
	 */
	@Override
	public int getColumnCount() {
		return _colNames.length;
	}

	/**
	 * Method overridden from AbstractTableModel.
	 * It returns the number of rows in the table
	 * @return The number of rows in the table
	 */
	@Override
	public int getRowCount() {
		return NUM_ROWS;
	}

	/**
	 * Method overridden from AbstractTableModel.
	 * It returns the value at the table in the position
	 * specified by the parameters
	 * @param rowIndex The index of the row
	 * @param columnIndex The index of the column
	 * @return The value at the table in the position specified
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (rowIndex) {
		case 0:
			s = _colNames[columnIndex];
			break;
		case 1:
			if(columnIndex == 0) s = "Score";
			else s = rivals.getJSONObject(columnIndex - 1).getInt("score");
			break;
		}
		return s;
	}

	/**
	 * onRegister method overridden (RolitObserver interface).
	 */
	@Override
	public void onRegister(GameState status) {}

	/**
	 * onError method overridden (RolitObserver interface).
	 */
	@Override
	public void onError(String err) {
	}

	/**
	 * onGameStatusChange method overridden (RolitObserver interface).
	 */
	@Override
	public void onGameStatusChange(GameState state) {
	}
	
	/**
	 * onTurnPlayed method overridden (RolitObserver interface).
	 * Particularly, gets the new rivals from the given state and updates
	 * the table
	 */
	@Override
	public void onTurnPlayed(GameState state) {
		update();
		this.rivals = state.getRivals();
		
	}

	/**
	 * onGameFinished method overridden (RolitObserver interface).
	 */
	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival, Replay replay) {
	}

	/**
	 * onGameExited method overridden (RolitObserver interface).
	 */
	@Override
	public void onGameExited(Replay replay) {}

	/**
	 * onReplayLeftButton method overridden (ReplayObserver interface).
	 * Particularly, gets the new rivals from the replay and updates
	 * the table
	 */
	@Override
	public void onReplayLeftButton() {
		update();
		this.rivals = replay.getCurrentState().getRivals();
	}

	/**
	 * onReplayRightButton method overridden (ReplayObserver interface).
	 * Particularly, gets the new rivals from the replay and updates
	 * the table
	 */
	@Override
	public void onReplayRightButton() {
		update();
		this.rivals = replay.getCurrentState().getRivals();
	}

	/**
	 * onReplayStatusChange method overridden (ReplayObserver interface).
	 */
	@Override
	public void onReplayStatusChange(String msg) {
		
	}

}
