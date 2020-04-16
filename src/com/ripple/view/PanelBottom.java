package com.ripple.view;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelBottom extends JPanel{
	  public void paintComponent(Graphics g) {
		  g.setColor(Color.darkGray);
		  g.fillRect(0, 0, getWidth(), getHeight());
	    }
}
