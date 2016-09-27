package com.school.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import com.school.game.Area;
import com.school.game.DoorGO;
import com.school.game.FixedGO;
import com.school.game.FloorTile;
import com.school.game.MarkerGO;
import com.school.game.Player;
import com.school.game.Player.Direction;
import com.school.game.SpookySchool;
import com.school.game.Tile;
import com.school.game.WallTile;


public class AreaDisplayPanel extends JPanel implements KeyListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final int offSetX = 150;
	private final int offSetY = 50;
	private final int tileSize = 32;

	private SpookySchool game;
	private String playerName;

	public AreaDisplayPanel() {
		this.setBackground(Color.darkGray);
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(this);

		this.game = new SpookySchool(this);
		this.playerName = "Test Player";

		this.game.addPlayerTest(this.playerName);
	}

	/**
	 * Updates the board.
	 */
	public void updateDisplay() {
		this.repaint();
	}



	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Player p = game.getPlayer(this.playerName);
		Area area = p.getCurrentArea();


		for (int row = 0; row < area.height; row++) {
			for (int col = 0; col < area.width; col++) {


				if (area.getArea()[row][col] == null) {
					g.setColor(Color.black);
					g.fillRect(this.offSetX + col * 32, this.offSetY + row * 32, 32, 32);
				} else if (area.getArea()[row][col] instanceof FloorTile) {
					g.setColor(Color.green);
					g.fillRect(this.offSetX + col * 32, this.offSetY + row * 32, 32, 32);
				} else if (area.getArea()[row][col] instanceof WallTile) {
					WallTile tile = (WallTile) area.getArea()[row][col];
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
				}


				if (p.getCurrentPosition().getPosX() == col && p.getCurrentPosition().getPosY() == row) {
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
			this.game.movePlayer(this.playerName, Direction.NORTH);
			this.updateDisplay();
			break;
		case KeyEvent.VK_DOWN:
			this.game.movePlayer(this.playerName, Direction.SOUTH);
			this.updateDisplay();
			break;
		case KeyEvent.VK_LEFT:
			this.game.movePlayer(this.playerName, Direction.WEST);
			this.updateDisplay();
			break;
		case KeyEvent.VK_RIGHT:
			this.game.movePlayer(this.playerName, Direction.EAST);
			this.updateDisplay();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}



}