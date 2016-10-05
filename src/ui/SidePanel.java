package ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

public class SidePanel extends JPanel{
	private JPanel chat;
	private JPanel debug;

	public SidePanel(ChatPanel chatPanel, DebugDisplay debugPanel){
		setLayout(new BorderLayout(20, 0));
		
		chat = new UIPanel(chatPanel, 1);
		debug = new UIPanel(debugPanel, 2);
		
		this.add(chat, BorderLayout.SOUTH);
		this.add(debug, BorderLayout.NORTH);
		
		setOpaque(true);
		setVisible(true);
		Color newGrey = new Color(49, 45, 43);
		setBackground(newGrey);
		validate();
	}
}