package view.GUIView;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import controller.Controller;
import model.commands.PlaceCubeCommand;
import model.replay.Replay;

/**
 * This class represents a cell of the BoardGUI. It is responsible of its
 * own design and of capturing the users' clicks.
 * @author PMC
 */

public class CellGUI {

	private boolean validButton;
	private boolean filled; // Una vez se ponga un cubo no se podrá poner otro (manualmente)
	private JButton button;
	private String iconPath;
	public static int SIDE_LENGTH;
	private static final String EMPTY_ICON_PATH = "resources/icons/emptyCell.png";
	private HashMap<model.logic.Color, ImageIcon> mapColorIcon = new HashMap<>();

	/**
	 * Constructor
	 * @param y The y-coordinate in which the cell must be created
	 * @param x The x-coordinate in which the cell must be created
	 * @param validButton A boolean value that specifies whether is a valid cell or not (i.e.
	 * clickable or unclickable)
	 * @param ctrl The controller
	 * @param sideLength The length of the side of the button (squared)
	 */
	public CellGUI(int y, int x, boolean validButton, Controller ctrl, int sideLength) {
		
		CellGUI.SIDE_LENGTH = sideLength;
		this.validButton = validButton;
		this.filled = false;
		this.button = new JButton();
		if (validButton) {
			this.button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!filled) {
						try {
							ctrl.executeCommand(new PlaceCubeCommand(x,y));
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});
		}
		
		this.button.setEnabled(validButton);
		this.validButton = validButton;
		this.iconPath = EMPTY_ICON_PATH;
		
		ImageIcon originalImgIcon = new ImageIcon(EMPTY_ICON_PATH);
		Image originalImg = originalImgIcon.getImage();
		Image resizedImg = originalImg.getScaledInstance(SIDE_LENGTH, SIDE_LENGTH, java.awt.Image.SCALE_SMOOTH);
		ImageIcon resizedImgIcon = new ImageIcon(resizedImg);
		this.button.setIcon(resizedImgIcon);
		
		this.button.setMinimumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		this.button.setMaximumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		this.button.setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		this.button.setVisible(true);
		
	}

	/**
	 * Constructor
	 * @param validButton A boolean value that specifies whether is a valid cell or not (i.e.
	 * clickable or unclickable)
	 * @param replay The replay
	 * @param sideLength The length of the side of the button (squared)
	 */
	public CellGUI(boolean validButton, Replay replay, int sideLength) {
		CellGUI.SIDE_LENGTH = sideLength;
		this.validButton = validButton;
		this.filled = false;
		this.button = new JButton();
		this.button.setEnabled(validButton);
		this.validButton = validButton;
		ImageIcon originalImgIcon = new ImageIcon(EMPTY_ICON_PATH);
		Image originalImg = originalImgIcon.getImage();
		Image resizedImg = originalImg.getScaledInstance(SIDE_LENGTH, SIDE_LENGTH, java.awt.Image.SCALE_SMOOTH);
		ImageIcon resizedImgIcon = new ImageIcon(resizedImg);
		this.button.setIcon(resizedImgIcon);
		this.button.setMinimumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		this.button.setMaximumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		this.button.setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		this.button.setVisible(true);
	}
	
	/**
	 * This method is in charge of putting the cell to an empty one
	 */
	public void resetIcon() {
		this.iconPath = EMPTY_ICON_PATH;
		this.filled = true;

		ImageIcon originalImgIcon = new ImageIcon(this.iconPath);
		Image originalImg = originalImgIcon.getImage();
		Image resizedImg = originalImg.getScaledInstance(SIDE_LENGTH, SIDE_LENGTH, java.awt.Image.SCALE_SMOOTH);
		ImageIcon resizedImgIcon = new ImageIcon(resizedImg);

		this.button.setIcon(resizedImgIcon);

		// else this.filled = false; //Por si pudiera desocuparse una casilla en una
		// replay, pero no estoy seguro de esto, porque en las replays no se debe poder
		// interactuar con el tablero

		this.button.repaint();
	}

	/**
	 * Getter of the JButton attribute
	 * @return The JButton attribute
	 */
	public JButton getButton() {
		return button;
	}
	
	/**
	 * This method paints the cell with a given color
	 * @param newColor The color that the cell must now show
	 */
	public void update(model.logic.Color newColor) {
		
		if (this.validButton) {
			
			ImageIcon icon = mapColorIcon.get(newColor);
			
			if (icon == null) {
				
				this.iconPath = newColor.getPath();
				this.filled = true;
				
				ImageIcon originalImgIcon = new ImageIcon(this.iconPath);
				
				Image originalImg = originalImgIcon.getImage();

				Image resizedImg = originalImg.getScaledInstance(SIDE_LENGTH, SIDE_LENGTH, java.awt.Image.SCALE_SMOOTH);

				icon = new ImageIcon(resizedImg);
				
				mapColorIcon.put(newColor, icon);
				
			}

			this.button.setIcon(icon);

			// else this.filled = false; //Por si pudiera desocuparse una casilla en una
			// replay, pero no estoy seguro de esto, porque en las replays no se debe poder
			// interactuar con el tablero

			this.button.repaint();
			
			
		}
	}

}
