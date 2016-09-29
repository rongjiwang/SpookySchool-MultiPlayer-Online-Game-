package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.swing.JPanel;

import game.GameObject;

import game.Area;
import game.Bundle;
import game.DoorGO;
import game.FixedGO;
import game.FloorTile;
import game.MarkerGO;
import game.Player;
import game.Position;
import game.Tile;
import game.WallTile;
import network.Client;

public class AreaDisplayPanel extends JPanel implements KeyListener {
	
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
	
	Area currentArea = null;

	private Client client;
	private SpriteMap spriteMap;
		
	private List<RenderGameObject> gameObjects;
	
	
	// Current Rotational view 0-3
	private int view;
	/*			2
			 _______
			|		|
		3	|		|	1
			|_______|
			
				0
		  Default view */
	
	

	public AreaDisplayPanel(Client client) {
		this.setBackground(Color.darkGray);
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(this);
		this.client = client;
		this.spriteMap = new SpriteMap();
		this.gameObjects = new ArrayList<>();
	}

	/**
	 * Updates the board.
	 */
	public void updateDisplay() {
		centerPlayer();
		this.repaint();
	}

	/**
	 * Process the received bundle. Display game according to the bundle.
	 */
	public void processBundle(Bundle bundle) {
		if (bundle.getNewArea() != null) {
			processAreaChange(bundle.getPlayerObj());
			
		}
		
		Scanner scan = null;
		for(String s: bundle.getGameObjectChanges()){
			scan = new Scanner(s);
			String ObjectID = scan.next();
			String ChangeType = scan.next();
			String Change = scan.next();
			processGameObjectChange(ObjectID, ChangeType, Change);
		}

		this.updateDisplay();
	}
	
	/**
	 * Process the gameObjectChanges from the most recent bundle
	 */
	public void processGameObjectChange(String objectID, String changeType, String change){
	

		for(RenderGameObject rgo: gameObjects){
			if(rgo.getID().equals(objectID)){
				if(changeType.equals("move")){
					rgo.move(change);
				}else if(changeType.equals("direction")){
					rgo.changeDirection(change);
				}
			}
		}
	}
	

	
	
	/**
	 * Process the gameObjectChanges from the most recent bundle
	 */
	public void processAreaChange(Player player){
		String name = player.getPlayerName();
		String token = player.getToken();
		int x = player.getCurrentPosition().getPosX();
		int y = player.getCurrentPosition().getPosY();
		currentArea = player.getCurrentArea();
		gameObjects.clear();
		RenderGameObject rgo = new RenderGameObject("", name, token, x, y, true);
		gameObjects.add(rgo);
		
		for(int i = 0; i < currentArea.height; i++){
			for(int j = 0; j < currentArea.width; j++){
				Tile tile = currentArea.getTile(new Position(j,i));
				if(tile != null){
					if(tile.isOccupied()){
						GameObject go = tile.getOccupant();
							if(! (go instanceof MarkerGO)){
								Boolean isPlayer = false;
								Boolean mainPlayer = false;
								if(go instanceof Player){
									if(player.getPlayerName().equals(((Player) go).getPlayerName())){
										mainPlayer = true;
									}else{
										isPlayer = true;
									}
								}
								if(!mainPlayer){
									token = go.getToken();
									x = go.getPosition().getPosX();
									y = go.getPosition().getPosY();
									rgo = new RenderGameObject("", "", token, x, y, isPlayer);
									gameObjects.add(rgo);
								}
									
								
							}
						}
					}
				}
			}
		}
		
	

	
	/**
	 * Centers the player in the window
	 */
	public void centerPlayer(){
		int playerXPos = gameObjects.get(0).getXPos();
		int playerYPos = gameObjects.get(0).getYPos();
		
		int[] view = getRotatedView(playerXPos, playerYPos, currentArea.width, currentArea.height);
		int viewX = view[0];
		int viewY = view[1];
		
		int playerX = (viewX * tileWidth) + tileWidth/2;
		int playerY = (viewY * tileHeight) + tileHeight/2;
		
		int windowCenterX = (this.windowWidth/2) + this.windowOffSetX;
		int windowCenterY = (this.windowHeight/2) + this.windowOffSetY;
		
		this.renderOffSetX = windowCenterX - playerX;
		this.renderOffSetY = windowCenterY - playerY;		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//render2D(g);
		
		render3D(g);		
	}
	
