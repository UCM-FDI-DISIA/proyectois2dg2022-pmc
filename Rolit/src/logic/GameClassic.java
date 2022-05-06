package logic;

import java.util.List;
import org.json.JSONObject;
import replay.GameState;
import utils.StringUtils;
import view.RolitObserver;

public class GameClassic extends Game {
	
	// Constructor de copia para generar los estados de las replays
	public GameClassic(GameClassic game) {
		super(game);
	}
	
	// Constructor de creaci�n a partir de carga
	public GameClassic(Board board, List<Cube> list_cubes, List<Player> list_players, Color currentPlayerColor) {
		super(board, list_cubes, list_players, currentPlayerColor);
	}
	
	@Override
	public Game copyMe() {
		return new GameClassic(this);
	}
	
	@Override
	public void play() throws IllegalArgumentException {
		if (!this.pendingCubes.isEmpty()) {
			Cube c = this.pendingCubes.poll();
			
			// En caso de poderse, ponemos el cubo en la posicion y actualizamos el tablero
			Cube newCube = new Cube(c.getX(), c.getY(), players.get(turnManager.getCurrentPlayerIndex()));
			this.board.addCubeInPos(newCube);
			
			this.board.update(newCube);
			

			//Comprobamos si la partida termina con este turno
			this.finished = board.isBoardFull();
			if (this.finished) {
				onTurnPlayed();
				try {
					Thread.sleep(500); // añadimos tiempo de espera para que se coloque ultimo cubo
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.onGameFinished();
			}
			// Cambiamos el turno al siguiente jugador en la lista si la partida no ha terminado
			else {
				onTurnPlayed();
				Cube nextCube = this.turnManager.nextTurn(this.state);//FIXME Se crea tambien en el onTurnPlayed
				if(nextCube != null) this.addCubeToQueue(nextCube);
				
				
				this.executedTurn = true;
			}
			
			
			
			this.state = new GameState("p " + newCube.getX() + " " + newCube.getY(), this);
			//Añadimos el estado actual a replay
			replay.addState(new GameState("p " + newCube.getX() + " " + newCube.getY(),copyMe()));
				
			
		}		
	}
	
	@Override
	public String toString() {
		StringBuilder bf = new StringBuilder();
		bf.append("Turno: ");
		bf.append(getCurrentPlayer().toString());
		bf.append(StringUtils.LINE_SEPARATOR);
		bf.append(board.toString());
		return bf.toString();
	}
	
	@Override
	public JSONObject report() {
		JSONObject gameJSONObject = super.report();
		gameJSONObject.put("type", "GameClassic");
		return gameJSONObject;
	}
	
	@Override
	public void onTurnPlayed() {
		for(RolitObserver o : observers) {
			o.onTurnPlayed(this.state);
		}
	}

	@Override
	protected void onGameFinished() {
		for (RolitObserver o : this.observers)
			o.onGameFinished(this.players, "Players", replay);		
	}
	
}

