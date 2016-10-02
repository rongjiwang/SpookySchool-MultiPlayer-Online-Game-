package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import game.Area;
import game.Bundle;
import game.DoorGO;
import game.FixedGO;
import game.FloorTile;
import game.MarkerGO;
import game.Player;
import game.Tile;
import game.WallTile;
import network.Client;

public class AreaDisplayPanel2D extends JPanel implements KeyListener {

	private final int offSetX = 0;
	private final int offSetY = 0;

	Area currentArea = null;

	private Client client;

	public AreaDisplayPanel2D(Client client) {
		this.setBackground(Color.darkGray);
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(this);
		this.client = client;
	}

	/**
	 * Updates the board.
	 */
	public void updateDisplay() {
		this.repaint();
	}

	/**
	 * Process the received bundle. Display game according to the bundle.
	 */
	public void processBundle(Bundle bundle) {
		if (bundle.getNewArea() != null) {
			this.currentArea = bundle.getNewArea();
		}
		this.updateDisplay();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Area area = this.currentArea;


		for (int row = 0; row < area.height; row++) {
			for (int col = 0; col < area.width; col++) {


				if (area.getArea()[row][col] == null) {
					g.setColor(Color.black);
					g.fillRect(this.offSetX + col * 32, this.offSetY + row * 32, 32, 32);
				} else if (area.getArea()[row][col] instanceof FloorTile) {
					g.setColor(Color.green);
					g.fillRect(this.offSetX + col * 32, this.offSetY + row * 32, 32, 32);
				} else if (area.getArea()[row][col] instanceof WallTile) {
					g.setColor(Color.orange);
					g.fillRect(this.offSetX + col * 32, this.offSetY + row * 32, 32, 32);
				}

				Tile tile = area.getArea()[row][col];

				if (tile != null && (tile.getOccupant() instanceof DoorGO)) {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(this.offSetX + col * 32, this.offSetY + row * 32, 32, 32);
				}

				else if (tile != null
						&& (tile.getOccupant() instanceof FixedGO || tile.getOccupant() instanceof MarkerGO)) {
					g.setColor(Color.RED);
					g.fillRect(this.offSetX + col * 32, this.offSetY + row * 32, 32, 32);
				} else if (tile != null && (tile.getOccupant() instanceof Player)) {
					g.setColor(Color.CYAN);
					g.fillRect(this.offSetX + col * 32, this.offSetY + row * 32, 32, 32);
				}

			}
		}
	}



	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_UP:
			this.client.sendCommand("NORTH");
			break;
		case KeyEvent.VK_DOWN:
			this.client.sendCommand("SOUTH");
			break;
		case KeyEvent.VK_LEFT:
			this.client.sendCommand("WEST");
			break;
		case KeyEvent.VK_RIGHT:
			this.client.sendCommand("EAST");
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

}
