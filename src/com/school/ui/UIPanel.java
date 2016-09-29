package com.school.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class UIPanel extends JPanel {
	private JPanel renderPanel;
	private int width;
	private int height;
	private int panelNum;
			
	public UIPanel(JPanel panel, int num ){
		super();
		this.panelNum = num;
		JLayeredPane layers = new JLayeredPane();
		layers.setLayout(null);
		
		switch(num){
			case 1 : 	width = 364;
						height = 364;
						break;
			case 2 : 	width = 262;
						height = 162;
						break;
			case 3 : 	width = 300;
						height = 500;
						break;
			default :	width = 0;
						height = 0;
						break;
		}
		layers.setPreferredSize(new Dimension(width, height));
		
		//gets background image and assigns it to JLabel background
		JLabel background = new JLabel(new ImageIcon(this.getClass().getResource("/com/school/ui/UIImages/"+panelNum+".png")));
		background.setBounds(0, 0, width, height);			
		layers.add(background, new Integer(0), 0);
	
		//assigns render panel and places in correctly
		this.renderPanel = panel;
		renderPanel.setBounds(6,6,(width-12),(height-12));
		layers.add(renderPanel, new Integer(1), 0);
		setOpaque(false);
		this.add(layers);
		this.setVisible(true);
		validate();
		
	}
}
