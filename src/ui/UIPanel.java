package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class UIPanel extends JPanel {
	private JPanel panel;
	private int width;
	private int height;
	private int panelNum;
	
	private JPanel background;
	
	private JLabel topLeft;
	private JLabel topRight;
	private JLabel bottomLeft;
	private JLabel bottomRight;
	
	private JLabel leftBorder;
	private JLabel rightBorder;
	private JLabel topBorder;
	private JLabel bottomBorder;
	
	private UIImageMap imageMap;
	
	//private BufferedImage[] backgroundAssets;
			
	public UIPanel(JPanel panel, int num, UIImageMap imageMap){
		super();
		this.panelNum = num;
		this.imageMap = imageMap;
		
		JLayeredPane layers = new JLayeredPane();
		layers.setLayout(null);
		
		switch(num){
			case 1 : 	width = 612;
						height = 512;
						break;
			case 2 : 	width = 262;
						height = 162;
						break;
			case 3 : 	width = 450;
						height = 220;
						break;
			default :	width = 0;
						height = 0;
						break;
		}
		layers.setPreferredSize(new Dimension(width, height));
		
	//	setBackgroundAssets();
		setLabels();
		
		//gets background image and assigns it to JLabel background
		background = new JPanel(new BorderLayout(0, 0));
		
		//top border
		JPanel top = new JPanel(new BorderLayout(0,0));
		top.add(topLeft, BorderLayout.WEST);
		top.add(topBorder, BorderLayout.CENTER);
		top.add(topRight, BorderLayout.EAST);
		
		//side borders
		JPanel mid = new JPanel(new BorderLayout(0,0));
		mid.add(leftBorder, BorderLayout.WEST);
		mid.add(rightBorder, BorderLayout.EAST);
		
		//bottom border
		JPanel bottom = new JPanel(new BorderLayout(0,0));
		bottom.add(bottomLeft, BorderLayout.WEST);
		bottom.add(bottomBorder, BorderLayout.CENTER);
		bottom.add(bottomRight, BorderLayout.EAST);
		
		background.add(top, BorderLayout.NORTH);
		background.add(mid, BorderLayout.CENTER);
		background.add(bottom, BorderLayout.SOUTH);
		
		
		background.setBounds(0, 0, width, height);	
		layers.add(background, new Integer(0), 0);
	
		//assigns render panel and places in correctly
		this.panel = panel;
		panel.setBounds(6,6,(width-12),(height-12));
		layers.add(panel, new Integer(1), 0);
		setOpaque(false);
		this.add(layers);
		this.setVisible(true);
		validate();
		
	}
	
	/**
	 * Sets up array of BufferedImages for each background pieace
	 */
	/**
	public void setBackgroundAssets(){
		backgroundAssets = new BufferedImage[8];
				
		try {
			backgroundAssets[0] = ImageIO.read(new File("src/ui/UIimages/topLeft.png"));
			backgroundAssets[1] = ImageIO.read(new File("src/ui/UIimages/topRight.png"));
			backgroundAssets[2] = ImageIO.read(new File("src/ui/UIimages/bottomLeft.png"));
			backgroundAssets[3] = ImageIO.read(new File("src/ui/UIimages/bottomRight.png"));
			backgroundAssets[4] = ImageIO.read(new File("src/ui/UIimages/bottomBorder.png"));
			backgroundAssets[5] = ImageIO.read(new File("src/ui/UIimages/leftBorder.png"));
			backgroundAssets[6] = ImageIO.read(new File("src/ui/UIimages/topBorder.png"));
			backgroundAssets[7] = ImageIO.read(new File("src/ui/UIimages/rightBorder.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}

	}**/
	
	/**
	 * Sets up the JLabels that make up the border of a UI Panel
	 */
	public void setLabels(){
		Dimension corner = new Dimension(7,7);
		Dimension horizontal = new Dimension(width-14, 7);
		Dimension vertical = new Dimension(7, height-14);
		
		//corner panels
		topLeft = new JLabel(new ImageIcon(imageMap.getImage("tL")));
		topLeft.setSize(new Dimension(corner));
		topRight = new JLabel(new ImageIcon(imageMap.getImage("tR")));
		topRight.setSize(new Dimension(corner));
		bottomLeft = new JLabel(new ImageIcon(imageMap.getImage("bL")));
		bottomLeft.setSize(new Dimension(corner));
		bottomRight = new JLabel(new ImageIcon(imageMap.getImage("bR")));
		bottomRight.setSize(new Dimension(corner));
		
		//side panels
		leftBorder = new JLabel();
		leftBorder.setSize(new Dimension(vertical));
		rightBorder = new JLabel();
		leftBorder.setSize(new Dimension(vertical));
		topBorder = new JLabel();
		leftBorder.setSize(new Dimension(horizontal));
		bottomBorder = new JLabel();
		leftBorder.setSize(new Dimension(horizontal));
		
		//scales border image to border panel size
		Image topImage = imageMap.getImage("tB").getScaledInstance(width-14, 7, Image.SCALE_SMOOTH);
		ImageIcon topIcon = new ImageIcon(topImage);
		topBorder.setIcon(topIcon);
		
		Image leftImage = imageMap.getImage("lB").getScaledInstance(7, height-14, Image.SCALE_SMOOTH);
		ImageIcon leftIcon= new ImageIcon(leftImage);
		leftBorder.setIcon(leftIcon);
		
		Image bottomImage = imageMap.getImage("bB").getScaledInstance(width-14, 7, Image.SCALE_SMOOTH);
		ImageIcon bottomIcon = new ImageIcon(bottomImage);
		bottomBorder.setIcon(bottomIcon);
		
		Image rightImage = imageMap.getImage("rB").getScaledInstance(7, height-14, Image.SCALE_SMOOTH);
		ImageIcon rightIcon = new ImageIcon(rightImage);
		rightBorder.setIcon(rightIcon);
	}
}
