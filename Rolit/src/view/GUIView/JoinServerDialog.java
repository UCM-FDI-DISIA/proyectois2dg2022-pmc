package view.GUIView;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import model.logic.Color;
import view.GUIView.RolitComponents.RolitButton;
import view.GUIView.RolitComponents.RolitComboBox;
import view.GUIView.RolitComponents.RolitPanel;
import view.GUIView.RolitComponents.RolitTextArea;
import view.GUIView.RolitComponents.RolitTextField;

/**
 * This class is a Swing JDialog in which the user can join a server
 * @author PMC
 */
public class JoinServerDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private JPanel connectPanel;
	
	private JLabel waitingLabel;
	
	private int status = 0;
	
	private String ip;
	private int port;
	
	private final int MAX_TEXT_LENGTH = 15;
	
	private JTextArea nameTextArea;
	private JComboBox<Color> colorComboBox;
	
	/**
	 * Constructor
	 * @param parent The frame of the caller
	 */
	public JoinServerDialog(Frame parent) {
		super(parent, true);
		initGUI();
		
	}
	
	/**
	 * This method creates and shows all the components relative to this dialog
	 */
	@SuppressWarnings("serial")
	private void initGUI() {
		this.setTitle("Join Server");
		
		setSize(300,400);

		connectPanel = new RolitPanel();
		connectPanel.setLayout(new GridBagLayout());
		
		nameTextArea = new RolitTextArea();
		nameTextArea.setDocument(new PlainDocument() {
		    @Override
		    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException{
		        if (str == null || nameTextArea.getText().length() >= MAX_TEXT_LENGTH) {
		            return;
		        }
		 
		        super.insertString(offs, str, a);
		    }
		});
		nameTextArea.setEditable(true);
		nameTextArea.setLineWrap(true);
		nameTextArea.setWrapStyleWord(true);
		colorComboBox = new RolitComboBox<Color>();
		colorComboBox.setRenderer(new ColorRenderer());
		
		for(Color color : Color.values()) {
			colorComboBox.addItem(color);
		}
		
		addComp(connectPanel, new JLabel("Name: "), 0, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
		addComp(connectPanel, nameTextArea, 1, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
		addComp(connectPanel, new JLabel("Color: "), 2, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
		addComp(connectPanel, colorComboBox, 3, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);

		
		JLabel ipLabel = new JLabel("IP-Address:");
		JTextField ipField = new RolitTextField(10);
		addComp(connectPanel, ipLabel, 0, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
		addComp(connectPanel, ipField, 1, 1, 2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		JLabel portLabel = new JLabel("Port:");
		JTextField portField = new RolitTextField(10);
		addComp(connectPanel, portLabel, 0, 2, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
		addComp(connectPanel, portField, 1, 2, 2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		JButton connect = new RolitButton("Connect");
		
		connect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == connect) {
					try {
						ip = ipField.getText().trim();
						port = Integer.parseInt(portField.getText().trim());
						status = 1;
						JoinServerDialog.this.setVisible(false);

					} catch (NumberFormatException e1) {
						status = 0;
						displayError("Wrong port format. Try again.");
					}
					
				}
			}
		});
		addComp(connectPanel, connect, 1, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);		
		add(connectPanel);
		
		pack();
		
		//To center the component
		int x = (getParent().getWidth() - this.getWidth()) / 2;
		setLocation(getParent().getX() + x, getParent().getY() + 50);
		
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
	 * This method opens the dialog
	 * @return The status (1-success, 0-failure)
	 */
	int open() {
		setVisible(true);
		return status;
	}
	
	/**
	 * This method uses a JOptionPane that shows an error
	 * @param error Error to show
	 */
	public void displayError(String error) {
		JOptionPane.showMessageDialog(JoinServerDialog.this, error, "Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Getter of the IP introduced by the user
	 * @return IP introduced by the user
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Getter of the port introduced by the user
	 * @return Port introduced by the user
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Getter of the player color introduced by the user
	 * @return Player color introduced by the user
	 */
	public Color getPlayerColor() {
		return (Color) colorComboBox.getSelectedItem();
	}
	
	/**
	 * Getter of the player name introduced by the user
	 * @return Player name introduced by the user
	 */
	public String getPlayerName() {
		return (String) nameTextArea.getText();
	}

	/**
	 * This methods redesigns the dialog notifying that the client
	 * needs to wait until all the expected players are connected to the server.
	 */
	public void showWaitingDialog() {
		connectPanel.removeAll();
		
		waitingLabel = new JLabel("Waiting for all players to be connected...");
		
		addComp(connectPanel, waitingLabel, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		JoinServerDialog.this.setSize(JoinServerDialog.this.getWidth(), 150);
		JoinServerDialog.this.revalidate();
		JoinServerDialog.this.repaint();
		connectPanel.setVisible(true);
		JoinServerDialog.this.setVisible(true);
		
	}

	/**
	 * This methods closes the JDialog, particularly at the
	 * point when the JDialog shows the waiting for players message
	 */
	public void closeWaitingDialog() {
		JoinServerDialog.this.setVisible(false);
		
	}
	
}
