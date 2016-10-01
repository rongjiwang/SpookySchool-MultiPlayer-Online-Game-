package game;

/**
 * Represents a player in the Spooky School game and holds all information related to the player.
 * @author Pritesh R. Patel
 *
 */
public class Player implements GameObject {

	private static final long serialVersionUID = -347596131285383989L;
	private final String playerName;
	private Area currentArea;
	private Position currentPosition;

	private String direction = "NORTH";
	private String token;


	public Player(String playerName, Area currentArea, Position currentPosition) {
		this.playerName = playerName;
		this.setCurrentArea(currentArea);
		this.setCurrentPosition(currentPosition);
		this.token = "p2";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((playerName == null) ? 0 : playerName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (playerName == null) {
			if (other.playerName != null)
				return false;
		} else if (!playerName.equals(other.playerName))
			return false;
		return true;
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

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	@Override
	public String getToken() {
		return this.token;
	}


	@Override
	public String getId() {
		return "";
	}


	@Override
	public Position getPosition() {
		return currentPosition;
	}

}