	/**
	 * Renders the game in 2D
	 * 
	 * @param g - graphics
	 */
	public void render2D(Graphics g){
		
		Area area = this.currentArea;


		for (int x = 0; x < area.width; x++) {
			for (int y = 0; y < area.height; y++) {


				if (area.getArea()[y][x] == null) {
					g.setColor(Color.black);
					g.fillRect(this.windowOffSetX + x * 32, this.windowOffSetY + y * 32, 32, 32);
				} else if (area.getArea()[y][x] instanceof FloorTile) {
					g.setColor(Color.green);
					g.fillRect(this.windowOffSetX + x * 32, this.windowOffSetY + y * 32, 32, 32);
				} else if (area.getArea()[y][x] instanceof WallTile) {
					WallTile tile = (WallTile) area.getArea()[y][x];
					g.setColor(Color.orange);
					g.fillRect(this.windowOffSetX + x * 32, this.windowOffSetY + y * 32, 32, 32);
				}

				Tile tile = area.getArea()[y][x];

				if (tile != null && (tile.getOccupant() instanceof DoorGO)) {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(this.windowOffSetX + x * 32, this.windowOffSetY + y * 32, 32, 32);
				}

				else if (tile != null
						&& (tile.getOccupant() instanceof FixedGO || tile.getOccupant() instanceof MarkerGO)) {
					g.setColor(Color.RED);
					g.fillRect(this.windowOffSetX + x * 32, this.windowOffSetY + y * 32, 32, 32);
				} else if (tile != null && (tile.getOccupant() instanceof Player)) {
					g.setColor(Color.CYAN);
					g.fillRect(this.windowOffSetX + x * 32, this.windowOffSetY + y * 32, 32, 32);
				}

			}
		}
		
	}
	
	/**
	 * Renders the game in 3D
	 * 
	 * @param g - graphics
	 */
	public void render3D(Graphics g){
		
		// add underlay
		g.setColor(Color.black);
		g.fillRect(this.windowOffSetX,this.windowOffSetY,this.windowWidth, this.windowHeight);
				
		renderArray(g, 0); // render carpet tiles		
		renderArray(g, 1); // render far walls
		renderArray(g, 2); // render gameObjects
		renderArray(g, 3); // render close and side walls
		
		addOverlay(g);
	}

	/**
	 * Iterates through the array in the appropriate direction 
	 * depending on the current view. Only renders the suggested layer
	 * 
	 * @param g - graphics 
	 * @param layer - layer to render
	 */
	public void renderArray(Graphics g, int layer){
		if(currentArea == null)
			return;
		
		if(view == 0){
			for(int y = 0; y < currentArea.height; y++)
				for(int x = 0; x < currentArea.width; x++)
					renderTile(g, layer, x, y); 
			
		}else if(view == 1){
			for(int x = 0; x < currentArea.width; x++)
				for(int y = currentArea.height - 1; y >= 0; y--)
					renderTile(g, layer, x, y);
				
		}else if(view == 2){
			for(int y = currentArea.height - 1; y >= 0; y--)
				for(int x = currentArea.width - 1; x >= 0; x--)
					renderTile(g, layer, x, y);
					
		}else if(view == 3){
			for(int x = currentArea.width - 1; x >= 0; x--)
				for(int y = 0; y < currentArea.height; y++)
					renderTile(g, layer, x, y);
					
		}
	}
	
