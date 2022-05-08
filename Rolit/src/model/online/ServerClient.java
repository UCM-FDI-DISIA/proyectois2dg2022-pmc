package model.online;

import java.io.IOException;
import java.net.Socket;

import org.json.JSONObject;

/**
 * This class represents a client from the point of view of Server;
 * particularly, it is uniquely associated with a ServerClientThread
 * that sends and gets information from an specific client, and the server
 * (class Server)
 * @author PMC
 */
public class ServerClient {
	
	private volatile ServerClientThread thread;

	/**
	 * Constructor
	 */
	public ServerClient() {
		
	}
	
	/**
	 * This method creates a new ServerClientThread so that it can
	 * communicate across the accepted socket
	 * @param socket Socket that has been accepted by server
	 * @param server The server
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	public void setUpServerClientConnection(Socket socket, Server server) throws IOException {
		this.thread = new ServerClientThread(server, socket, this);
		thread.start();
		
	}
	
	/**
	 * This method tells the ServerClientThread to send to client the
	 * new information of game (json) that has been sent from another client
	 * @param json JSONObject that contains a new game information from another client
	 */
	public void updateGraphics(JSONObject json) {
		thread.sendUpdateGraphicsToClient(json);
	}

	/**
	 * This method tells the ServerClientThread to send to client the
	 * request of choosing in which team it wants to play
	 * @param gameConfigJSON JSONObject that contains an incomplete
	 * game configuration so that client can complete it with its team preference
	 */
	public void notifyClientToChooseTeam(JSONObject gameConfigJSON) {
		thread.sendChooseTeamToClient(gameConfigJSON);
		
	}
	
}
