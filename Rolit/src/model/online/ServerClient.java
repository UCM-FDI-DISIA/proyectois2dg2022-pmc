package model.online;

import java.io.IOException;
import java.net.Socket;

import org.json.JSONObject;

public class ServerClient {
	
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
	
	public void updateGraphics(JSONObject json) {
		thread.sendUpdateGraphicsToClient(json);
	}

	public void notifyClientToChooseTeam(JSONObject gameConfigJSON) {
		thread.sendChooseTeamToClient(gameConfigJSON);
		
	}
	
}
