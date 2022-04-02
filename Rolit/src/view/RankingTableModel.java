package view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import commands.Command;
import logic.Board;
import logic.Color;
import logic.Game;
import logic.Player;
import logic.Rival;

public class RankingTableModel extends AbstractTableModel implements RolitObserver {

	private Game game;
	private String[] _colNames;
	private List<Rival> rivals;
	public static final int NUM_ROWS = 2;
	
	public RankingTableModel(Game game) {
		this.game = game;
		this.rivals = game.getRivals();
		this._colNames = new String[this.rivals.size() + 1];
		this._colNames[0] = this.rivals.get(0).getType() + "s";
		for(int i = 1; i <= this.rivals.size(); i++) {
			this._colNames[i] = this.rivals.get(i - 1).getName();
		}
		game.addObserver(this);
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
			else s = rivals.get(columnIndex - 1).getScore();
			break;
		}
		return s;
	}

	@Override
	public void onTurnPlayed(String name, Color color) {
		update();
	}

	@Override
	public void onGameFinished() {}

	@Override
	public void onCommandIntroduced(Game game, Board board, Command command) {}

	@Override
	public void onRegister(Game game, Board board, Command command) {}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChange(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFirstPlay(String name, Color color) {
		// TODO Auto-generated method stub
		
	}

}
