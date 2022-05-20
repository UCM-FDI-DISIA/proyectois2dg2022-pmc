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

/**
 * This class is in charge of creating the server connection, get
 * data from ServerView and CreateGameDialog, creating the first
 * completed game JSON, as well as processing the data sent by clients.
 * @author PMC
 */

public class Server {
	
	private ServerSocket serverSocket = null;

	private List<Pair<Player, Socket>> sockets = new ArrayList<>();
	private volatile List<Pair<ServerClient, Player>> clients = new ArrayList<>();
	private HashMap<ServerClient, String> mapClientTeam = new HashMap<>();
	
	private volatile List<Player> incomingPlayers = new ArrayList<>();
	
	private int expectedPlayers;
	
	private ServerView sv;
	
	private JSONObject gameConfigJSON;

	/**
	 * Constructor. It stores a primitive version of the JSON that
	 * contains the information of the game, as well as being in charge
	 * of calling ServerView to get the connection data.
	 * @param gameConfigJSON Primitive version of the JSON that contains the information of the game
	 * specified by CreateGameDialog
	 */
	public Server(JSONObject gameConfigJSON) {
		this.gameConfigJSON = gameConfigJSON;
		loadExpectedPlayers();
		sv = new ServerView(this);
	}
	
	/**
	 * This method creates a ServerSocket given the given port from GUI.
	 * Then, it calls waitForPlayers the perform the expected connections
	 * @param port Integer that specifies the port in which user has specified to open the connection
	 * specified by CreateGameDialog.
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	public void start(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		waitForPlayers();
	}
	
	/**
	 * This method stops the server
	 */
	public void stop() {
		System.exit(0);
	}
	
	/**
	 * This method loads the expectedPlayers value so that the server can
	 * know which number of players to connect is expected
	 */
	private void loadExpectedPlayers() {
		expectedPlayers = gameConfigJSON.getJSONArray("players").length();
	}

	/**
	 * This method creates all the connections expected from the number of players,
	 * completes the game JSON which the additional information sent by clients,
	 * and send to each client a finally completed game JSON
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
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
	
		if (gameConfigJSON.get("type").equals("GameTeams")) {
			for (int i = 0; i < clients.size(); ++i) {
				clients.get(i).getFirst().notifyClientToChooseTeam(gameConfigJSON);
			}
			
			waitForAllPlayersToChooseTeam();
			
			completeGameConfigTeams();
			
		}
		
		
		JSONArray playersJSONArray = getPlayersJSONArray();
		
		this.gameConfigJSON.put("players", playersJSONArray);
		this.gameConfigJSON.put("turn", clients.get(0).getSecond().getColor().toString());
		
		for (int i = 0; i < clients.size(); ++i) {
			clients.get(i).getFirst().updateGraphics(gameConfigJSON);
		}

	}
	
	/**
	 * This method creates a JSONArray from the info sent by all the clients' player
	 * connected to the server
	 * @return The JSONArray that represents all players' information
	 */
	private JSONArray getPlayersJSONArray() {
		JSONArray playersJSONArray = new JSONArray();
		
		for (Player player : incomingPlayers) {
			playersJSONArray.put(player.report());
		}
		
		
		return playersJSONArray;
	}


	/**
	 * This synchronized method allows server to process the information
	 * sent by a client and perform several tasks depending on the nature
	 * of the information sent (i.e. the type of notification)
	 * @param json JSONObject that represents the message sent by client
	 * @param client The instance of the ServerClient associated with the client that sent the message
	 */
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

	/**
	 * This method is used to completed the first completed game JSON from the information
	 * sent by clients
	 */
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


	/**
	 * This synchronized method is used to stop server's game processing methods until
	 * all players have been connected.
	 */
	public synchronized void waitForAllPlayersToChooseTeam() {
		
		while ((mapClientTeam.size() != expectedPlayers)) {
			try {
				wait(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	}
	
	/**
	 * This method returns the expected number of players to be connected,
	 * specified previously by GUI
	 * @return Integer that represents the number of players to be connected
	 */
	public int getExpectedPlayers() {
		return this.expectedPlayers;
	}
	
	/**
	 * This method returns the number of players that have been already connected
	 * so that it can be shown in GUI to send feedback to user
	 * @return Integer that represents the number of players that have been already connected
	 */
	public int getNumPlayers() {
		return this.clients.size();
	}
}
