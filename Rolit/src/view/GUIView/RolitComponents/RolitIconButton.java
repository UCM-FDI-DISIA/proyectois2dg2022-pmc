package view.GUIView.RolitComponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;

public class RolitIconButton extends JButton {

	private static final long serialVersionUID = 1L;
	
	public RolitIconButton(ImageIcon icon) {
		this(icon, new Dimension(50, 50));
	}

	public RolitIconButton(ImageIcon icon, Dimension d) {
		this.setBackground(Color.WHITE);
		Image img = icon.getImage();
		this.setIcon(new ImageIcon(img.getScaledInstance((int)d.getWidth(), (int) d.getHeight(),  java.awt.Image.SCALE_SMOOTH)));
		this.setMinimumSize(d);
	}
}
