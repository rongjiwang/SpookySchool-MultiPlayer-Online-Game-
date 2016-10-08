package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * This class is responsible for loading the game on a new server and joining a client to it.
 * @author Pritesh R. Patel
 *
 */
public class LoadGamePanel extends JPanel {

	private BufferedImage uiBackground;

	public LoadGamePanel(JPanel contentPane) {
		this.setLayout(null); //Use no layout manager in this panel.
		this.setBackground(Color.darkGray);

		this.setupPanel(); //Sets up this panel. Adds various buttons and input fields.

		try {
			this.uiBackground = ImageIO.read(new File("src/ui/images/networkui_bg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Set up the panel with the different buttons e.g. load etc.
	 */
	private void setupPanel() {
		// TODO Auto-generated method stub

	}


	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.uiBackground, 0, 0, null); //Draw the UI background
	}

}
