package ui;

import game.GameObject;

public class AnimationObject {

	private GameObject gameObj;

	private String imgToken;

	private int currentX;
	private int currentY;
	private int aimX;
	private int aimY;

	public AnimationObject(GameObject gameObj, int startX, int startY, int aimX, int aimY) {
		this.gameObj = gameObj;
		this.currentX = startX;
		this.currentY = startY;
		this.aimX = aimX;
		this.aimY = aimY;
	}

	/*
	private String getNextImgToken() {
		
	}
	*/

	/*
	private Position nextPosition() {
	
	}
	*/

	public boolean animationComplete() {
		return this.aimX == currentX && this.aimY == currentY;
	}


}
