package replay;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import model.logic.Board;
import model.logic.Color;
import model.logic.Cube;
import model.logic.Game;
import model.logic.GameClassic;
import model.logic.Player;
import model.logic.Shape;
import model.replay.GameState;
import model.replay.Replay;

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
	
	
	@Test
	void test_2() {
		Board b = new Board(Shape.DL);
		Player p1 = new Player(Color.ORANGE, "werfj");
		Player p2 = new Player(Color.BLUE, "Lesdhfe wkerrnfo");
		Player p3 = new Player(Color.BLACK, "djjjjjjd jdj");
		List<Player> lp = new ArrayList<Player>();
		lp.add(p1);
		lp.add(p2);
		lp.add(p3);
		List<Cube> lc = new ArrayList<Cube>();
		Cube c = new Cube(8, 9, p2);
		Cube c2 = new Cube(8, 8, p1);
		lc.add(c);
		lc.add(c2);
		Game game = new GameClassic(b, lc, lp, Color.ORANGE);
		game.addCubeToQueue(new Cube(8,7, null));
		game.play();
		GameState state = new GameState("p", game);
		
		Replay replay = new Replay();
		replay.addState(state);
		
		String s = "{\"states\":[{\"game\":{\"players\":[{\"score\":2,\"color\":\"O\",\"name\":\"werfj\"},{\"score\":1,\"color\":\"L\",\"name\":\"Lesdhfe wkerrnfo\"},{\"score\":0,\"color\":\"B\",\"name\":\"djjjjjjd jdj\"}],\"turn\":\"L\",\"type\":\"GameClassic\",\"board\":{\"shape\":\"DL\",\"cubes\":[{\"color\":\"L\",\"pos\":[8,9]},{\"color\":\"O\",\"pos\":[8,8]},{\"color\":\"O\",\"pos\":[8,7]}]}},\"command\":\"p\"}]}";
		assertTrue (new JSONObject(s).similar(replay.report()));
	}
}
