package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuPanel extends JPanel{
	private JLabel title;
	private UIImageMap imageMap;
	private ButtonPanel buttons;
	
	public MenuPanel(String title, ButtonPanel buttons){
		super(new BorderLayout(0,0));
		this.title = new JLabel(title);
		
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("slkscr.ttf"));
			this.title.setFont(font.deriveFont(Font.TRUETYPE_FONT, 12f));
			this.title.setForeground(Color.WHITE);
		} catch (Exception e) {}
		
		this.buttons = buttons;
		
		this.add(this.title, BorderLayout.WEST);
		
		setOpaque(true);
		setVisible(true);
		Color newGrey = new Color(49, 45, 43);
		setBackground(newGrey);
		validate();
	}

}
