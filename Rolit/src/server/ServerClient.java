package server;

import java.io.IOException;
import java.net.Socket;

import org.json.JSONObject;

import logic.Player;

public class ServerClient {
	
	private Player p;
	private volatile ServerClientThread thread;
	

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

	public void updateGraphics(JSONObject json) {
		thread.updateGraphics(json);
	}

	public Player getPlayer() {
		return p;
	}

	public void notifyClientToChooseTeam(JSONObject gameConfigJSON) {
		thread.notifyClientToChooseTeam(gameConfigJSON);
		
	}
	
}
