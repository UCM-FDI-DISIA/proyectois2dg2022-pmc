package IA;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import model.builders.GameClassicBuilder;
import model.logic.Cube;
import model.logic.Game;

public class IATest {
	//A normal player and an AI. The normal player has the last turn and it has to put the last cube. We check that no exception is thrown
	@Test
	void IA_test1() {
		 try {
				String s = "{\"players\":[{\"score\":57,\"color\":\"R\",\"name\":\"fer\"},{\"score\":23,\"color\":\"Y\",\"name\":\"waerg\",\"strategy\":\"RANDOM\"}],\"turn\":\"R\",\"type\":\"GameClassic\",\"board\":{\"shape\":\"SS\",\"cubes\":[{\"color\":\"R\",\"pos\":[1,1]},{\"color\":\"R\",\"pos\":[2,1]},{\"color\":\"R\",\"pos\":[3,1]},{\"color\":\"R\",\"pos\":[4,2]},{\"color\":\"Y\",\"pos\":[5,3]},{\"color\":\"R\",\"pos\":[5,1]},{\"color\":\"R\",\"pos\":[4,1]},{\"color\":\"Y\",\"pos\":[4,3]},{\"color\":\"R\",\"pos\":[6,1]},{\"color\":\"R\",\"pos\":[7,1]},{\"color\":\"Y\",\"pos\":[4,4]},{\"color\":\"R\",\"pos\":[6,4]},{\"color\":\"R\",\"pos\":[8,1]},{\"color\":\"Y\",\"pos\":[3,3]},{\"color\":\"R\",\"pos\":[7,5]},{\"color\":\"Y\",\"pos\":[6,3]},{\"color\":\"R\",\"pos\":[2,3]},{\"color\":\"R\",\"pos\":[9,2]},{\"color\":\"Y\",\"pos\":[7,3]},{\"color\":\"R\",\"pos\":[7,2]},{\"color\":\"R\",\"pos\":[8,2]},{\"color\":\"R\",\"pos\":[5,2]},{\"color\":\"R\",\"pos\":[6,2]},{\"color\":\"R\",\"pos\":[4,5]},{\"color\":\"R\",\"pos\":[4,6]},{\"color\":\"Y\",\"pos\":[5,4]},{\"color\":\"R\",\"pos\":[5,5]},{\"color\":\"R\",\"pos\":[1,2]},{\"color\":\"R\",\"pos\":[1,3]},{\"color\":\"Y\",\"pos\":[8,4]},{\"color\":\"R\",\"pos\":[9,3]},{\"color\":\"R\",\"pos\":[2,2]},{\"color\":\"R\",\"pos\":[9,1]},{\"color\":\"R\",\"pos\":[5,6]},{\"color\":\"R\",\"pos\":[5,7]},{\"color\":\"Y\",\"pos\":[3,6]},{\"color\":\"R\",\"pos\":[2,6]},{\"color\":\"Y\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[3,4]},{\"color\":\"R\",\"pos\":[6,6]},{\"color\":\"R\",\"pos\":[7,7]},{\"color\":\"Y\",\"pos\":[8,5]},{\"color\":\"Y\",\"pos\":[8,6]},{\"color\":\"R\",\"pos\":[2,4]},{\"color\":\"R\",\"pos\":[2,5]},{\"color\":\"Y\",\"pos\":[3,2]},{\"color\":\"R\",\"pos\":[6,5]},{\"color\":\"Y\",\"pos\":[8,8]},{\"color\":\"R\",\"pos\":[4,7]},{\"color\":\"R\",\"pos\":[6,8]},{\"color\":\"R\",\"pos\":[3,7]},{\"color\":\"R\",\"pos\":[2,8]},{\"color\":\"R\",\"pos\":[2,7]},{\"color\":\"Y\",\"pos\":[9,6]},{\"color\":\"R\",\"pos\":[1,4]},{\"color\":\"R\",\"pos\":[2,9]},{\"color\":\"R\",\"pos\":[1,5]},{\"color\":\"Y\",\"pos\":[8,7]},{\"color\":\"R\",\"pos\":[1,6]},{\"color\":\"R\",\"pos\":[3,8]},{\"color\":\"R\",\"pos\":[1,7]},{\"color\":\"Y\",\"pos\":[9,8]},{\"color\":\"R\",\"pos\":[1,8]},{\"color\":\"R\",\"pos\":[6,9]},{\"color\":\"R\",\"pos\":[1,9]},{\"color\":\"R\",\"pos\":[7,4]},{\"color\":\"R\",\"pos\":[4,8]},{\"color\":\"Y\",\"pos\":[9,7]},{\"color\":\"R\",\"pos\":[3,9]},{\"color\":\"R\",\"pos\":[7,6]},{\"color\":\"R\",\"pos\":[4,9]},{\"color\":\"Y\",\"pos\":[9,9]},{\"color\":\"R\",\"pos\":[5,9]},{\"color\":\"R\",\"pos\":[7,8]},{\"color\":\"R\",\"pos\":[5,8]},{\"color\":\"Y\",\"pos\":[9,4]},{\"color\":\"R\",\"pos\":[7,9]},{\"color\":\"Y\",\"pos\":[9,5]},{\"color\":\"R\",\"pos\":[6,7]},{\"color\":\"Y\",\"pos\":[8,3]}]}}";
				Game game = GameClassicBuilder.createGame(new JSONObject(s));
				game.addCubeToQueue(new Cube(8,9, null));
				game.play();
		   } catch(Exception e) {
		      fail("Should not have thrown any exception");
		   }
	}
	
