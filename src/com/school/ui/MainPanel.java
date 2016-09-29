package com.school.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class MainPanel extends JPanel{
	private JPanel game;
	private JPanel inv;
		
	public MainPanel(AreaDisplayPanel gamePanel, InventoryPanel invPanel){
		setLayout(new BorderLayout(20, 0));
	    
		
		game = new UIPanel(gamePanel, 1);
		inv = new UIPanel(invPanel, 2);
		
		this.add(game, BorderLayout.NORTH);
		this.add(inv, BorderLayout.CENTER);
		setOpaque(false);
		setVisible(true);
		setBackground(Color.BLACK);
		validate();
	}

}
