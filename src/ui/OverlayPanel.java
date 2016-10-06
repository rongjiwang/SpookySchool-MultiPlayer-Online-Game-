package ui;

import java.awt.Font;
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
	private long now;
	private long then;
	
	
	public OverlayPanel(AreaDisplayPanel panel, SpriteMap spriteMap){
		this.panel = panel;
		this.spriteMap = spriteMap;
		this.thread = new Thread(){
			@Override
			public void run(){
				while(true){
					tick();
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
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("slkscr.ttf"));
			setFont(font.deriveFont(Font.TRUETYPE_FONT, 12f));
			

		} catch (Exception e) {}
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
		g.drawImage(spriteMap.getImage("H0"), headerX, headerY, null);
		if(headerName != null)
			g.drawString(headerName, headerX + 10, headerY + 17);
	}

	public void tick(){
		headerX += increment;
		now = System.currentTimeMillis();
		if(headerX == 5){
			headerX = 6;
			increment = 0;
			then = now + 3000;
		}else if(increment == 0){
			if(now > then){
				headerX = 4;
				increment = -1;
			}
		}
	}
	
}
