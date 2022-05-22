package commands;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import model.commands.Command;
import model.commands.SaveCommand;

public class SaveCommandTest {
	//We check that the save command works correctly
	@Test
	void parseTest() {
		SaveCommand save = new SaveCommand();
		String[] s = {"s", "4", "a"};
		assertThrows(IllegalArgumentException.class, () -> save.parse(s));
	}

	//We check that the exception is thrown
	@Test
	void exceptionTest() {
		String[] s = {"77","43"};
		assertThrows(IllegalArgumentException.class, () -> Command.getCommand(s));
	}

}