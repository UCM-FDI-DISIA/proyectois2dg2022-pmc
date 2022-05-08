package model.online;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import model.logic.Player;
import utils.Pair;


public class Server {
	
	private ServerSocket serverSocket = null;

	private List<Pair<Player, Socket>> sockets = new ArrayList<>();
	private volatile List<Pair<ServerClient, Player>> clients = new ArrayList<>();
	private HashMap<ServerClient, String> mapClientTeam = new HashMap<>();
	
	private volatile List<Player> incomingPlayers = new ArrayList<>();
	
	private int expectedPlayers;
	
	private ServerView sv;
	
	private JSONObject gameConfigJSON;

	public Server(JSONObject gameConfigJSON) {
		this.gameConfigJSON = gameConfigJSON;
		loadExpectedPlayers();
		sv = new ServerView(this);
	}
	
	public void start(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		waitForPlayers();
	}
	

	public void stop() {
		System.exit(0);
	}
	
	private void loadExpectedPlayers() {
		expectedPlayers = gameConfigJSON.getJSONArray("players").length();
	}


	private void waitForPlayers() throws IOException {
	
		for (int i = 0; i < expectedPlayers; ++i) {
		
		ServerClient serverClient = new ServerClient();
		Socket socket = new Socket();
		Thread t = new ServerWaitPlayerThread(serverSocket, socket, serverClient, this);
		
		t.start();
		
		try {
			t.join();
		} catch (InterruptedException e) { e.printStackTrace(); }
		
		Pair<Player, Socket> parPlayerSocket = new Pair<Player, Socket>(incomingPlayers.get(i), socket);
		sockets.add(parPlayerSocket);
		
		Pair<ServerClient, Player> parServerClientPlayer = new Pair<ServerClient, Player>(serverClient, incomingPlayers.get(i));
		clients.add(parServerClientPlayer);
		
		sv.updateNumPlayers();
		
		
		}
		
		completeGameConfigAndSendToClients();
	
	}
	
	private void completeGameConfigAndSendToClients() {
		
		if (gameConfigJSON.get("type").equals("GameTeams")) {
			for (int i = 0; i < clients.size(); ++i) {
				clients.get(i).getFirst().notifyClientToChooseTeam(gameConfigJSON);
			}
			
			waitForAllPlayersToChooseTeam();
			
			completeGameConfigTeams();
			
		}
	
		complateGameConfigPlayers();
	
	
		for (int i = 0; i < clients.size(); ++i) {
			clients.get(i).getFirst().updateGraphics(gameConfigJSON);
		}
		
	}

	private void complateGameConfigPlayers() {
		JSONArray playersJSONArray = getPlayersJSONArray();
		
		this.gameConfigJSON.put("players", playersJSONArray);
		this.gameConfigJSON.put("turn", clients.get(0).getSecond().getColor().toString());
		
	}

	private JSONArray getPlayersJSONArray() {
		JSONArray playersJSONArray = new JSONArray();
		
		for (Player player : incomingPlayers) {
			playersJSONArray.put(player.report());
		}
		
		
		return playersJSONArray;
	}


	public synchronized void receiveFromClient(JSONObject json, ServerClient client) {
		
		String notification = json.getString("notification");
		
		if (notification.equals("chooseTeam")) {
			mapClientTeam.put(client, json.getString("team"));
				
		}
		else if (notification.equals("updateGraphics")) { //estamos ante un juego

			for (int i = 0; i < clients.size(); ++i) {
				if (client != clients.get(i).getFirst())
					clients.get(i).getFirst().updateGraphics(json);
			}
			
		}
		else if (notification.equals("playerInfo")){ //informacion de un player nuevo
			Player p = new Player(json);
			incomingPlayers.add(p);
			
		}		
	}


	private void completeGameConfigTeams() {
		
		JSONArray teamsArray = gameConfigJSON.getJSONArray("teams");
		
		for (Map.Entry<ServerClient, String> entry : mapClientTeam.entrySet()) {
		    ServerClient s = entry.getKey();
		    String t = entry.getValue();
		    
		    JSONObject teamToFind = null;
		    
		    for (int i = 0; i < teamsArray.length(); ++i) {
				JSONObject team = teamsArray.getJSONObject(i);
				if (team.getString("name").equals(t))
					teamToFind = team;
			}
		    
		    JSONArray arrayPlayers = new JSONArray();
		    teamToFind.put("players", arrayPlayers);
		    JSONArray teamPlayers = teamToFind.getJSONArray("players");
		   
		    Player playerToFind = null;
		    
		    for (int i = 0; i < clients.size(); ++i) {
				Pair<ServerClient, Player> p = clients.get(i);
				if (p.getFirst().equals(s))
					playerToFind = p.getSecond();
			}
		    
		    teamPlayers.put(playerToFind.report());
		    
		}
		
	}


	public synchronized void waitForAllPlayersToChooseTeam() {
		while ((mapClientTeam.size() != expectedPlayers)) {
			try {
				wait(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getExpectedPlayers() {
		return this.expectedPlayers;
	}
	
	public int getNumPlayers() {
		return this.clients.size();
	}
}
