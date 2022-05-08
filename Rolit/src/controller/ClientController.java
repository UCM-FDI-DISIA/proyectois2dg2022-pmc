package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;

import model.online.Client;

public class ClientController extends Thread{

	private Client clientRolit;
	boolean puedeJugar = false;
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out; 


	public ClientController(Client clientRolit, String ipAdress, int port) throws Exception {
		this.clientRolit = clientRolit;
		socket = new Socket(ipAdress, port);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
	}
	

	public void updateGameToServer() {
		puedeJugar = false;
		JSONObject report = clientRolit.getGameReport();
		sendGameToServer(report);
		
	}


	public void run(){
		try {
			// FIXME hay un while true
			while(true){				
				String msgFromServer = in.readLine(); //se para en esta línea hasta que llega un mensaje
				
				JSONObject newGameJSON = new JSONObject(msgFromServer);
				
				if (newGameJSON.getString("notification").equals("updateGraphics"))
					clientRolit.updateGameFromServer(newGameJSON);
				else if (newGameJSON.getString("notification").equals("chooseTeam"))
					clientRolit.chooseTeamFromServer(newGameJSON);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(out != null)
				out.close();	
				try {
					in.close();
					socket.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				System.exit(0);
			}
	}


	public void sendToServer(JSONObject report) {
		String msg = report.toString();
		out.println(msg);
	}

	public void sendChosenTeamToServer(JSONObject selectedTeamJSON) {
		selectedTeamJSON.put("notification", "chooseTeam");
		sendToServer(selectedTeamJSON);
		
	}

	public void sendPlayerInfoToServer(JSONObject report) {
		report.put("notification", "playerInfo");
		sendToServer(report);
	}
	
	public void sendGameToServer(JSONObject report) {
		report.put("notification", "updateGraphics");
		sendToServer(report);
	}




}