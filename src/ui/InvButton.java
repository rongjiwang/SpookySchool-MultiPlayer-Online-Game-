package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class InvButton extends JPanel implements MouseListener{
	final static Dimension squareSize = new Dimension(50,50);
	private Image image = null;
	private Image highLight;
	private Image noHighLight;
	private int row;
	private int column;
	private String inventoryItem;
	
	public InvButton(int row, int column, String item){
		this.row = row;
		this.column = column;
		inventoryItem = item;
		highLight = new ImageIcon(this.getClass().getResource("UIimages/highlight.png")).getImage();
		noHighLight = new ImageIcon(this.getClass().getResource("UIimages/nohighlight.png")).getImage();
		image = noHighLight;
		setPreferredSize(squareSize);
		setOpaque(false);
		addMouseListener(this);
		validate();
	}
	
	public void changeItem(String item){
		inventoryItem = item;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(image, 0, 0, image.getWidth(null), image.getHeight(null), null);
		if(inventoryItem != null){
			//here it will render the inventory item
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		image = highLight;
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		image = noHighLight;
		repaint();		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		JOptionPane.showMessageDialog(null,"Item.", "No Item", JOptionPane.PLAIN_MESSAGE);
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}
