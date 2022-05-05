package view.GUIView.RolitComponents;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class RolitTextArea extends JTextArea {
	private static final long serialVersionUID = 1L;
	
	private static final Color BLUE_ROLIT = new Color(0, 67, 152);

	public RolitTextArea(){
		   Border blueLineBorder=  BorderFactory.createLineBorder(BLUE_ROLIT, 1);
	        Border emptyBorder = BorderFactory.createEmptyBorder(this.getBorder().getBorderInsets(this).top, this.getBorder().getBorderInsets(this).left, this.getBorder().getBorderInsets(this).bottom, this.getBorder().getBorderInsets(this).right);
		    this.setBorder(BorderFactory.createCompoundBorder( blueLineBorder, emptyBorder));
	}
}
