package com.school.ui;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;


/**
 * Holds a map that maps all of the image objects to there representing String 'type' e.g."w0"

 * @author Cameron McLachlan
 *
 */
public class SpriteMap {
	
	private static final String IMAGE_PATH = "images/";
	private HashMap<String, Image> spriteMap;
	
	public SpriteMap(){
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
		
		//regular wall
		spriteMap.put("w0",loadImage("wall0.png"));
		spriteMap.put("w1",loadImage("wall1.png"));
		spriteMap.put("w2",loadImage("wall2.png"));
		spriteMap.put("w3",loadImage("wall3.png"));
		
		// corner wall
		spriteMap.put("W0",loadImage("wallCorner0.png"));
		spriteMap.put("W1",loadImage("wallCorner1.png"));
		spriteMap.put("W2",loadImage("wallCorner2.png"));
		spriteMap.put("W3",loadImage("wallCorner3.png"));
		
		// floor
		spriteMap.put("c0",loadImage("carpet0.png"));
		spriteMap.put("c1",loadImage("carpet1.png"));
		spriteMap.put("c2",loadImage("carpet2.png"));
		spriteMap.put("c3",loadImage("carpet3.png"));
		
		// door
		spriteMap.put("d0",loadImage("door0.png"));
		spriteMap.put("d1",loadImage("door1.png"));
		spriteMap.put("d2",loadImage("door2.png"));
		spriteMap.put("d3",loadImage("door3.png"));
		
		// Animated Images for the door
		/*spriteMap.put("d00",loadImage("door00.png"));
		spriteMap.put("d01",loadImage("door01.png"));
		spriteMap.put("d02",loadImage("door02.png"));
		spriteMap.put("d03",loadImage("door03.png"));
		spriteMap.put("d10",loadImage("door10.png"));
		spriteMap.put("d20",loadImage("door20.png"));
		spriteMap.put("d30",loadImage("door30.png"));*/
		
		// bed
		spriteMap.put("b0",loadImage("bed0.png"));
		spriteMap.put("b1",loadImage("bed1.png"));
		spriteMap.put("b2",loadImage("bed2.png"));
		spriteMap.put("b3",loadImage("bed3.png"));
		
		// furniture
		spriteMap.put("f0",loadImage("furniture0.png"));
		spriteMap.put("f1",loadImage("furniture1.png"));
		spriteMap.put("f2",loadImage("furniture2.png"));
		spriteMap.put("f3",loadImage("furniture3.png"));
		
		// table - big
		spriteMap.put("T0",loadImage("tableRound0.png"));
		spriteMap.put("T1",loadImage("tableRound1.png"));
		spriteMap.put("T2",loadImage("tableRound2.png"));
		spriteMap.put("T3",loadImage("tableRound3.png"));
		
		// table - small
		spriteMap.put("t0",loadImage("tableSmall0.png"));
		spriteMap.put("t1",loadImage("tableSmall1.png"));
		spriteMap.put("t2",loadImage("tableSmall2.png"));
		spriteMap.put("t3",loadImage("tableSmall3.png"));
		
		// main player
		spriteMap.put("p0",loadImage("player0.png"));
		spriteMap.put("p1",loadImage("player1.png"));
		spriteMap.put("p2",loadImage("player2.png"));
		spriteMap.put("p3",loadImage("player3.png"));
		
		//Animated Images for the player
		/*spriteMap.put("p00",loadImage("player00.png"));
		spriteMap.put("p01",loadImage("player01.png"));
		spriteMap.put("p02",loadImage("player02.png"));
		spriteMap.put("p03",loadImage("player03.png"));
		spriteMap.put("p10",loadImage("player10.png"));
		spriteMap.put("p11",loadImage("player11.png"));
		spriteMap.put("p12",loadImage("player12.png"));
		spriteMap.put("p13",loadImage("player13.png"));
		spriteMap.put("p20",loadImage("player20.png"));
		spriteMap.put("p21",loadImage("player21.png"));
		spriteMap.put("p22",loadImage("player22.png"));
		spriteMap.put("p23",loadImage("player23.png"));
		spriteMap.put("p30",loadImage("player30.png"));
		spriteMap.put("p31",loadImage("player31.png"));
		spriteMap.put("p32",loadImage("player32.png"));
		spriteMap.put("p33",loadImage("player33.png"));*/
		
		
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
