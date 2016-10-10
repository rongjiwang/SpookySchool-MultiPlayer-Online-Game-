package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JPanel;

/**
 * Main Panel that contains all the game panels
 * 
 * @author Andy
 *
 */
public class MainPanel extends JPanel{
	//references to other panels
	private JPanel game;
	private JPanel chat;
	private JPanel inv;
	private GameFrame home;
	private ButtonPanel buttons;
	
	public MainPanel(GameFrame home, JPanel gamePanel, ChatPanel chatPanel, InventoryPanel invPanel, ButtonPanel buttons, UIImageMap imageMap){
		setLayout(new BorderLayout(20, 0));
		//sets game frame
		this.home = home;
		//sets button panel
		this.buttons = buttons;
		
		//creates new UIPanels for game, chat and inventory panel
		game = new UIPanel(gamePanel, 600, 500, imageMap);
		chat = new UIPanel(chatPanel, 400, 220, imageMap);
		inv = new UIPanel(invPanel, 262, 162, imageMap);
		//adds game to panel
		this.add(game, BorderLayout.NORTH);
		
		//creates panels for displaying panels correctly
		JPanel left = new JPanel(new BorderLayout(20, 0));
		JPanel inven = new JPanel(new FlowLayout());
		
		//adds rigid area for allignment
		inven.add(Box.createRigidArea(new Dimension(40,1)));
		//adds inventory panel
		inven.add(inv);
		//adds new scroll panel
		inven.add(new ScrollInvPanel(invPanel, imageMap));
		inven.setOpaque(false);
		left.setOpaque(false);
		//adds inventory panel
		left.add(inven, BorderLayout.CENTER);
		//adds button panel
		left.add(buttons,BorderLayout.SOUTH);
		//adds all panels to this panel
		this.add(left, BorderLayout.WEST);
		this.add(chat, BorderLayout.EAST);
		this.setBorder(null);
		
		//visibility
		setOpaque(true);
		setVisible(true);
		Color newGrey = new Color(49, 45, 43);
		setBackground(newGrey);
		validate();
	}
}
