package game;

public class FixedContainerGO implements GameObject {

	private static final long serialVersionUID = 118153527696427133L;
	private final String id;
	private final String token;
	private boolean open;
	private boolean locked;
	private final String keyID;
	private final int size; // For container size.
	private final Position position;

	private String description;

	public FixedContainerGO(String id, String token, boolean open, boolean locked, String keyID, int size,
			Position position) {
		this.id = id;
		this.token = token;
		this.open = open;
		this.locked = locked;
		this.keyID = keyID;
		this.size = size;
		this.position = position;
	}

	//TODO Need to add functionality here.


	/** GETTERS AND SETTERS **/

	@Override
	public String getToken() {
		return this.token;
	}


	@Override
	public String getId() {
		return id;
	}

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public void setCurrentPosition(Position position) {
		throw new Error("Cannot change positions of fixed container game objects!");
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(String desc) {
		this.description = desc;
	}
}
