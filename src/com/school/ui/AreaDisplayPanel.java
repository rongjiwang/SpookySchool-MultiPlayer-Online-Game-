package com.school.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import javax.swing.JPanel;

import com.school.game.Area;
import com.school.game.FloorTile;
import com.school.game.GameObject;
import com.school.game.Player;
import com.school.game.Player.Direction;
import com.school.game.SpookySchool;
import com.school.game.Tile;




public class AreaDisplayPanel extends JPanel implements KeyListener{

	// Window size and offset
	private final int windowOffSetX = 150;
	private final int windowOffSetY = 50;
	private final int windowWidth = 352;
	private final int windowHeight = 352;
	
	// Renderer Tile Size
	private final int tileWidth = 32;
	private final int tileHeight = 25;
	
	// Map Offset
	private int renderOffSetX;
	private int renderOffSetY;

	// Current Rotational view 0-3
	private int rotate;
	
	/*			2
			 _______
			|		|
		3	|		|	1
			|_______|
			
				0
		  Default view */
	

	// Main Game objects 
	private SpookySchool game;
	private SpriteMap spriteMap;
	private String playerName;
	
	
	/**
	 * Constructs a new AreaDisplayPanel
	 */
	public AreaDisplayPanel() {
		
		// Setting up JPanel
		this.setBackground(Color.darkGray);
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(this);

		// Initialise fields and add player
		this.game = new SpookySchool(this);
		this.spriteMap = new SpriteMap();
		this.playerName = "Test Player";
		this.game.addPlayer(this.playerName);
		
		// Initialize with default view(0)
		this.rotate = 0;
	
		game.start();
	}

