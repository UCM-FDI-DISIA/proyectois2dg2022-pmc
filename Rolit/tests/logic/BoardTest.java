package logic;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class BoardTest {
	//El nombre de la función que se está probando aparece en el nombre de cada test
	
	@Test
	void addCubeInPos_trytoAdd() {
		Board b = new Board(Shape.CM);
		Player p1 = new Player(Color.GREEN, "delegau");
		Cube c2 = new Cube(6, 5, p1);
		b.addCubeInPos(c2);
		Cube c = new Cube(5, 5, p1);
		assertTrue(b.tryToAddCube(5, 5));
		assertFalse(b.tryToAddCube(30, 15));
		b.addCubeInPos(c);
		assertEquals(c, b.getCubeInPos(5, 5));
		b.clearOrderedCubeList();
	}
	
	@Test
	void board_full_numCubes() {
		Board b = new Board(Shape.SL);
		Player p1 = new Player(Color.GREEN, "Sergio");
		Cube c = new Cube(6, 5, p1);
		b.addCubeInPos(c);
		assertFalse(b.isBoardFull());
		b.clearOrderedCubeList();
	}
	
	@Test
	void update_test() {
		Board b = new Board(Shape.SL);
		Player p1 = new Player(Color.BEIGE, "Juandi");
		Player p2 = new Player(Color.BLUE, "Leo");
		Cube c = new Cube(3, 4, p2);
		b.addCubeInPos(c);
		b.update(c);
		Cube c1 = new Cube(4, 4, p1);
		b.addCubeInPos(c1);
		b.update(c1);
		Cube c2 = new Cube(5, 4, p1);
		b.addCubeInPos(c2);
		b.update(c2);
		Cube c3 = new Cube(6, 4, p1);
		b.addCubeInPos(c3);
		b.update(c3);
		Cube c4 = new Cube(7, 4, p2);
		b.addCubeInPos(c4);
		b.update(c4);
		assertEquals(Color.BLUE, c1.getColor());
		assertEquals(Color.BLUE, c2.getColor());
		assertEquals(Color.BLUE, c3.getColor());
		b.clearOrderedCubeList();
	}
	
	@Test
	void update_test2() {
		Board b = new Board(Shape.SM);
		Player p1 = new Player(Color.BEIGE, "Juandi");
		Player p2 = new Player(Color.BLUE, "Leo");
		Cube c = new Cube(3, 4, p2);
		b.addCubeInPos(c);
		b.update(c);
		Cube c1 = new Cube(4, 4, p1);
		b.addCubeInPos(c1);
		b.update(c1);
		Cube c3 = new Cube(6, 4, p1);
		assertThrows(IllegalArgumentException.class, () -> b.addCubeInPos(c3));
		b.update(c3);
		Cube c4 = new Cube(7, 4, p2);
		assertThrows(IllegalArgumentException.class, () -> b.addCubeInPos(c3));
		b.update(c4);
		assertEquals(Color.BEIGE, c1.getColor());
		b.clearOrderedCubeList();
	}
	
	@Test
	void test_report() {
		Board b = new Board(Shape.CL);
		Player p1 = new Player(Color.BEIGE, "Juandi");
		Player p2 = new Player(Color.BLUE, "Leo");
		Cube c = new Cube(3, 4, p2);
		b.addCubeInPos(c);
		b.update(c);
		Cube c1 = new Cube(4, 4, p1);
		b.addCubeInPos(c1);
		b.update(c1);
		Cube c2 = new Cube(5, 4, p1);
		b.addCubeInPos(c2);
		b.update(c2);
		Cube c3 = new Cube(6, 4, p1);
		b.addCubeInPos(c3);
		b.update(c3);
		Cube c4 = new Cube(7, 4, p2);
		b.addCubeInPos(c4);
		b.update(c4);
		
		String s = "{\"shape\":\"CL\",\"cubes\":[{\"color\":\"L\",\"pos\":[3,4]},{\"color\":\"L\",\"pos\":[4,4]},{\"color\":\"L\",\"pos\":[5,4]},{\"color\":\"L\",\"pos\":[6,4]},{\"color\":\"L\",\"pos\":[7,4]}]}";
		assertTrue (new JSONObject(s).similar(b.report()));
		b.clearOrderedCubeList();
	}
	
	@Test
	void test_report2() {
		Board b = new Board(Shape.SM);
		
		Player p1 = new Player(Color.BEIGE, "Juandi");
		Player p2 = new Player(Color.BLUE, "Leo");
		Cube c = new Cube(3, 4, p2);
		b.addCubeInPos(c);
		b.update(c);
		Cube c1 = new Cube(4, 4, p1);
		b.addCubeInPos(c1);
		b.update(c1);
		Cube c3 = new Cube(5, 4, p1);
		b.addCubeInPos(c3);
		b.update(c3);
		Cube c4 = new Cube(6, 4, p2);
		b.addCubeInPos(c4);
		b.update(c4);

		String s = "{\"shape\":\"SM\",\"cubes\":[{\"color\":\"L\",\"pos\":[3,4]},{\"color\":\"L\",\"pos\":[4,4]},{\"color\":\"L\",\"pos\":[5,4]},{\"color\":\"L\",\"pos\":[6,4]}]}";
		assertTrue (new JSONObject(s).similar(b.report()));
		b.clearOrderedCubeList();
	}
}
