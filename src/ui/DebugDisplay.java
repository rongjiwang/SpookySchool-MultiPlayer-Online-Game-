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
		this.areaDisplayPanel = areaDisplayPanel;
		/*this.setOpaque(true);
		this.setBackground(Color.WHITE);
		this.setSize(300, 200);
		setVisible(true);
			
		
		messageList = new JTextArea();
	    messageList.setEditable(false);
	    messageList.setOpaque(true);
	    messageList.setLineWrap(true);
	    JScrollPane area = new JScrollPane(messageList);
	    this.add(area);*/
	    
	    validate();
	}
	
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	
		String client = null;
		String area = null;
		String viewX = null;
		String viewY = null;
		String command = null;
		
		if(areaDisplayPanel == null){
			return;
		}

		if(areaDisplayPanel.getMainPlayer() == null){
			return;
		}
	
		if(areaDisplayPanel.mainPlayer.getID() == null)
			client = "null";
		else
			client = areaDisplayPanel.mainPlayer.getID();		
	
		
		client = areaDisplayPanel.mainPlayer.getID();
			
		if(areaDisplayPanel.mainPlayer.getArea() == null)
			area = "null";
		else
			area = areaDisplayPanel.mainPlayer.getArea();
		
		viewX = "" + areaDisplayPanel.mainPlayer.getXPos();
		viewY = "" + areaDisplayPanel.mainPlayer.getYPos();
		
		
		g.drawString("Client: " + client, 20, 20);
		g.drawString("Area: " + area, 20, 40);
		g.drawString("ViewX: " + viewX, 20, 60);
		g.drawString("ViewY: " + viewY, 20, 80);
		g.drawString("command: " + viewY, 20, 80);
		
		
		

	}
	
	public void updateDisplay(){
		
		repaint();
		
		
		

		
		
	}
}
