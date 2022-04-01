package logic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class GameTeamsTest {
	@Test
	void play_test() {
		Board b = new Board(Shape.SM);
		Player p1 = new Player(Color.YELLOW, "Juandi");
		Player p2 = new Player(Color.BROWN, "Leo");
		Player p3 = new Player(Color.RED, "dani");
		List<Player> lp = new ArrayList<Player>();
		List<Player> equipo1 = new ArrayList<Player>();
		List<Player> equipo2 = new ArrayList<Player>();
		equipo1.add(p1);
		equipo1.add(p2);
		equipo2.add(p3);
		Team e1 = new Team("equipo1", equipo1);
		Team e2 = new Team("equipo2", equipo2);
		lp.add(p1);
		lp.add(p2);
		List<Cube> lc = new ArrayList<Cube>();
		List<Team> lt = new ArrayList<Team>();
		lt.add(e1);
		lt.add(e2);
		Cube c = new Cube(3, 4, p2);
		Cube c2 = new Cube(3, 5, p1);
		lc.add(c);
		lc.add(c2);
		Game game = new GameTeams(b, lc, lp, Color.YELLOW, lt);
		game.play(3, 6);
		
		assertFalse(game.play(3, 4));
		assertFalse(game.play(0, -1));
		assertTrue(game.play(3,6));
		assertEquals(b.getCubeInPos(3, 6).getColor(), Color.YELLOW);
		assertEquals(1, e1.getScore());
	}
	
	@Test
	void test_report() {
		Board b = new Board(Shape.SM);
		Player p1 = new Player(Color.YELLOW, "Juandi");
		Player p2 = new Player(Color.BROWN, "Leo");
		Player p3 = new Player(Color.RED, "dani");
		List<Player> lp = new ArrayList<Player>();
		List<Player> equipo1 = new ArrayList<Player>();
		List<Player> equipo2 = new ArrayList<Player>();
		equipo1.add(p1);
		equipo1.add(p2);
		equipo2.add(p3);
		Team e1 = new Team("equipo1", equipo1);
		Team e2 = new Team("equipo2", equipo2);
		lp.add(p1);
		lp.add(p2);
		List<Cube> lc = new ArrayList<Cube>();
		List<Team> lt = new ArrayList<Team>();
		lt.add(e1);
		lt.add(e2);
		Cube c = new Cube(3, 4, p2);
		Cube c2 = new Cube(3, 5, p1);
		lc.add(c);
		lc.add(c2);
		Game game = new GameTeams(b, lc, lp, Color.YELLOW, lt);
		game.play(3, 6);
		
		String s = "{\"teams\":[{\"players\":[{\"color\":\"Y\",\"name\":\"Juandi\"},{\"color\":\"W\",\"name\":\"Leo\"}],\"name\":\"equipo1\"},{\"players\":[{\"color\":\"R\",\"name\":\"dani\"}],\"name\":\"equipo2\"}],\"players\":[{\"color\":\"Y\",\"name\":\"Juandi\"},{\"color\":\"W\",\"name\":\"Leo\"}],\"turn\":\"BROWN\",\"type\":\"GameTeams\",\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]}}";
		assertTrue (new JSONObject(s).similar(game.report()));
	}
}
