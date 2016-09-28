package com.school.ui;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	private JPanel renderPanel;
			
	public GamePanel(JPanel panel){
		super();
		JLayeredPane layers = new JLayeredPane();
		layers.setLayout(null);
		layers.setPreferredSize(new Dimension(364, 364));
		
		//gets background image and assigns it to JLabel background
		JLabel background = new JLabel(new ImageIcon(this.getClass().getResource("/com/school/ui/UIImages/1.png")));
		background.setBounds(0, 0, 364, 364);			
		layers.add(background, new Integer(0), 0);
	
		//assigns render panel and places in correctly
		this.renderPanel = panel;
		renderPanel.setBounds(6,6,352,352);
		layers.add(renderPanel, new Integer(1), 0);
		
		this.add(layers);
		this.setVisible(true);
		validate();
		
	}
}
