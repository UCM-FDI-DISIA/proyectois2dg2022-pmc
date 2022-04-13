package network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import Builders.GameBuilder;
import logic.Color;
import logic.Game;
import logic.Player;


public class Server {
	private ServerSocket serverSocket = null;

	private HashMap<Player, Socket> sockets = new HashMap<>();
	private HashMap<Player, Client> clients = new HashMap<>();
	
	private ServerView sv;

	private Game game;

	/**
	 * Creates a new {@link ServerView}
	 */
	public Server() {
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
	public void start(int port, Game game) throws IOException {
		serverSocket = new ServerSocket(port);
		this.game = game;
		waitForPlayers();
		
		//logic.setGameRunning(true);
	}
	
	
	/**
	 * Sends a message to all {@link Client clients} and stops the server
	 * 
	 * @see	ChessLogic#setGameRunning(boolean)
	 */
	public void stop() {
		game.setGameRunning(false);
		for (PlayerColor c : clients.keySet()) {
			clients.get(c).send("Error_Server not responding.");
		}
		System.exit(0);
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
					
					List<Player> listaJugadores = game.getPlayers();
					
					//FIXME problema con orden, y estaría bien que cada jugador enviara su propia info
					for (Player p : listaJugadores) {
						sockets.put(p, serverSocket.accept());
						clients.put(p, new Client(p, sockets.get(p), Server.this));
						sendPositions(clients.get(p));
						
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
	public synchronized void fromClient(JSONObject json, Client client) {

		if(game.isFinished()) {
			return;
		}
		


		/*
		 * Player needs to have turn
		 */
		if(game.getCurrentPlayer().equals(client.getPlayer())) {


			//Parsing JSON
			game = GameBuilder.createGame(json);
			
			

		}



	}


	/**
	 * Sends positions
	 * 
	 * @param client		Whom to send the positions
	 * @see 				Positions
	 */
	private void sendPositions(Client client) {

		client.send(game.report());
	}

	
	/**
	 * Sends positions to all clients
	 * 
	 * @param clientMap		Map of all clients
	 * @see 					#sendPositions(Client)
	 */
	private void sendPositions(HashMap<Player, Client> clientMap) {
		for (Player p : clientMap.keySet()) {
			sendPositions(clientMap.get(p));
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

	public static void main(String[] args) throws IOException, InterruptedException {
		new Server();
	}

}