	/**
	 * Updates the board.
	 */
	public void updateDisplay() {
		centerPlayer();
		this.repaint();
	}

	
	/**
	 * Centers the player in the window
	 */
	public void centerPlayer(){
		Player p = game.getPlayer(playerName);
		Area area = p.getCurrentArea();
		
		// Determine the players current grid position in the area
		int xPos = p.getCurrentPosition().getPosX();
		int yPos = p.getCurrentPosition().getPosY();
		
		// Determine the players viewed grid position depending on 'rotate'
		int[] view = getRotatedView(xPos, yPos, area.width, area.height);
		int viewRow = view[0];
		int viewCol = view[1];

		// If the Array is rotated 0 or 180 degrees, e.g. width = height
		if(rotate == 0 || rotate == 2){
			this.renderOffSetX = (((this.windowWidth/2 + this.windowOffSetX) - (viewRow * this.tileWidth)) - tileWidth/2);
			this.renderOffSetY = (((this.windowHeight/2 + this.windowOffSetY) - (viewCol * this.tileHeight)) - tileHeight/2);
		
		// If the Array is rotated 90 or 270 degrees, e.g. width =! height
		}else if(rotate == 1 || rotate == 3){
			viewCol = area.height -1 - viewCol;
			viewRow = area.width - viewRow + 1;
			this.renderOffSetX = (((this.windowWidth/2 + this.windowOffSetX) - (viewRow * this.tileWidth)) - tileWidth/2);
			this.renderOffSetY = (((this.windowHeight/2 + this.windowOffSetY) - (viewCol * this.tileHeight)) - tileHeight/2);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	
		// add underlay
		g.setColor(Color.black);
		g.fillRect(this.windowOffSetX,this.windowOffSetY,this.windowWidth, this.windowHeight);
	
		renderArray(g, 0); // render carpet tiles
		renderArray(g, 1); // render far walls
		renderArray(g, 2); // render gameObjects
		renderArray(g, 3); // render close and side walls
		
		addOverlay(g);
		
		//addGrid(g);
		
		
	}

	/**
	 * Draws a grid over the area, helpful for testing
	 * 
	 * @param g - Graphics
	 */
	private void addGrid(Graphics g) {
		Player p = game.getPlayer(playerName);
		Area area = p.getCurrentArea();
		
		
		g.setColor(Color.red);
		for(int row = 0; row < area.height; row++){
			for(int col = 0; col < area.width; col++){
				int[] view = getRotatedView(row, col, area.width, area.height);
				int viewRow = view[0];
				int viewCol = view[1];
				
				g.drawRect(this.renderOffSetX + (viewRow * this.tileWidth), this.renderOffSetY + viewCol * this.tileHeight, this.tileWidth, this.tileHeight);
			}
		}	
	}
	
	/**
	 * Determines the approriate direction to iterate through the 2D array, 
	 * and then iterates through it in that order
	 * 
	 * @param g Graphics 
	 * @param layer - layer to draw 0 - 4
	 */
	public void renderArray(Graphics g, int layer){
		Player p = game.getPlayer(this.playerName);
		Area area = p.getCurrentArea();
		
		// determine iteration direction for 2D array from defaut view(0)
		int rowLength = area.height, colLength = area.width, 
	        row = 0, col = 0,
		    changeRow = 1, changeCol = 1;
		
		// determine iteration direction for 2D array from particular rotational view
		switch(rotate){
			case 1: row = colLength-1;
					changeRow = -1;
					rowLength = colLength;
					colLength = area.height;
					break;
					  
			case 2: row = rowLength-1; col = colLength-1;
			  		changeRow = -1; 
			  		changeCol = -1;
			  		break;
			
			case 3: col = rowLength-1;
			  		changeCol = -1;
			  		rowLength = colLength;
					colLength = area.height;
			  		break;
		}
		int originalCol = col;
		
		// Iterate through the 2D array using the appropriate variables calculated above
		for(int i = 0; i < rowLength; i++){
			col = originalCol;
			for(int j = 0; j < colLength; j++){
				if(rotate == 0 || rotate == 2)
					renderTile(g,layer, row, col); 
				else
					renderTile(g,layer, col, row);
			col += changeCol;
			}
		row += changeRow;
		}
	}
	
	public void renderTile(Graphics g, int layer, int row, int col){
		Player p = game.getPlayer(this.playerName);
		Area area = p.getCurrentArea();
		
		// Determine tile at row/col location
		Tile tile = area.getArea()[row][col];
		
		// Determine view grid location for the tile depending on the current view
		int[] view = getRotatedView(row, col, area.width, area.height);
		int viewRow = view[0];
		int viewCol = view[1];

		if(tile == null){
			return;
		}
		
		String token = getRotatedToken(tile.getToken()); // Determine the rotated token
		
		// Draw floor tile
		if(layer == 0){
			if(tile instanceof FloorTile){
				g.drawImage(spriteMap.getImage(token),this.renderOffSetX + viewCol * tileWidth, this.renderOffSetY + viewRow * tileHeight, null);
			}
	
		// Draw GameObjects(including player)
		}else if(layer == 2){ 
			GameObject go = tile.getOccupant();
			if(go != null){
				String GOtoken = getRotatedToken(go.getToken()); // Determine the rotated token
				if(GOtoken != null){
					Image tileImage = spriteMap.getImage(GOtoken);
					int adjustX = tileImage.getWidth(null)/2;
					int adjustY = tileImage.getHeight(null)/2;
					if(go instanceof Player){
						adjustX = tileImage.getWidth(null) - tileWidth;
						adjustY = tileImage.getHeight(null) - tileHeight;
					}
					g.drawImage(tileImage,(this.renderOffSetX + viewCol * tileWidth) - adjustX, (this.renderOffSetY + viewRow * tileHeight - adjustY), null);
				}
			}	
		}	
		// Draw Walls(Back and side walls with layer 1, front with layer 3)
		if(	((token.equals("w0") || token.equals("W1") || token.equals("W2"))  && layer == 1) || 
				((!(token.equals("w0") || token.equals("W1") || token.equals("W2"))) && layer == 3)){
			if(token.contains("w") || token.contains("W")){
				Image tileImage = spriteMap.getImage(token);
				int adjustX = tileImage.getWidth(null) - tileWidth;
				int adjustY = tileImage.getHeight(null) - tileHeight;
				g.drawImage(tileImage,(this.renderOffSetX + viewCol * tileWidth) - adjustX, (this.renderOffSetY + viewRow * tileHeight) - adjustY, null);			
			}				
		}
	}
	
	
	/**
	 * Determine the correct direction depending on the 
	 * current rotation of the display
	 * 
	 * @param direction - 2d direction
	 * @return direction - direction the user will see
	 */
	public Direction determineDirection(Direction direction){
		for(int i = 0; i < rotate; i++){
			direction = rotateDirection(direction, 1);
		}
		return direction;
	}
	
	/**
	 * Determine the direction in 90 degree rotation 
	 * either left of right
	 *
	 * @param direction
	 * @param i - either positive(anti-Clockwise) or negative(clockwise)
	 * @return - rotated direction
	 * 		   - null(when passed 0) - shouldn't happen
	 */
	public Direction rotateDirection(Direction direction, int i){
		switch(direction){
		
			case NORTH: if(i > 0) return direction.EAST;	
						if(i < 0) return direction.WEST;	
			
			case EAST:  if(i > 0) return direction.SOUTH;
						if(i < 0) return direction.NORTH;
			
			case SOUTH: if(i > 0) return direction.WEST;
						if(i < 0) return direction.EAST;
			
			case WEST:  if(i > 0) return direction.NORTH;
						if(i < 0) return direction.SOUTH;
		}
		return null;
	}
	
	/**
	 * Adds a layer around the boundary of the game so only the window is seen.
	 * Wont be required when the window has its own JPanel
	 *
	 * @param g - Graphics
	 */
	public void addOverlay(Graphics g){
		g.setColor(Color.darkGray);
		g.fillRect(0,0,this.getWidth(), this.windowOffSetY);
		g.fillRect(0,this.windowOffSetY,this.windowOffSetX, this.getHeight() - this.windowOffSetY);
		g.fillRect(this.windowOffSetX + windowWidth,this.windowOffSetY,
				this.getWidth() - (this.windowOffSetX + this.windowWidth), this.getHeight() - this.windowOffSetY);
		g.fillRect(this.windowOffSetX, this.windowOffSetY + this.windowHeight, 
				this.windowWidth, this.getHeight() - (this.windowOffSetY + this.windowHeight));
	}
	
	/**
	 * Determines the new x,y position(where the tile will be drawn) 
	 * of a tile from it logical position and the current view
	 * 
	 * @param x - player x position
	 * @param y - player y position
	 * @param width - width of the area array
	 * @param height - height of the area array
	 *
	 * @return int[] - returns coordinates as an array of size 2 where
	 * 					x = [0] and y = [1]
	 */
	public int[] getRotatedView(int x, int y, int width, int height) {
		int[] r = new int[2];
		int newX = 0, newY = 0;
		int oldX = x, oldY = y;
		
		for (int i = 0; i < rotate; i++) {
			newY = oldX;
			newX = oldY;
			newX =  height - oldY - 1;
			oldX = newX;
			oldY = newY;
		}
		r[0] = oldX;
		r[1] = oldY;
		return r;

	}
	
	/**
	 *  Iterate the rotate field approriately,
	 *  must be 0 >= x >= 3
	 * 
	 * @param r - particular rotation,
	 * 			  either 1 (anti-clockwise)
	 * 			  or -1 (clockwise)
	 */
	public void rotate(int r){
		rotate += r;
		if(rotate == -1)
			rotate = 3;
		else if(rotate == 4)
			rotate = 0;
	}
		
	/**
	 * Determine the approriate token string depending on the view
	 * e.g. "w1" when rotate = 1 should be "w0" 
	 * @param token
	 * @return
	 */
	public String getRotatedToken(String token){
		
		if(token == null)
			return null;
		
		for(int b = 0; b < rotate; b++){
			String j = "" + token.charAt(token.length()-1);
			int i = Integer.valueOf(j) - 1;
			if(i == -1)
				i = 3;
			else if(i == 4)
				i = 0;
			String a = token.substring(0, token.length()-1);
			token = a + i;	
		}
		return token;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		boolean move = false;
		Direction direction = null;
		switch (keyCode) {
		case KeyEvent.VK_UP:
			direction = determineDirection(Direction.NORTH);
			move = this.game.movePlayer(this.playerName, direction);			
			break;
		case KeyEvent.VK_DOWN:
			direction = determineDirection(Direction.SOUTH);
			move = this.game.movePlayer(this.playerName, direction);
			break;
		case KeyEvent.VK_LEFT:
			direction = determineDirection(Direction.WEST);
			move = this.game.movePlayer(this.playerName, direction);
			break;
		case KeyEvent.VK_RIGHT:
			direction = determineDirection(Direction.EAST);
			move = this.game.movePlayer(this.playerName, direction);
			break;
		case KeyEvent.VK_R:
			rotate(-1);
			break;
		case KeyEvent.VK_L:
			rotate(1);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	
	
	
	
	
	
	
	

}
