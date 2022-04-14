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

import org.json.JSONObject;

import Builders.GameBuilder;
import logic.Color;
import logic.Game;
import logic.Player;
import utils.Pair;


public class Server {
	private ServerSocket serverSocket = null;

	private List<Pair<Player, Socket>> sockets = new ArrayList<>();
	private List<Pair<Player, ServerClient>> clients = new ArrayList<>();
	
	private List<Player> incomingPlayers = new ArrayList<>();
	
	private int expectedPlayers;
	
	private int turno;
	
	private ServerView sv;
	
	private JSONObject firstReport;
	/**
	 * Creates a new {@link ServerView}
	 */
	public Server(JSONObject firstReport) {
		this.firstReport = firstReport;
		sv = new ServerView(this);
		
	}
	
	
	/**
	 * Starts the server at port
	 * 
	 * @param port			Requested port of the new server
	 * @throws IOException
	 * @see					Server#waitForPlayers()	
	 * @see					ChessLogic#setGameRunning(boolean)			
	 */
	public void start(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		waitForPlayers();
		
		//logic.setGameRunning(true);
	}
	
	
	/**
	 * Sends a message to all {@link ServerClient clients} and stops the server
	 * 
	 * @see	ChessLogic#setGameRunning(boolean)
	 */
	public void stop() {
//		game.setGameRunning(false);
//		for (PlayerColor c : clients.keySet()) {
//			clients.get(c).send("Error_Server not responding.");
//		}
//		System.exit(0);
	}

	
	/**
	 * A thread that waits for new players to connect
	 * 
	 * @throws IOException
	 * @see	Server#sendPositions(PlayerColor)
	 * @see ServerView#showError(String)
	 */
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
	
	clients.get(turno).getSecond().setTurn();
	
	for (int i = 0; i < clients.size(); ++i) {
		clients.get(i).getSecond().updateGraphics(firstReport);
	}

	}

	/**
	 * Handles incoming click from a client
	 * 
	 * @param json		Clock in json-format
	 * @param client		The client sending the data
	 * @see				ChessLogic
	 */
	public synchronized void receiveFromClient(JSONObject json, ServerClient client) {

		if (json.has("turn")) { //estamos ante un juego
			turno = (turno+1)%clients.size();
			
			clients.get(turno).getSecond().setTurn();
			
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

	/**
	 * @return	The IP-address of the server
	 */
	public String getIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {}
		return null;
	}

}
