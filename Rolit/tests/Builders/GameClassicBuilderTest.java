package Builders;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import logic.Game;

public class GameClassicBuilderTest {
	//GameClassic debería crearse bien y devolver el mismo report
	@Test
	void test_1() {
		String inputJSon = "{\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]},\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"},{\"score\":0,\"color\":\"R\",\"name\":\"dani\"}],\"turn\":\"W\",\"type\":\"GameClassic\"}";
		Game game = GameClassicBuilder.createGame(new JSONObject(inputJSon));
		assertTrue(new JSONObject(inputJSon).similar(game.report()));
	}
	
	//probamos a meter una shape que está mal, vemos que se lanza adecuadamente la excepción
	@Test
	void test_2() {
		String inputJSon = "{\"board\":{\"shape\":\"kjdfifwe\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]},\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"},{\"score\":0,\"color\":\"R\",\"name\":\"dani\"}],\"turn\":\"W\",\"type\":\"GameClassic\"}";
		assertThrows(Exception.class, () -> GameClassicBuilder.createGame(new JSONObject(inputJSon)));
	}
	
	// si el tipo no es un GameClassic debe saltar una excepción
	@Test
	void test_3() {
		String inputJson = "{\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]},\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"},{\"score\":0,\"color\":\"R\",\"name\":\"dani\"}],\"turn\":\"W\",\"type\":\"blabla\"}";;
		assertThrows(Exception.class, () -> GameClassicBuilder.createGame(new JSONObject(inputJson)));
	}
	
	//probamos a meter un color que está mal, vemos que se lanza adecuadamente la excepción
	@Test
	void test_4() {
		String inputJSon = "{\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]},\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"},{\"score\":0,\"color\":\"zdef4en\",\"name\":\"dani\"}],\"turn\":\"W\",\"type\":\"GameClassic\"}";
		assertThrows(Exception.class, () -> GameClassicBuilder.createGame(new JSONObject(inputJSon)));
	}
	
	//probamos a meter una pos x que está mal, vemos que se lanza adecuadamente la excepción
	@Test
	void test_5() {
		String inputJSon = "{\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[5000,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]},\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"},{\"score\":0,\"color\":\"R\",\"name\":\"dani\"}],\"turn\":\"W\",\"type\":\"GameClassic\"}";
		assertThrows(Exception.class, () -> GameClassicBuilder.createGame(new JSONObject(inputJSon)));
	}
	
	//probamos a meter una clave que está mal, vemos que se lanza adecuadamente la excepción
	@Test
	void test_6() {
		String inputJSon = "{\"ckex\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]},\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"},{\"score\":0,\"color\":\"R\",\"name\":\"dani\"}],\"turn\":\"W\",\"type\":\"GameClassic\"}";
		assertThrows(Exception.class, () -> GameClassicBuilder.createGame(new JSONObject(inputJSon)));
	}
}
