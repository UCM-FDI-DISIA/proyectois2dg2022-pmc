package view.GUIView;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.json.JSONArray;
import control.Controller;
import logic.Rival;
import replay.GameState;

public class RankingTableModel extends AbstractTableModel implements RolitObserver {

	private String[] _colNames;
	private JSONArray rivals;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameStatusChange(GameState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFirstPlay(GameState state) {}
	
	@Override
	public void onTurnPlayed(GameState state) {
		// TODO Auto-generated method stub
		update();
		this.rivals = state.getRivals();
	}

	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival) {
		// TODO Auto-generated method stub
		
	}

}
