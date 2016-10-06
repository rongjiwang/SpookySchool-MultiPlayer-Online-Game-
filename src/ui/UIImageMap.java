package ui;


import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Similar to SpriteMap, holds a map of all image objects 
 * 
 * @author Andy
 *
 */
public class UIImageMap {
	private static final String IMAGE_PATH = "UIimages/";
	private HashMap<String, Image> spriteMap;
	
	public UIImageMap(){
		loadMap();
	}
	
	/**
	 * Returns corresponding image object from given token string
	 * 
	 * @param - token
	 * @return - Image
	 */
	public Image getImage(String token){
		Image image = spriteMap.get(token);
		if(image == null){
			System.out.println("null image on input " + token);
		}
		return image;	
	}

	
	public void loadMap(){
		spriteMap = new HashMap<String, Image>();
		
		//borders
		spriteMap.put("bB",loadImage("bottomBorder.png"));
		spriteMap.put("lB",loadImage("leftBorder.png"));
		spriteMap.put("rB",loadImage("rightBorder.png"));
		spriteMap.put("tB",loadImage("topBorder.png"));
		
		//corner
		spriteMap.put("tL",loadImage("topLeft.png"));
		spriteMap.put("tR",loadImage("topRight.png"));
		spriteMap.put("bL",loadImage("bottomLeft.png"));
		spriteMap.put("bR",loadImage("bottomRight.png"));
		
		//highlight
		spriteMap.put("hi",loadImage("highlight.png"));
		
		//buttons
		spriteMap.put("ib",loadImage("info.png"));
		spriteMap.put("ibhi",loadImage("infohighlight.png"));
		spriteMap.put("pb",loadImage("players.png"));
		spriteMap.put("pbhi",loadImage("playershighlight.png"));
		spriteMap.put("qb",loadImage("quit.png"));
		spriteMap.put("qbhi",loadImage("quithighlight.png"));
		spriteMap.put("sb",loadImage("send.png"));
		spriteMap.put("sbhi",loadImage("sendhighlight.png"));
		
		//inv background
		spriteMap.put("invBack",loadImage("invBackground.png"));
		
		
	}

	/**
	 * Load an image from the file system, using a given filename.
	 * 
	 * @param filename
	 * @return
	 */
	public Image loadImage(String filename) {
		// using the URL means the image loads when stored
		// in a jar or expanded into individual files.
		java.net.URL imageURL = SpriteMap.class.getResource(IMAGE_PATH + filename);
		try {
			Image img = ImageIO.read(imageURL);
			return img;
		} catch (IOException e) {
			// we've encountered an error loading the image. There's not much we
			// can actually do at this point, except to abort the game.
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}

}