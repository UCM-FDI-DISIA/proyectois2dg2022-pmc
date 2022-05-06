package view.GUIView;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.json.JSONArray;

import controller.Controller;
import model.logic.Rival;
import model.replay.GameState;
import model.replay.Replay;
import view.RolitObserver;

public class RankingTableModel extends AbstractTableModel implements RolitObserver, ReplayObserver {

	private static final long serialVersionUID = 1L;
	
	private String[] _colNames;
	private JSONArray rivals;
	private Replay replay;
	public static final int NUM_ROWS = 2;
	
	public RankingTableModel(Controller ctrl, GameState state) {
		this.rivals = state.getRivals();
		this._colNames = new String[this.rivals.length() + 1];
		this._colNames[0] = state.getType();
		for(int i = 1; i <= this.rivals.length(); i++) {
			this._colNames[i] = this.rivals.getJSONObject(i - 1).getString("name");
		}
		ctrl.addObserver(this);
	}
	
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
	
	public void update() {
		fireTableDataChanged();
	}
	
	@Override
	public String getColumnName(int col) {
		return _colNames[col];
	}

	@Override
	public int getColumnCount() {
		return _colNames.length;
	}

	@Override
	public int getRowCount() {
		return NUM_ROWS;
	}

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

	@Override
	public void onRegister(GameState status) {}

	@Override
	public void onError(String err) {
	}

	@Override
	public void onGameStatusChange(GameState state) {
	}
	
	@Override
	public void onTurnPlayed(GameState state) {
		update();
		this.rivals = state.getRivals();
		
	}

	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival, Replay replay) {
	}

	@Override
	public void onGameExited(Replay replay) {}

	@Override
	public void onReplayLeftButton() {
		update();
		this.rivals = replay.getCurrentState().getRivals();
	}

	@Override
	public void onReplayRightButton() {
		update();
		this.rivals = replay.getCurrentState().getRivals();
	}

	@Override
	public void onReplayStatusChange(String msg) {
		
	}

}
