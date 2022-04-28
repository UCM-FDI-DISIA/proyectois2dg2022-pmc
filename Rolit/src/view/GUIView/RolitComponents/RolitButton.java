package view.GUIView.RolitComponents;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

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
	
	public RolitButton(String text, Dimension d, int thickness){
		this.setBackground(Color.WHITE);
		this.setText(text);
		this.setMaximumSize(d);
		this.setSize(d);
		this.setOpaque(false);
        this.setFocusPainted(false);
        //this.setForeground(BLUE_ROLIT);
        
       // Border blueLineBorder=  BorderFactory.createStrokeBorder(new BasicStroke());
        Border blueLineBorder=  BorderFactory.createLineBorder(BLUE_ROLIT, thickness);
        Border emptyBorder = BorderFactory.createEmptyBorder(this.getBorder().getBorderInsets(this).top, this.getBorder().getBorderInsets(this).left, this.getBorder().getBorderInsets(this).bottom, this.getBorder().getBorderInsets(this).right);
	    this.setBorder(BorderFactory.createCompoundBorder( blueLineBorder, emptyBorder));
	}
	
	
	public RolitButton(String text, int thickness){
		this(text, new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT), thickness);
	}
}
