package Builders;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import logic.Game;

public class GameClassicBuilderTest {
	@Test
	void test_1() {
		String inputJSon = "{\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]},\"players\":[{\"color\":\"Y\",\"name\":\"Juandi\"},{\"color\":\"W\",\"name\":\"Leo\"},{\"color\":\"R\",\"name\":\"dani\"}],\"turn\":\"W\",\"type\":\"GameClassic\"}";
		Game game = GameClassicBuilder.createGame(new JSONObject(inputJSon));
		assertTrue(new JSONObject(inputJSon).similar(game.report()));
	}
	
	@Test
	void test_2() {
		String inputJson = "{\"players\":[{\"color\":\"LO\",\"name\":\"Juandi\"},{\"color\":\"W\",\"name\":\"Leo\"},{\"color\":\"R\",\"name\":\"dani\"}],\"turn\":\"BROWN\",\"type\":\"GameClassic\"}";
		assertThrows(Exception.class, () -> GameClassicBuilder.createGame(new JSONObject(inputJson)));
	}
	
	@Test
	void test_3() {
		// if type is not GameClassic it returns null
		String inputJson = "{ \"type\" : \"bla\" }";
		assertThrows(Exception.class, () -> GameClassicBuilder.createGame(new JSONObject(inputJson)));
	}
	
	@Test
	void test_4() {
		//TODO definir política valueOfIgnoreCase de Color: ¿primer caracter o analizar toda la cadena encontrada?
		String inputJson = "{\"players\":[{\"color\":\"Y\",\"name\":\"Juandi\"},{\"color\":\"W\",\"name\":\"Leo\"},{\"color\":\"R\",\"name\":\"dani\"}],\"turn\":\"W\",\"type\":\"GameClassic\",\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"zjfniwejnu\",\"pos\":[3,6]}]}}";
		assertThrows(Exception.class, () -> GameClassicBuilder.createGame(new JSONObject(inputJson)));
	}
	
	@Test
	void test_5() {
		String inputJson = "{\"players\":[{\"color\":\"Y\",\"name\":\"Juandi\"},{\"color\":\"W\",\"name\":\"Leo\"},{\"wf2efq3e\":\"R\",\"name\":\"dani\"}],\"turn\":\"W\",\"type\":\"GameClassic\",\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"zjfniwejnu\",\"pos\":[3,6]}]}}";
		assertThrows(Exception.class, () -> GameClassicBuilder.createGame(new JSONObject(inputJson)));
	}
	
	@Test
	void test_6() {
		String inputJson = "{\"players\":[{\"color\":\"Y\",\"name\":\"Juandi\"},{\"color\":\"W\",\"name\":\"Leo\"},{\"color\":\"R\",\"zkjniw\":\"dani\"}],\"turn\":\"W\",\"type\":\"GameClassic\",\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]}}";
		assertThrows(Exception.class, () -> GameClassicBuilder.createGame(new JSONObject(inputJson)));
	}
}
