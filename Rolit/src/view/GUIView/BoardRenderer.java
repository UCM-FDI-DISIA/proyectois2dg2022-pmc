package view.GUIView;

import java.awt.Component;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.UIManager;

import logic.Shape;


public class BoardRenderer extends DefaultListCellRenderer {
    private Map<Shape, ImageIcon> iconMap = new HashMap<>();
    private java.awt.Color background = new java.awt.Color(0, 100, 255, 15);
    private java.awt.Color defaultBackground = (java.awt.Color) UIManager.get("List.background");
    
    private final int SIDE_LENGTH = 16;

    public BoardRenderer() {
        for (Shape s : Shape.values()) {
        	ImageIcon originalImgIcon = new ImageIcon(s.getPath());
			Image originalImg = originalImgIcon.getImage();
			Image resizedImg = originalImg.getScaledInstance(SIDE_LENGTH, SIDE_LENGTH,  java.awt.Image.SCALE_SMOOTH);
			ImageIcon resizedImgIcon = new ImageIcon(resizedImg);
			iconMap.put(s, resizedImgIcon);	
        }
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        Shape c = (Shape) value;
        this.setText(c.toString());
        this.setIcon(iconMap.get(c));
        if (!isSelected) {
            this.setBackground(index % 2 == 0 ? background : defaultBackground);
        }
        return this;
    }
}