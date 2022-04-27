package view.GUIView.RolitComponents;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

public class RolitButton extends JButton {

	private static final long serialVersionUID = 1L;
	
	private static final Color BLUE_ROLIT = new Color(0, 67, 152);
	
	private static final int PREFERRED_WIDTH = 200;
	private static final int PREFERRED_HEIGHT = 50;
	private static final int PREFERRED_BORDER = 2;


	public RolitButton(String text){
		this(text, new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
	}
	
	public RolitButton(String text, Dimension d){
		this(text, d, PREFERRED_BORDER);

	}
	
	public RolitButton(String text, Dimension d, int borderWidth){
		this.setBackground(Color.WHITE);
		this.setText(text);
		this.setMaximumSize(d);
		this.setSize(d);
		Border innerBorder = this.getBorder();
		Border outerBorder = BorderFactory.createLineBorder(BLUE_ROLIT, borderWidth);
		this.setBorder(new CompoundBorder(outerBorder, innerBorder));
	}
	
	
	public RolitButton(String text, int borderWidth){
		this(text, new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT), borderWidth);
	}
}
