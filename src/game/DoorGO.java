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
	private final Position position;

	//FIXME Should these be transient???
	private String otherSide; //Name of the area on the otherside
	private transient Position otherSidePos; //Position on the other side.

	public DoorGO(String id, String token, boolean open, boolean locked, String keyID, Position position) {
		this.id = id;
		this.token = token;
		this.open = open;
		this.locked = locked;
		this.keyID = keyID;
		this.position = position;
	}


	//TODO Need to add functionality here.



	/** GETTERS AND SETTERS **/

	@Override
	public String getToken() {
		return this.token;
	}


	public String getOtherSide() {
		return otherSide;
	}


	public void setOtherSide(String otherSide) {
		this.otherSide = otherSide;
	}


	public Position getOtherSidePos() {
		return otherSidePos;
	}


	public void setOtherSidePos(Position otherSidePos) {
		this.otherSidePos = otherSidePos;
	}

	public boolean isOpen() {
		return this.open;

	}

	public Position getPosition() {
		return position;
	}


	public String getId() {
		return id;
	}

}
