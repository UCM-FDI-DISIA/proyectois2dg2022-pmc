package commands;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class SaveCommandTest {
	//prueba que el comando de guardar funcione adecuadamente
	@Test
	void parseTest() {
		SaveCommand save = new SaveCommand();
		String[] s = {"s", "4", "a"};
		assertThrows(IllegalArgumentException.class, () -> save.parse(s));
	}

	//Comprueba que se lance la excepción
	@Test
	void exceptionTest() {
		String[] s = {"77","43"};
		assertThrows(IllegalArgumentException.class, () -> Command.getCommand(s));
	}

}