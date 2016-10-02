package game;

public class MovableGO implements GameObject {

	private static final long serialVersionUID = 4547800855384957101L;

	private final String id;
	private final String token;

	private final Area area; //Movable objects cannot be taken out of their room/area.
	private Position position;

	public MovableGO(String id, String token, Area area, Position position) {
		this.id = id;
		this.token = token;
		this.area = area;
		this.position = position;
	}


	/** GETTERS AND SETTERS **/

	@Override
	public String getToken() {
		return this.token;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public Position getPosition() {
		return this.position;
	}

	public Area getArea() {
		return area;
	}

}
