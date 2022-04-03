package logic;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
 
public class PlayerTest {
	@Test
	void add_score_test() {
	Player p1= new Player(Color.BLACK, "dani");
	Player p2= new Player(Color.BLUE, "juan diego");
	
	p1.addScore(1);
	
	assertEquals(1,p1.getScore());
	
	Cube c1 = new Cube(1,0,p1);
	c1.changeOwner(Color.BLUE);
	assertEquals(0, p1.getScore());
	assertEquals(1, p2.getScore());
	}
	
	@Test
	void test_report() {
		Player p1 = new Player(Color.PINK, "mar");
		Player p2 = new Player(Color.RED, "Juan Diego");
		
		String s = "{\"name\":mar,\"color\":K}";
		assertTrue (new JSONObject(s).similar(p1.report()));
		String s2 = "{\"name\":Juan Diego,\"color\":R}";
		assertTrue (new JSONObject(s2).similar(p2.report()));
	}
}
