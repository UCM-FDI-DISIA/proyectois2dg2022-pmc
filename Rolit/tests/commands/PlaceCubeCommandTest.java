package commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;


public class PlaceCubeCommandTest {
	//prueba que se lance excepción adecuadamente en el comando de poner un cubo 
	@Test
	void parseTest() {
		PlaceCubeCommand placeCube = new PlaceCubeCommand();
		String[] s = {"p", "4"};
		assertThrows(IllegalArgumentException.class, () -> placeCube.parse(s));
		String[] t = {"p","b","c"};
		assertThrows(IllegalArgumentException.class, () -> placeCube.parse(t));
	}

}


