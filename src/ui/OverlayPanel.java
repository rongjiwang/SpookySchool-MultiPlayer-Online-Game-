package ui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import javax.swing.JPanel;

public class OverlayPanel extends JPanel {

	private AreaDisplayPanel panel;
	private SpriteMap spriteMap;
	private Thread thread;

	private long now; //holds time

	//Header box
	private String headerMessage;
	private int headerX;
	private int headerY = 5;
	private int headerWidth = 155;
	private int headerIncrement;
	private long headerThen; //holds time
	private boolean firstHeaderReceived = false; //To stop the null pointer at the start of the game.

	//Footer Message box
	private int footerX = 50;
	private int footerY = 580;
	private String footerMessage;
	private int footerIncrement;
	private long footerThen; //holds time
	private boolean firstFooterReceived = false; //To stop the null pointer at the start of the game.
	private Font font;
	
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
			font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("slkscr.ttf"));
			Graphics2D g2d = (Graphics2D) getGraphics();
			g2d.setFont(font.deriveFont(Font.TRUETYPE_FONT, 12f));

		
		} catch (Exception e) {
		}

		thread.start();
	}

	public void setHeaderMessage(int x, String name) {
		this.headerMessage = name;
		this.headerX = x;
		headerIncrement = 1;
		this.firstHeaderReceived = true;
	}

	public void setFooterMessage(String message) {
		this.footerMessage = message;
		this.footerY = 580;
		this.footerIncrement = -4;
		this.firstFooterReceived = true;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Draw the header
		if (this.footerMessage != null) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.drawImage(spriteMap.getImage("H0"), headerX, headerY, null);
			Font font = new Font("Calibri", Font.PLAIN, 15);
			g2d.setFont(font);
			/*try {
				font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("slkscr.ttf"));
				g2d.setFont(font.deriveFont(Font.TRUETYPE_FONT, 12f));

			} catch (FontFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			Rectangle2D r = font.getStringBounds(headerMessage, g2d.getFontRenderContext());
			int x = (int) (headerWidth - r.getWidth()) /2;
			g2d.drawString(headerMessage, headerX + x, headerY + 17);
		}
		
		//Draw the footer
		if (this.footerMessage != null) {
			g.drawImage(spriteMap.getImage("P0"), footerX, footerY, null);
			g.drawString(this.footerMessage, footerX + 10, footerY + 25);
		}
	}

	public void tick() {

		now = System.currentTimeMillis();

		//Display the header message. The boolean is there so that we don't try to print 
		//a message when we have never had one before yet as this can cause null pointer.
		if (firstHeaderReceived) {
			headerX += headerIncrement;
			if (headerX == 5) {
				headerX = 6;
				headerIncrement = 0;
				headerThen = now + 3000;
			} else if (headerIncrement == 0) {
				if (now > headerThen) {
					headerX = 4;
					headerIncrement = -2;
				}
			}
		}

		//Display the footer message. The boolean is there so that we don't try to print 
		//a message when we have never had one before yet as this can cause null pointer.
		if (this.firstFooterReceived) {
			this.footerY = this.footerY + footerIncrement;
			if (footerY == 420) {
				this.footerY = 421;
				this.footerIncrement = 0;
				this.footerThen = now + 3500; //Pause for 3.5secs.
			} else if (this.footerIncrement == 0) {
				if (now > footerThen) {
					this.footerY = 421;
					this.footerIncrement = 4;
				}
			}
		}
	}

}
