package view.GUIView;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import controller.Controller;
import model.SaveLoadManager;
import model.logic.Color;
import model.logic.Rival;
import model.logic.Shape;
import model.replay.GameState;
import model.replay.Replay;
import view.RolitObserver;
import view.GUIView.RolitComponents.RolitPanel;

/**
 * This class is in charge of managing and updating all the CellGUIs
 * when needed.
 * @author PMC
 */

public class BoardGUI extends RolitPanel implements RolitObserver, ReplayObserver {
	private static final long serialVersionUID = 1L;
	private int nRows;
	private int nColumns;
	private volatile CellGUI[][] cells;
	private volatile Controller ctrl;
	private volatile GameState state;
	private Replay replay;
	private JSONObject lastCubeAdded;
	private JSONArray cubes;
	
	private final int BIG_SHAPE_BUTTON_LENGTH = 28;
	private final int MEDIUM_SHAPE_BUTTON_LENGTH = 36;
	private final int SMALL_SHAPE_BUTTON_LENGTH = 48;
	private final int MATRIX_LENGTH_FRONTIER_VALUE = 15;
	
	/**
	 * Constructor
	 * @param ctrl The controller
	 */
	public BoardGUI(Controller ctrl) {
		this.ctrl = ctrl;
		
		this.ctrl.addObserver(this);	

		boolean[][] shapeMatrix = SaveLoadManager.loadShape(Shape.valueOf(state.getShape()).getFilename());
		
		this.nRows = shapeMatrix.length;
		this.nColumns = shapeMatrix[0].length;
		
		this.cells = new CellGUI[nRows][nColumns];
		
		int sideButtonLength = shapeMatrixToSideButtonLength(shapeMatrix);
		
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nColumns; j++) {
				this.cells[i][j] = new CellGUI(i, j, shapeMatrix[i][j], ctrl, sideButtonLength);
			}
		}
		initGUI();
		this.update();
	}
	
	/**
	 * Constructor
	 * @param replay The replay
	 */
	public BoardGUI(Replay replay) {
		this.replay = replay;
		Shape shape = Shape.valueOf(replay.getShape());
		boolean[][] shapeMatrix  = SaveLoadManager.loadShape(shape.getFilename());
		this.nRows = shapeMatrix.length;
		this.nColumns = shapeMatrix[0].length;
		
		this.cells = new CellGUI[nRows][nColumns];
		
		int sideButtonLength = shapeMatrixToSideButtonLength(shapeMatrix);
		
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nColumns; j++) {
				this.cells[i][j] = new CellGUI(shapeMatrix[i][j], replay, sideButtonLength);
			}
		}
		
		updateReplay();

		initGUI();
		
		this.replay.addObserver(this);
	}
	
	/**
	 * Given a boolean matrix that define a shape, it is needed to determine
	 * whether the size of that matrix passes a given MATRIX_LENGTH_FRONTIER_VALUE
	 * size so that the length of all the CellGUI buttons needs to be resized in order
	 * to keep the application window at a reasonable size
	 * @param shapeMatrix The boolean matrix that define a shape
	 * @return Integer that specifies the size of each of the CellGUI.
	 */
	private int shapeMatrixToSideButtonLength(boolean[][] shapeMatrix) {
		if (shapeMatrix.length > MATRIX_LENGTH_FRONTIER_VALUE)
			return this.BIG_SHAPE_BUTTON_LENGTH;
		else if (shapeMatrix.length == MATRIX_LENGTH_FRONTIER_VALUE)
			return this.MEDIUM_SHAPE_BUTTON_LENGTH;
		else if (shapeMatrix.length < MATRIX_LENGTH_FRONTIER_VALUE)
			return this.SMALL_SHAPE_BUTTON_LENGTH;

		
		return 0;
	}
	
	/**
	 * This method is in charge of initializing and creating the board graphically using
	 * a GridBagLayout with CellGUIs.
	 */
	public void initGUI() {
		this.removeAll();
		GridBagLayout gridbag = new GridBagLayout(); //Queremos que el tamaño del tablero sea fijo
		this.setLayout(gridbag);
		
		for (int i = 0; i < nRows; i++) {
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = i;
			c.gridy = 0;
			
			for (int j = 0; j < nColumns; j++) {
				c.gridy = j;
				this.add(cells[i][j].getButton(), c);
			}
		}
				
		this.revalidate();
	}

	/**
	 * This method is in charge of updating all the CellGUIs
	 * given the new information retrieved from methods such as
	 * onRegister or onTurnPlayed.
	 */
	public void update() {
		cubes = state.getCubes();
		for (int i = 0; i < cubes.length(); i++) {
			JSONObject cube = cubes.getJSONObject(i);
			cells[cube.getJSONArray("pos").getInt(1)][cube.getJSONArray("pos").getInt(0)].update(Color.valueOfIgnoreCase(cube.getString("color").charAt(0)));
		}

		
	}

	/**
	 * onError method overridden (RolitObserver interface)
	 */
	@Override
	public void onError(String err) {
		
	}

	/**
	 * onReplayLeftButton method overridden (ReplayObserver interface)
	 * It is in charge of retrieving the state of the last cube added
	 * to its previous, so that the replay can go back, and then updating the replay
	 */
	@Override
	public void onReplayLeftButton() {
		if(lastCubeAdded != null) {
			JSONArray posLast = lastCubeAdded.getJSONArray("pos");
			cells[posLast.getInt(1)][posLast.getInt(0)].resetIcon();// Cuando vamos hacia atrás es necesario quitar los iconos que estuvieran de un estado posterior
		}
		
		updateReplay();
	}

	/**
	 * onReplayRightButton method overridden (ReplayObserver interface)
	 * It is in charge of updating the replay (already been loaded with a new state)
	 */
	@Override
	public void onReplayRightButton() {
		updateReplay();
	}
	
	/**
	 * This method is in charge of updating all the cells given the new state of
	 * a replay
	 */
	private void updateReplay() {
		JSONArray cubes = replay.getCurrentState().report().getJSONObject("game").getJSONObject("board").getJSONArray("cubes");
		for (int i = 0; i < cubes.length(); i++) {
			JSONArray pos = cubes.getJSONObject(i).getJSONArray("pos");
			char color = cubes.getJSONObject(i).getString("color").charAt(0);
			cells[pos.getInt(1)][pos.getInt(0)].update(model.logic.Color.valueOfIgnoreCase(color));
		}
		this.lastCubeAdded = cubes.getJSONObject(cubes.length() - 1);
	}

	/**
	 * onRegister method overridden (RolitObserver interface)
	 * It is in charge of updating the state with the new one
	 */
	@Override
	public void onRegister(GameState state) {
		this.state = state;
	}

	/**
	 * onTurnPlayed method overridden (RolitObserver interface)
	 * It is in charge of updating the state with the new one
	 * then calling update() method
	 */
	@Override
	public void onTurnPlayed(GameState state) {
		this.state = state;
		update();
	}

	/**
	 * onGameStatusChange method overridden (RolitObserver interface)
	 */
	@Override
	public void onGameStatusChange(GameState state) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * onReplayStatusChange method overridden (ReplayObserver interface)
	 */
	@Override
	public void onReplayStatusChange(String msg) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * onGameFinished method overridden (RolitObserver interface)
	 */
	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival, Replay replay) {
	}

	/**
	 * onGameExited method overridden (RolitObserver interface)
	 */
	@Override
	public void onGameExited(Replay replay) {}
	
}
