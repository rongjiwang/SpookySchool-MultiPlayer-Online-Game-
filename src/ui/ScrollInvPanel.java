package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ScrollInvPanel extends JPanel{
	private InventoryPanel invPanel;
	private ImageIcon[] icons;
	private ButtonListen listen;
	private JLabel up;
	private JLabel down;

	private UIImageMap imageMap;

	public ScrollInvPanel(InventoryPanel invPanel, UIImageMap imageMap){
		super(new BorderLayout());
		
		JPanel fit = new JPanel(new FlowLayout());
		fit.setOpaque(false);
		fit.setPreferredSize(new Dimension(40, 100));
		

		this.invPanel = invPanel;
		this.imageMap = imageMap;

		setIcons();

		listen = new ButtonListen();

		up = new JLabel(icons[0]);
		down = new JLabel(icons[2]);

		up.addMouseListener(listen);
		down.addMouseListener(listen);
		
		fit.add(up);
		fit.add(down);
		this.add(fit, BorderLayout.EAST);

		setOpaque(false);
	}

	/**
	 * Sets the default and highlighted states for each button
	 */
	public void setIcons(){
		icons = new ImageIcon[6];

		icons[0] = new ImageIcon(imageMap.getImage("ub"));
		icons[1] = new ImageIcon(imageMap.getImage("ubhi"));
		icons[2] = new ImageIcon(imageMap.getImage("db"));
		icons[3] = new ImageIcon(imageMap.getImage("dbhi"));

	}

	/**
	 * This is the buttonListener for the ScrollInvPanel
	 * 
	 * @author Andy
	 *
	 */
	private class ButtonListen implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getSource() == up){ //up has been pressed
				invPanel.upOne();
			} else { //down game has been pressed
				invPanel.downOne();
			}
		}


		@Override
		public void mouseEntered(MouseEvent e) {
			if(e.getSource() == up){ //up is highlighted
				up.setIcon(icons[1]);
			} else { //down  is highlighted
				down.setIcon(icons[3]);
			}

		}
		@Override
		public void mouseExited(MouseEvent e) {
			if(e.getSource() == up){ //up is unhighlighted
				up.setIcon(icons[0]);
			} else { //down is highlighted
				down.setIcon(icons[2]);
			}

		}

		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
	}
}
