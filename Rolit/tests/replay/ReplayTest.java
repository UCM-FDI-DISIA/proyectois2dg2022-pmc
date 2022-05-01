package replay;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import logic.Board;
import logic.Color;
import logic.Cube;
import logic.Game;
import logic.GameClassic;
import logic.Player;
import logic.Shape;

public class ReplayTest {
	@Test
	void test_1() {
		Board b = new Board(Shape.SL);
		Player p1 = new Player(Color.YELLOW, "Juandi");
		Player p2 = new Player(Color.BROWN, "Leo");
		Player p3 = new Player(Color.RED, "dani");
		List<Player> lp = new ArrayList<Player>();
		lp.add(p1);
		lp.add(p2);
		lp.add(p3);
		List<Cube> lc = new ArrayList<Cube>();
		Cube c = new Cube(3, 4, p2);
		Cube c2 = new Cube(3, 5, p1);
		lc.add(c);
		lc.add(c2);
		Game game = new GameClassic(b, lc, lp, Color.YELLOW);
		game.addCubeToQueue(new Cube(3,6, null));
		game.play();
		GameState state = new GameState("p", game);
		
		Replay replay = new Replay();
		replay.addState(state);
		
		String s = "{\"states\":[{\"game\":{\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"},{\"score\":0,\"color\":\"R\",\"name\":\"dani\"}],\"turn\":\"W\",\"type\":\"GameClassic\",\"board\":{\"shape\":\"SL\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]}},\"command\":\"p\"}]}";
		assertTrue (new JSONObject(s).similar(replay.report()));
	}
	
}
