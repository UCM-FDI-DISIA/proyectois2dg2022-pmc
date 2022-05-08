package model.online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;

/**
 * This class is in charge of getting and sending information from the client
 * that is associated with this thread 
 * @author PMC
 */
public class ServerClientThread extends Thread {

	private BufferedReader input;
	private PrintWriter output;
	private volatile ServerClient serverClient;
	private volatile Server server;

	/**
	 * Constructor
	 * @param server Server
	 * @param socket Socket that holds the connection already made by the server and the specific client
	 * @param serverClient The instance of the ServerClient associated with this thread
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	public ServerClientThread(Server server, Socket socket, ServerClient serverClient) throws IOException {
		this.server = server;
		this.serverClient = serverClient;
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
	} 
	
	/**
	 * This methods runs the thread, and its mission is to get information from the client
	 * and subsequently sending it to server
	 */
	public void run(){
		while(true){
			try {
				String s = input.readLine();
				if (s != null) {
					JSONObject json = new JSONObject(s);	
					server.receiveFromClient(json, serverClient);					
				}
				
			} catch (IOException e){
				server.stop();
			}

		}
	}

	/**
	 * This methods sends a given information from the server to the client
	 * @param json Information that wants to be sent in the shape of a json
	 */
	public void sendToClient(JSONObject json) {
		String msg = json.toString();
		if(msg != null) 
			output.println(msg);
	}

	/**
	 * This method creates a new JSON given the sent one from server, putting in it
	 * the notification of updateGraphics so that the client knows that this information
	 * should be used in order to update its GUI
	 * @param json Information sent by server
	 */
	public void sendUpdateGraphicsToClient(JSONObject json){
		json.put("notification", "updateGraphics");
		sendToClient(json);
	}

	/**
	 * This method creates a new JSON given the sent one from server, putting in it
	 * the notification of chooseTeam so that the client knows that this information
	 * should be used in order to let the user choose one of the teams included
	 * in the JSON sent
	 * @param gameConfigJSON Information sent by server
	 */
	public void sendChooseTeamToClient(JSONObject gameConfigJSON) {
		gameConfigJSON.put("notification", "chooseTeam");
		sendToClient(gameConfigJSON);
	}

}