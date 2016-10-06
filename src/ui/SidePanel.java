package ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class SidePanel extends JPanel{
	private JPanel chat;
	private JPanel inv;
	private JPanel buttons;
	
	public SidePanel(ChatPanel chatPanel, InventoryPanel invPanel, UIImageMap imageMap){
		setLayout(new BorderLayout(20, 0));
		buttons = new ButtonPanel(imageMap);
				
		chat = new UIPanel(chatPanel, 3, imageMap);
		inv = new UIPanel(invPanel, 2, imageMap);
		
		JPanel left = new JPanel(new BorderLayout(20, 0));
		left.setOpaque(false);
		
		left.add(inv, BorderLayout.NORTH);
		left.add(buttons,BorderLayout.CENTER);
		
		this.add(left, BorderLayout.WEST);
		this.add(chat, BorderLayout.EAST);
			
		setOpaque(true);
		setVisible(true);
		Color newGrey = new Color(49, 45, 43);
		setBackground(newGrey);
		validate();
	}
	
	
}