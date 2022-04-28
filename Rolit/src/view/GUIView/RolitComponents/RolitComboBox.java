package view.GUIView.RolitComponents;

import java.awt.Color;
import java.awt.Shape;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;

public class RolitComboBox<E> extends JComboBox<E> {
	private static final Color BLUE_ROLIT = new Color(0, 67, 152);

	public RolitComboBox(){
		this.setBackground(Color.WHITE);
	}
	
	public RolitComboBox(E[] items){
		super(items);
		this.setBackground(Color.WHITE);
	}


}
