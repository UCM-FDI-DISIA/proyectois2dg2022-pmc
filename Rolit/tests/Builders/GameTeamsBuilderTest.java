package Builders;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import model.builders.GameTeamsBuilder;
import model.logic.Game;

public class GameTeamsBuilderTest {
	@Test
	void test_1() {
		String inputJSon = "{\"teams\":[{\"score\":0,\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"}],\"name\":\"equipo1\"},{\"score\":0,\"players\":[],\"name\":\"equipo2\"}],\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"}],\"turn\":\"W\",\"type\":\"GameTeams\",\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]}}";
		Game game = GameTeamsBuilder.createGame(new JSONObject(inputJSon));
		assertTrue(new JSONObject(inputJSon).similar(game.report()));
	}
	
	@Test
	void test_2() {
		String inputJson = "{\"teams\":[{\"score\":3,\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"}],\"name\":\"equipo1\"},{\"score\":0,\"players\":[{\"score\":0,\"color\":\"R\",\"name\":\"dani\"}],\"name\":\"equipo2\"}],\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"}],\"turn\":\"W\",\"type\":\"GameTeams\",\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,-50]}]}}";
		assertThrows(Exception.class, () -> GameTeamsBuilder.createGame(new JSONObject(inputJson)));
	}
	
	@Test
	void test_3() {
		String inputJson = "{ \"type\" : \"bla\" }";
		assertThrows(Exception.class, () -> GameTeamsBuilder.createGame(new JSONObject(inputJson)));
	}
	
	@Test
	void test_4() {
		String inputJson = "{\"teams\":[{\"score\":3,\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"}],\"name\":\"equipo1\"},{\"score\":0,\"players\":[{\"score\":0,\"color\":\"R\",\"name\":\"dani\"}],\"name\":\"equipo2\"}],\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"}],\"turn\":\"W\",\"type\":\"GameTeams\",\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"zsuwbdweud\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]}}";
		assertThrows(Exception.class, () -> GameTeamsBuilder.createGame(new JSONObject(inputJson)));
	}
	
	@Test
	void test_5() {
		String inputJson = "{\"teams\":[{\"score\":3,\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"}],\"name\":\"equipo1\"},{\"score\":0,\"players\":[{\"score\":0,\"color\":\"R\",\"name\":\"dani\"}],\"name\":\"equipo2\"}],\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"}],\"turn\":\"W\",\"type\":\"cjnei\",\"board\":{\"shape\":\"SM\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]}}";
		assertThrows(Exception.class, () -> GameTeamsBuilder.createGame(new JSONObject(inputJson)));
	}
	
	@Test
	void test_6() {
		String inputJson = "{\"teams\":[{\"score\":3,\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"}],\"name\":\"equipo1\"},{\"score\":0,\"players\":[{\"score\":0,\"color\":\"R\",\"name\":\"dani\"}],\"name\":\"equipo2\"}],\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"Juandi\"},{\"score\":1,\"color\":\"W\",\"name\":\"Leo\"}],\"turn\":\"W\",\"type\":\"GameTeams\",\"board\":{\"shape\":\"danigmsi\",\"cubes\":[{\"color\":\"W\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,6]}]}}";
		assertThrows(Exception.class, () -> GameTeamsBuilder.createGame(new JSONObject(inputJson)));
	}
}

