package com.ripple.view;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelTop extends JPanel {

	  public void paintComponent(Graphics g) {
		    try {
		        Image img = ImageIO.read(new File("ripple.png"));
		        g.drawImage(img, this.getWidth()/2-175, 20, 350, 100, this);
		      } catch (IOException e) {
		        e.printStackTrace();
		      }
	    }

}
