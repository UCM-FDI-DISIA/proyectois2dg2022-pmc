package model.online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;

public class ServerClientThread extends Thread {

	private BufferedReader input;
	private PrintWriter output;
	private volatile ServerClient client;
	private volatile Server server;

	public ServerClientThread(Server server, Socket socket, ServerClient client) throws IOException {
		this.server = server;
		this.client = client;
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
	} 
	
	public void run(){

		while(true){
			try {
				String s = input.readLine();
				if (s != null) {
					JSONObject json = new JSONObject(s);	
					server.receiveFromClient(json, client);					
				}
				
			} catch (IOException e){
				server.stop();
			}

		}
	}

	public void sendToClient(JSONObject json) {
		String msg = json.toString();
		if(msg != null) 
			output.println(msg);
		
	}

	public void sendUpdateGraphicsToClient(JSONObject json){
		json.put("notification", "updateGraphics");
		sendToClient(json);
	}



	public void sendChooseTeamToClient(JSONObject gameConfigJSON) {
		gameConfigJSON.put("notification", "chooseTeam");
		sendToClient(gameConfigJSON);
		
	}

}