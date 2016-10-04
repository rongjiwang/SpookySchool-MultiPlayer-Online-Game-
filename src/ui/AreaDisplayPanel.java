package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import game.Area;
import game.Bundle;
import game.DoorGO;
import game.FloorTile;
import game.GameObject;
import game.MarkerGO;
import game.Player;
import game.Position;
import game.Tile;
import network.Client;

public class AreaDisplayPanel extends JPanel implements KeyListener{

	// Window size and offset
	private final int windowOffSetX = 0;
	private final int windowOffSetY = 0;
	private final int windowWidth = 352;
	private final int windowHeight = 352;

	// Renderer Tile Size
	private final int tileWidth = 32;
	private final int tileHeight = 25;
	
	private int mainPlayerXBuff;
	private int mainPlayerYBuff;

	// Map Offset
	private int renderOffSetX;
	private int renderOffSetY;

	String[] keyQueue;
	
	
	// Keep track of system time
	private long then;
	private long now;
	
	private String currentKey;
	
	private Area currentArea = null;

	private Client client;
	private SpriteMap spriteMap;

	RenderGameObject mainPlayer;
	List<RenderGameObject> gameObjects;

	private Timer timer;
	private GameFrame gameFrame;
	

	// Current Rotational view 0-3
	private int view;
	/*			2
			 _______
			|		|
		3	|		|	1
			|_______|
			
				0
		  Default view */


	public AreaDisplayPanel(Client client, GameFrame gf) {
		this.gameFrame = gf;
		this.setBackground(Color.darkGray);
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(this);
		this.client = client;
		this.spriteMap = new SpriteMap();
		this.gameObjects = new ArrayList<>();
		this.keyQueue = new String[2];
		
		
	}

	/**
	 * Updates the board.
	 */
	public void updateDisplay() {
		if(gameFrame.getDebugDisplay() != null)
			gameFrame.getDebugDisplay().updateDisplay();
		centerPlayer();
		this.repaint();
	}

	
	/**
	 * Process the received bundle. Display game according to the bundle.
	 */
	public void processBundle(Bundle bundle) {
		
		
		if (bundle.getPlayerObj() != null) {
			processAreaChange(bundle.getPlayerObj());
		}

		Scanner scan = null;
		
		for (String s : bundle.getGameObjectChanges()) {
			
			scan = new Scanner(s);
			String ObjectID = scan.next();
			String ChangeType = scan.next();
			String Change = scan.next();
			String x = scan.next();
			String y = scan.next();
			String token = scan.next();
			processGameObjectChange(ObjectID, ChangeType, Change, x, y, token);
		}

		
		this.updateDisplay();
	}
	
	

	/**
	 * Process the gameObjectChanges from the most recent bundle
	 */
	public void processGameObjectChange(String objectID, String changeType, String change, String x, String y, String token) {
		
		if(changeType.equals("appear")){
			if(!(objectID.equals(mainPlayer.getID()))){
				if(token.contains("p")){
					token = getRotatedAnimatedToken(token);
					gameObjects.add(new RenderGameObject(objectID, token, Integer.valueOf(x), Integer.valueOf(y), true, change));		
				}else{
					token = getRotatedToken(token);
					gameObjects.add(new RenderGameObject(objectID, token, Integer.valueOf(x), Integer.valueOf(y), false, change));		
				}
			}
		}
		
		
		RenderGameObject toRemove = null;

		for (RenderGameObject rgo : gameObjects) {
			if (rgo.getID().equals(objectID)) {
				if (changeType.equals("move")) {
					rgo.move(change);
				} else if (changeType.equals("direction")) {
					rgo.changeDirection(change);
				} else if (changeType.equals("open")){
					
				} else if (changeType.equals("close")){
					
				}else if(changeType.equals("disappear")){
					if (rgo.getID().equals(objectID)) {
						toRemove = rgo;
					}
				}
			}
			
		}
		gameObjects.remove(toRemove);
		
		if (mainPlayer.getID().equals(objectID)) {
			if (changeType.equals("move")) {
				//addCommandToQueue(determineDirection(change));
				//executeCommand(mainPlayer);
				//smoothPlayerMove(rgo, change);
				mainPlayer.move(change);
			} else if (changeType.equals("direction")) {
				mainPlayer.changeDirection(change);
			} 
		}
			
		
	}
	
	

	public void printQueue(){
		
		System.out.println(keyQueue[0]);
		System.out.println(keyQueue[0]);
		System.out.println("*******************");

	}

