package ui;

import game.GameObject;
import game.Position;

public class AnimationObject {

	private AreaDisplayPanel adp;
	private GameObject gameObj;

	private String direction;
	private int startX;
	private int startY;
	private int aimX;


	private int aimY;

	private boolean mainPlayer;

	private String imgToken;
	private int current = 0;
	private int tokenCurrent = 0;

	public AnimationObject(AreaDisplayPanel adp, GameObject gameObj, boolean mainPlayer, String direction, int startX,
			int startY, int aimX, int aimY) {

		this.adp = adp;
		this.gameObj = gameObj;
		this.mainPlayer = mainPlayer;
		this.direction = direction;
		this.startX = startX;
		this.startY = startY;
		this.aimX = aimX;
		this.aimY = aimY;

		System.out.println("New Animation: Starting at x: " + this.startX + " y: " + this.startY + " Finishing: x: "
				+ this.aimX + " y: " + this.aimY + " main player: " + this.mainPlayer);
	}


	public Position getPosition() {
		if (this.direction.equals("NORTH")) {
			return new Position(adp.getRenderOffSetX() + this.aimX * adp.tileWidth,
					adp.getRenderOffSetY() + this.aimY * adp.tileHeight + (25 - current));

		} else if (this.direction.equals("SOUTH")) {
			return new Position(adp.getRenderOffSetX() + this.aimX * adp.tileWidth,
					adp.getRenderOffSetY() + this.aimY * adp.tileHeight - (25 - current));

		} else if (this.direction.equals("EAST")) {
			return new Position(adp.getRenderOffSetX() + this.aimX * adp.tileWidth - (32 - current),
					adp.getRenderOffSetY() + this.aimY * adp.tileHeight);
		}

		return new Position(adp.getRenderOffSetX() + this.aimX * adp.tileWidth + (32 - current),
				adp.getRenderOffSetY() + this.aimY * adp.tileHeight);

	}

	public void incrementCurrent() {
		this.current++;
	}

	public void changeBuffs() {
		//If main player, change the main player buff
		if (this.mainPlayer) {
			if (this.direction.equals("NORTH")) {
				adp.mainPlayerYBuff -= 1;
			} else if (this.direction.equals("SOUTH")) {
				adp.mainPlayerYBuff += 1;
			} else if (this.direction.equals("EAST")) {
				adp.mainPlayerXBuff += 1;
			} else {
				adp.mainPlayerXBuff -= 1;
			}
		}
	}

	public String getNextImgToken() {

		int nextToken = 0;

		if (this.direction.equals("NORTH") || this.direction.equals("SOUTH")) {
			if (this.current <= 6) {
				nextToken = 0;
			} else if (this.current <= 12) {
				nextToken = 1;
			} else if (this.current <= 18) {
				nextToken = 2;
			} else {
				nextToken = 3;
			}
		} else {
			if (this.current <= 8) {
				nextToken = 0;
			} else if (this.current <= 16) {
				nextToken = 1;
			} else if (this.current <= 24) {
				nextToken = 2;
			} else {
				nextToken = 3;
			}
		}

		String token = gameObj.getToken().substring(0, gameObj.getToken().length() - 1) + String.valueOf(nextToken);
		return token;
	}


	public boolean animationComplete() {
		if (this.direction.equals("NORTH") || this.direction.equals("SOUTH")) {
			return this.current == 25;
		}

		return this.current == 32;
	}

	public GameObject getGameObj() {
		return gameObj;
	}


	public boolean isMainPlayer() {
		return this.mainPlayer;
	}


	public int getStartX() {
		return this.startX;
	}

	public int getStartY() {
		return this.startY;
	}

	public void setAimX(int aimX) {
		this.aimX = aimX;
	}

	public void setAimY(int aimY) {
		this.aimY = aimY;
	}

}
