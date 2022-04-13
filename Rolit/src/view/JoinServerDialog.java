package view;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class JoinServerDialog extends JDialog {

	
	private JPanel connectPanel;
	private int status = 0;
	
	private String ip;
	private String port;
	
	
	public JoinServerDialog(Frame parent) {
		super(parent, true);
		initGUI();
		
	}
	
	private void initGUI() {

		setSize(300,400);

		connectPanel = new JPanel();
		connectPanel.setLayout(new GridBagLayout());

		JLabel ipLabel = new JLabel("IP-Address:");
		JTextField ipField = new JTextField(10);
		addComp(connectPanel, ipLabel, 0, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
		addComp(connectPanel, ipField, 1, 0, 2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		JLabel portLabel = new JLabel("Port:");
		JTextField portField = new JTextField(10);
		addComp(connectPanel, portLabel, 0, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
		addComp(connectPanel, portField, 1, 1, 2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

		JButton connect = new JButton("Connect");
		
		//Actionlistner for the connect button
		connect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == connect) {
					ip = ipField.getText().trim();
					port = portField.getText().trim();

					try {
						status = 1;
						JoinServerDialog.this.setVisible(false);

					} catch (NumberFormatException e1) {
						displayError("Wrong port format. Try again.");
					}


				}

			}
		});
		addComp(connectPanel, connect, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
		
		add(connectPanel);
		pack();
		
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
	
	int open() {
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		setVisible(true);
		return status;
	}
	
	public void displayError(String error) {
		JOptionPane.showMessageDialog(JoinServerDialog.this, error, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public String getIp() {
		return ip;
	}

	public String getPort() {
		return port;
	}
	
}
