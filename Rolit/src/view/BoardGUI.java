package view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONObject;

import commands.Command;
import control.SaveLoadManager;
import logic.Board;
import logic.Color;
import logic.Game;
import logic.Shape;
import replay.Replay;

public class BoardGUI implements RolitObserver, ReplayObserver {

	private int nFilas;
	private int nColumnas;
	private CeldaGUI[][] celdas;
	private Game game;
	private Replay replay;
	private JSONObject lastCubeAdded;
	

	public BoardGUI(Game game) {
		this.game = game;

		boolean[][] shapeMatrix = game.getShapeMatrix();
		
		this.nFilas = shapeMatrix.length;
		this.nColumnas = shapeMatrix[0].length;
		
		this.celdas = new CeldaGUI[nFilas][nColumnas];
		
		int sideButtonLength = shapeMatrixToSideButtonLength(shapeMatrix);
		
		for (int i = 0; i < nFilas; i++) {
			for (int j = 0; j < nColumnas; j++) {
				this.celdas[i][j] = new CeldaGUI(i, j, shapeMatrix[i][j], game, sideButtonLength);
			}
		}

		this.game.addObserver(this);
	
	}
	
	public BoardGUI(Replay replay) {
		this.replay = replay;
		Shape shape = Shape.valueOf(replay.getShape());
		boolean[][] shapeMatrix  = SaveLoadManager.loadShape(shape.getFilename());
		this.nFilas = shapeMatrix.length;
		this.nColumnas = shapeMatrix[0].length;
		
		this.celdas = new CeldaGUI[nFilas][nColumnas];
		
		int sideButtonLength = shape.shapeToSideButtonLength();
		
		for (int i = 0; i < nFilas; i++) {
			for (int j = 0; j < nColumnas; j++) {
				this.celdas[i][j] = new CeldaGUI(i, j, shapeMatrix[i][j], replay, sideButtonLength);
			}
		}
		
		updateReplay();

		this.replay.addObserver(this);
	}
	
	private int shapeMatrixToSideButtonLength(boolean[][] shapeMatrix) {
		if (shapeMatrix.length > 15)
			return Shape.BIG_SHAPE_BUTTON_LENGTH;
		else if (shapeMatrix.length == 15)
			return Shape.MEDIUM_SHAPE_BUTTON_LENGTH;
		else if (shapeMatrix.length < 15)
			return Shape.SMALL_SHAPE_BUTTON_LENGTH;

		
		return 0;
	}
	
	public void crearTablero(JPanel panel) {
		panel.removeAll();
		GridBagLayout gridbag = new GridBagLayout(); //Queremos que el tamaño del tablero sea fijo
		panel.setLayout(gridbag);
		
		for (int i = 0; i < nFilas; i++) {
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = i;
			c.gridy = 0;
			
			for (int j = 0; j < nColumnas; j++) {
				c.gridy = j;
				panel.add(celdas[i][j].getButton(), c);
			}
		}
		panel.revalidate();
	}

	public void update(Game game, Board board) {
		for(int i = 0; i < nFilas; i++) {
			for(int j = 0; j < nColumnas; j++) {
				celdas[i][j].update();
			}
		}
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onCommandIntroduced(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayLeftButton() {
		if(lastCubeAdded != null) {
			JSONArray posLast = lastCubeAdded.getJSONArray("pos");
			celdas[posLast.getInt(0)][posLast.getInt(1)].resetIcon();// Cuando vamos hacia atrás es necesario quitar los iconos que estuvieran de un estado posterior
		}
		
		updateReplay();
	}

	@Override
	public void onReplayRightButton() {
		updateReplay();
	}
	
	private void updateReplay() {
		JSONArray cubes = replay.currentStateReport().getJSONObject("game").getJSONObject("board").getJSONArray("cubes");
		for (int i = 0; i < cubes.length(); i++) {
			JSONArray pos = cubes.getJSONObject(i).getJSONArray("pos");
			char color = cubes.getJSONObject(i).getString("color").charAt(0);
			celdas[pos.getInt(0)][pos.getInt(1)].update(logic.Color.valueOfIgnoreCase(color));
		}
		this.lastCubeAdded = cubes.getJSONObject(cubes.length() - 1);
	}

	@Override
	public void onRegister(Game game, Board board, Command command) {
		update(game, board);
	}

	@Override
	public void onGameFinished() {
		
	}

	@Override
	public void onTurnPlayed(String name, Color color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameStatusChange(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFirstPlay(String name, Color color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayStatusChange(String msg) {
		// TODO Auto-generated method stub
		
	}

	
}
