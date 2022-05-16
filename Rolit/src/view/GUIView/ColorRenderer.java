package view.GUIView;

import java.awt.Component;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.UIManager;

import model.logic.Color;

/**
 * This class is a Renderer Swing that designs the ComboBox that shows
 * the various colors that the user can choose to each of the players
 * @author PMC
 */

public class ColorRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1L;
	
	private Map<Color, ImageIcon> iconMap = new HashMap<>();
    private java.awt.Color background = new java.awt.Color(0, 100, 255, 15);
    private java.awt.Color defaultBackground = (java.awt.Color) UIManager.get("List.background");
    
    private final int SIDE_LENGTH = 8;

    /**
     * Constructor. Its mission is to load all the icons of colors
     * and storing them
     */
    public ColorRenderer() {
        for (Color c : Color.values()) {
        	ImageIcon originalImgIcon = new ImageIcon(c.getPath());
			Image originalImg = originalImgIcon.getImage();
			Image resizedImg = originalImg.getScaledInstance(SIDE_LENGTH, SIDE_LENGTH,  java.awt.Image.SCALE_SMOOTH);
			ImageIcon resizedImgIcon = new ImageIcon(resizedImg);
			iconMap.put(c, resizedImgIcon);	
        }
    }

    /**
     * This method is used to look for the desired characteristics that the
     * item of the ComboBox should show
     * @return The specific item of the ComboBox
     */
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        Color c = (Color) value;
        this.setText(c.toFullNameString());
        this.setIcon(iconMap.get(c));
        if (!isSelected) {
            this.setBackground(index % 2 == 0 ? background : defaultBackground);
        }
        return this;
    }
}
