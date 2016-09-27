package com.school.game;

/**
 * Used for game objects that take up more than one tile.
 * Can only be a marker for container and fixed objects.
 * @author Pritesh R. Patel
 *
 */
public class MarkerGO implements GameObject {

	private final GameObject baseGO;
	private final Position position;

	protected String token;

	public MarkerGO(GameObject baseGO, Position pos) {
		//Throw error if the base object that this tile is a marker for is not a fixed or container game object.
		if (!(baseGO instanceof FixedGO || baseGO instanceof ContainerGO)) {
			throw new Error("Error: Invalid marker base object.");
		}
		this.baseGO = baseGO;
		this.position = pos;
	}

	/**
	 * 
	 * @return the "base" game object that this marker represents.
	 */
	public GameObject getBaseGO() {
		return this.baseGO;
	}

	public String getToken() {
		return this.token;
	}

}
