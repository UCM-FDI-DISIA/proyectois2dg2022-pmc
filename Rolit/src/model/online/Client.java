package model.online;

import org.json.JSONObject;

import controller.ClientController;
import model.logic.Color;
import model.logic.Player;
import view.GUIView.MainWindow;

/**
 * This class is from the Client's perspective. It is in charge of being the intermediary between
 * GUI (MainWindow) and the client's thread which is in contact
 * with the server (ClientController).
 * @author PMC
 */

public class Client {

	private volatile MainWindow mainWindow;
	private ClientController clientController;
	private Player player;
	
	
	/**
	 * Constructor
	 */
	public Client(MainWindow mW) {		
		mainWindow = mW;		
	}
	
	/**
	 * This method is used to get the JSONObject that represents
	 * the game from the GUI
	 * @return JSONObject that represents the report of the game being played currently
	 */
	public JSONObject getGameReport() {
		return mainWindow.getGameReport();		
	}
	
	/**
	 * This method is used to send to GUI the JSONObject report of the game
	 * that has been sent by the server from another client
	 * @param newGameJSON JSONObject that represents the report of the game sent by server
	 */
	public void updateGameFromServer(JSONObject newGameJSON) {
		mainWindow.updateGameFromServer(newGameJSON);
	}

	/**
	 * This method is used to start the client thread that allows
	 * the exchange of information between server and the current client
	 * @param ip String that represents the ip in which the server is operating
	 * @param port Integer that represents the port in which the ip of the server is operating
	 */
	public void startMatch(String ip, int port) throws Exception {
		clientController = new ClientController(this, ip, port);
		clientController.start();		
	}

	/**
	 * This method is used to notify the client thread (in direct contact with the server)
	 * that it needs to send to the server the new state of the game. 
	 */
	public void updateGameToServer() {
		clientController.updateGameToServer();		
	}

	/**
	 * This method is used to notify the client thread (in direct contact with the server)
	 * that it needs to send to server the info of the player that has been chosen in this current client
	 * @param name String that represents the name of the player, chosen previously in GUI.
	 * @param color Color that represents the color of the player, chosen previously in GUI.
	 */
	public void join(String name, Color color) {
		if (this.player == null) {
			this.player = new Player(color, name);
			clientController.sendPlayerInfoToServer(player.report());
		}
	}

	/**
	 * This method is used to get the player that plays in this client.
	 * @return The player that plays in this client.
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * This method is used to notify GUI that a team should be chosen
	 * between the teams stored in newGameJSON
	 * @param newGameJSON Part of the game JSON that should be completed by client
	 * with the team chosen before returning it to server
	 */
	public void chooseTeamFromServer(JSONObject newGameJSON) {
		mainWindow.chooseTeamFromServer(newGameJSON);
		
	}

	/**
	 * This method is used to notify the client thread (in direct contact with server)
	 * that it needs to send to the server a new version of the game JSON that
	 * has been completed with the client's team preference.
	 * @param selectedTeamJSON New version of the game JSON that
	 * has been completed with the client's team preference.
	 */
	public void sendChosenTeamToServer(JSONObject selectedTeamJSON) {
		clientController.sendChosenTeamToServer(selectedTeamJSON);		
	}

	
}
