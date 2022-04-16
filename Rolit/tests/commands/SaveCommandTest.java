package commands;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class SaveCommandTest {
	@Test

	void parseTest() {
	
	SaveCommand save = new SaveCommand();
	
	
	String[] s = {"s", "4", "a"};
	assertThrows(IllegalArgumentException.class, () -> save.parse(s));
	}

	@Test
	void exceptionTest() {
		String[] s = {"77","43"};
		assertThrows(IllegalArgumentException.class, () -> Command.getCommand(s));
	}

}