package view.GUIView.RolitComponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class RolitIconButton extends JButton {

	private static final long serialVersionUID = 1L;
	
	public RolitIconButton(ImageIcon icon) {
		this.setBackground(Color.WHITE);
		Image img = icon.getImage();
		this.setIcon(new ImageIcon(img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH)));
		this.setMinimumSize(new Dimension(75, 20));
	}

}
