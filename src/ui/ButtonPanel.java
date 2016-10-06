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
	private JLabel quit;
	private JLabel players;
	private UIImageMap imageMap;
		
	public ButtonPanel(UIImageMap imageMap){
		setLayout(new FlowLayout());
		
		//sets imageMap
		this.imageMap = imageMap;
		
		//sets icons
		setIcons();
		
		//creates button listener for buttons
		listen = new ButtonListen();
		
		//creates 3 buttons
		info = new JLabel(icons[0]);
		players = new JLabel(icons[2]);
		quit = new JLabel(icons[4]);

		//assigns button listener
		players.addMouseListener(listen);
		info.addMouseListener(listen);
		quit.addMouseListener(listen);

		//adds 3 buttons to panel
		this.add(info);
		this.add(players);
		this.add(quit);
		
		this.setOpaque(false);
	}
	
	/**
	 * Sets the default and highlighted states for each button
	 */
	public void setIcons(){
		icons = new ImageIcon[6];

		icons[0] = new ImageIcon(imageMap.getImage("ib"));
		icons[1] = new ImageIcon(imageMap.getImage("ibhi"));
		icons[2] = new ImageIcon(imageMap.getImage("pb"));
		icons[3] = new ImageIcon(imageMap.getImage("pbhi"));
		icons[4] = new ImageIcon(imageMap.getImage("qb"));
		icons[5] = new ImageIcon(imageMap.getImage("qbhi"));
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

			} else if(e.getSource() == players){ //players has been pressed

			} else { //quit game has been pressed
				String ObjButtons[] = {"Yes","No"};
				int PromptResult = JOptionPane.showOptionDialog(null,"Are you sure you want to exit?","Quit Spooky School?",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
				if(PromptResult==JOptionPane.YES_OPTION)
				{
					System.exit(0);
				}
			}
		}

		
		@Override
		public void mouseEntered(MouseEvent e) {
			if(e.getSource() == info){ //info is highlighted
				info.setIcon(icons[1]);
			} else if(e.getSource() == players){ //players is highlighted
				players.setIcon(icons[3]);
			} else { //quit game is highlighted
				quit.setIcon(icons[5]);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if(e.getSource() == info){ //info is unhighlighted
				info.setIcon(icons[0]);
			} else if(e.getSource() == players){ //player is highlighted
				players.setIcon(icons[2]);
			} else { //info is unhighlighted
				quit.setIcon(icons[4]);
			}
		}
		
		//UNUSED METHODS
		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}

}
