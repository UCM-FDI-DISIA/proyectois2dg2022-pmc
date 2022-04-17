package commands;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import logic.Board;
import logic.Color;
import logic.Cube;
import logic.GameClassic;
import logic.Player;
import logic.Shape;

public class ExitCommandTest {
@Test

	void executeTest() throws Exception {
	Shape shape = Shape.CL;
	Board board1 = new Board(shape);
	Player p1 = new Player(Color.PINK, "mar");
	Cube c1 = new Cube(3,4, p1);
	Player p2 = new Player(Color.BLUE, "Leo");
	Cube c2 = new Cube(4, 4, p2);
	
	List<Cube> listCubes = new ArrayList<Cube>();
	listCubes.add(c1);
	listCubes.add(c2);
	
	List<Player> listPlayers = new ArrayList<Player>();
	listPlayers.add(p1);
	listPlayers.add(p2);
	
	Color currColorPlayer = Color.BLUE;
	
	GameClassic game = new GameClassic(board1,listCubes,listPlayers,currColorPlayer);
	
	ExitCommand exit = new ExitCommand();
	
	exit.execute(game);
	
	assertTrue(game.exited());
	}

}
