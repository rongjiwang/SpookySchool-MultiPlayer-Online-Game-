package com.school.game;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import com.school.control.Client;
import com.school.control.ClientServerFrame;
import com.school.control.Server;
import com.school.game.Player.Direction;
import com.school.ui.AreaDisplayPanel;

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
	private List<String> chatLog = new ArrayList<String>();

	private Map<String, Bundle> playerBundles = new HashMap<String, Bundle>();

	private AreaDisplayPanel display;

	public SpookySchool(AreaDisplayPanel display) {
		System.out.println("Check display");

		this.display = display;
		this.loadAreas(); // Load maps
		// this.setDoors(); //Sets up doors on the areas.
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
			this.players.add(newPlayer); // Add the player to the list of
											// players in the game.
			this.chatLog.add("Player Added to Game: " + name);

			// Set up the bundle for the new player.
			Bundle bundle = new Bundle(name);
			bundle.setNewArea(spawnRoom);
			bundle.setLog(this.chatLog);

			this.playerBundles.put(name, bundle);

			return true;
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
	 * @return true if player moves to a new tile or changes direction,
	 *         Otherwise false.
	 */
	public boolean movePlayer(String playerName, Direction direction) {
		socketClient.sendData("ping".getBytes());

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
			if (this.movePlayerNorth(player)) {
				this.playerBundles.get(playerName).addChange(playerName + " " + "NORTH");
				return true;
			}
			return false;
		// return this.movePlayerNorth(player);
		case SOUTH:
			if (this.movePlayerSouth(player)) {
				this.playerBundles.get(playerName).addChange(playerName + " " + "SOUTH");
				return true;
			}
			return false;
		// return this.movePlayerSouth(player);
		case EAST:
			if (this.movePlayerEast(player)) {
				this.playerBundles.get(playerName).addChange(playerName + " " + "EAST");
				return true;
			}
			return false;
		// return this.movePlayerEast(player);
		case WEST:
			if (this.movePlayerWest(player)) {
				this.playerBundles.get(playerName).addChange(playerName + " " + "WEST");
				return true;
			}
			return false;
		// return this.movePlayerWest(player);
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
	private boolean movePlayerNorth(Player player) {
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
	private boolean movePlayerSouth(Player player) {
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
	private boolean movePlayerEast(Player player) {
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
	private boolean movePlayerWest(Player player) {

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
	 * 
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
	 * Returns bundle relative to the playerName given.
	 * 
	 * @param playerName
	 *            of the player who's bundle is required
	 * @return the players bundle in the form of a byte array.
	 */
	public byte[] getBundle(String playerName) {

		Bundle playerBundle = this.playerBundles.get(playerName);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(playerBundle);
			out.flush();
			byte[] yourBytes = bos.toByteArray();

			playerBundle.clearBundle(); // Clear bundle as bundle has been sent
										// to client.

			return yourBytes;

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}

		throw new Error("Error: in creating bundle into byte array.");
	}

	// *****************C&S*****************
	private static boolean running;

	private static Thread thread;

	private static Server socketServer;

	private static Client socketClient;
	private static boolean serverSwitch;

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "GameThread");
		thread.start();
		serverSwitch = ClientServerFrame.serverOn;
		if (serverSwitch) {
			try {
				socketServer = new Server(this);
				socketServer.start();
				System.out.println("Server start£¡");
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}

		try {
			socketClient = new Client(this, InetAddress.getLocalHost().getHostAddress());
			socketClient.start();
			// this.addPlayer("player2");
			System.out.println("Client start£¡");

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public synchronized void stop() {
		running = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public synchronized void run() {
		while (running) {
			tick();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void tick() {
		display.updateDisplay();
		// System.out.println("tick function");
	}

	public Area findEmptySpawnRoomTest() {

		// Finds an unoccupied/un-owned spawn area and returns it.
		for (Entry<String, Area> entry : this.areas.entrySet()) {
			if (entry.getKey().contains("Spawn") || (entry.getValue().hasOwner())) {
				return entry.getValue();
			}
		}
		throw new Error("Error: Could not find an empty spawn room. This should not be possible.");
	}

	public boolean addPlayerTest(String name) {

		if (this.players.size() < 4) {
			Area spawnRoom = this.findEmptySpawnRoomTest();
			System.out.println(spawnRoom.toString() + "**");
			Player newPlayer = new Player(name, spawnRoom, this.defaultSpawnPosition);
			spawnRoom.setOwner(newPlayer); // Set player as the owner of the
											// spawn room.

			// Set the player as the occupant of the tile.
			FloorTile spawnTile = (FloorTile) spawnRoom.getTile(this.defaultSpawnPosition);

			assert spawnTile != null;

			spawnTile.setOccupant(newPlayer);
			this.players.add(newPlayer); // Add the player to the list of
											// players in the game.
			this.chatLog.add("Player Added to Game: " + name);

			// Set up the bundle for the new player.
			Bundle bundle = new Bundle(name);
			bundle.setNewArea(spawnRoom);
			bundle.setLog(this.chatLog);

			this.playerBundles.put(name, bundle);

			return true;
		}

		return false;
	}

}
