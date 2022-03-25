package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import control.Controller;

public class ControlPanel extends JPanel implements ActionListener {

	private Controller ctrl;
	JFileChooser fc;
	
	public ControlPanel(Controller _ctrl) {
		ctrl = _ctrl;
		
		JButton btnOpenFile = new JButton();
		btnOpenFile.setActionCommand("abrir");
		btnOpenFile.setIcon(new ImageIcon("resources/icons/open.png"));
		btnOpenFile.addActionListener(this);
		btnOpenFile.setMinimumSize(new Dimension(75, 20));
		this.add(btnOpenFile);
		
		JButton btnSaveFile = new JButton();
		btnSaveFile.setActionCommand("guardar");
		btnSaveFile.setIcon(new ImageIcon("resources/icons/save.png"));
		btnSaveFile.addActionListener(this);
		btnSaveFile.setMinimumSize(new Dimension(75, 20));
		this.add(btnSaveFile);
		
		fc = new JFileChooser();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("abrir")) {
			int ret = fc.showOpenDialog(this);
			//según salga yo de esta ventana, "ret" tomará un valor u otro
			if (ret == JFileChooser.APPROVE_OPTION) {
				//...
			} else {
				//...
			}
		} else if (e.getActionCommand().equals("guardar")) {
			int ret = fc.showSaveDialog(this);
			if (ret == JFileChooser.APPROVE_OPTION) {
				//...
			} else {
				//...
			}
		}
		
	}

}
