package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DebugDisplay extends JPanel{
	
	private JTextArea messageList;
	private AreaDisplayPanel areaDisplayPanel;
	
	public DebugDisplay(AreaDisplayPanel areaDisplayPanel){
		super(new BorderLayout());
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
		this.setSize(300, 200);
		setVisible(true);
			
		messageList = new JTextArea();
	    messageList.setEditable(false);
	    messageList.setOpaque(true);
	    messageList.setLineWrap(true);
	    JScrollPane area = new JScrollPane(messageList);
	    this.add(area);
	    
	    validate();
	}
	
	public void updateDisplay(String info){
		messageList.append("---\n");
		messageList.append(info);
		
	}
}
