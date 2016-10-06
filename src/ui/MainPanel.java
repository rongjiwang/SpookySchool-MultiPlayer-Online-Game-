package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class MainPanel extends JPanel{
	private JPanel game;
	
	
	public MainPanel(JPanel gamePanel, UIImageMap imageMap){
		setLayout(new BorderLayout(20, 0));
		
		game = new UIPanel(gamePanel, 1, imageMap);
		
		this.add(game, BorderLayout.CENTER);
		

		setOpaque(true);
		setVisible(true);
		Color newGrey = new Color(49, 45, 43);
		setBackground(newGrey);
		validate();
	}
}
