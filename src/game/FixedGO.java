package game;

/**
 * Represents a "fixed" game object. A game object that does not move and is not interactive. The position of this game object is also 
 * the base tile that is used when rendering in 3d.
 * @author Pritesh R. Patel
 *
 */
public class FixedGO implements GameObject {

	private static final long serialVersionUID = -7324582572175905569L;
	private final String id;
	private final String token;
	private final Position position;

	public FixedGO(String id, String token, Position position) {
		this.id = id;
		this.token = token;
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
		throw new Error("Canno't change positions of fixed game objects.");
	}
}
