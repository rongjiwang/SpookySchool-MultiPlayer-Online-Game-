package com.school.game;

import java.net.InetAddress;

/**
 * Represents a player in the Spooky School game and holds all information related to the player.
 * @author Pritesh R. Patel
 *
 */
public class Player implements GameObject {

	private final String playerName;
	private Area currentArea;
	private Position currentPosition;
	private Direction direction = Direction.NORTH;

	protected String token;
	private InetAddress address;
	private int port;


	public enum Direction {
		NORTH, SOUTH, EAST, WEST;
	}

	public Player(String playerName, Area currentArea, Position currentPosition) {
		this.playerName = playerName;
		this.setCurrentArea(currentArea);
		this.setCurrentPosition(currentPosition);
		this.token = "p0";
	}
	
	public Player(String name, InetAddress address, int port){
		this.playerName = name;
		this.address = address;
		this.port = port;
	}

	/** GETTERS AND SETTERS **/

	public String getPlayerName() {
		return playerName;
	}

	public Area getCurrentArea() {
		return currentArea;
	}

	public void setCurrentArea(Area currentArea) {
		this.currentArea = currentArea;
	}

	public Position getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(Position currentPosition) {
		this.currentPosition = currentPosition;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}


	public String getToken() {
		return this.token;
	}

}
