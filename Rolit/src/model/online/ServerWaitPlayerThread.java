package model.online;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class is in charge of running a thread associated with an
 * expected player that makes a connection when the player tries to
 * connect to server
 * @author PMC
 */
public class ServerWaitPlayerThread extends Thread {
	
	private volatile Server server;
	private ServerSocket serverSocket;
	private Socket socket;
	public volatile ServerClient serverClient;
	
	/**
	 * Constructor
	 * @param serverSocket The instance of the servers' socket
	 * @param socket The instance of a socket, associated with a specific hypothetical client
	 * @param serverClient The instance of a ServerClient, associated with a specific hypothetical client
	 * @param server The instance of the server
	 */
	public ServerWaitPlayerThread(ServerSocket serverSocket, Socket socket, ServerClient serverClient, Server server) {
		this.server = server;
		this.serverSocket = serverSocket;
		this.serverClient = serverClient;
		this.socket = socket;
	}
	
	/**
	 * This method runs the thread that makes the connection; after making it,
	 * it tells the serverClient to make a new thread (ServerClientThread) in charge
	 * of exchanging information between server and this specific client
	 */
	public void run() {

		try {
		
			socket = serverSocket.accept();
			serverClient.setUpServerClientConnection(socket, server);

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
