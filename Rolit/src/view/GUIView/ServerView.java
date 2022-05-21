package view.GUIView;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import model.online.Server;
import view.GUIView.RolitComponents.RolitButton;
import view.GUIView.RolitComponents.RolitPanel;
import view.GUIView.RolitComponents.RolitTextField;

/**
 * This class is in charge of showing a GUI that lets the user
 * choose the port in which they want the server to connect
 * @author PMC
 */
public class ServerView extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String ICONS_PATH = "resources\\icons\\";

	private JPanel thePanel;
	private JLabel ipLabel, portLabel, waitingLabel, numPlayersLabel;
	private JTextField portField;
	private JButton startButton;
	private JButton stopButton;
	protected Server server;
	protected int port;

	/**
	 * Constructor
	 * @param server The instance of the server, which needs to be stored
	 */
	public ServerView(Server server) {
		this.setTitle("Server Set-Up");
		this.setIconImage(new ImageIcon(ICONS_PATH + "\\rolitIcon.png").getImage());
		this.server = server;
		setSize(420,420);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		thePanel = new RolitPanel();
		thePanel.setLayout(new GridBagLayout());

		ipLabel = new JLabel("Server IP: " + "your IPv4");
		addComp(thePanel, ipLabel, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		portLabel = new JLabel("Insert desired host port (9001-65500): ");
		addComp(thePanel, portLabel, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		waitingLabel = new JLabel("Waiting for players...");
		
		portField = new RolitTextField(5);
		addComp(thePanel, portField, 1, 1, 100, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		startButton = new RolitButton("Start server");
		addComp(thePanel, startButton, 0, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		stopButton = new RolitButton("Stop server");
		
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				port = Integer.parseInt(portField.getText().trim());
				if(port < 9001 || port > 65500) {
					JOptionPane.showMessageDialog(thePanel, "Port not within limits, please try again.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				thePanel.remove(portLabel);
				thePanel.remove(portField);
				thePanel.remove(startButton);
				numPlayersLabel = new JLabel(server.getNumPlayers() + " / " + server.getExpectedPlayers());
				addComp(thePanel, numPlayersLabel, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
				addComp(thePanel, waitingLabel, 0, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
				addComp(thePanel, stopButton, 0, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
				ServerView.this.setSize(ServerView.this.getWidth(), 150);
				ServerView.this.revalidate();
				ServerView.this.repaint();
				thePanel.setVisible(true);
				
				new ServerWorker().execute();
				
				
			}
		});
		

		stopButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				server.stop();
			}
			
		});
		

		add(thePanel);
		pack();
		setVisible(true);
		
	}
	
	/**
	 * This method adds a JComponent in a specified JPanel
	 * @param thePanel Panel in which the component is to be added
	 * @param comp The JComponent to be added
	 * @param xPos The horizontal component of the position in which the component is to be added
	 * @param yPos The vertical component of the position in which the component is to be added
	 * @param compWidth The width of the component to be added
	 * @param compHeight The height of the component to be added
	 * @param place Where in the display area should the component be added
	 * @param stretch Integer that determines whether to resize the component, and if so, how. 
	 */
	private void addComp(JPanel thePanel, JComponent comp, int xPos, int yPos, int compWidth, int compHeight, int place, int stretch){

		GridBagConstraints gridConstraints = new GridBagConstraints();

		gridConstraints.gridx = xPos;
		gridConstraints.gridy = yPos;
		gridConstraints.gridwidth = compWidth;
		gridConstraints.gridheight = compHeight;
		gridConstraints.weightx = 100;
		gridConstraints.weighty = 100;
		gridConstraints.insets = new Insets(5,5,5,5);
		gridConstraints.anchor = place;
		gridConstraints.fill = stretch;

		thePanel.add(comp, gridConstraints);
	}
	
	/**
	 * This method updates in GUI the number of players that have already been connected
	 */
	public void updateNumPlayers() {
		thePanel.remove(numPlayersLabel);
		numPlayersLabel = new JLabel(server.getNumPlayers() + " / " + server.getExpectedPlayers());
		addComp(thePanel, numPlayersLabel, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		ServerView.this.revalidate();
		ServerView.this.repaint();
	}
	
	/**
	 * This class is in charge of starting a server from a SwingWorker
	 * so that the servers' threads can correctly work with Swing's thread
	 */
	public class ServerWorker extends SwingWorker<Void, Void> {

		@Override
		protected Void doInBackground() {
			try {
				server.start(port);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(thePanel, "Server could not be started.", "Error", JOptionPane.ERROR_MESSAGE);
				server.stop();
			}
			return null;
		}
	}

}
