package Rolit;

import org.json.JSONObject;

import view.MainWindow;

public class ClientRolit {

	private MainWindow mainWindow;
	private ClientController clientController;
	
	public ClientRolit() {
		
		mainWindow = new MainWindow(this);
		
	}
	
	protected JSONObject getGameReport() {
		return mainWindow.getGameReport();
		
		
	}

	public void updateGameFromServer(JSONObject JSONJuegoNuevo, boolean puedeJugar) {
		mainWindow.updateGameFromServer(JSONJuegoNuevo, puedeJugar);
	}
	
	public void updateGameFromServer(boolean puedeJugar) {
		mainWindow.updateGameFromServer(puedeJugar);
		
	}

	public void empezarPartida(String ip, int port) {
		clientController = new ClientController(this, ip, port);
		clientController.start();
		
	}

	public void updateGameToServer() {
		clientController.updateGameToServer();
		
	}

	
}
