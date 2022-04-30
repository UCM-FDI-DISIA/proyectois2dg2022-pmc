package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientController extends Thread{

	private Client clientRolit;
	boolean puedeJugar = false;
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out; 


	public ClientController(Client clientRolit, String ipAdress, int port){
		this.clientRolit = clientRolit;

		try {
			socket = new Socket(ipAdress, port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException  | IllegalArgumentException e) {
			System.exit(0);
		}
	}
	

	public void updateGameToServer() {
		puedeJugar = false;
		JSONObject report = clientRolit.getGameReport();
		sendToServer(report);
		
	}

	
	public void run(){
		try {
			// FIXME hay un while true
			while(true){				
				String msgFromServer = in.readLine(); //se para en esta línea hasta que llega un mensaje
				JSONObject JSONJuegoNuevo = new JSONObject(msgFromServer);
				if (JSONJuegoNuevo.getString("notification").equals("updateGraphics"))
					clientRolit.updateGameFromServer(JSONJuegoNuevo);
				else if (JSONJuegoNuevo.getString("notification").equals("chooseTeam"))
					clientRolit.chooseTeamFromServer(JSONJuegoNuevo);
				System.out.println("Actualizado cliente " + clientRolit.getPlayer());
			}
		} catch (Exception e) {
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
		String msg = selectedTeamJSON.toString();
		out.println(msg);
		
	}




}