	/**
	 * Process the gameAreaChange from the most recent bundle
	 */
	public void processAreaChange(Player player) {
		String id = player.getPlayerName();
		String token = player.getToken();
		int x = player.getCurrentPosition().getPosX();
		int y = player.getCurrentPosition().getPosY();
		currentArea = player.getCurrentArea();
		gameObjects.clear();
		mainPlayer = new RenderGameObject(id, token, x, y, true, currentArea.getAreaName());
		RenderGameObject rgo = null;
		
		for (int i = 0; i < currentArea.height; i++) {
			for (int j = 0; j < currentArea.width; j++) {
				Tile tile = currentArea.getTile(new Position(j, i));
				if (tile != null) {
					if (tile.isOccupied()) {
						GameObject go = tile.getOccupant();
						if(go instanceof DoorGO){
							DoorGO doorToken = (DoorGO) go;
							id = go.getId();
							token = doorToken.getToken(currentArea.getAreaName());
							x = doorToken.getPosition(currentArea.getAreaName()).getPosX();
							y = doorToken.getPosition(currentArea.getAreaName()).getPosY();
							rgo = new RenderGameObject(id, token, x, y, false, currentArea.getAreaName());
						}else if (go instanceof Player){
							Player p = (Player) go;
							if(!(p.getPlayerName().equals(player.getPlayerName()))){
								id = go.getId();
								token = go.getToken();
								x = go.getPosition().getPosX();
								y = go.getPosition().getPosY();
								rgo = new RenderGameObject(id, token, x, y,false, currentArea.getAreaName());
							}
						}else if (!(go instanceof MarkerGO)) {
								id = go.getId();
								token = go.getToken();
								x = go.getPosition().getPosX();
								y = go.getPosition().getPosY();
								rgo = new RenderGameObject(id, token, x, y, false, currentArea.getAreaName());
						}
						gameObjects.add(rgo);
					}
				}
			}
		}
	}



	/**
	 * Centers the player in the window
	 */
	public void centerPlayer() {
		int playerXPos = mainPlayer.getXPos();
		int playerYPos = mainPlayer.getYPos();

		int[] view = getRotatedView(playerXPos, playerYPos, currentArea.width, currentArea.height);
		int viewX = view[0];
		int viewY = view[1];

		int playerX = (viewX * tileWidth) + tileWidth / 2;
		int playerY = (viewY * tileHeight) + tileHeight / 2;

		int windowCenterX = (this.windowWidth / 2) + this.windowOffSetX;
		int windowCenterY = (this.windowHeight / 2) + this.windowOffSetY;

		this.renderOffSetX = (windowCenterX - playerX) - mainPlayerXBuff;
		this.renderOffSetY = (windowCenterY - playerY) - mainPlayerYBuff;
	}

	


	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// add underlay
		g.setColor(Color.black);
		g.fillRect(this.windowOffSetX, this.windowOffSetY, this.windowWidth, this.windowHeight);

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
	public void renderArray(Graphics g, int layer) {
		if (currentArea == null)
			return;

		if (view == 0) {
			for (int y = 0; y < currentArea.height; y++)
				for (int x = 0; x < currentArea.width; x++)
					renderTile(g, layer, x, y);

		} else if (view == 1) {
			for (int x = 0; x < currentArea.width; x++)
				for (int y = currentArea.height - 1; y >= 0; y--)
					renderTile(g, layer, x, y);

		} else if (view == 2) {
			for (int y = currentArea.height - 1; y >= 0; y--)
				for (int x = currentArea.width - 1; x >= 0; x--)
					renderTile(g, layer, x, y);

		} else if (view == 3) {
			for (int x = currentArea.width - 1; x >= 0; x--)
				for (int y = 0; y < currentArea.height; y++)
					renderTile(g, layer, x, y);

		}
	}

