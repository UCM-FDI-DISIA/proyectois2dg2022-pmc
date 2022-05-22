package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;

import model.online.Client;

/**
 * This class is from the Client's perspective. It is the controller that
 * manages the information sent by the server, as well as it sending it
 * @author PMC
 */
public class ClientController extends Thread{

	private Client clientRolit;
	boolean puedeJugar = false;
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out; 


	/**
	 * Constructor 
	 * @param clientRolit Instance of the client associated
	 * @param ipAddress String of the IP address chosen to connect
	 * @param port Integer of the port in which that IP address from server hypothetically operates  
	 * @throws Exception Exception that shows that a connection had not been possible
	 */
	public ClientController(Client clientRolit, String ipAddress, int port) throws Exception {
		this.clientRolit = clientRolit;
		socket = new Socket(ipAddress, port);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
	}
	

	/**
	 * This method extract the report of the game currently played
	 * and sends it to the server
	 */
	public void updateGameToServer() {
		puedeJugar = false;
		JSONObject report = clientRolit.getGameReport();
		sendGameToServer(report);
		
	}

	/**
	 * This method runs the thread in charge of receiving information from server.
	 * It checks the nature of the information looking in the notification field of the
	 * sent JSON, and based from that it calls particular methods.
	 */
	public void run(){
		try {
			while(true){				
				String msgFromServer = in.readLine(); //it stops in this line until a message is received
				
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


	/**
	 * This method sends a given JSONObject to the server
	 * @param report JSONObject to be sent
	 */
	public void sendToServer(JSONObject report) {
		String msg = report.toString();
		out.println(msg);
	}

	/**
	 * This method modifies a given JSONObject including it the
	 * chooseTeam notification so that, when sent, the Server knows
	 * that this information is to be processed as the order of
	 * retrieving information of the new team chosen.
	 * @param selectedTeamJSON JSONObject to be modified
	 */
	public void sendChosenTeamToServer(JSONObject selectedTeamJSON) {
		selectedTeamJSON.put("notification", "chooseTeam");
		sendToServer(selectedTeamJSON);
		
	}

	/**
	 * This method modifies a given JSONObject including it the
	 * playerInfo notification so that, when sent, the Server knows
	 * that this information is to be processed as the order of
	 * getting information of the player created by the client.
	 * @param report JSONObject to be modified
	 */
	public void sendPlayerInfoToServer(JSONObject report) {
		report.put("notification", "playerInfo");
		sendToServer(report);
	}
	
	/**
	 * This method modifies a given JSONObject including it the
	 * chooseTeam notification so that, when sent, the Server knows
	 * that this information is to be processed as the order of
	 * update all the other clients' GUI with the new game data
	 * @param report JSONObject to be modified
	 */
	public void sendGameToServer(JSONObject report) {
		report.put("notification", "updateGraphics");
		sendToServer(report);
	}

}