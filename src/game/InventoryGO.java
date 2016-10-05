package game;

public class InventoryGO implements GameObject {

	private static final long serialVersionUID = -1272721150110182719L;

	private final String id;
	private final String token;
	private String description = "Seems to be some kind of item you can pick up. Try pressing 'z'.";

	private Position position;

	public InventoryGO(String id, String token) {
		this.id = id;
		this.token = token;
	}

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

	@Override
	//FIXME: NEED TO SET INVENTORY POSITION PREPERLY.
	public void setCurrentPosition(Position position) {
		// TODO Auto-generated method stub

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
