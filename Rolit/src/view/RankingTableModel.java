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

public abstract class RankingTableModel extends AbstractTableModel implements RolitObserver {

	protected Game game;
	protected String[] _colNames = {"Rivals", "Score"};
	protected List<String> rivals;
	private static RankingTableModel[] tables = {
			new ClassicRankingTableModel(),
			new TeamsRankingTableModel()
	};
	
	public RankingTableModel(Game game) {
		this.game = game;
		this.rivals = new ArrayList<>();
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
		return rivals.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = rowIndex;
			break;
		case 1:
			s = rivals.get(rowIndex);
			break;
		case 2:
			s = rivals.get(rowIndex);
			break;
		}
		return s;
	}

	public static RankingTable parse(String type) {
		
	}
	
	@Override
	public void onTurnPlayed(String name, Color color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameFinished() {}

	@Override
	public void onCommandIntroduced(Game game, Board board, Command command) {}

	@Override
	public void onReplayLeftButton(Game game, Board board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayRightButton(Game game, Board board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
