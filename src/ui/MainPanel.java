package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class MainPanel extends JPanel{
	private JPanel game;
	private JPanel inv;
	private JLabel info;
	private JLabel quit;
	private JLabel players;
	private ButtonListen listen;

	private ImageIcon[] icons;

	public MainPanel(JPanel gamePanel, InventoryPanel invPanel){
		setLayout(new BorderLayout(20, 0));
		listen = new ButtonListen();

		game = new UIPanel(gamePanel, 1);
		inv = new UIPanel(invPanel, 2);

		this.add(game, BorderLayout.NORTH);
		this.add(inv, BorderLayout.CENTER);

		setIcons();

		JPanel buttonSort = buttonMenu();
		buttonSort.setOpaque(false);

		this.add(buttonSort, BorderLayout.SOUTH);

		setOpaque(true);
		setVisible(true);
		Color newGrey = new Color(49, 45, 43);
		setBackground(newGrey);
		validate();
	}
	
	public void setUp(){
		setLayout(new BorderLayout(20, 0));
		listen = new ButtonListen();
	}

	public void setIcons(){
		icons = new ImageIcon[6];

		icons[0] = new ImageIcon(this.getClass().getResource("UIImages/info.png"));
		icons[1] = new ImageIcon(this.getClass().getResource("UIImages/infohighlight.png"));
		icons[2] = new ImageIcon(this.getClass().getResource("UIImages/players.png"));
		icons[3] = new ImageIcon(this.getClass().getResource("UIImages/playershighlight.png"));
		icons[4] = new ImageIcon(this.getClass().getResource("UIImages/quit.png"));
		icons[5] = new ImageIcon(this.getClass().getResource("UIImages/quithighlight.png"));
	}

	public JPanel buttonMenu(){
		JPanel toReturn = new JPanel(new FlowLayout());

		info = new JLabel(icons[0]);
		players = new JLabel(icons[2]);
		quit = new JLabel(icons[4]);

		players.addMouseListener(listen);
		info.addMouseListener(listen);
		quit.addMouseListener(listen);

		toReturn.add(info);
		toReturn.add(players);
		toReturn.add(quit);
		
		
		return toReturn;
	}

	private class ButtonListen implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getSource() == info){ //info

			} else if(e.getSource() == players){

			} else { //quit game
				String ObjButtons[] = {"Yes","No"};
				int PromptResult = JOptionPane.showOptionDialog(null,"Are you sure you want to exit?","Quit Spooky School?",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
				if(PromptResult==JOptionPane.YES_OPTION)
				{
					System.exit(0);
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {
			if(e.getSource() == info){ //info
				info.setIcon(icons[1]);
			} else if(e.getSource() == players){
				players.setIcon(icons[3]);
			} else { //quit game
				quit.setIcon(icons[5]);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if(e.getSource() == info){ //info
				info.setIcon(icons[0]);
			} else if(e.getSource() == players){
				players.setIcon(icons[2]);
			} else { //quit game
				quit.setIcon(icons[4]);
			}
		}
	}

}
