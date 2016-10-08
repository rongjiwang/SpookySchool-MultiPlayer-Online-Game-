package game;

public class ContainerGO extends InventoryGO {

	private final int size;
	private int sizeRemaining;


	public ContainerGO(String name, String id, String token, int size, String areaName, Position pos,
			String description) {

		super(name, id, token, size, areaName, pos, description);

		//Initially sizes are the same.
		this.size = size;
		this.sizeRemaining = size;

	}

}
