package ui;

import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Button panel is a panel of 3 option buttons, which allow for various functionality
 * 
 * @author Andy
 *
 */
public class ButtonPanel extends JPanel{
	private ImageIcon[] icons;
	private ButtonListen listen;
	private JLabel info;
	private JLabel about;
	private JLabel save;
	private UIImageMap imageMap;
	private GameFrame home;
		
	public ButtonPanel(GameFrame home, UIImageMap imageMap){
		setLayout(new FlowLayout());
		
		//sets imageMap
		this.imageMap = imageMap;
		this.home = home;
		
		//sets icons
		setIcons();
		
		//creates button listener for buttons
		listen = new ButtonListen();
		
		//creates 3 buttons
		info = new JLabel(icons[0]);
		about = new JLabel(icons[2]);
		save = new JLabel(icons[4]);

		//assigns button listener
		info.addMouseListener(listen);
		about.addMouseListener(listen);
		save.addMouseListener(listen);

		//adds 3 buttons to panel
		this.add(info);
		this.add(about);
		this.add(save);
		
		this.setOpaque(false);
	}
	
	/**
	 * Sets the default and highlighted states for each button
	 */
	public void setIcons(){
		icons = new ImageIcon[6];

		icons[0] = new ImageIcon(imageMap.getImage("ib"));
		icons[1] = new ImageIcon(imageMap.getImage("ibhi"));
		icons[2] = new ImageIcon(imageMap.getImage("ab"));
		icons[3] = new ImageIcon(imageMap.getImage("abhi"));
		icons[4] = new ImageIcon(imageMap.getImage("sab"));
		icons[5] = new ImageIcon(imageMap.getImage("sabhi"));
	}
	
	/**
	 * This is the buttonListener for the ButtonPanel
	 * 
	 * @author Andy
	 *
	 */
	private class ButtonListen implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getSource() == info){ //info has been pressed
				home.setGlass(true);
			} else if(e.getSource() == about){ //about has been pressed
				home.setGlass(false);
			} else { //save game has been pressed
				//TODO: Save functionality needs to be added
			}
		}

		
		@Override
		public void mouseEntered(MouseEvent e) {
			if(e.getSource() == info){ //info has been pressed
				info.setIcon(icons[1]);
			} else if(e.getSource() == about){ //about has been pressed
				about.setIcon(icons[3]);
			} else { //save game has been pressed
				save.setIcon(icons[5]);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if(e.getSource() == info){ //info has been pressed
				info.setIcon(icons[0]);
			} else if(e.getSource() == about){ //about has been pressed
				about.setIcon(icons[2]);
			} else { //save game has been pressed
				save.setIcon(icons[4]);
			}
		}
		
		//UNUSED METHODS
		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}

}
