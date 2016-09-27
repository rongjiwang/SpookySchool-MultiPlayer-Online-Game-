package com.school.game;

/**
 * 
 * Represents a door game object in the game.
 * @author Pritesh R. Patel
 *
 */
public class DoorGO implements GameObject {

	private final String id;
	private boolean open;
	private boolean locked;
	private final String keyID;
	private final Position position;

	protected String token;

	public DoorGO(String id, String token, boolean open, boolean locked, String keyID, Position position) {
		this.id = id;
		this.token = token;
		this.open = open;
		this.locked = locked;
		this.keyID = keyID;
		this.position = position;
	}

	public String getToken() {
		return this.token;
	}

	//TODO Need to add functionality here.



}
