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
		sendToServer(clientRolit.getGameReport());
		
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
				
				String msgFromServer = in.readLine(); //se para en esta línea hasta que llega un mensaje
				JSONObject JSONJuegoNuevo = new JSONObject(msgFromServer){
				    /**
				     * changes the value of JSONObject.map to a LinkedHashMap in order to maintain
				     * order of keys.
				     */
				    @Override
				    public JSONObject put(String key, Object value) throws JSONException {
				        try {
				            Field map = JSONObject.class.getDeclaredField("map");
				            map.setAccessible(true);
				            Object mapValue = map.get(this);
				            if (!(mapValue instanceof LinkedHashMap)) {
				                map.set(this, new LinkedHashMap<>());
				            }
				        } catch (NoSuchFieldException | IllegalAccessException e) {
				            throw new RuntimeException(e);
				        }
				        return super.put(key, value);
				    }
				};
				clientRolit.updateGameFromServer(JSONJuegoNuevo);
				System.out.println("Actualizado cliente " + clientRolit.getPlayer());
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
				//theView.displayError("Crashed.");
				System.exit(0);
			}
	}


	public void sendToServer(JSONObject report) {
		out.println(report.toString());
		
	}




}