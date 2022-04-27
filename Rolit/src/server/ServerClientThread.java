package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerClientThread extends Thread {

	private BufferedReader input;
	private PrintWriter output;
	private volatile ServerClient client;
	private volatile Server server;
	private boolean isRunning = true;

	public ServerClientThread(Server server, Socket socket, ServerClient client) throws IOException {
		this.server = server;
		this.client = client;
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
	} 

	

	public void kill() {
		isRunning = false;
	}

	
	public void run(){

		while(isRunning){
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


	public void updateGraphics(JSONObject json){
		json.put("notification", "updateGraphics");
		String msg = json.toString();
		if(msg != null) 
			output.println(msg);
	}



	public void notifyClientToChooseTeam(JSONObject gameConfigJSON) {
		gameConfigJSON.put("notification", "chooseTeam");
		String msg = gameConfigJSON.toString();
		if(msg != null) 
			output.println(msg);
		
	}

}