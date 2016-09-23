package com.school.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class represents an area/room in the game. Each area/room is represented by a 2d array of tiles.
 * @author Pritesh R. Patel
 *
 */
public class Area {

	public int width;
	public int height;

	private String areaName;
	private Tile[][] area;
	private Player owner;

	public Area(String areaName, String areaFile) {

		this.areaName = areaName;

		//To make it compile.
		this.width = -1;
		this.height = -1;

		Scanner scan = null;
		Scanner gameObjScanner = null;

		try {
			scan = new Scanner(new File("src/areas/" + areaFile));


			//Scan width and height.
			this.width = scan.nextInt();
			this.height = scan.nextInt();


			this.area = new Tile[height][width];

			int xPos = 0;
			int yPos = 0;

			//Read the areaFile into the 2D area array.
			while (yPos < this.height) {
				String tileString = scan.next();

				this.area[yPos][xPos] = createTile(tileString, new Position(xPos, yPos)); //Create appropriate tile and add it to the area 2d array.
				xPos++;
				if (xPos >= this.width) {
					xPos = 0;
					yPos++;
				}
			}

			//Read in the gameObjects within the room. E.g. Tables, bed, items on the wall etc.
			while (scan.hasNextLine()) {
				String line = scan.nextLine();

				//Skip any blank lines.
				if (line.equals("")) {
					continue;
				}

				//System.out.println("Reading Game Object Line: " + line);

				gameObjScanner = new Scanner(line);

				//Scan the first three tokens of 
				String objType = gameObjScanner.next();
				String id = gameObjScanner.next();
				String token = gameObjScanner.next();

				//Add the appropriate object onto this tile and add marker objects on tiles where applicable.
				if (objType.equals("DOOR")) {
					boolean open = gameObjScanner.next().equals("open");
					boolean locked = gameObjScanner.next().equals("locked");
					String keyID = gameObjScanner.next();
					keyID = keyID.equals("null") ? null : keyID;
					Position pos = (new Position(gameObjScanner.nextInt(), gameObjScanner.nextInt()));
					GameObject gameObject = new DoorGO(id, token, open, locked, keyID, pos);

					if (!(this.area[pos.getPosY()][pos.getPosX()] instanceof WallTile)) {
						throw new Error("Error: Can only add doors onto walls/wall tiles.");
					}

					this.area[pos.getPosY()][pos.getPosX()].setOccupant(gameObject);

				} else if (objType.equals("CONTAINER")) {
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

				} else if (objType.equals("FIXED")) {
					Position pos = new Position(gameObjScanner.nextInt(), gameObjScanner.nextInt());
					GameObject gameObject = new FixedGO(id, token, pos);

					if (!(this.area[pos.getPosY()][pos.getPosX()] instanceof FloorTile)) {
						throw new Error("Error: Can only add fixed items to floor tiles.");
					}

					this.area[pos.getPosY()][pos.getPosX()].setOccupant(gameObject);

					//Set up the rest of the marker tiles that make up this game object.
					while (gameObjScanner.hasNext()) {
						Position markerPos = new Position(gameObjScanner.nextInt(), gameObjScanner.nextInt());
						GameObject markerObj = new MarkerGO(gameObject, markerPos); //Link marker to original game object.
						this.area[markerPos.getPosY()][markerPos.getPosX()].setOccupant(markerObj);
					}
				}
			}

		} catch (

		FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates and returns the appropriate type of tile given a token(tileString).
	 * @param tileString A token(string) that corresponds to a tile type.
	 * @param pos Position of this tile.
	 * @return Tile Tile that is created according to the given token(tileString).
	 */
	private Tile createTile(String tileString, Position pos) {

		if (tileString.equals("n")) {
			return null;
		} else if (tileString.equals("c0")) { //NOTE: ADD More token types here to add different types of floor tiles.
			return new FloorTile(pos, tileString);
		}

		return new WallTile(pos, tileString);
	}


	/**
	 * 
	 * @return the name of this area.
	 */
	public String getAreaName() {
		return this.areaName;
	}

	/**
	 * Returns the tile at given position.
	 * @param position of the tile
	 * @return Tile at the given position
	 */
	public Tile getTile(Position position) {
		return this.area[position.getPosY()][position.getPosX()];
	}

	/**
	 * 
	 * @return The 2D array that represents the area.
	 */
	public Tile[][] getArea() {
		return this.area;
	}


	/**
	 * Sets the given player as the owner of the spawn room. Throws error if you try to set an owner for a non-spawn room.
	 * @param owner
	 */
	public void setOwner(Player owner) {
		//Throw error if trying to set owner for a non-spawn room.
		if (!this.areaName.contains("Spawn")) {
			throw new Error("Can only add an owner to a spawn location!");
		}
		this.owner = owner;
	}


	/**
	 * Returns true if this area has an owner. This is used only for the spawn room.
	 * @return true if this area has an owner and false otherwise.
	 */
	public boolean hasOwner() {
		return this.owner != null;
	}

}
