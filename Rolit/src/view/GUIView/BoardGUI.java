package view.GUIView;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.BoxLayout;
import org.json.JSONArray;
import org.json.JSONObject;
import control.Controller;
import control.SaveLoadManager;
import logic.Color;
import logic.Rival;
import logic.Shape;
import replay.Replay;
import replay.GameState;
import view.GUIView.RolitComponents.RolitPanel;

public class BoardGUI extends RolitPanel implements RolitObserver, ReplayObserver {
	private static final long serialVersionUID = 1L;
	private int nFilas;
	private int nColumnas;
	private volatile CeldaGUI[][] celdas;
	private volatile Controller ctrl;
	private volatile GameState state;
	private Replay replay;
	private JSONObject lastCubeAdded;
	private JSONArray cubes;
	
	private final int BIG_SHAPE_BUTTON_LENGTH = 28;
	private final int MEDIUM_SHAPE_BUTTON_LENGTH = 36;
	private final int SMALL_SHAPE_BUTTON_LENGTH = 48;
	private final int MATRIX_LENGTH_FRONTIER_VALUE = 15;
	
	public BoardGUI(Controller ctrl) {
		this.ctrl = ctrl;
		
		this.ctrl.addObserver(this);	

		boolean[][] shapeMatrix = SaveLoadManager.loadShape(Shape.valueOf(state.getShape()).getFilename());
		
		this.nFilas = shapeMatrix.length;
		this.nColumnas = shapeMatrix[0].length;
		
		this.celdas = new CeldaGUI[nFilas][nColumnas];
		
		int sideButtonLength = shapeMatrixToSideButtonLength(shapeMatrix);
		
		for (int i = 0; i < nFilas; i++) {
			for (int j = 0; j < nColumnas; j++) {
				this.celdas[i][j] = new CeldaGUI(i, j, shapeMatrix[i][j], ctrl, sideButtonLength);
			}
		}
		initGUI();
		this.update();
	}
	
	public BoardGUI(Replay replay) {
		this.replay = replay;
		Shape shape = Shape.valueOf(replay.getShape());
		boolean[][] shapeMatrix  = SaveLoadManager.loadShape(shape.getFilename());
		this.nFilas = shapeMatrix.length;
		this.nColumnas = shapeMatrix[0].length;
		
		this.celdas = new CeldaGUI[nFilas][nColumnas];
		
		int sideButtonLength = shapeMatrixToSideButtonLength(shapeMatrix);
		
		for (int i = 0; i < nFilas; i++) {
			for (int j = 0; j < nColumnas; j++) {
				this.celdas[i][j] = new CeldaGUI(i, j, shapeMatrix[i][j], replay, sideButtonLength);
			}
		}
		
		updateReplay();

		initGUI();
		
		this.replay.addObserver(this);
	}
	
	private int shapeMatrixToSideButtonLength(boolean[][] shapeMatrix) {
		if (shapeMatrix.length > MATRIX_LENGTH_FRONTIER_VALUE)
			return this.BIG_SHAPE_BUTTON_LENGTH;
		else if (shapeMatrix.length == MATRIX_LENGTH_FRONTIER_VALUE)
			return this.MEDIUM_SHAPE_BUTTON_LENGTH;
		else if (shapeMatrix.length < MATRIX_LENGTH_FRONTIER_VALUE)
			return this.SMALL_SHAPE_BUTTON_LENGTH;

		
		return 0;
	}
	
	public void initGUI() {
		this.removeAll();
		GridBagLayout gridbag = new GridBagLayout(); //Queremos que el tamaño del tablero sea fijo
		this.setLayout(gridbag);
		
		for (int i = 0; i < nFilas; i++) {
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = i;
			c.gridy = 0;
			
			for (int j = 0; j < nColumnas; j++) {
				c.gridy = j;
				this.add(celdas[i][j].getButton(), c);
			}
		}
				
		this.revalidate();
	}

	public void update() {
		cubes = state.getCubes();
		for (int i = 0; i < cubes.length(); i++) {
			JSONObject cube = cubes.getJSONObject(i);
			celdas[cube.getJSONArray("pos").getInt(1)][cube.getJSONArray("pos").getInt(0)].update(Color.valueOfIgnoreCase(cube.getString("color").charAt(0)));
		}

		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayLeftButton() {
		if(lastCubeAdded != null) {
			JSONArray posLast = lastCubeAdded.getJSONArray("pos");
			celdas[posLast.getInt(1)][posLast.getInt(0)].resetIcon();// Cuando vamos hacia atrás es necesario quitar los iconos que estuvieran de un estado posterior
		}
		
		updateReplay();
	}

	@Override
	public void onReplayRightButton() {
		updateReplay();
	}
	
	private void updateReplay() {
		JSONArray cubes = replay.getCurrentState().report().getJSONObject("game").getJSONObject("board").getJSONArray("cubes");
		for (int i = 0; i < cubes.length(); i++) {
			JSONArray pos = cubes.getJSONObject(i).getJSONArray("pos");
			char color = cubes.getJSONObject(i).getString("color").charAt(0);
			celdas[pos.getInt(1)][pos.getInt(0)].update(logic.Color.valueOfIgnoreCase(color));
		}
		this.lastCubeAdded = cubes.getJSONObject(cubes.length() - 1);
	}

	public GameState getState() {
		return this.state;
	}
	
	@Override
	public void onRegister(GameState state) {
		this.state = state;
	}

	@Override
	public void onTurnPlayed(GameState state) {
		this.state = state;
		update();
	}

	@Override
	public void onGameStatusChange(GameState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayStatusChange(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival, Replay replay) {
	}

	@Override
	public void onFirstPlay(GameState state) {}

	@Override
	public void onGameExited(Replay replay) {}
	
}
