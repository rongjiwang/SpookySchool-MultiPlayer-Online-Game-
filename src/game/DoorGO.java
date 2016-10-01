package game;

/**
 * 
 * Represents a door game object in the game.
 * @author Pritesh R. Patel
 *
 */
public class DoorGO implements GameObject {

	private static final long serialVersionUID = 2625782518677987399L;
	private final String id;
	private final String token;
	private boolean open;
	private boolean locked;
	private final String keyID;

	private final String sideA;
	private final Position sideAPos;
	private final Position sideAEntryPos;

	private final String sideB;
	private final Position sideBPos;
	private final Position sideBEntryPos;


	public DoorGO(String id, String token, boolean open, boolean locked, String keyID, String sideA, Position sideAPos,
			Position sideAEntryPos, String sideB, Position sideBPos, Position sideBEntryPos) {
		this.id = id;
		this.token = token;
		this.open = open;
		this.locked = locked;
		this.keyID = keyID;

		this.sideA = sideA;
		this.sideAPos = sideAPos;
		this.sideAEntryPos = sideAEntryPos;
		this.sideB = sideB;
		this.sideBPos = sideBPos;
		this.sideBEntryPos = sideBEntryPos;


	}


	//TODO Need to add functionality here.


	/** GETTERS AND SETTERS **/

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getToken() {
		return this.token;
	}

	public boolean isOpen() {
		return this.open;
	}

	/**
	 * Return the name of the area on the other side of this door.
	 * @param currentSide the name of the area the player is currently in.
	 * @return the name of the are that is on the other side of this door.
	 */
	public String getOtherSide(String currentSide) {
		if (currentSide.equals(sideA)) {
			return this.sideB;
		}
		return this.sideA;
	}


	/**
	 * Returns the position of tile that the player lands on (on the other side) when they pass through this door.
	 * @param currentSide the name of the area the player is currently in.
	 * @return the position of the tile that the player lands on when they apss through the door.
	 */
	public Position getOtherSideEntryPos(String currentSide) {
		if (currentSide.equals(sideA)) {
			return this.sideBEntryPos;
		}
		return this.sideAEntryPos;
	}


	/**
	 * Return the position of the door in a given area.
	 * @param areaName name of the area in which the door is.
	 * @return Position of the door in the given area.
	 */
	public Position getPosition(String areaName) {
		if (areaName.equals(sideA)) {
			return this.sideAPos;
		}
		return sideBPos;
	}


	@Override
	public Position getPosition() {
		throw new Error("This method is not avaliavle for door objects! Use getPosition(String areaName) instead!");
	}

}
