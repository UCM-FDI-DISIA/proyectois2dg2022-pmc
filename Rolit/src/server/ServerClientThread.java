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

/**
 * Thread that handles all the IO for a client
 *
 * @author  Oliver Ekberg
 * @since   2018-04-01
 * @version 1.0
 */
public class ServerClientThread extends Thread{

	private BufferedReader input;
	private PrintWriter output;
	private ServerClient client;
	private Server server;
	private boolean isRunning = true;

	
	/**
	 * Sets variables and creates a input and output
	 * 
	 * @param server		Reference to the server
	 * @param socket		Reference to socket
	 * @param client		Reference to owner
	 * @throws IOException
	 */
	public ServerClientThread(Server server, Socket socket, ServerClient client) throws IOException {
		this.server = server;
		this.client = client;
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
	} 

	
	/**
	 * Kills the thread
	 */
	public void kill() {
		isRunning = false;
	}

	
	/**
	 * While game {@link #isRunning is running} it takes in json from client, formats it and sends it to the {@link Server server}
	 * 
	 * @see Server#receiveFromClient(String, ServerClient)
	 */
	public void run(){

		while(isRunning){
			try {
				String s = input.readLine();
				if (s != null) {
					JSONObject json = new JSONObject(s){
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
					server.receiveFromClient(json, client);
					
				}
				
			} catch (IOException e){}

		}
	}

	
	/**
	 * @param msg	Message to send to the client
	 */
	public void updateGraphics(JSONObject json){
		String msg = json.toString();
		if(msg != null) 
			output.println(msg);
	}

}