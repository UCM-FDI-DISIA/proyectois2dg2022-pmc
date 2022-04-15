package client;

import org.json.JSONObject;

import logic.Player;
import view.GUIView.MainWindow;

public class Client {

	private MainWindow mainWindow;
	private ClientController clientController;
	private Player player;
	
	public Client() {
		
		mainWindow = new MainWindow(this);
		
	}
	
	protected JSONObject getGameReport() {
		return mainWindow.getGameReport();
		
		
	}

	public void updateGameFromServer(JSONObject JSONJuegoNuevo) {
		mainWindow.updateGameFromServer(JSONJuegoNuevo);
	}

	public void empezarPartida(String ip, int port) {
		clientController = new ClientController(this, ip, port);
		clientController.start();
		
	}

	public void updateGameToServer() {
		clientController.updateGameToServer();
		
	}

	public void setPlayer(Player player) {
		if (this.player == null) {
			this.player = player;
			clientController.sendToServer(player.report());
		}

	}

	public Player getPlayer() {
		return this.player;
	}

	
}