	public void renderTile(Graphics g, int layer, int x, int y) {

		Tile tile = currentArea.getTile(new Position(x, y));

		int[] view = getRotatedView(x, y, currentArea.width, currentArea.height);
		int viewX = view[0];
		int viewY = view[1];

		int finalX = this.renderOffSetX + viewX * tileWidth;
		int finalY = this.renderOffSetY + viewY * tileHeight;

		if (tile == null)
			return;

		String token = getRotatedToken(tile.getToken()); // Determine the rotated token

		// Draw floor tile
		if (layer == 0) {
			if (tile instanceof FloorTile)
				g.drawImage(spriteMap.getImage(token), finalX, finalY, null);


			// Draw GameObjects(including player)
		} else if (layer == 2) {
			int adjustX = 0;
			int adjustY = 0;
			Image tileImage = null;
			for (RenderGameObject rgo : gameObjects) {
				if (rgo.getXPos() == x && rgo.getYPos() == y && rgo.getArea().equals(currentArea.getAreaName())) {
					if (rgo.isPlayer()) {
						//System.out.println("player: " + rgo.getToken());
						tileImage = spriteMap.getImage(getRotatedAnimatedToken(rgo.getToken()));
						adjustX = (tileImage.getWidth(null) - tileWidth);
						adjustY = (tileImage.getHeight(null) - tileHeight);
					}else{
						//System.out.println("non-player: " + rgo.getToken());
						tileImage = spriteMap.getImage(getRotatedToken(rgo.getToken()));
						adjustX = (tileImage.getWidth(null) / 2);
						adjustY = (tileImage.getHeight(null) / 2);
					}
				}
				g.drawImage(tileImage, finalX - adjustX, finalY - adjustY, null);
			}
			if (mainPlayer.getXPos() == x && mainPlayer.getYPos() == y && mainPlayer.getArea().equals(currentArea.getAreaName())) {
				tileImage = spriteMap.getImage(getRotatedAnimatedToken(mainPlayer.getToken()));
				adjustX = (tileImage.getWidth(null) - tileWidth) - mainPlayerXBuff;
				adjustY = (tileImage.getHeight(null) - tileHeight) - mainPlayerYBuff;
				g.drawImage(tileImage, finalX - adjustX, finalY - adjustY, null);
			}
		}
		// Draw Walls(Back and side walls with layer 1, front with layer 3)
		if (((token.equals("w0") || token.equals("W1") || token.equals("W2")) && layer == 1)
				|| ((!(token.equals("w0") || token.equals("W1") || token.equals("W2"))) && layer == 3)) {
			if (token.contains("w") || token.contains("W")) {
				Image tileImage = spriteMap.getImage(token);
				int adjustX = tileImage.getWidth(null) - tileWidth;
				int adjustY = tileImage.getHeight(null) - tileHeight;
				g.drawImage(tileImage, finalX - adjustX, finalY - adjustY, null);
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
			newX = height - oldY - 1;
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
	public void rotate(int r) {


		view += r;
		if (view == -1)
			view = 3;
		else if (view == 4)
			view = 0;

		for (RenderGameObject rgo : gameObjects) {
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
	 * 
	 * @param token
	 * @return
	 */
	public String getRotatedToken(String token) {

		if (token == null)
			return null;
		
		for (int b = 0; b < view; b++) {
			
			String j = "" + token.charAt(token.length() - 1);
			
			int i = Integer.valueOf(j) + 1;
			if (i == -3)
				i = 0;
			else if (i == 4)
				i = 0;
			String a = token.substring(0, token.length() - 1);
			token = a + i;
		}
		return token;
	}
	
	/**
	 * Determine the approriate token string depending on the view
	 * e.g. "w1" when rotate = 1 should be "w0" 
	 * 
	 * @param token
	 * @return
	 */
	public String getRotatedAnimatedToken(String token) {
		if (token == null)
			return null;
		
		for (int b = 0; b < view; b++) {
			String j = "" + token.charAt(token.length() - 1);
			String z = "" + token.charAt(token.length() - 2);
			int i = Integer.valueOf(z) + 1;
			if (i == -3)
				i = 0;
			else if (i == 4)
				i = 0;
			String a = token.substring(0, token.length() - 2);
			token = a + i + j;
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
	public String determineDirection(String direction) {
		for (int i = 0; i < view; i++) {
			direction = rotateDirection(direction, -1);
		}
		return direction;
	}
	
	public void smoothPlayerMove(RenderGameObject rgo, String change){
		if(change.equals("NORTH") || change.equals("SOUTH")){
			for(int i = 0; i < 25; i++){
				if(change.equals("NORTH"))
					rgo.setYBuff(rgo.getYBuff() - 1);				
				else
					rgo.setYBuff(rgo.getYBuff() + 1);				
				updateDisplay();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			for(int i = 0; i < 32; i++){
				if(change.equals("EAST"))
					rgo.setXBuff(rgo.getXBuff() + 1);				
				else
					rgo.setXBuff(rgo.getXBuff() - 1);				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		rgo.setXBuff(0);
		rgo.setYBuff(0);		
		rgo.move(change);
	}
	
	public void smoothMainPlayerMove(RenderGameObject rgo, String change){
		
		if(change.equals("NORTH") || change.equals("SOUTH")){
			for(int i = 0; i < 25; i++){
				if(change.equals("NORTH"))
					mainPlayerYBuff--;
				else
					mainPlayerYBuff++;
				updateDisplay();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			for(int i = 0; i < 32; i++){
				if(change.equals("EAST"))
					mainPlayerXBuff++;
				else
					mainPlayerXBuff--;
				updateDisplay();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		mainPlayerYBuff = 0;
		mainPlayerXBuff = 0;
		rgo.move(change);
		
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
	public String rotateDirection(String direction, int i) {
		switch (direction) {

		case "NORTH":
			if (i > 0)
				return "EAST";
			if (i < 0)
				return "WEST";

		case "EAST":
			if (i > 0)
				return "SOUTH";
			if (i < 0)
				return "NORTH";

		case "SOUTH":
			if (i > 0)
				return "WEST";
			if (i < 0)
				return "EAST";

		case "WEST":
			if (i > 0)
				return "NORTH";
			if (i < 0)
				return "SOUTH";
		}
		return null;
	}

	/**
	 * Adds a layer around the boundary of the game so only the window is seen.
	 * Wont be required when the window has its own JPanel
	 *
	 * @param g - Graphics
	 */
	public void addOverlay(Graphics g) {
		g.setColor(Color.darkGray);
		g.fillRect(0, 0, this.getWidth(), this.windowOffSetY);
		g.fillRect(0, this.windowOffSetY, this.windowOffSetX, this.getHeight() - this.windowOffSetY);
		g.fillRect(this.windowOffSetX + windowWidth, this.windowOffSetY,
				this.getWidth() - (this.windowOffSetX + this.windowWidth), this.getHeight() - this.windowOffSetY);
		g.fillRect(this.windowOffSetX, this.windowOffSetY + this.windowHeight, this.windowWidth,
				this.getHeight() - (this.windowOffSetY + this.windowHeight));
	}

	public void addCommandToQueue(String command){
		keyQueue[1] = command;
		
		
	}
	
	public String getCommandFromQueue(){
	
		if(keyQueue[0] == null && keyQueue[1] != null){
			keyQueue[0] = keyQueue[1];
			keyQueue[1] = null;
		}
		String toReturn = null;
		if(keyQueue[0] != null){
			toReturn = keyQueue[0];
			keyQueue[0] = null;
		}
		
		
		return toReturn;
	}
	
	public void executeCommand(RenderGameObject rgo){		
		if(rgo.getID().equals(mainPlayer.getID()))
			rgo.move(getCommandFromQueue());
		//smoothMainPlayerMove(rgo, getCommandFromQueue());
		
	
		
		
		
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		now = System.currentTimeMillis();
		
		if((now - then) < 280)
			return;
	
		switch (keyCode) {
		case KeyEvent.VK_UP:
			this.client.sendCommand(determineDirection("NORTH"));
			//currentKey = determineDirection("NORTH");
			break;
		case KeyEvent.VK_DOWN:
			this.client.sendCommand(determineDirection("SOUTH"));
			//currentKey = determineDirection("SOUTH");
			break;
		case KeyEvent.VK_LEFT:
			//currentKey = determineDirection("WEST");
			this.client.sendCommand(determineDirection("WEST"));
			break;
		case KeyEvent.VK_RIGHT:
			//currentKey = determineDirection("EAST");
			this.client.sendCommand(determineDirection("EAST"));
			break;
		case KeyEvent.VK_Z:
			//currentKey = determineDirection("EAST");
			this.client.sendCommand("ACTION");
		case KeyEvent.VK_R:
			rotate(1);
			break;
		case KeyEvent.VK_L:
			rotate(-1);
			break;
		}
		then = System.currentTimeMillis();
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
				
		switch (keyCode) {
		case KeyEvent.VK_UP:
			//this.client.sendCommand(determineDirection("NORTH"));
			//currentKey = null;
			break;
		case KeyEvent.VK_DOWN:
			//this.client.sendCommand(determineDirection("SOUTH"));
			//currentKey = null;
			break;
		case KeyEvent.VK_LEFT:
			//currentKey = null;
			//this.client.sendCommand(determineDirection("WEST"));
			break;
		case KeyEvent.VK_RIGHT:
			//currentKey = null;
			//this.client.sendCommand(determineDirection("EAST"));
			break;
		case KeyEvent.VK_R:
			//rotate(1);
			break;
		case KeyEvent.VK_L:
			//rotate(-1);
			break;
		}
		//then = System.currentTimeMillis();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	
	
	public RenderGameObject getMainPlayer(){
		return mainPlayer;
	}
}

