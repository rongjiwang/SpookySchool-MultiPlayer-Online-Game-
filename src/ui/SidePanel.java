package ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

public class SidePanel extends JPanel{
	private JPanel chat;

	public SidePanel(ChatPanel chatPanel){
		setLayout(new BorderLayout(20, 0));
		
		chat = new UIPanel(chatPanel, 1);
		
		this.add(chat, BorderLayout.SOUTH);

		setOpaque(false);
		setVisible(true);
		setBackground(Color.BLACK);
		validate();
	}
}