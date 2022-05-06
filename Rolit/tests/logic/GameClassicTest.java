package logic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import model.logic.Board;
import model.logic.Color;
import model.logic.Cube;
import model.logic.Game;
import model.logic.GameClassic;
import model.logic.Player;
import model.logic.Shape;

public class GameClassicTest {
	@Test
	void play_test() {
		Board b = new Board(Shape.SM);
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
		game.addCubeToQueue(new Cube(3,4, null));
		game.addCubeToQueue(new Cube(0,-1, null));
		assertThrows(IllegalArgumentException.class, () -> game.play());
		assertThrows(IllegalArgumentException.class, () -> game.play());
		game.addCubeToQueue(new Cube(3,6, null));
		game.play();
		assertEquals(b.getCubeInPos(3, 6).getColor(), Color.YELLOW);
	}
	
	@Test
	void test_report() {
		Board b = new Board(Shape.SM);
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
		String s = "{\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]},\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"},{\"score\":0,\"color\":\"R\",\"name\":\"dani\"}],\"turn\":\"W\",\"type\":\"GameClassic\"}";
		assertTrue (new JSONObject(s).similar(game.report()));
	}
	
	@Test
	void play_test2() {
		Board b = new Board(Shape.CM);
		Player p1 = new Player(Color.PURPLE, "Maria jose");
		Player p2 = new Player(Color.BLUE, "paco");
		Player p3 = new Player(Color.RED, "lucia la de cuenca");
		List<Player> lp = new ArrayList<Player>();
		lp.add(p1);
		lp.add(p2);
		lp.add(p3);
		List<Cube> lc = new ArrayList<Cube>();
		Cube c = new Cube(5, 7, p2);
		Cube c2 = new Cube(5, 6, p1);
		lc.add(c);
		lc.add(c2);
		Game game = new GameClassic(b, lc, lp, Color.PURPLE);
		game.addCubeToQueue(new Cube(3,4, null));
		game.addCubeToQueue(new Cube(100,5, null));
		assertThrows(IllegalArgumentException.class, () -> game.play());
		assertThrows(IllegalArgumentException.class, () -> game.play());
		game.addCubeToQueue(new Cube(5,5, null));
		game.play();
		assertEquals(b.getCubeInPos(5, 5).getColor(), Color.PURPLE);
	}
	
	@Test
	void test_report2() {
		Board b1 = new Board(Shape.SM);
		Player p1 = new Player(Color.PURPLE, "Maria jose");
		Player p2 = new Player(Color.BLUE, "paco");
		Player p3 = new Player(Color.RED, "lucia la de cuenca");
		List<Player> lp = new ArrayList<Player>();
		lp.add(p1);
		lp.add(p2);
		lp.add(p3);
		List<Cube> lc = new ArrayList<Cube>();
		Cube c = new Cube(5, 7, p2);
		Cube c2 = new Cube(5, 6, p1);
		lc.add(c);
		lc.add(c2);
		Game game = new GameClassic(b1, lc, lp, Color.BLUE);
		game.addCubeToQueue(new Cube(5,8, null));
		game.play();
		String s = "{\"players\":[{\"score\":1,\"color\":\"P\",\"name\":\"Maria jose\"},{\"score\":2,\"color\":\"L\",\"name\":\"paco\"},{\"score\":0,\"color\":\"R\",\"name\":\"lucia la de cuenca\"}],\"turn\":\"R\",\"type\":\"GameClassic\",\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"L\",\"pos\":[5,7]},{\"color\":\"P\",\"pos\":[5,6]},{\"color\":\"L\",\"pos\":[5,8]}]}}"; 
		assertTrue (new JSONObject(s).similar(game.report()));
	}
}
