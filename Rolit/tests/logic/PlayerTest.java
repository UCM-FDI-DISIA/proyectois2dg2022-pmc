package logic;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
 
public class PlayerTest {
	//comprueba que se añada adecuadamente el score
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
	
	//prueba que el report final sea igual al deseado
	@Test
	void test_report() {
		Player p1 = new Player(Color.PINK, "mar");
		Player p2 = new Player(Color.BLUE, "Juan Diego");
		
		p1.addScore(1);
		
		assertEquals(1,p1.getScore());
		
		Cube c1 = new Cube(1,0,p1);
		c1.changeOwner(Color.BLUE);
		
		String s = "{\"score\":0,\"color\":\"K\",\"name\":\"mar\"}";
		assertTrue (new JSONObject(s).similar(p1.report()));
		String s2 = "{\"score\":1,\"color\":\"L\",\"name\":\"Juan Diego\"}";
		assertTrue (new JSONObject(s2).similar(p2.report()));
	}
	
	//prueba que el report final sea igual al deseado
	@Test
	void test_report2() {
		Player p1 = new Player(Color.BROWN, "hehehehhehehehhe");
		Player p2 = new Player(Color.BLACK, "BARRADO garbanzo");
		
		p1.addScore(5);
		
		assertEquals(5,p1.getScore());
		
		Cube c1 = new Cube(1,0,p1);
		c1.changeOwner(Color.BLACK);
		
		String s = "{\"score\":4,\"color\":\"W\",\"name\":\"hehehehhehehehhe\"}";
		assertTrue (new JSONObject(s).similar(p1.report()));
		String s2 = "{\"score\":1,\"color\":\"B\",\"name\":\"BARRADO garbanzo\"}";
		assertTrue (new JSONObject(s2).similar(p2.report()));
	}
}