	public void renderTile(Graphics g, int layer, int x, int y){
		
		Tile tile = currentArea.getTile(new Position(x,y));
		
		int[] view = getRotatedView(x, y, currentArea.width, currentArea.height);
		int viewX = view[0];
		int viewY = view[1];
		
		int finalX = this.renderOffSetX + viewX * tileWidth;
		int finalY = this.renderOffSetY + viewY * tileHeight;
		
		if(tile == null)
			return;
		
		String token = getRotatedToken(tile.getToken()); // Determine the rotated token
		
		// Draw floor tile
		if(layer == 0){
			if(tile instanceof FloorTile)
				g.drawImage(spriteMap.getImage(token),finalX, finalY, null);
		
			
		// Draw GameObjects(including player)
		}else if(layer == 2){ 
			for(RenderGameObject rgo : gameObjects){
				if(rgo.getXPos() == x && rgo.getYPos() == y){
					Image tileImage = spriteMap.getImage(getRotatedToken(rgo.getToken()));
					int adjustX = tileImage.getWidth(null)/2;
					int adjustY = tileImage.getHeight(null)/2;
					if(rgo.isPlayer()){
						adjustX = tileImage.getWidth(null) - tileWidth;
						adjustY = tileImage.getHeight(null) - tileHeight;
					}
					g.drawImage(tileImage, finalX - adjustX, finalY- adjustY, null);
					
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
				g.drawImage(tileImage,finalX - adjustX, finalY - adjustY, null);			
			}				
		}
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
		
		for (int i = 0; i < view; i++) {
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
	 *  Iterate the view field approriately,
	 *  must be 0 >= x >= 3
	 * 
	 * @param r - particular rotation,
	 * 			  either 1 (anti-clockwise)
	 * 			  or -1 (clockwise)
	 */
	public void rotate(int r){
		
		
		view += r;
		if(view == -1)
			view = 3;
		else if(view == 4)
			view = 0;
		
		for(RenderGameObject rgo: gameObjects){
			int x = rgo.getXPos();
			int y = rgo.getYPos();
			int[] view = getRotatedView(x, y, currentArea.width, currentArea.height);
			rgo.setViewXPos(view[0]);
			rgo.setViewXPos(view[1]);
		}

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
		
		for(int b = 0; b < view; b++){
			String j = "" + token.charAt(token.length()-1);
			int i = Integer.valueOf(j) + 1;
			if(i == -3)
				i = 0;
			else if(i == 4)
				i = 0;
			String a = token.substring(0, token.length()-1);
			token = a + i;	
		}
		return token;
	}
	

	
	 
	/**
	 * Determine the correct direction depending on the 
	 * current rotation of the display
	 * 
	 * @param direction - 2d direction
	 * @return direction - direction the user will see
	 */
	public String determineDirection(String direction){
		for(int i = 0; i < view; i++){
			direction = rotateDirection(direction, -1);
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
	public String rotateDirection(String direction, int i){
		switch(direction){
		
			case "NORTH": if(i > 0) return "EAST";	
						if(i < 0) return "WEST";	
			
			case "EAST":  if(i > 0) return "SOUTH";
						if(i < 0) return "NORTH";
			
			case "SOUTH": if(i > 0) return "WEST";
						if(i < 0) return "EAST";
			
			case "WEST":  if(i > 0) return "NORTH";
						if(i < 0) return "SOUTH";
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
	

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_UP:
			this.client.sendCommand(determineDirection("NORTH"));
			break;
		case KeyEvent.VK_DOWN:
			this.client.sendCommand(determineDirection("SOUTH"));
			break;
		case KeyEvent.VK_LEFT:
			this.client.sendCommand(determineDirection("WEST"));
			break;
		case KeyEvent.VK_RIGHT:
			this.client.sendCommand(determineDirection("EAST"));
			break;
		case KeyEvent.VK_R:
			rotate(1);
			break;
		case KeyEvent.VK_L:
			rotate(-1);
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
