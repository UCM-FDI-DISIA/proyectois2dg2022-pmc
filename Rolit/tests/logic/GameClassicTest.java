package logic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class GameClassicTest {
	//comprueba que la lista de players no se pueda modificar
	@Test
	void getPlayertest() {
		Board b = new Board(Shape.SM);
		Player p1 = new Player(Color.YELLOW, "Juandi");
		Player p2 = new Player(Color.YELLOW, "Leo");
		Player p3 = new Player(Color.RED, "dani");
		List<Player> lp = new ArrayList<Player>();
		List<Player> equipo1 = new ArrayList<Player>();
		List<Player> equipo2 = new ArrayList<Player>();
		equipo1.add(p1);
		equipo1.add(p2);
		equipo2.add(p3);
		Team e1 = new Team("equipo1", Color.YELLOW, l);
		Team e2 = new Team("equipo2", Color.YELLOW, l);
		lp.add(p1);
		lp.add(p2);
		List<Cube> lc = new ArrayList<Cube>();
		Cube c = new Cube(3, 4, p2);
		Cube c2 = new Cube(3, 5, p1);
		lc.add(c);
		lc.add(c2);
		Game game = new GameClassic(b, lc, lp, Color.BEIGE);
		List<Player> lp2 = game.getPlayers();
		assertThrows(UnsupportedOperationException.class, () -> lp2.add(p3));
	}
	
	@Test
	void play_test() {
		Board b = new Board(Shape.DS);
		Player p1 = new Player(Color.BEIGE, "Juandi");
		Player p2 = new Player(Color.BLACK, "Leo");
		Player p3 = new Player(Color.YELLOW, "dani");
		List<Player> lp = new ArrayList<Player>();
		lp.add(p1);
		lp.add(p2);
		lp.add(p3);
		List<Cube> lc = new ArrayList<Cube>();
		Cube c = new Cube(3, 4, p2);
		Cube c2 = new Cube(3, 5, p1);
		lc.add(c);
		lc.add(c2);
		Game game = new GameClassic(b, lc, lp, Color.BEIGE);
		assertFalse(game.play(3, 4));
		assertFalse(game.play(0, -1));
		assertTrue(game.play(3,6));
		assertEquals(b.getCubeInPos(3, 6).getColor(), Color.BEIGE);
	}
	
	@Test
	void test_report() {
		Board b = new Board(Shape.DS);
		Player p1 = new Player(Color.BEIGE, "Juandi");
		Player p2 = new Player(Color.BLACK, "Leo");
		Player p3 = new Player(Color.YELLOW, "dani");
		List<Player> lp = new ArrayList<Player>();
		lp.add(p1);
		lp.add(p2);
		lp.add(p3);
		List<Cube> lc = new ArrayList<Cube>();
		Cube c = new Cube(3, 4, p2);
		Cube c2 = new Cube(3, 5, p1);
		lc.add(c);
		lc.add(c2);
		Game game = new GameClassic(b, lc, lp, Color.BEIGE);
		game.play(3,6);
		
		String s = "{\"players\":[{\"color\":\"E\",\"name\":\"Juandi\"},{\"color\":\"B\",\"name\":\"Leo\"},{\"color\":\"Y\",\"name\":\"dani\"}],\"turn\":\"BLACK\",\"type\":\"GameClassic\",\"board\":{\"shape\":\"DS\",\"cubes\":[{\"color\":\"B\",\"pos\":[3,4]},{\"color\":\"E\",\"pos\":[3,5]},{\"color\":\"E\",\"pos\":[3,6]}]}}";
		assertTrue (new JSONObject(s).similar(game.report()));
	}
}
