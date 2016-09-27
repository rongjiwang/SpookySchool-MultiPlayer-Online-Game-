package com.school.game;

/**
 * Represents a "fixed" game object. A game object that does not move and is not interactive.
 * @author Pritesh R. Patel
 *
 */
public class FixedGO implements GameObject {

	private final String id;
	private final Position position;

	protected String token;

	public FixedGO(String id, String token, Position position) {
		this.id = id;
		this.token = token;
		this.position = position;
	}

	public String getToken() {
		return this.token;
	}

	//TODO Need to add functionality here.


}
