package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A bundle holds the different bits of information that needs to be passed to the respective client.
 * @author Pritesh R. Patel
 *
 */
public class Bundle implements Serializable {


	private static final long serialVersionUID = 4395316495298730037L;
	private String playerName;
	private String message; //Message to be displayed onto the game screen.
	private Player playerObj;
	private Area newArea;
	//private List<String> gameObjectChanges = new ArrayList<String>();
	private List<String> chatLogChanges = new ArrayList<String>();

	public Bundle(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * Clear relevant fields of this bundle. THis should be called once the bundle has been sent to the respective client.
	 */
	public void clearBundle() {
		this.playerObj = null;
		this.message = null;
		//this.gameObjectChanges = new ArrayList<String>();
		//this.newArea = null; //FIXME **COMMENT OUT FOR TESTING 2D!!**

		this.chatLogChanges = new ArrayList<String>(); //FIXME keep commented out for now. Want to send entire chat log over network (for now!)
	}

	/** GETTERS AND SETTERS **/
	public String getPlayerName() {
		return playerName;
	}

	public Area getNewArea() {
		return newArea;
	}

	public void setNewArea(Area newArea) {
		this.newArea = newArea;
	}

	/*
	public List<String> getGameObjectChanges() {
		return gameObjectChanges;
	}
	
	public void addGameObjectChange(String change) {
		this.gameObjectChanges.add(change);
	}
	*/

	public List<String> getLog() {
		return this.chatLogChanges;
	}

	public void addToChatLog(String logItem) {
		this.chatLogChanges.add(logItem);
	}

	public void setChatLog(List<String> log) {
		this.chatLogChanges = log;
	}

	public Player getPlayerObj() {
		return playerObj;
	}

	public void setPlayerObj(Player playerObj) {
		this.playerObj = playerObj;
	}



}
