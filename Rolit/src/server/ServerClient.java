package server;

import java.io.IOException;
import java.net.Socket;

import org.json.JSONObject;

import logic.Player;

/**
 * Holds all client data
 *
 * @author  Oliver Ekberg
 * @since   2018-04-01
 * @version 1.0
 */
public class ServerClient {
	
	private Player p;
	private ServerClientThread thread;
	
	
	/**
	 * Sets values and starts the {@link #thread thread}
	 * 
	 * @param color		Color of the player
	 * @param socket		Reference to the socket socket
	 * @param server		Reference to the server
	 * @throws IOException
	 */
	
	public ServerClient() {
		
	}
	
	public void setUpServer(Socket socket, Server server) throws IOException {
		this.thread = new ServerClientThread(server, socket, this);
		thread.start();
		
	}
	
	public ServerClient(Socket socket, Server server) throws IOException{
		this.thread = new ServerClientThread(server, socket, this);
		thread.start();
	}
	
	public void setPlayer(Player p) {
		this.p = p;
	}
	
	/**
	 * @param str	String to send to the client via the {@link #thread}
	 * @see			ServerClientThread#updateGraphics(String)
	 */
	public void updateGraphics(JSONObject json) {
		thread.updateGraphics(json);
	}

	/**
	 * @return	The color of the player
	 */
	public Player getPlayer() {
		return p;
	}
	
}
