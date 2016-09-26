package com.school.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A bundle is holds the different bits of information that needs to be passed to the 
 * @author Pritesh R. Patel
 *
 */
public class Bundle implements Serializable {

	private String playerName;
	private Area newArea;
	private List<String> gameObjectChanges = new ArrayList<String>();
	private List<String> chatLogChanges = new ArrayList<String>();

	public Bundle(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * Clear relevant fields of this bundle. This should be called once the bundle has been sent to the respective client.
	 */
	public void clearBundle() {
		this.newArea = null;
		this.gameObjectChanges = null;
		this.chatLogChanges = null;
	}


	/** GETTERS AND SETTERS **/

	public String getPlayerID() {
		return playerName;
	}

	public Area getNewArea() {
		return newArea;
	}

	public void setNewArea(Area newArea) {
		this.newArea = newArea;
	}

	public List<String> getChanges() {
		return gameObjectChanges;
	}

	public void addChange(String change) {
		this.gameObjectChanges.add(change);
	}

	public List<String> getLog() {
		return chatLogChanges;
	}

	public void addToLog(String logItem) {
		this.chatLogChanges.add(logItem);
	}

	public void setLog(List<String> log) {
		this.chatLogChanges = log;
	}
}
