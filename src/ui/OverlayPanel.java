package ui;

import java.awt.Graphics;

import javax.swing.JPanel;

public class OverlayPanel  extends JPanel{
	
	private AreaDisplayPanel panel;
	private SpriteMap spriteMap;
	private int headerX;
	private int headerY = 5;
	private String headerName;
	private Thread thread;
	private int increment;
	
	
	public OverlayPanel(AreaDisplayPanel panel, SpriteMap spriteMap){
		this.panel = panel;
		this.spriteMap = spriteMap;
		this.thread = new Thread(){
			@Override
			public void run(){
				while(true){
					move();
					repaint();
					try {
						sleep(4);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
	}
	
	public void setHeaderX(int x, String name){
		this.headerName = name;
		this.headerX = x;
		increment = 1;
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(spriteMap.getImage("h0"), headerX, headerY, null);
		g.drawString(headerName, headerX + 10, headerY + 17);
	}

	public void move(){
		headerX += increment;
		if(headerX == 5){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			increment = -1;
		}
		
		
		
		
			
		
			
		
	}
	
}
