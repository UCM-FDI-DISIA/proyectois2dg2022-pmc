package server;

import java.awt.Dimension;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.json.JSONObject;

import view.GUIView.RolitComponents.RolitButton;
import view.GUIView.RolitComponents.RolitPanel;
import view.GUIView.RolitComponents.RolitTextArea;
import view.GUIView.RolitComponents.RolitTextField;


public class ServerView extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String ICONS_PATH = "resources\\icons\\";

	
	private JPanel thePanel;
	private JLabel ipLabel, portLabel;
	private JTextField portField;
	private JButton startButton;
	private JButton stopButton;
	protected Server server;
	protected int port;

	public ServerView(Server server) {
		this.setTitle("Server Set-Up");
		this.setIconImage(new ImageIcon(ICONS_PATH + "\\rolitIcon.png").getImage());
		this.server = server;
		setSize(400,400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		thePanel = new RolitPanel();
		thePanel.setLayout(new GridBagLayout());

		ipLabel = new JLabel("Server IP: " + server.getIp());
		addComp(thePanel, ipLabel, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		portLabel = new JLabel("Insert desired host port (9001-65500): ");
		addComp(thePanel, portLabel, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
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
				addComp(thePanel, stopButton, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
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
	
	

	public void showError(String msg) {
		JOptionPane.showMessageDialog(thePanel, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public class ServerWorker extends SwingWorker<Void, Void> {

		// Método obligatorio
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
