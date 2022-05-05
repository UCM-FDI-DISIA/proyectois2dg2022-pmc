package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WaitPlayerThread extends Thread {
	
	private volatile Server server;
	private ServerSocket serverSocket;
	private Socket socket;
	public volatile ServerClient serverClient;
	
	public WaitPlayerThread(ServerSocket serverSocket, Socket socket, ServerClient serverClient, Server server) {
		this.server = server;
		this.serverSocket = serverSocket;
		this.serverClient = serverClient;
		this.socket = socket;
	}
	
	public void run() {

		try {
		
			socket = serverSocket.accept();
			serverClient.setUpServer(socket, server);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
				
		} catch (IOException e) {
		}

	}

}
