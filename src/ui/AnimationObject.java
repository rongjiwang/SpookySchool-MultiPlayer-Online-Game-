package ui;

import game.GameObject;

public class AnimationObject {

	private AreaDisplayPanel adp;

	private GameObject gameObj;

	private int startX;
	private int startY;
	private int aimX;
	private int aimY;

	private String direction;

	int currentBuff = 0;

	int currentToken = 0;

	public AnimationObject(AreaDisplayPanel adp, GameObject gameObj, String direction, int startX, int startY, int aimX,
			int aimY) {
		this.adp = adp;
		this.gameObj = gameObj;
		this.direction = direction;

		this.startX = startX;
		this.startY = startY;
		this.aimX = aimX;
		this.aimY = aimY;

		System.out.println("New Animation Object: Starting at x: " + this.startX + " y: " + this.startY
				+ " Finishing: x: " + this.aimX + " y: " + this.aimY);

	}

	public GameObject getGameObj() {
		return gameObj;
	}


	public String getDirection() {
		return direction;
	}


	public String getNextImgToken() {

		currentToken++;

		if (currentToken > 3) {
			currentToken = 0;
		}

		String token = gameObj.getToken().substring(0, gameObj.getToken().length() - 1) + String.valueOf(currentToken);
		System.out.println("returning: " + token);
		return token;
	}

	public void changeBuff() {

		this.currentBuff++;

		if (this.direction.equals("NORTH") && this.currentBuff < 25) {
			//this.currentBuff += 6;
			adp.mainPlayerYBuff--;
		} else if (this.direction.equals("SOUTH") & this.currentBuff < 25) {
			//this.currentBuff += 6;
			adp.mainPlayerYBuff++;
		} else if (this.direction.equals("EAST") && this.currentBuff < 32) {
			//this.currentBuff += 8;
			adp.mainPlayerXBuff++;
		} else if (this.direction.equals("WEST") && this.currentBuff < 32) {
			//his.currentBuff += 8;
			adp.mainPlayerXBuff--;
		}
	}

	public boolean animationComplete() {

		if ((this.direction.equals("NORTH") || this.direction.equals("SOUTH")) && this.currentBuff >= 25) {
			return true;
		} else if ((this.direction.equals("EAST") || this.direction.equals("WEST")) && this.currentBuff >= 32) {
			return true;
		}

		return false;
	}

}
