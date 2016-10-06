package ui;

import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class OverlayPanel extends JPanel {

	private AreaDisplayPanel panel;
	private SpriteMap spriteMap;
	private Thread thread;

	private long now;
	private long then;

	//Header box
	private String headerMessage;
	private int headerX;
	private int headerY = 5;
	private int headerIncrement;

	//Footer Message box
	private int footerX = 25;
	private int footerY;
	private String footerMessage;
	private int footerIncrement;

	public OverlayPanel(AreaDisplayPanel panel, SpriteMap spriteMap) {
		this.panel = panel;
		this.spriteMap = spriteMap;
		this.thread = new Thread() {
			@Override
			public void run() {
				while (true) {
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


		} catch (Exception e) {
		}
		thread.start();
	}

	public void setHeaderX(int x, String name) {
		this.headerMessage = name;
		this.headerX = x;
		headerIncrement = 1;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(spriteMap.getImage("H0"), headerX, headerY, null);

		if(headerMessage != null)
			g.drawString(headerMessage, headerX + 10, headerY + 17);
	}

	public void tick() {
		headerX += headerIncrement;
		now = System.currentTimeMillis();
		if (headerX == 5) {
			headerX = 6;
			headerIncrement = 0;
			then = now + 3000;
		} else if (headerIncrement == 0) {
			if (now > then) {
				headerX = 4;
				headerIncrement = -1;
			}
		}
	}

}
