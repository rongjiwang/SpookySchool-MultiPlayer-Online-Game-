package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;


/**
 * This class contains all of the logic Spooky School game. This class controls game state and provides various helper methods
 * for the server.
 * @author Pritesh R. Patel
 *
 */
public class SpookySchool {

	private final Position defaultSpawnPosition = new Position(5, 8); //Default position that a player spawns in, in a spawn room.

	//Should make xml implementation easier?!
	private String areasFileLoc = "src/areas/areas.txt";
	private String doorsFileLoc = "src/areas/game_objects/doors.txt";
	private String movableObjectsFileLoc = "src/areas/game_objects/movable_objects.txt";

	private Map<String, Area> areas = new HashMap<String, Area>();
	private List<Player> players = new ArrayList<Player>();
	private Map<String, Bundle> playerBundles = new HashMap<String, Bundle>();

	public SpookySchool() {
		this.loadAreas(); //Load maps
		this.setDoors(); //Sets up doors on the areas.
		this.loadRemainingGameObjects(); //Load the remaining game objects.

		System.out.println("Game Loaded.");
	}


	/**
	 * Load all of the "areas" of the game into the list of areas.
	 */
	public void loadAreas() {
		Scanner scan;
		try {
			scan = new Scanner(new File(areasFileLoc));
			while (scan.hasNextLine()) {
				String areaName = scan.next();
				String fileName = scan.next();
				this.areas.put(areaName, new Area(areaName, fileName));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Sets up the doors in the areas.
	 */
	public void setDoors() {
		Scanner scan;

		try {
			scan = new Scanner(new File(doorsFileLoc));
			while (scan.hasNextLine()) {

				//Scan Door Specific information
				String doorID = scan.next();
				boolean open = scan.next().equals("open");
				boolean locked = scan.next().equals("locked");
				String keyID = scan.next();
				keyID = keyID.equals("null") ? null : keyID;

				//Scan information about rooms on either side.
				String sideA = scan.next();
				String tokenA = scan.next();
				Position sideADoorPos = new Position(scan.nextInt(), scan.nextInt());
				Position sideAEntryPos = new Position(scan.nextInt(), scan.nextInt());

				String sideB = scan.next();
				String tokenB = scan.next();
				Position sideBDoorPos = new Position(scan.nextInt(), scan.nextInt());
				Position sideBEntryPos = new Position(scan.nextInt(), scan.nextInt());

				//Create the door object.
				DoorGO door = new DoorGO(doorID, open, locked, keyID, sideA, tokenA, sideADoorPos, sideAEntryPos, sideB,
						tokenB, sideBDoorPos, sideBEntryPos);

				//Get the area objects of both sides.
				Area areaA = this.areas.get(sideA);
				Area areaB = this.areas.get(sideB);

				//Add the door objects onto the appropriate tiles in their area...
				areaA.getTile(sideADoorPos).setOccupant(door);
				areaB.getTile(sideBDoorPos).setOccupant(door);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load all game objects that are not fixed and not door objects.
	 */
	public void loadRemainingGameObjects() {

		Scanner scan;

		//Scan and load Movable objects
		try {
			scan = new Scanner(new File(movableObjectsFileLoc));
			while (scan.hasNextLine()) {

				//Scan movable object information.
				String id = scan.next();
				String token = scan.next();
				String areaName = scan.next();
				Position objPosition = new Position(scan.nextInt(), scan.nextInt());

				//Create the movable object using the scanned information.
				MovableGO movableGO = new MovableGO(id, token, areaName, objPosition);

				Area area = this.areas.get(areaName);
				Tile tile = area.getTile(objPosition); //Get the tile of 

				tile.setOccupant(movableGO);
			}

		} catch (FileNotFoundException e) {
			throw new Error(e.getMessage());
		}


		/*
		if (objType.equals("CONTAINER")) {
		
			boolean open = gameObjScanner.next().equals("open");
			boolean locked = gameObjScanner.next().equals("locked");
			String keyID = gameObjScanner.next();
			keyID = keyID.equals("null") ? null : keyID;
			int size = gameObjScanner.nextInt();
			Position pos = (new Position(gameObjScanner.nextInt(), gameObjScanner.nextInt()));
			GameObject gameObject = new ContainerGO(id, token, open, locked, keyID, size, pos);
		
			if (!(this.area[pos.getPosY()][pos.getPosX()] instanceof FloorTile)) {
				throw new Error("Error: Can only add containers to floor tiles.");
			}
		
			this.area[pos.getPosY()][pos.getPosX()].setOccupant(gameObject);
		
			//Set up the rest of the marker tiles that make up this game object.
			while (gameObjScanner.hasNext()) {
				Position markerPos = new Position(gameObjScanner.nextInt(), gameObjScanner.nextInt());
				GameObject markerObj = new MarkerGO(gameObject, markerPos); //Link marker to original game object.
				this.area[markerPos.getPosY()][markerPos.getPosX()].setOccupant(markerObj);
			}
		}
		*/
	}


	/**
	 * Adds player to the game if the game is not full and player with this name does not already exist.
	 * @param name of the player being added to the game.
	 * @return true if player successfully added, otherwise false.
	 */
	public synchronized boolean addPlayer(String name) {

		if (this.players.size() < 8 && this.getPlayer(name) == null) {
			Area spawnRoom = this.findEmptySpawnRoom();
			Player newPlayer = new Player(name, spawnRoom, this.defaultSpawnPosition);
			spawnRoom.setOwner(newPlayer); //Set player as the owner of the spawn room.

			//Set the player as the occupant of the tile.
			FloorTile spawnTile = (FloorTile) spawnRoom.getTile(this.defaultSpawnPosition);

			assert spawnTile != null;

			spawnTile.setOccupant(newPlayer);
			this.players.add(newPlayer); //Add the player to the list of players in the game.
			this.addChatLogItemToAllBundles(name + " entered the game.");

			//Set up the bundle for the new player.
			Bundle bundle = new Bundle(name);
			bundle.setPlayerObj(newPlayer);

			//Add game object change to all bundles.
			this.addLogToAllBundles(name + " appear " + newPlayer.getCurrentArea().getAreaName() + " "
					+ newPlayer.getPosition().getPosX() + " " + newPlayer.getPosition().getPosY() + " "
					+ newPlayer.getToken());

			bundle.setNewArea(spawnRoom); //FIXME **UNCOMMENT FOR TESTING 2D RENDERING**

			this.playerBundles.put(name, bundle);

			return true;
		}
		
		return false;
	}


	/**
	 * Remove player from the game
	 * @param name of the player to remove from the game
	 * FIXME Ensure everything relevant to the disconnecting player is removed!
	 */
	public synchronized void removePlayer(String name) {

		//Remove player as their spawn area's owner
		if (this.getPlayer(name).getCurrentArea().getAreaName().contains("Spawn")) {
			this.getPlayer(name).getCurrentArea().setOwner(null); //Set the players spawn room owner as null
		}

		this.getPlayer(name).getCurrentArea().getTile(this.getPlayer(name).getCurrentPosition()).removeOccupant(); //Remove player from the tile

		this.addLogToAllBundles(
				name + " disappear " + this.getPlayer(name).getCurrentArea().getAreaName() + " null null null"); //Used to make this GO disappear in game render panel

		this.players.remove(this.getPlayer(name)); //Remove the player from this game by removing them from players list.
		this.playerBundles.remove(name); //Remove this player's bundle.

		//Add player disconnection information to the chatlog
		this.addChatLogItemToAllBundles(name + " has left the game.");
	}


	/**
	 * Finds and returns a spawn area that is currently not owned by a player.
	 * @return a spawn area that is currently not owned by a player.
	 */
	public Area findEmptySpawnRoom() {
		//Finds an unoccupied/un-owned spawn area and returns it.
		for (Entry<String, Area> entry : this.areas.entrySet()) {
			if (entry.getKey().contains("Spawn") && (!entry.getValue().hasOwner())) {
				return entry.getValue();
			}
		}

		throw new Error("Error: Could not find an empty spawn room. This should not be possible.");
	}


	/**
	 * This is called when a player presses the action button. This method makes any changes that are required to the game state and
	 * adds changes to game bundles if and when required.
	 */
	public synchronized void processAction(String playerName) {
		Player player = this.getPlayer(playerName);
	}


	/**
	 * Moves player in a given direction if possible.
	 * @param playerName the name of the player to move.
	 * @param direction the direction the player needs to move into.
	 * @return true if player moves to a new tile or changes direction.. Otherwise false.
	 */
	public synchronized boolean movePlayer(String playerName, String direction) {

		Player player = this.getPlayer(playerName);
		//Ensure that the player we are trying to move exists.
		if (player == null) {
			throw new Error("Player (" + playerName + ") you are trying to move does not exist in game.");
		}

		//If player is facing a different direction than the direction given, make the player face the given direction.
		if (!player.getDirection().equals(direction)) {
			//this.getBundle(playerName).addGameObjectChange(playerName + " " + "direction " + direction.toString());
			this.addLogToAllBundles(playerName + " " + "direction " + direction.toString() + " null null null");
			player.setDirection(direction);
			return true;
		}

		//Player is facing the correct direction. attempt to move player in the given direction.
		switch (direction) {
		case "NORTH":
			return this.movePlayerNorth(player);
		case "SOUTH":
			return this.movePlayerSouth(player);
		case "EAST":
			return this.movePlayerEast(player);
		case "WEST":
			return this.movePlayerWest(player);
		}

		throw new Error("ERROR in movePlayer() method.");
	}

	/**
	 * Attempts to move the player NORTH by one tile.
	 * This method should be used once the player is facing the correct direction.
	 * @param player that is to be moved
	 * @return true if movement to the north is successful, otherwise false.
	 */
	private boolean movePlayerNorth(Player player) {
		int posX = player.getCurrentPosition().getPosX();
		int potentialPosY = player.getCurrentPosition().getPosY() - 1;

		Tile potentialTile = null;

		//Check if potential new position is within the bounds of the array.
		if (potentialPosY >= 0) {
			potentialTile = player.getCurrentArea().getTile(new Position(posX, potentialPosY));
		} else {
			return false; //Not a valid move.
		}

		//If the potential tile is a floor tile and is not currently occupied, then move the player.
		if (potentialTile instanceof FloorTile && (!((FloorTile) potentialTile).isOccupied())) {
			((FloorTile) player.getCurrentArea().getTile(player.getCurrentPosition())).removeOccupant(); //Remove player from old tile
			this.moveGOToTile(player, potentialTile); //Move the player to the new tile.
			this.addLogToAllBundles(player.getPlayerName() + " " + "move NORTH null null null"); //Add GO change to all bundles.
			return true; //Player movement complete.
		}

		//If the potential tile has a movable object, then attempt to push it.
		if (potentialTile instanceof FloorTile && potentialTile.getOccupant() instanceof MovableGO) {

			MovableGO movableGO = (MovableGO) potentialTile.getOccupant();
			int movableX = movableGO.getPosition().getPosX();
			int potentialMovableY = movableGO.getPosition().getPosY() - 1;

			Tile potentialMovableTile = null;

			//Check if potential new position of the movable object is within the bounds of the array.
			if (potentialMovableY >= 0) {
				potentialMovableTile = this.areas.get(movableGO.getAreaName())
						.getTile(new Position(movableX, potentialMovableY));

			} else {
				return false; //Not a valid move.
			}

			//If movable go can be pushed, then move the player and the movable object.
			if (potentialMovableTile instanceof FloorTile && (!((FloorTile) potentialMovableTile).isOccupied())) {
				((FloorTile) player.getCurrentArea().getTile(player.getCurrentPosition())).removeOccupant(); //Remove player from old tile
				this.areas.get(movableGO.getAreaName()).getTile(movableGO.getPosition()).removeOccupant(); //Remove movable tile from the old tile.
				this.moveGOToTile(player, potentialTile); //Move the player to the new tile.
				this.addLogToAllBundles(player.getPlayerName() + " " + "move NORTH null null null");
				this.moveGOToTile(movableGO, potentialMovableTile); //Move the player to the new tile.
				this.addLogToAllBundles(movableGO.getId() + " " + "move NORTH null null null");
				return true;
			}

			return false; //Movable tile cannot be pushed.
		}

		return processDoorMovement(potentialTile, player); //Return true if there is a door movement, else false.

	}

	/**
	 * Attempts to move the player SOUTH by one tile.
	 * This method should be called once the player is facing the correct direction.
	 * @param player that is to be moved
	 * @return true if successful, otherwise false.
	 */
	private boolean movePlayerSouth(Player player) {
		int posX = player.getCurrentPosition().getPosX();
		int potentialPosY = player.getCurrentPosition().getPosY() + 1;

		Tile potentialTile = null;

		//Check if potential new position is within the bounds of the array.
		if (potentialPosY < player.getCurrentArea().height) {
			potentialTile = player.getCurrentArea().getTile(new Position(posX, potentialPosY));
		} else {
			return false; //Not a valid move.
		}

		//If the potential tile is a floor tile and is not currently occupied, then move the player.
		if (potentialTile instanceof FloorTile && (!((FloorTile) potentialTile).isOccupied())) {
			((FloorTile) player.getCurrentArea().getTile(player.getCurrentPosition())).removeOccupant(); //Remove player from old tile
			this.moveGOToTile(player, potentialTile); //Move the player to the new tile.
			this.addLogToAllBundles(player.getPlayerName() + " " + "move SOUTH null null null"); //Add GO change to all bundles.
			return true; //Player movement complete.
		}

		//If the potential tile has a movable object, then attempt to push it.
		if (potentialTile instanceof FloorTile && potentialTile.getOccupant() instanceof MovableGO) {

			MovableGO movableGO = (MovableGO) potentialTile.getOccupant();
			int movableX = movableGO.getPosition().getPosX();
			int potentialMovableY = movableGO.getPosition().getPosY() + 1;

			Tile potentialMovableTile = null;

			//Check if potential new position of the movable object is within the bounds of the array.
			if (potentialMovableY < player.getCurrentArea().height) {
				potentialMovableTile = this.areas.get(movableGO.getAreaName())
						.getTile(new Position(movableX, potentialMovableY));

			} else {
				return false; //Not a valid move.
			}

			//If movable go can be pushed, then move the player and the movable object.
			if (potentialMovableTile instanceof FloorTile && (!((FloorTile) potentialMovableTile).isOccupied())) {
				((FloorTile) player.getCurrentArea().getTile(player.getCurrentPosition())).removeOccupant(); //Remove player from old tile
				this.areas.get(movableGO.getAreaName()).getTile(movableGO.getPosition()).removeOccupant(); //Remove movable tile from the old tile.
				this.moveGOToTile(player, potentialTile); //Move the player to the new tile.
				this.addLogToAllBundles(player.getPlayerName() + " " + "move SOUTH null null null");
				this.moveGOToTile(movableGO, potentialMovableTile); //Move the player to the new tile.
				this.addLogToAllBundles(movableGO.getId() + " " + "move SOUTH null null null");
				return true;
			}

			return false; //Movable tile cannot be pushed.
		}

		return processDoorMovement(potentialTile, player); //Return true if there is a door movement, else false.

	}

	/**
	 * Attempts to move the player EAST by one tile.
	 * This method should be used once the player is facing the correct direction.
	 * @param player that is to be moved
	 * @return true if movement to the east is successful, otherwise false.
	 */
	private boolean movePlayerEast(Player player) {
		int potentialPosX = player.getCurrentPosition().getPosX() + 1;
		int posY = player.getCurrentPosition().getPosY();

		Tile potentialTile = null;

		//Check if potential new position is within the bounds of the array.
		if (potentialPosX < player.getCurrentArea().width) {
			potentialTile = player.getCurrentArea().getTile(new Position(potentialPosX, posY));
		} else {
			return false; //Not a valid move.
		}

		//If the potential tile is a floor tile and is not currently occupied, then move the player.
		if (potentialTile instanceof FloorTile && (!((FloorTile) potentialTile).isOccupied())) {
			((FloorTile) player.getCurrentArea().getTile(player.getCurrentPosition())).removeOccupant(); //Remove player from old tile
			this.moveGOToTile(player, potentialTile); //Move the player to the new tile.
			this.addLogToAllBundles(player.getPlayerName() + " " + "move EAST null null null"); //Add GO change to all bundles.
			return true; //Player movement complete.
		}

		//If the potential tile has a movable object, then attempt to push it.
		if (potentialTile instanceof FloorTile && potentialTile.getOccupant() instanceof MovableGO) {

			MovableGO movableGO = (MovableGO) potentialTile.getOccupant();
			int potentialMovableX = movableGO.getPosition().getPosX() + 1;
			int movableY = movableGO.getPosition().getPosY();

			Tile potentialMovableTile = null;

			//Check if potential new position of the movable object is within the bounds of the array.
			if (potentialMovableX < player.getCurrentArea().width) {
				potentialMovableTile = this.areas.get(movableGO.getAreaName())
						.getTile(new Position(potentialMovableX, movableY));

			} else {
				return false; //Not a valid move.
			}

			//If movable go can be pushed, then move the player and the movable object.
			if (potentialMovableTile instanceof FloorTile && (!((FloorTile) potentialMovableTile).isOccupied())) {
				((FloorTile) player.getCurrentArea().getTile(player.getCurrentPosition())).removeOccupant(); //Remove player from old tile
				this.areas.get(movableGO.getAreaName()).getTile(movableGO.getPosition()).removeOccupant(); //Remove movable tile from the old tile.
				this.moveGOToTile(player, potentialTile); //Move the player to the new tile.
				this.addLogToAllBundles(player.getPlayerName() + " " + "move EAST null null null");
				this.moveGOToTile(movableGO, potentialMovableTile); //Move the player to the new tile.
				this.addLogToAllBundles(movableGO.getId() + " " + "move EAST null null null");
				return true;
			}

			return false; //Movable tile cannot be pushed.
		}

		return processDoorMovement(potentialTile, player); //Return true if there is a door movement, else false.
	}

	/**
	 * Attempts to move the player WEST by one tile.
	 * This method should be used once the player is facing the correct direction.
	 * @param player that is to be moved
	 * @return true if movement to the west is successful, otherwise false.
	 */
	private boolean movePlayerWest(Player player) {

		int potentialPosX = player.getCurrentPosition().getPosX() - 1;
		int posY = player.getCurrentPosition().getPosY();

		Tile potentialTile = null;

		//Check if potential new position is within the bounds of the array.
		if (potentialPosX >= 0) {
			potentialTile = player.getCurrentArea().getTile(new Position(potentialPosX, posY));
		} else {
			return false; //Not a valid Move
		}

		//If the potential tile is a floor tile and is not currently occupied, then move the player.
		if (potentialTile instanceof FloorTile && (!((FloorTile) potentialTile).isOccupied())) {
			((FloorTile) player.getCurrentArea().getTile(player.getCurrentPosition())).removeOccupant(); //Remove player from old tile
			this.moveGOToTile(player, potentialTile); //Move the player to the new tile.
			this.addLogToAllBundles(player.getPlayerName() + " " + "move WEST null null null"); //Add GO change to all bundles.

			return true; //Player movement complete.
		}

		//If the potential tile has a movable object, then attempt to push it.
		if (potentialTile instanceof FloorTile && potentialTile.getOccupant() instanceof MovableGO) {

			MovableGO movableGO = (MovableGO) potentialTile.getOccupant();
			int potentialMovableX = movableGO.getPosition().getPosX() - 1;
			int movableY = movableGO.getPosition().getPosY();

			Tile potentialMovableTile = null;

			//Check if potential new position of the movable object is within the bounds of the array.
			if (potentialMovableX >= 0) {
				potentialMovableTile = this.areas.get(movableGO.getAreaName())
						.getTile(new Position(potentialMovableX, movableY));

			} else {
				return false; //Not a valid move.
			}

			//If movable go can be pushed, then move the player and the movable object.
			if (potentialMovableTile instanceof FloorTile && (!((FloorTile) potentialMovableTile).isOccupied())) {
				((FloorTile) player.getCurrentArea().getTile(player.getCurrentPosition())).removeOccupant(); //Remove player from old tile
				this.areas.get(movableGO.getAreaName()).getTile(movableGO.getPosition()).removeOccupant(); //Remove movable tile from the old tile.
				this.moveGOToTile(player, potentialTile); //Move the player to the new tile.
				this.addLogToAllBundles(player.getPlayerName() + " " + "move WEST null null null");
				this.moveGOToTile(movableGO, potentialMovableTile); //Move the player to the new tile.
				this.addLogToAllBundles(movableGO.getId() + " " + "move WEST null null null");
				return true;
			}

			return false; //Movable tile cannot be pushed.
		}

		return processDoorMovement(potentialTile, player); //Return true if there is a door movement, else false.

	}


	/**
	 * Move player to next room if they move on to a door.
	 * @param potentialTile the tile that the player has tried to move on to.
	 * @param player the player that has tried to move.
	 * @return true if player moves to a new room successfully and false otherwise.
	 */
	public boolean processDoorMovement(Tile potentialTile, Player player) {

		//If the potential tile is a wall tile and has a door game object on it, attempt to go through.
		//FIXME change this code to improve door functionality...
		if (potentialTile instanceof WallTile && potentialTile.getOccupant() instanceof DoorGO) {

			String playerName = player.getPlayerName();

			DoorGO door = (DoorGO) potentialTile.getOccupant();

			String currentSide = player.getCurrentArea().getAreaName();
			String otherSide = door.getOtherSide(currentSide);

			Area otherSideArea = this.areas.get(otherSide);

			Tile otherSideTile = otherSideArea.getTile(door.getOtherSideEntryPos(currentSide)); //The tile on the other side of the door.

			//If the door is open and the position on the other side is not occupied, then move player.
			if (door.isOpen() && !otherSideTile.isOccupied()) {

				player.getCurrentArea().getTile(player.getCurrentPosition()).removeOccupant(); //Remove player from this tile.
				this.addLogToAllBundles(
						playerName + " disappear " + player.getCurrentArea().getAreaName() + " null null null");

				player.setCurrentArea(this.areas.get(otherSide)); //Set the player's new area.

				this.getBundle(playerName).setPlayerObj(player); //Add the player object to the bundle since they've moved to a new room.

				this.getBundle(playerName).setNewArea(this.getPlayer(playerName).getCurrentArea()); //FIXME **UNCOMMENT FOR TESTING 2D RENDERING**

				this.moveGOToTile(player, otherSideTile); //Add player to the new tile.

				//Add GO change to all bundles.
				this.addLogToAllBundles(playerName + " appear " + player.getCurrentArea().getAreaName() + " "
						+ player.getCurrentPosition().getPosX() + " " + player.getCurrentPosition().getPosY() + " "
						+ player.getToken());

				//Add movement to new room to the log.
				this.addChatLogItemToAllBundles(
						playerName + "entered the following room " + otherSide.replace('_', ' '));

				return true;
			}
		}

		return false;
	}

	/**
	 * Move the game object to the given tile. Method moves game object to new tile and removes them from the old tile.
	 * Also sets game objects position to the new position.
	 * @param gameObj the game object that is to be moved.
	 * @param tile that the game object needs to be moved onto.
	 */
	public void moveGOToTile(GameObject gameObj, Tile tile) {
		FloorTile newTile = (FloorTile) tile;
		newTile.setOccupant(gameObj); //Add player to new tile.
		gameObj.setCurrentPosition(newTile.getPosition()); //Set the player's new position.
	}


	/**
	 * Returns the player associated with the given player name.
	 * @param playerName name of the player to check for.
	 * @return the Player object of the given name if one exists, otherwise return null.
	 */
	public Player getPlayer(String playerName) {
		for (int i = 0; i < this.players.size(); i++) {
			if (this.players.get(i).getPlayerName().equals(playerName)) {
				return this.players.get(i);
			}
		}
		return null; //Player with given name not found.
	}

	/**
	 * Returns the bundle of the given player name.
	 * @param playerName of the player we are getting the bundle for.
	 * @return bundle of the playerName given.
	 */
	public Bundle getBundle(String playerName) {
		return this.playerBundles.get(playerName);
	}

	/**
	 * Adds a change to the game object change log in all bundles.
	 */
	public synchronized void addLogToAllBundles(String change) {
		for (Bundle b : this.playerBundles.values()) {
			b.addGameObjectChange(change);
		}
	}


	/**
	 * Add chat string to all bundles so everyone can display the latest chat log.
	 * @param addition the item to add to the chat log
	 */
	public synchronized void addChatLogItemToAllBundles(String addition) {
		for (Bundle b : this.playerBundles.values()) {
			b.addToChatLog(addition);
		}
	}

}
