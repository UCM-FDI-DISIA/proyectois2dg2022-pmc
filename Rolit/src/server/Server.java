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
		new Thread()
		{
			public void run() {

				try {
					/*
					 * First player is black
					 */
					
					
					Player p1 = new Player(Color.BLUE,"Leo");
					Pair<Player, Socket> parPlayerSocket = new Pair<Player, Socket>(p1, serverSocket.accept());
					sockets.add(parPlayerSocket);
					Pair<Player, ServerClient> parPlayerServerClient = new Pair<Player, ServerClient>(p1, new ServerClient(p1, parPlayerSocket.getSecond(), Server.this));
					clients.add(parPlayerServerClient);
					
					turno = 0;
					
					Player p2 = new Player(Color.RED,"Sergio");
					parPlayerSocket = new Pair<Player, Socket>(p2, serverSocket.accept());
					sockets.add(parPlayerSocket);
					parPlayerServerClient = new Pair<Player, ServerClient>(p2, new ServerClient(p2, parPlayerSocket.getSecond(), Server.this));
					clients.add(parPlayerServerClient);
					
					for (int i = 0; i < clients.size(); ++i) {
						clients.get(i).getSecond().updateGraphics(firstReport);
						
					}
					
					
				} catch (IOException e) {
					sv.showError("Could not connect both players.");
				}

			}
		}.start();

	}

	/**
	 * Handles incoming click from a client
	 * 
	 * @param json		Clock in json-format
	 * @param client		The client sending the data
	 * @see				ChessLogic
	 */
	public synchronized void receiveFromClient(JSONObject json, ServerClient client) {

		for (int i = 0; i < clients.size(); ++i) {
			if (client != clients.get(i).getSecond())
				clients.get(i).getSecond().updateGraphics(json);
			
		}
		
		turno = (turno+1)%clients.size();
		
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
