package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONObject;

import view.MainWindow;


/**
 * Handles all client-side logic
 *
 * @author  Oliver Ekberg
 * @since   2018-04-01
 * @version 1.0
 */
public class ClientController extends Thread{

	private Client clientRolit;
	boolean puedeJugar = false;

	/*
	 * Server stuff
	 */
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out; 



	/**
	 * Connects to server
	 * 
	 * @param mainWindow 	Reference to view
	 * @param ipAdress 	Address of server
	 * @param port 		Port of server
	 * @see 				View#displayError(String)
	 */
	public ClientController(Client clientRolit, String ipAdress, int port){
		this.clientRolit = clientRolit;

		try {
			socket = new Socket(ipAdress, port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException  | IllegalArgumentException e) {
			//mainWindow.displayError("Could not connect to server.");
			System.exit(0);
		}
	}
	

	public void updateGameToServer() {
		puedeJugar = false;
		clientRolit.updateGameFromServer(puedeJugar);
		out.println(clientRolit.getGameReport().toString());
	}

	
	/**
	 * Waits for and handles incoming commands from the server
	 * 
	 * @see View#render(ArrayList)
	 * @see View#render(ArrayList, Markers)
	 * @see View#gameOver(String)
	 */
	public void run(){
		try {
			while(true){
				
				String msgFromServer = in.readLine();
				if (msgFromServer.equals("Es tu turno")) {
					puedeJugar = true;
				}
				else {
					JSONObject JSONJuegoNuevo = new JSONObject(msgFromServer);
					clientRolit.updateGameFromServer(JSONJuegoNuevo, puedeJugar);
				}
				

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
				} catch (IOException e2) {}
				//theView.displayError("Crashed.");
				System.exit(0);
			}
	}




}