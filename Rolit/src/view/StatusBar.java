package view;

import javax.swing.JLabel;
import javax.swing.JPanel;

import control.Controller;

public class StatusBar extends JPanel {

	private Controller ctrl;
	
	public StatusBar(Controller _ctrl) {
		_ctrl = ctrl;
		JLabel statusLabel = new JLabel("Status: ");
		this.add(statusLabel);
	}

}
