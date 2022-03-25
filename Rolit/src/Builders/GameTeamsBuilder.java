package Builders;

import org.json.JSONObject;

import logic.GameClassic;

public class GameTeamsBuilder extends GameBuilder {
	private static final String TYPE = "GameClassic";

	GameTeamsBuilder() {
				
	}
	
	@Override
	protected boolean match(String type) {
		return TYPE.equals(type);
	}

	@Override
	protected GameClassic GenerateGame(JSONObject o) {		
		return null;
	}

	@Override
	protected void whatINeed() {
		// TODO Auto-generated method stub
		
	}

	
}
