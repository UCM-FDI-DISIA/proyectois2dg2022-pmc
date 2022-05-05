package view.GUIView.RolitComponents;

import java.awt.Color;
import javax.swing.JComboBox;

public class RolitComboBox<E> extends JComboBox<E> {

	private static final long serialVersionUID = 1L;

	public RolitComboBox(){
		this.setBackground(Color.WHITE);
	}
	
	public RolitComboBox(E[] items){
		super(items);
		this.setBackground(Color.WHITE);
	}


}
