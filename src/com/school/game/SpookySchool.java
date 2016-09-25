package com.school.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.school.ui.AreaDisplayPanel;

import java.util.Scanner;

import com.school.game.Player.Direction;

/**
 * This class contains all of the logic Spooky School game. This class controls
 * game state.
 * 
 * @author Pritesh R. Patel
 *
 */
public class SpookySchool implements Runnable {

	private final Position defaultSpawnPosition = new Position(5, 8); // Default
																		// position
																		// that
																		// a
																		// player
																		// spawns
																		// in,
																		// in a
																		// spawn
																		// room.

	private Map<String, Area> areas = new HashMap<String, Area>();
	private List<Player> players = new ArrayList<Player>();
	private List<String> log = new ArrayList<String>();

	public SpookySchool(AreaDisplayPanel display) {
		this.loadAreas(); // Load maps
		// this.setDoors(); //Sets up doors on the areas.
		this.display = display;
	}

	public SpookySchool() {
		this.loadAreas();
	}

	/**
	 * Load all of the "areas" of the game into the list of areas.
	 */
	public void loadAreas() {
		Scanner scan;
		try {
			scan = new Scanner(new File("src/com/school/areas/areas.txt"));
			while (scan.hasNextLine()) {
				String areaName = scan.next();
				String fileName = scan.next();
				this.areas.put(areaName, new Area(areaName, fileName));
			}

			System.out.println("Areas Loaded!");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Sets up the doors in the areas.
	 */
	public void setDoors() {

	}

	/**
	 * Adds player to the game if the game is not full.
	 * 
	 * @param name
	 *            of the player being added to the game.
	 * @return true if player successfully added, otherwise false.
	 */
	public boolean addPlayer(String name) {
		if (this.players.size() < 4) {
			Area spawnRoom = this.findEmptySpawnRoom();
			Player newPlayer = new Player(name, spawnRoom, this.defaultSpawnPosition);
			spawnRoom.setOwner(newPlayer); // Set player as the owner of the
											// spawn room.

			// Set the player as the occupant of the tile.
			FloorTile spawnTile = (FloorTile) spawnRoom.getTile(this.defaultSpawnPosition);

			assert spawnTile != null;

			spawnTile.setOccupant(newPlayer);

			return this.players.add(newPlayer); // Add the player to the list of
												// players in the game.
		}
		return false;
	}

	/**
	 * Finds and returns a spawn area that is currently not owned by a player.
	 * 
	 * @return a spawn area that is currently not owned by a player.
	 */
	public Area findEmptySpawnRoom() {

		// Finds an unoccupied/un-owned spawn area and returns it.
		for (Entry<String, Area> entry : this.areas.entrySet()) {
			if (entry.getKey().contains("Spawn") && (!entry.getValue().hasOwner())) {
				return entry.getValue();
			}
		}
		throw new Error("Error: Could not find an empty spawn room. This should not be possible.");
	}

	/**
	 * Moves player in a given direction if possible.
	 * 
	 * @param playerName
	 *            the name of the player to move.
	 * @param direction
	 *            the direction the player needs to move into.
	 * @return true if player moves to a new tile or changes direction..
	 *         Otherwise false.
	 */
	public boolean movePlayer(String playerName, Direction direction) {

		Player player = this.getPlayer(playerName);
		// Ensure that the player we are trying to move exists.
		if (player == null) {
			throw new Error("Player (" + playerName + ") you are trying to move does not exist in game.");
		}

		// If player is facing a different direction than the direction given,
		// make the player face the given direction.
		if (!player.getDirection().equals(direction)) {
			player.setDirection(direction);
			return false; // Returns false because the player hasn't "moved"
							// location
		}

		// Player is facing the correct direction. attempt to move player in the
		// given direction.
		switch (direction) {
		case NORTH:
			return this.movePlayerNorth(player);
		case SOUTH:
			return this.movePlayerSouth(player);
		case EAST:
			return this.movePlayerEast(player);
		case WEST:
			return this.movePlayerWest(player);
		}

		throw new Error("ERROR in movePlayer() method.");
	}

	/**
	 * Attempt to move the player NORTH by one tile. Return true if successful,
	 * otherwise return false. This method should be used once the player is
	 * facing the correct direction.
	 * 
	 * @param player
	 *            that is to be moved
	 * @return true if movement to the north is successful, otherwise false.
	 */
	public boolean movePlayerNorth(Player player) {
		int posX = player.getCurrentPosition().getPosX();
		int potentialPosY = player.getCurrentPosition().getPosY() - 1;

		Tile potentialTile = null;

		// Check if potential new position is within the bounds of the array.
		if (potentialPosY >= 0) {
			potentialTile = player.getCurrentArea().getTile(new Position(posX, potentialPosY));
		} else {
			return false; // Not a valid move.
		}

		// If the potential tile is a floor tile and is not currently occupied,
		// then move the player.
		if (potentialTile instanceof FloorTile && (!((FloorTile) potentialTile).isOccupied())) {
			this.movePlayerToTile(player, potentialTile); // Move the player to
															// the new tile.
			return true; // Player movement complete.
		}

		return false; // Not a valid move.
	}

	/**
	 * Attempt to move the player SOUTH by one tile. Return true if successful,
	 * otherwise return false. This method should be used once the player is
	 * facing the correct direction.
	 * 
	 * @param player
	 *            that is to be moved
	 * @return
	 */
	public boolean movePlayerSouth(Player player) {
		int posX = player.getCurrentPosition().getPosX();
		int potentialPosY = player.getCurrentPosition().getPosY() + 1;

		Tile potentialTile = null;

		// Check if potential new position is within the bounds of the array.
		if (potentialPosY < player.getCurrentArea().height) {
			potentialTile = player.getCurrentArea().getTile(new Position(posX, potentialPosY));
		} else {
			return false; // Not a valid move.
		}

		// If the potential tile is a floor tile and is not currently occupied,
		// then move the player.
		if (potentialTile instanceof FloorTile && (!((FloorTile) potentialTile).isOccupied())) {
			this.movePlayerToTile(player, potentialTile); // Move the player to
															// the new tile.
			return true; // Player movement complete.
		}

		return false; // Not a valid move.
	}

	/**
	 * Attempt to move the player EAST by one tile. Return true if successful,
	 * otherwise return false. This method should be used once the player is
	 * facing the correct direction.
	 * 
	 * @param player
	 *            that is to be moved
	 * @return true if movement to the east is successful, otherwise false.
	 * @param player
	 * @return
	 */
	public boolean movePlayerEast(Player player) {
		int potentialPosX = player.getCurrentPosition().getPosX() + 1;
		int posY = player.getCurrentPosition().getPosY();

		Tile potentialTile = null;

		// Check if potential new position is within the bounds of the array.
		if (potentialPosX < player.getCurrentArea().width) {
			potentialTile = player.getCurrentArea().getTile(new Position(potentialPosX, posY));
		} else {
			return false; // Not a valid move.
		}

		// If the potential tile is a floor tile and is not currently occupied,
		// then move the player.
		if (potentialTile instanceof FloorTile && (!((FloorTile) potentialTile).isOccupied())) {
			this.movePlayerToTile(player, potentialTile); // Move the player to
															// the new tile.
			return true; // Player movement complete.
		}

		return false; // Not a valid move.
	}

	/**
	 * Attempt to move the player WEST by one tile. Return true if successful,
	 * otherwise return false. This method should be used once the player is
	 * facing the correct direction.
	 * 
	 * @param player
	 *            that is to be moved
	 * @return true if movement to the west is successful, otherwise false.
	 */
	public boolean movePlayerWest(Player player) {

		int potentialPosX = player.getCurrentPosition().getPosX() - 1;
		int posY = player.getCurrentPosition().getPosY();

		Tile potentialTile = null;

		// Check if potential new position is within the bounds of the array.
		if (potentialPosX >= 0) {
			potentialTile = player.getCurrentArea().getTile(new Position(potentialPosX, posY));
		} else {
			return false; // Not a valid Move
		}

		// If the potential tile is a floor tile and is not currently occupied,
		// then move the player.
		if (potentialTile instanceof FloorTile && (!((FloorTile) potentialTile).isOccupied())) {
			this.movePlayerToTile(player, potentialTile); // Move the player to
															// the new tile.
			return true; // Player movement complete.
		}

		return false; // Not a valid move.
	}

	/**
	 * Move the player to the given tile. Method moves player to new tile and
	 * removes them from the old tile. Also sets player's position to the new
	 * position.
	 * 
	 * @param player
	 *            to be moved.
	 * @param tile
	 *            that the player needs to be moved onto.
	 */
	public void movePlayerToTile(Player player, Tile tile) {
		((FloorTile) player.getCurrentArea().getTile(player.getCurrentPosition())).removeOccupant(); // Remove
																										// player
																										// from
																										// old
																										// tile

		FloorTile newTile = (FloorTile) tile;
		newTile.setOccupant(player); // Add player to new tile.
		player.setCurrentPosition(newTile.getPosition()); // Set the player's
															// new position.
	}

	/**
	 * Returns the player associated with the given player name.
	 * 
	 * @param playerName
	 *            name of the player to check for.
	 * @return the Player object of the given name if one exists, otherwise
	 *         return null.
	 */
	public Player getPlayer(String playerName) {
		for (int i = 0; i < this.players.size(); i++) {
			if (this.players.get(i).getPlayerName().equals(playerName)) {
				return this.players.get(i);
			}
		}
		return null; // Player with given name not found.
	}

	/**
	 * Below is code for reccuring 60fps
	 */
	private AreaDisplayPanel display;
	private Thread thread;
	private boolean running;

	public synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		if (!running)
			return;

		running = true;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}

	/**
	 * The game Loop
	 */
	public void run() {

		// initialise variables for keeping game synchronized
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0, updates = 0, frames = 0;

		// main running loop
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				updates++;
				delta--;
			}
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("Ticks " + updates + ", Fps " + frames);
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	/**
	 * tick is called 60 times per second
	 */
	public void tick() {
		display.updateDisplay();
	}
}
