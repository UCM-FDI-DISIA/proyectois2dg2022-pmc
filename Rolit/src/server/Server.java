package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import Builders.GameBuilder;
import logic.Color;
import logic.Game;
import logic.Player;
import utils.Pair;


public class Server {
	private ServerSocket serverSocket = null;

	private List<Pair<Player, Socket>> sockets = new ArrayList<>();
	private volatile List<Pair<Player, ServerClient>> clients = new ArrayList<>();
	
	private volatile List<Player> incomingPlayers = new ArrayList<>();
	
	private int expectedPlayers;
	
	private ServerView sv;
	
	private JSONObject gameConfigJSON;

	public Server(JSONObject gameConfigJSON) {
		sv = new ServerView(this);
		this.gameConfigJSON = gameConfigJSON;
	}
	
	public void start(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		waitForPlayers();
	}
	

	public void stop() {
		System.exit(0);
	}


	private void waitForPlayers() throws IOException {
	
		expectedPlayers = 2;
		
	for (int i = 0; i < expectedPlayers; ++i) {
		
		ServerClient serverClient = new ServerClient();
		Socket socket = new Socket();
		Thread t = new WaitPlayerThread(serverSocket, socket, serverClient, this);
		
		t.start();
		
		try {
			t.join();
		} catch (InterruptedException e) { e.printStackTrace(); }
		
		Pair<Player, Socket> parPlayerSocket = new Pair<Player, Socket>(incomingPlayers.get(i), socket);
		sockets.add(parPlayerSocket);
		
		serverClient.setPlayer(incomingPlayers.get(i));
		Pair<Player, ServerClient> parPlayerServerClient = new Pair<Player, ServerClient>(incomingPlayers.get(i), serverClient);
		clients.add(parPlayerServerClient);
		
		
	}
	
	JSONArray playersJSONArray = getPlayersJSONArray();
	
	this.gameConfigJSON.put("players", playersJSONArray);
	this.gameConfigJSON.put("turn", clients.get(0).getFirst().getColor().toString());
	
	for (int i = 0; i < clients.size(); ++i) {
		clients.get(i).getSecond().updateGraphics(gameConfigJSON);
	}

	}
	
	private JSONArray getPlayersJSONArray() {
		JSONArray playersJSONArray = new JSONArray();
		
		for (Player player : incomingPlayers) {
			playersJSONArray.put(player.report());
		}
		
		
		return playersJSONArray;
	}


	public synchronized void receiveFromClient(JSONObject json, ServerClient client) {

		if (json.has("turn")) { //estamos ante un juego

			for (int i = 0; i < clients.size(); ++i) {
				if (client != clients.get(i).getSecond())
					clients.get(i).getSecond().updateGraphics(json);				
			}
			
		}
		else { //informacion de un player nuevo
			Player p = new Player(json);
			incomingPlayers.add(p);
			
		}		
	}


	public String getIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {}
		return null;
	}

}
