package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.border.Border;

import commands.Command;
import logic.Board;
import logic.Game;

//Clase joseada de https://www.javacodex.com/More-Examples/2/14

class RoundButton extends JButton implements RolitObserver {

	
	public RoundButton(String label) {
	    super(label);
	 
	    setBackground(Color.lightGray);
	    setFocusable(false);
	 
	    /*
	     These statements enlarge the button so that it 
	     becomes a circle rather than an oval.
	    */
	    Dimension size = getPreferredSize();
	    size.width = size.height = Math.max(size.width, size.height);
	    setPreferredSize(size);
	 
	    /*
	     This call causes the JButton not to paint the background.
	     This allows us to paint a round background.
	    */
	    setContentAreaFilled(false);
	  }
	 
	  protected void paintComponent(Graphics g) {
	    if (getModel().isArmed()) {
	      g.setColor(Color.gray);
	    } else {
	      g.setColor(getBackground());
	    }
	    g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
	 
	    super.paintComponent(g);
	  }
	 
	  protected void paintBorder(Graphics g) {
	    g.setColor(Color.darkGray);
	    g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
	  }
	 
	  // Hit detection.
	  Shape shape;
	 
	  public boolean contains(int x, int y) {
	    // If the button has changed size,  make a new shape object.
	    if (shape == null || !shape.getBounds().equals(getBounds())) {
	      shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
	    }
	    return shape.contains(x, y);
	  }
	 
	  public static void main(String[] args) {
	 
	    JFrame.setDefaultLookAndFeelDecorated(true);
	    JFrame frame = new JFrame("Rounded Button Example");
	    frame.setLayout(new FlowLayout());
	 
	    JButton b1 = new RoundButton("B1");
	    JButton b2 = new RoundButton("B2");
	 
	    frame.add(b1);
	    frame.add(b2);
	 
	    frame.setSize(300, 150);
	    frame.setVisible(true);
	  }
	  
	  public RoundButton() {this("");}

	@Override
	public void onGameCreated(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTurnPlayed(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCommandIntroduced(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayLeftButton(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayRightButton(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}