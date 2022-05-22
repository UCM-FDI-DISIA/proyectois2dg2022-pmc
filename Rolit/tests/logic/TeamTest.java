package logic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import model.logic.Color;
import model.logic.Player;
import model.logic.Team;

public class TeamTest {
	
	//We check that the update method works correctly
	@Test
	void update_test() {
		Player p1 = new Player(Color.YELLOW, "Chacon Chacon");
		Player p2 = new Player(Color.RED, "Leonardo Macias Pasteles");
		List<Player> l = new ArrayList<Player>();
		l.add(p1); l.add(p2);
		Team equipo1 = new Team("Basados", l);
		p1.addScore(5);
		p2.addScore(3);
		equipo1.update();
		assertEquals(8, equipo1.getScore());
	}
	
	//We check that the final report equals the expected one
	@Test
	void test_report() {
		Player p1 = new Player(Color.YELLOW, "Chacon Chacon");
		Player p2 = new Player(Color.RED, "Leonardo Macias Pasteles");
		List<Player> l = new ArrayList<Player>();
		l.add(p1); l.add(p2);
		Team equipo1 = new Team("Basados", l);
		String s = "{\"score\":0,\"players\":[{\"score\":0,\"color\":\"Y\",\"name\":\"Chacon Chacon\"},{\"score\":0,\"color\":\"R\",\"name\":\"Leonardo Macias Pasteles\"}],\"name\":\"Basados\"}";
		assertTrue (new JSONObject(s).similar(equipo1.report()));
	}
	
	//We check that the final report equals the expected one
		@Test
		void test_report2() {
			Player p1 = new Player(Color.BROWN, "uwuuuuuuuu");
			Player p2 = new Player(Color.ORANGE, "no quiero estudiar");
			List<Player> l = new ArrayList<Player>();
			l.add(p1); l.add(p2);
			Team equipo1 = new Team("buenisimos", l);
			String s = "{\"score\":0,\"players\":[{\"score\":0,\"color\":\"W\",\"name\":\"uwuuuuuuuu\"},{\"score\":0,\"color\":\"O\",\"name\":\"no quiero estudiar\"}],\"name\":\"buenisimos\"}";
			assertTrue (new JSONObject(s).similar(equipo1.report()));
		}
	
}
