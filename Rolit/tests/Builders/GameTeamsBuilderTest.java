package Builders;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import logic.Game;

public class GameTeamsBuilderTest {
	@Test
	void test_1() {
		String inputJSon = "{\"teams\":[{\"players\":[{\"color\":\"Y\",\"name\":\"Juandi\"},{\"color\":\"W\",\"name\":\"Leo\"}],\"name\":\"equipo1\"},{\"players\":[{\"color\":\"R\",\"name\":\"dani\"}],\"name\":\"equipo2\"}],\"players\":[{\"color\":\"Y\",\"name\":\"Juandi\"},{\"color\":\"W\",\"name\":\"Leo\"}],\"turn\":\"BROWN\",\"type\":\"GameTeams\",\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]}}";
		Game game = GameTeamsBuilder.createGame(new JSONObject(inputJSon));
		assertTrue(new JSONObject(inputJSon).similar(game.report()));
	}
	
	@Test
	void test_2() {
		String inputJson = "{\"teams\":[{\"players\":[{\"color\":\"Y\",\"name\":\"Juandi\"},{\"color\":\"W\",\"name\":\"Leo\"}],\"name\":\"equipo1\"},{\"players\":[{\"color\":\"R\",\"name\":\"dani\"}],\"name\":\"equipo2\"}],\"players\":[{\"color\":\"Y\",\"name\":\"Juandi\"},{\"color\":\"W\",\"name\":\"Leo\"}],\"turn\":\"BROWN\",\"type\":\"GameTeams\",\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"sidjgpie\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]}}";
		assertThrows(Exception.class, () -> GameTeamsBuilder.createGame(new JSONObject(inputJson)));
	}
	
	@Test
	void test_3() {
		String inputJson = "{ \"type\" : \"bla\" }";
		assertThrows(Exception.class, () -> GameTeamsBuilder.createGame(new JSONObject(inputJson)));
	}
	
	@Test
	void test_4() {
		String inputJson = "{\"players\":[{\"color\":\"DANIGA\",\"Y\":\"Juandi\"},{\"color\":\"W\",\"name\":\"Leo\"},{\"color\":\"R\",\"name\":\"dani\"}],\"turn\":\"BROWN\",\"type\":\"GameClassic\",\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"kjfniwejnu\",\"pos\":[3,6]}]}}";
		assertThrows(Exception.class, () -> GameTeamsBuilder.createGame(new JSONObject(inputJson)));
	}
	
	@Test
	void test_5() {
		// if type is not GameClassic it returns null
		String inputJson = "{\"teams\":[{\"players\":[{\"color\":\"Y\",\"name\":\"Juandi\"},{\"color\":\"W\",\"name\":\"Leo\"}],\"name\":\"equipo1\"},{\"players\":[{\"color\":\"R\",\"name\":\"dani\"}],\"name\":\"equipo2\"}],\"players\":[{\"color\":\"Y\",\"name\":\"Juandi\"},{\"color\":\"W\",\"name\":\"Leo\"}],\"webier\":\"BROWN\",\"type\":\"GameTeams\",\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]}}";
		assertThrows(Exception.class, () -> GameTeamsBuilder.createGame(new JSONObject(inputJson)));
	}
	
	@Test
	void test_6() {
		// if type is not GameClassic it returns null
		String inputJson = "{\"teams\":[{\"players\":[{\"color\":\"Y\",\"name\":\"Juandi\"},{\"color\":\"W\",\"name\":\"Leo\"}],\"name\":\"equipo1\"},{\"players\":[{\"color\":\"R\",\"name\":\"dani\"}],\"name\":\"equipo2\"}],\"players\":[{\"color\":\"Y\",\"name\":\"Juandi\"},{\"color\":\"W\",\"name\":\"Leo\"}],\"turn\":\"BROWN\",\"type\":\"GameTeams\",\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[kfnwie,5]},{\"color\":\"Y\",\"pos\":[3,6]}]}}";
		assertThrows(Exception.class, () -> GameTeamsBuilder.createGame(new JSONObject(inputJson)));
	}
}

