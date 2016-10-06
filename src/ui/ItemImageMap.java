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
public class ItemImageMap {
	private static final String IMAGE_PATH = "itemimages/";
	private HashMap<String, Image> spriteMap;
	
	public ItemImageMap(){
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
		
		spriteMap.put("b0",loadImage("box.png"));
		spriteMap.put("c0",loadImage("coin.png"));
		spriteMap.put("k0",loadImage("key.png"));
		
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
