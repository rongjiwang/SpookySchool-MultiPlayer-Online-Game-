package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class SidePanel extends JPanel{
	private JPanel chat;
	private JPanel inv;
	private JPanel buttons;
	
	public SidePanel(ChatPanel chatPanel, InventoryPanel invPanel, UIImageMap imageMap){
		setLayout(new BorderLayout(10, 0));
		buttons = new ButtonPanel(imageMap);
				
		chat = new UIPanel(chatPanel, 3, imageMap);
		inv = new UIPanel(invPanel, 2, imageMap);
		
		JPanel left = new JPanel(new BorderLayout(20, 0));
		JPanel inven = new JPanel(new FlowLayout());
		inven.add(Box.createRigidArea(new Dimension(40,1)));
		inven.add(inv);
		inven.add(new ScrollInvPanel(invPanel, imageMap));
		inven.setOpaque(false);
		left.setOpaque(false);
		
		left.add(inven, BorderLayout.CENTER);
		left.add(buttons,BorderLayout.SOUTH);
		this.add(Box.createRigidArea(new Dimension(1,10)), BorderLayout.NORTH);
		this.add(left, BorderLayout.WEST);
		this.add(chat, BorderLayout.EAST);
			
		setOpaque(true);
		setVisible(true);
		Color newGrey = new Color(49, 45, 43);
		setBackground(newGrey);
		validate();
	}
	
	
}