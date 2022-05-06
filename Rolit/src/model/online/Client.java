package model.online;

import org.json.JSONObject;

import controller.ClientController;
import model.logic.Color;
import model.logic.Player;
import view.GUIView.MainWindow;

public class Client {

	private volatile MainWindow mainWindow;
	private ClientController clientController;
	private Player player;
	
	public Client(MainWindow mW) {		
		mainWindow = mW;		
	}
	
	public JSONObject getGameReport() {
		return mainWindow.getGameReport();		
	}

	public void updateGameFromServer(JSONObject JSONJuegoNuevo) {
		mainWindow.updateGameFromServer(JSONJuegoNuevo);
	}

	public void empezarPartida(String ip, int port) throws Exception {
		clientController = new ClientController(this, ip, port);
		clientController.start();		
	}

	public void updateGameToServer() {
		clientController.updateGameToServer();		
	}

	public void join(String name, Color color) {
		if (this.player == null) {
			this.player = new Player(color, name);
			clientController.sendPlayerInfoToServer(player.report());
		}
	}

	public Player getPlayer() {
		return this.player;
	}

	public void chooseTeamFromServer(JSONObject JSONJuegoNuevo) {
		mainWindow.chooseTeamFromServer(JSONJuegoNuevo);
		
	}

	public void sendChosenTeamToServer(JSONObject selectedTeamJSON) {
		clientController.sendChosenTeamToServer(selectedTeamJSON);		
	}

	
}
