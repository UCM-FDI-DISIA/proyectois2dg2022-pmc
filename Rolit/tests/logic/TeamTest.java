package logic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class TeamTest {
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
	
	@Test
	void test_report() {
		Player p1 = new Player(Color.YELLOW, "Chacon Chacon");
		Player p2 = new Player(Color.RED, "Leonardo Macias Pasteles");
		List<Player> l = new ArrayList<Player>();
		l.add(p1); l.add(p2);
		Team equipo1 = new Team("Basados", l);
		String s = "{\"players\":[{\"color\":\"Y\",\"name\":\"Chacon Chacon\"},{\"color\":\"R\",\"name\":\"Leonardo Macias Pasteles\"}],\"name\":\"Basados\"}";
		assertTrue (new JSONObject(s).similar(equipo1.report()));
	}
}
