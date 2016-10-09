package ui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class InfoPanel extends JPanel{
	private Container contentPane;
	private GameFrame home;
	private ImageIcon[] icons;
	private UIImageMap imageMap;
	
	private JLabel ok;
	private JTextPane infoTest;
	private ButtonListen listen;

	public InfoPanel(Container contentPane, GameFrame home, UIImageMap imageMap){
		super(new BorderLayout(0,0));
		
		this.home = home;
		this.contentPane = contentPane;
		this.imageMap = imageMap;
		
		setIcons();
		infoTest = new JTextPane();
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("slkscr.ttf"));
			infoTest.setFont(font.deriveFont(Font.TRUETYPE_FONT, 12f));
		} catch (Exception e) {}		
		
		infoTest.setForeground(Color.WHITE);
		infoTest.setEditable(false);
		infoTest.setOpaque(false);
				
		listen = new ButtonListen();
		
		ok = new JLabel(icons[0]);
		ok.addMouseListener(listen);
		
		this.add(infoTest, BorderLayout.NORTH);
		this.add(ok, BorderLayout.WEST);
	}
	
	/**
	 * Sets the default and highlighted states for each button
	 */
	public void setIcons(){
		icons = new ImageIcon[6];

		icons[0] = new ImageIcon(imageMap.getImage("ob"));
		icons[1] = new ImageIcon(imageMap.getImage("obhi"));
		
	}
	/**
	 * Determines if this panel should display Info or About
	 * 
	 * @param info if panel will be Info Panel or not
	 */
	public void setInfo(boolean info){
		if(info){ 
			setAsInfo();
		} else {
			setAsAbout();
		}
	}
	
	public void setAsInfo(){
		infoTest.setText("Spooky School! \n\nSome information about Spooky School goes here.\n\n"+
						"Controls: \n\n"+
						"Use arrow keys to move.\n'l' : rotate screen anti-clockwise\n'r' : rotate screen clockwise\n'z' : perform an action (examine/open a door)");
	}
	
	public void setAsAbout(){
		infoTest.setText("SWEN 222 - Assignment 03 - Team 8 - \nSpooky School - Created by:\n\n"+
				"- Cameron McLachlan ( mclachcame ) \nCameron.McLachlan@ecs.vuw.ac.nz\n\n"+
				"- Andrew Mundt ( mundtandy ) \nAndrew.Mundt@ecs.vuw.ac.nz\n\n"+
				"- Priteshbhai Patel ( patelprit2 ) \npatelprit2@ecs.vuw.ac.nz\n\n"+
				"- Rong Wang ( wangrong ) \nRong.Wang@ecs.vuw.ac.nz\n\n"+
		        "- Chethana Wijesekera ( wijesechet ) \nChethana.Wijesekera@ecs.vuw.ac.nz");
	}
	
	public void addComponents(){
		
	}
	
	@Override
	public void paintComponent(Graphics gr) {
		super.paintComponent(gr);//makes sure MyGlass's widgets are drawn automatically
		
		Graphics2D g = (Graphics2D) gr;
				
		//create (fake) transparency
		AlphaComposite transparent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER , .8f);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);
		g.setComposite(transparent);
		//draw the contents of the JFrame's content pane upon our glass pane.
		contentPane.paint(gr);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	private class ButtonListen implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getSource() == ok)
				setVisible(false);
		}


		@Override
		public void mouseEntered(MouseEvent e) {
			if(e.getSource() == ok)
				ok.setIcon(icons[1]);

		}

		@Override
		public void mouseExited(MouseEvent e) {
			if(e.getSource() == ok)
				ok.setIcon(icons[0]);
		}

		//UNUSED
		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}
}