	//A normal player and two AIs (both of them MiniMax) and two boxes empty. Normal player, AI, and then the last
	//AI. We see that an error does not occur.
	@Test
	void IA_test2() {
		 try {
				String s = "{\"players\":[{\"score\":29,\"color\":\"R\",\"name\":\"minimax1\",\"strategy\":\"MINIMAX\"},{\"score\":37,\"color\":\"Y\",\"name\":\"Mar\"},{\"score\":13,\"color\":\"E\",\"name\":\"minimax2\",\"strategy\":\"MINIMAX\"}],\"turn\":\"Y\",\"type\":\"GameClassic\",\"board\":{\"shape\":\"SS\",\"cubes\":[{\"color\":\"Y\",\"pos\":[4,6]},{\"color\":\"E\",\"pos\":[4,5]},{\"color\":\"Y\",\"pos\":[4,7]},{\"color\":\"R\",\"pos\":[4,8]},{\"color\":\"R\",\"pos\":[4,9]},{\"color\":\"R\",\"pos\":[5,9]},{\"color\":\"R\",\"pos\":[6,9]},{\"color\":\"R\",\"pos\":[7,9]},{\"color\":\"R\",\"pos\":[8,9]},{\"color\":\"R\",\"pos\":[9,9]},{\"color\":\"R\",\"pos\":[7,8]},{\"color\":\"R\",\"pos\":[3,9]},{\"color\":\"R\",\"pos\":[2,9]},{\"color\":\"Y\",\"pos\":[1,9]},{\"color\":\"Y\",\"pos\":[1,8]},{\"color\":\"R\",\"pos\":[8,7]},{\"color\":\"Y\",\"pos\":[1,7]},{\"color\":\"Y\",\"pos\":[1,6]},{\"color\":\"R\",\"pos\":[5,4]},{\"color\":\"R\",\"pos\":[9,6]},{\"color\":\"Y\",\"pos\":[1,5]},{\"color\":\"E\",\"pos\":[6,3]},{\"color\":\"R\",\"pos\":[9,8]},{\"color\":\"Y\",\"pos\":[1,4]},{\"color\":\"Y\",\"pos\":[4,4]},{\"color\":\"R\",\"pos\":[7,2]},{\"color\":\"Y\",\"pos\":[1,3]},{\"color\":\"E\",\"pos\":[3,4]},{\"color\":\"Y\",\"pos\":[3,6]},{\"color\":\"Y\",\"pos\":[1,2]},{\"color\":\"E\",\"pos\":[5,5]},{\"color\":\"Y\",\"pos\":[1,1]},{\"color\":\"Y\",\"pos\":[2,1]},{\"color\":\"Y\",\"pos\":[8,1]},{\"color\":\"E\",\"pos\":[2,4]},{\"color\":\"Y\",\"pos\":[2,2]},{\"color\":\"Y\",\"pos\":[2,7]},{\"color\":\"R\",\"pos\":[8,8]},{\"color\":\"E\",\"pos\":[2,3]},{\"color\":\"R\",\"pos\":[5,8]},{\"color\":\"E\",\"pos\":[6,4]},{\"color\":\"E\",\"pos\":[2,5]},{\"color\":\"Y\",\"pos\":[5,2]},{\"color\":\"Y\",\"pos\":[3,7]},{\"color\":\"Y\",\"pos\":[2,6]},{\"color\":\"R\",\"pos\":[9,7]},{\"color\":\"R\",\"pos\":[6,8]},{\"color\":\"Y\",\"pos\":[2,8]},{\"color\":\"R\",\"pos\":[6,5]},{\"color\":\"R\",\"pos\":[3,8]},{\"color\":\"Y\",\"pos\":[3,1]},{\"color\":\"Y\",\"pos\":[5,7]},{\"color\":\"R\",\"pos\":[6,7]},{\"color\":\"Y\",\"pos\":[3,2]},{\"color\":\"Y\",\"pos\":[5,6]},{\"color\":\"R\",\"pos\":[7,4]},{\"color\":\"E\",\"pos\":[3,3]},{\"color\":\"E\",\"pos\":[3,5]},{\"color\":\"Y\",\"pos\":[6,6]},{\"color\":\"Y\",\"pos\":[4,1]},{\"color\":\"Y\",\"pos\":[5,3]},{\"color\":\"Y\",\"pos\":[5,1]},{\"color\":\"Y\",\"pos\":[4,2]},{\"color\":\"R\",\"pos\":[9,5]},{\"color\":\"Y\",\"pos\":[6,2]},{\"color\":\"E\",\"pos\":[4,3]},{\"color\":\"R\",\"pos\":[8,4]},{\"color\":\"Y\",\"pos\":[6,1]},{\"color\":\"Y\",\"pos\":[7,1]},{\"color\":\"R\",\"pos\":[9,4]},{\"color\":\"Y\",\"pos\":[9,1]},{\"color\":\"E\",\"pos\":[7,3]},{\"color\":\"R\",\"pos\":[9,3]},{\"color\":\"R\",\"pos\":[7,7]},{\"color\":\"Y\",\"pos\":[7,5]},{\"color\":\"Y\",\"pos\":[7,6]},{\"color\":\"Y\",\"pos\":[8,6]},{\"color\":\"E\",\"pos\":[8,2]},{\"color\":\"R\",\"pos\":[8,3]}]}}";
				Game game = GameClassicBuilder.createGame(new JSONObject(s));
				game.addCubeToQueue(new Cube(8,5, null));
				game.play();
		   } catch(Exception e) {
		      fail("Should not have thrown any exception");
		   }
		 
	}
	
