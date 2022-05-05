package view.GUIView.RolitComponents;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class RolitTextField extends JTextField {
	private static final long serialVersionUID = 1L;
	
	private static final Color BLUE_ROLIT = new Color(0, 67, 152);

	public RolitTextField(int n){
		super(n);
		this.setBorder(BorderFactory.createLineBorder(BLUE_ROLIT, 1));
	}
}
