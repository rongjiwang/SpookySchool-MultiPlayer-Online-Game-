package ui;

import java.awt.Font;
import java.awt.Graphics;

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
	private int headerIncrement;
	private long headerThen; //holds time
	private boolean firstHeaderReceived = false; //To stop the null pointer at the start of the game.

	//Footer Message box
	private int footerX = 25;
	private int footerY = 550;
	private String footerMessage;
	private int footerIncrement;
	private long footerThen; //holds time
	private boolean firstFooterReceived = false; //To stop the null pointer at the start of the game.


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

	public void setHeaderMessage(int x, String name) {
		this.headerMessage = name;
		this.headerX = x;
		headerIncrement = 1;
		this.firstHeaderReceived = true;
	}

	public void setFooterMessage(String message) {
		this.footerMessage = message;
		this.footerY = 550;
		this.footerIncrement = -3;
		this.firstFooterReceived = true;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Draw the header
		g.drawImage(spriteMap.getImage("H0"), headerX, headerY, null);
		g.drawString(headerMessage, headerX + 10, headerY + 17);

		//Draw the footer
		g.drawImage(spriteMap.getImage("H0"), footerX, footerY, null);

		String wrappedString = wrap(footerMessage);

		g.drawString(footerMessage, footerX + 10, footerY + 17);
	}

	public void tick() {

		now = System.currentTimeMillis();

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

		if (this.firstFooterReceived) {
			this.footerY = this.footerY + footerIncrement;
			if (footerY == 460) {
				this.footerY = 461;
				this.footerIncrement = 0;
				this.footerThen = now + 5000; //Pause for 5secs.
			} else if (this.footerIncrement == 0) {
				if (now > footerThen) {
					this.footerY = 461;
					this.footerIncrement = 3;
				}
			}
		}

	}

}
