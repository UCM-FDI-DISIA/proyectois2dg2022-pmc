package logic;

 import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import model.logic.Color;
import model.logic.Cube;
import model.logic.Player;
 
public class CubeTest {
	//The name of the method that is being checked appears in the name of each test
	
	@Test
	void change_owner() {
		Player p1 = new Player(Color.PINK, "mar");
		Cube c1 = new Cube(3,4, p1);
		
		c1.changeOwner(Color.PURPLE);
		assertEquals(Color.PURPLE, c1.getColor());
	}
	
	@Test
	void test_report() {
		Player p1 = new Player(Color.PINK, "mar");
		Cube c1 = new Cube(3,4, p1);
		
		String s = "{\"color\":K,\"pos\":[3,4]}";
		assertTrue (new JSONObject(s).similar(c1.report()));
	}
}
