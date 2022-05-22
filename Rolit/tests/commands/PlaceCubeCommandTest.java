package commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import model.commands.PlaceCubeCommand;


public class PlaceCubeCommandTest {
	//It checks that an exception is thrown correctly in the Place Cube Command 
	@Test
	void parseTest() {
		PlaceCubeCommand placeCube = new PlaceCubeCommand();
		String[] s = {"p", "4"};
		assertThrows(IllegalArgumentException.class, () -> placeCube.parse(s));
		String[] t = {"p","b","c"};
		assertThrows(IllegalArgumentException.class, () -> placeCube.parse(t));
	}

}


