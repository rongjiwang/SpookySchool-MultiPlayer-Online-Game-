package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class MainPanel extends JPanel{
	private JPanel game;
	private JPanel chat;
	private JPanel inv;
	
	public MainPanel(JPanel gamePanel, ChatPanel chatPanel, InventoryPanel invPanel, UIImageMap imageMap){
		setLayout(new BorderLayout(20, 0));
		
		game = new UIPanel(gamePanel, 600, 500, imageMap);
		chat = new UIPanel(chatPanel, 400, 220, imageMap);
		inv = new UIPanel(invPanel, 262, 162, imageMap);
		ButtonPanel buttons = new ButtonPanel(imageMap);
		this.add(game, BorderLayout.NORTH);
		
		JPanel left = new JPanel(new BorderLayout(20, 0));
		JPanel inven = new JPanel(new FlowLayout());
		
		inven.add(Box.createRigidArea(new Dimension(40,1)));
		inven.add(inv);
		inven.add(new ScrollInvPanel(invPanel, imageMap));
		inven.setOpaque(false);
		left.setOpaque(false);
		
		left.add(inven, BorderLayout.CENTER);
		left.add(buttons,BorderLayout.SOUTH);
		
		this.add(left, BorderLayout.WEST);
		this.add(chat, BorderLayout.EAST);
		this.setBorder(null);
		
		setOpaque(true);
		setVisible(true);
		Color newGrey = new Color(49, 45, 43);
		setBackground(newGrey);
		validate();
	}
}
