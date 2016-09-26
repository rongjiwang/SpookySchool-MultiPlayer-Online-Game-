package com.school.game;

public class ContainerGO implements GameObject {

	private final String id;
	private boolean open;
	private boolean locked;
	private final String keyID;
	private final int size; // For container size.
	private final Position position;
	protected String token;

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

	public String getToken() {
		return this.token;
	}

	//TODO Need to add functionality here.

}
