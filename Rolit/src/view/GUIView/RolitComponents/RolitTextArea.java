package view.GUIView.RolitComponents;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

public class RolitTextArea extends JTextArea {
	private static final long serialVersionUID = 1L;
	
	private static final Color BLUE_ROLIT = new Color(0, 67, 152);

	public RolitTextArea(){
		Border innerBorder = this.getBorder();
		Border outerBorder = BorderFactory.createLineBorder(BLUE_ROLIT, 1);
		this.setBorder(new CompoundBorder(outerBorder, innerBorder));
	}
}
