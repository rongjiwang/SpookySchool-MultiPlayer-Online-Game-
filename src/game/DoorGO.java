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

	private String sideA;
	private String sideB;

	private Position sideAPos;
	private Position sideBPos;



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

	public String getOtherSide(String currentSide) {

		if (currentSide.equals(sideA)) {
			return this.sideB;
		}
		return this.sideA;
	}


	public Position getOtherSidePos(String currentSide) {

		if (currentSide.equals(sideA)) {
			return this.sideBPos;
		}

		return this.sideAPos;
	}



	public void setSideA(String sideA) {
		this.sideA = sideA;
	}


	public void setSideB(String sideB) {
		this.sideB = sideB;
	}



	public void setSideAPos(Position sideAPos) {
		this.sideAPos = sideAPos;
	}



	public void setSideBPos(Position sideBPos) {
		this.sideBPos = sideBPos;
	}



	@Override
	public String getToken() {
		return this.token;
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
