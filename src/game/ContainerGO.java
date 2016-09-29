package game;

public class ContainerGO implements GameObject {

	private static final long serialVersionUID = 118153527696427133L;
	private final String id;
	private final String token;
	private boolean open;
	private boolean locked;
	private final String keyID;
	private final int size; // For container size.
	private final Position position;

	public ContainerGO(String id, String token, boolean open, boolean locked, String keyID, int size,
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

	public String getToken() {
		return this.token;
	}

	
	public String getId() {
		return id;
	}

	public Position getPosition() {
		return position;
	}
}
