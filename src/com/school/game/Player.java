package com.school.game;

/**
 * Represents a player in the Spooky School game and holds all information related to the player.
 * @author Pritesh R. Patel
 *
 */
public class Player extends GameObject {

	private final String playerName;
	private Area currentArea;
	private Position currentPosition;
	private Direction direction = Direction.NORTH;
	

	public enum Direction {
		NORTH, SOUTH, EAST, WEST;
	}

	public Player(String playerName, Area currentArea, Position currentPosition) {
		this.playerName = playerName;
		this.setCurrentArea(currentArea);
		this.setCurrentPosition(currentPosition);
		this.token = "p0";
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
	

}
