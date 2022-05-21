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
import model.logic.GameTeams;
import model.logic.Player;
import model.logic.Shape;
import model.logic.Team;

public class GameTeamsTest {
	//se prueba play que es la función principal de la clase
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
		game.addCubeToQueue(new Cube(3,4, null));
		game.addCubeToQueue(new Cube(0,-1, null));
		assertThrows(IllegalArgumentException.class, () -> game.play());
		assertThrows(IllegalArgumentException.class, () -> game.play());
		game.addCubeToQueue(new Cube(3,6, null));
		game.play();
		assertEquals(b.getCubeInPos(3, 6).getColor(), Color.YELLOW);
	}
	
	//Comprobamos que el report final es igual al esperado
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
		game.addCubeToQueue(new Cube(3,6, null));
		game.play();
		String s = "{\"teams\":[{\"score\":3,\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"}],\"name\":\"equipo1\"},{\"score\":0,\"players\":[{\"score\":0,\"color\":\"R\",\"name\":\"dani\"}],\"name\":\"equipo2\"}],\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"}],\"turn\":\"W\",\"type\":\"GameTeams\",\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]}}";
		assertTrue (new JSONObject(s).similar(game.report()));
	}
	
	@Test
	void play_test2() {
		Board b = new Board(Shape.DL);
		Player p1 = new Player(Color.PURPLE, "pepita la de los palotes");
		Player p2 = new Player(Color.GREEN, "paco paco paco eh mi paco");
		Player p3 = new Player(Color.BLACK, "hellou kittaso");
		Player p4 = new Player(Color.RED, "hellou kittasa");
		List<Player> lp = new ArrayList<Player>();
		List<Player> equipo1 = new ArrayList<Player>();
		List<Player> equipo2 = new ArrayList<Player>();
		equipo1.add(p1);
		equipo1.add(p2);
		equipo2.add(p3);
		equipo2.add(p4);
		Team e1 = new Team("los locos del barrio", equipo1);
		Team e2 = new Team("pura calle modo diablo", equipo2);
		lp.add(p1);
		lp.add(p2);
		List<Cube> lc = new ArrayList<Cube>();
		List<Team> lt = new ArrayList<Team>();
		lt.add(e1);
		lt.add(e2);
		Cube c = new Cube(12, 13, p2);
		Cube c2 = new Cube(12, 12, p1);
		lc.add(c);
		lc.add(c2);
		Game game = new GameTeams(b, lc, lp, Color.PURPLE, lt);
		game.addCubeToQueue(new Cube(12,13, null));
		game.addCubeToQueue(new Cube(8, 9, null));
		assertThrows(IllegalArgumentException.class, () -> game.play());
		assertThrows(IllegalArgumentException.class, () -> game.play());
		game.addCubeToQueue(new Cube(12,14, null));
		game.play();
		assertEquals(b.getCubeInPos(12, 14).getColor(), Color.PURPLE);
	}
	
	@Test
	void test_report2() {
		Board b = new Board(Shape.DL);
		Player p1 = new Player(Color.PURPLE, "pepita la de los palotes");
		Player p2 = new Player(Color.GREEN, "paco paco paco eh mi paco");
		Player p3 = new Player(Color.BLACK, "hellou kittaso");
		Player p4 = new Player(Color.RED, "hellou kittasa");
		List<Player> lp = new ArrayList<Player>();
		List<Player> equipo1 = new ArrayList<Player>();
		List<Player> equipo2 = new ArrayList<Player>();
		equipo1.add(p1);
		equipo1.add(p2);
		equipo2.add(p3);
		equipo2.add(p4);
		Team e1 = new Team("los locos del barrio", equipo1);
		Team e2 = new Team("pura calle modo diablo", equipo2);
		lp.add(p1);
		lp.add(p2);
		List<Cube> lc = new ArrayList<Cube>();
		List<Team> lt = new ArrayList<Team>();
		lt.add(e1);
		lt.add(e2);
		Cube c = new Cube(12, 13, p2);
		Cube c2 = new Cube(12, 12, p1);
		lc.add(c);
		lc.add(c2);
		Game game = new GameTeams(b, lc, lp, Color.PURPLE, lt);
		game.addCubeToQueue(new Cube(12,14, null));
		game.play();
		String s = "{\"teams\":[{\"score\":3,\"players\":[{\"score\":3,\"color\":\"P\",\"name\":\"pepita la de los palotes\"},{\"score\":0,\"color\":\"G\",\"name\":\"paco paco paco eh mi paco\"}],\"name\":\"los locos del barrio\"},{\"score\":0,\"players\":[{\"score\":0,\"color\":\"B\",\"name\":\"hellou kittaso\"},{\"score\":0,\"color\":\"R\",\"name\":\"hellou kittasa\"}],\"name\":\"pura calle modo diablo\"}],\"players\":[{\"score\":3,\"color\":\"P\",\"name\":\"pepita la de los palotes\"},{\"score\":0,\"color\":\"G\",\"name\":\"paco paco paco eh mi paco\"}],\"turn\":\"G\",\"type\":\"GameTeams\",\"board\":{\"shape\":\"DL\",\"cubes\":[{\"color\":\"P\",\"pos\":[12,13]},{\"color\":\"P\",\"pos\":[12,12]},{\"color\":\"P\",\"pos\":[12,14]}]}}";
		assertTrue (new JSONObject(s).similar(game.report()));
	}
	
	@Test
	void play_test3() {
		Board b = new Board(Shape.SL);
		Player p1 = new Player(Color.BLUE, "kelokeeee");
		Player p2 = new Player(Color.ORANGE, "no se que mas poner");
		Player p3 = new Player(Color.BLACK, "socorro");
		Player p4 = new Player(Color.RED, "tengo mieo");
		List<Player> lp = new ArrayList<Player>();
		List<Player> equipo1 = new ArrayList<Player>();
		List<Player> equipo2 = new ArrayList<Player>();
		equipo1.add(p1);
		equipo1.add(p2);
		equipo1.add(p3);
		equipo2.add(p4);
		Team e1 = new Team("equipo perdedor", equipo1);
		Team e2 = new Team("beti beti", equipo2);
		lp.add(p1);
		lp.add(p2);
		List<Cube> lc = new ArrayList<Cube>();
		List<Team> lt = new ArrayList<Team>();
		lt.add(e1);
		lt.add(e2);
		Cube c = new Cube(3, 13, p2);
		Cube c2 = new Cube(3, 12, p1);
		lc.add(c);
		lc.add(c2);
		Game game = new GameTeams(b, lc, lp, Color.BLUE, lt);
		game.addCubeToQueue(new Cube(3,14, null));
		game.play();
		assertEquals(b.getCubeInPos(3, 14).getColor(), Color.BLUE);
	}
	
	@Test
	void test_report3() {
		Board b = new Board(Shape.SL);
		Player p1 = new Player(Color.BLUE, "kelokeeee");
		Player p2 = new Player(Color.ORANGE, "no se que mas poner");
		Player p3 = new Player(Color.BLACK, "socorro");
		Player p4 = new Player(Color.RED, "tengo mieo");
		List<Player> lp = new ArrayList<Player>();
		List<Player> equipo1 = new ArrayList<Player>();
		List<Player> equipo2 = new ArrayList<Player>();
		equipo1.add(p1);
		equipo1.add(p2);
		equipo1.add(p3);
		equipo2.add(p4);
		Team e1 = new Team("equipo perdedor", equipo1);
		Team e2 = new Team("beti beti", equipo2);
		lp.add(p1);
		lp.add(p2);
		List<Cube> lc = new ArrayList<Cube>();
		List<Team> lt = new ArrayList<Team>();
		lt.add(e1);
		lt.add(e2);
		Cube c = new Cube(3, 13, p2);
		Cube c2 = new Cube(3, 12, p1);
		lc.add(c);
		lc.add(c2);
		Game game = new GameTeams(b, lc, lp, Color.BLUE, lt);
		game.addCubeToQueue(new Cube(3,13, null));
		game.addCubeToQueue(new Cube(-5,500, null));
		assertThrows(IllegalArgumentException.class, () -> game.play());
		assertThrows(IllegalArgumentException.class, () -> game.play());
		game.addCubeToQueue(new Cube(3,14, null));
		game.play();
		String s = "{\"teams\":[{\"score\":3,\"players\":[{\"score\":3,\"color\":\"L\",\"name\":\"kelokeeee\"},{\"score\":0,\"color\":\"O\",\"name\":\"no se que mas poner\"},{\"score\":0,\"color\":\"B\",\"name\":\"socorro\"}],\"name\":\"equipo perdedor\"},{\"score\":0,\"players\":[{\"score\":0,\"color\":\"R\",\"name\":\"tengo mieo\"}],\"name\":\"beti beti\"}],\"players\":[{\"score\":3,\"color\":\"L\",\"name\":\"kelokeeee\"},{\"score\":0,\"color\":\"O\",\"name\":\"no se que mas poner\"}],\"turn\":\"O\",\"type\":\"GameTeams\",\"board\":{\"shape\":\"SL\",\"cubes\":[{\"color\":\"L\",\"pos\":[3,13]},{\"color\":\"L\",\"pos\":[3,12]},{\"color\":\"L\",\"pos\":[3,14]}]}}";
		assertTrue (new JSONObject(s).similar(game.report()));
	}
}