	//Three AIs, one random, other MiniMax and other greedy, and we se that an
	//error does not occur.
		@Test
		void IA_test3() {
			 try {
					String s = "{\"players\":[{\"score\":2,\"color\":\"Y\",\"name\":\"random\",\"strategy\":\"RANDOM\"},{\"score\":8,\"color\":\"G\",\"name\":\"greedy\",\"strategy\":\"GREEDY\"},{\"score\":1,\"color\":\"K\",\"name\":\"minimax\",\"strategy\":\"MINIMAX\"}],\"turn\":\"G\",\"type\":\"GameClassic\",\"board\":{\"shape\":\"CL\",\"cubes\":[{\"color\":\"Y\",\"pos\":[4,6]},{\"color\":\"G\",\"pos\":[5,7]},{\"color\":\"G\",\"pos\":[6,7]},{\"color\":\"G\",\"pos\":[5,5]},{\"color\":\"G\",\"pos\":[7,7]},{\"color\":\"G\",\"pos\":[8,7]},{\"color\":\"G\",\"pos\":[6,6]},{\"color\":\"G\",\"pos\":[4,4]},{\"color\":\"K\",\"pos\":[6,4]},{\"color\":\"Y\",\"pos\":[7,4]},{\"color\":\"G\",\"pos\":[9,7]}]}}";
					Game game = GameClassicBuilder.createGame(new JSONObject(s));
					game.play();
			   } catch(Exception e) {
			      fail("Should not have thrown any exception");
			   }
			 
		}
}
