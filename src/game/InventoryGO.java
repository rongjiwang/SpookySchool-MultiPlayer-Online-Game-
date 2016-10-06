package game;

public class InventoryGO implements GameObject {

	private static final long serialVersionUID = -1272721150110182719L;

	private final String name;
	private final String id;
	private final String token;
	private final int size;
	private Position position;

	private String description = "Seems to be some kind of item you can pick up. Try pressing 'z'.";

	public InventoryGO(String name, String id, String token, int size, Position pos, String description) {
		this.name = name;
		this.id = id;
		this.token = token;
		this.size = size;
		this.position = pos;
		this.description = description;
	}


	/** GETTERS AND SETTERS **/

	public String getName() {
		return name;
	}

	@Override
	public String getToken() {
		return this.token;
	}

	@Override
	public String getId() {
		return this.id;
	}

	public int getSize() {
		return size;
	}

	@Override
	public Position getPosition() {
		return this.position;
	}

	@Override
	//FIXME: NEED TO SET INVENTORY POSITION PREPERLY.
	public void setCurrentPosition(Position position) {
		this.position = position;
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
