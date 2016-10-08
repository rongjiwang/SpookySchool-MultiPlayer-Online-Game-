package ui;

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

	public SpriteMap() {
		loadMap();
	}

	/**
	 * Returns corresponding image object from given token string
	 * 
	 * @param - token
	 * @return - Image
	 */
	public Image getImage(String token) {
		Image image = spriteMap.get(token);
		if (image == null) {
			System.out.println("null image on input " + token);
		}
		return image;
	}

	public void loadMap() {

		spriteMap = new HashMap<String, Image>();
		
		//walls
		spriteMap.put("w0", loadImage("wall0.png"));
		spriteMap.put("w1", loadImage("wall1.png"));
		spriteMap.put("w2", loadImage("wall2.png"));
		spriteMap.put("w3", loadImage("wall3.png"));
		spriteMap.put("u0", loadImage("bathroomWall0.png"));
		spriteMap.put("u1", loadImage("bathroomWall1.png"));
		spriteMap.put("u2", loadImage("bathroomWall2.png"));
		spriteMap.put("u3", loadImage("bathroomWall3.png"));
		spriteMap.put("f0", loadImage("fence0.png"));
		spriteMap.put("f1", loadImage("fence1.png"));
		spriteMap.put("f2", loadImage("fence2.png"));
		spriteMap.put("f3", loadImage("fence3.png"));

		// wall Corners
		spriteMap.put("W0", loadImage("wallCorner0.png"));
		spriteMap.put("W1", loadImage("wallCorner1.png"));
		spriteMap.put("W2", loadImage("wallCorner2.png"));
		spriteMap.put("W3", loadImage("wallCorner3.png"));
		spriteMap.put("U0", loadImage("bathroomWallCorner0.png"));
		spriteMap.put("U1", loadImage("bathroomWallCorner1.png"));
		spriteMap.put("U2", loadImage("bathroomWallCorner2.png"));
		spriteMap.put("U3", loadImage("bathroomWallCorner3.png"));
		spriteMap.put("F0", loadImage("fenceCorner0.png"));
		spriteMap.put("F1", loadImage("fenceCorner1.png"));
		spriteMap.put("F2", loadImage("fenceCorner2.png"));
		spriteMap.put("F3", loadImage("fenceCorner3.png"));
	
		

		// floors
		spriteMap.put("c0", loadImage("carpet0.png"));
		spriteMap.put("c1", loadImage("carpet1.png"));
		spriteMap.put("c2", loadImage("carpet2.png"));
		spriteMap.put("c3", loadImage("carpet3.png"));
		spriteMap.put("g0", loadImage("grass0.png"));
		spriteMap.put("g1", loadImage("grass1.png"));
		spriteMap.put("g2", loadImage("grass2.png"));
		spriteMap.put("g3", loadImage("grass3.png"));
		spriteMap.put("h0", loadImage("hard0.png"));
		spriteMap.put("h1", loadImage("hard1.png"));
		spriteMap.put("h2", loadImage("hard2.png"));
		spriteMap.put("h3", loadImage("hard3.png"));
		spriteMap.put("G0", loadImage("grassLarge0.png"));
		spriteMap.put("G1", loadImage("grassLarge1.png"));
		spriteMap.put("G2", loadImage("grassLarge2.png"));
		spriteMap.put("G3", loadImage("grassLarge3.png"));
		spriteMap.put("L0", loadImage("lino0.png"));
		spriteMap.put("L1", loadImage("lino1.png"));
		spriteMap.put("L2", loadImage("lino2.png"));
		spriteMap.put("L3", loadImage("lino3.png"));

		// doors/windows
		spriteMap.put("d00", loadImage("door00.png"));
		spriteMap.put("d10", loadImage("door10.png"));
		spriteMap.put("d20", loadImage("door20.png"));
		spriteMap.put("d30", loadImage("door30.png"));
		spriteMap.put("d01", loadImage("door01.png"));
		spriteMap.put("d11", loadImage("door11.png"));
		spriteMap.put("d21", loadImage("door21.png"));
		spriteMap.put("d31", loadImage("door31.png"));
		spriteMap.put("w00", loadImage("window00.png"));
		spriteMap.put("w10", loadImage("window10.png"));
		spriteMap.put("w20", loadImage("window20.png"));
		spriteMap.put("w30", loadImage("window30.png"));
		spriteMap.put("w01", loadImage("window01.png"));
		spriteMap.put("w11", loadImage("window11.png"));
		spriteMap.put("w21", loadImage("window21.png"));
		spriteMap.put("w31", loadImage("window31.png"));

		// bed
		spriteMap.put("o0", loadImage("bed0.png"));
		spriteMap.put("o1", loadImage("bed1.png"));
		spriteMap.put("o2", loadImage("bed2.png"));
		spriteMap.put("o3", loadImage("bed3.png"));

		// key
		spriteMap.put("k0", loadImage("key0.png"));
		spriteMap.put("k1", loadImage("key1.png"));
		spriteMap.put("k2", loadImage("key2.png"));
		spriteMap.put("k3", loadImage("key3.png"));

		// furniture
		spriteMap.put("x0", loadImage("furniture0.png"));
		spriteMap.put("x1", loadImage("furniture1.png"));
		spriteMap.put("x2", loadImage("furniture2.png"));
		spriteMap.put("x3", loadImage("furniture3.png"));

		// trees
		spriteMap.put("v0", loadImage("treeOne0.png"));
		spriteMap.put("v1", loadImage("treeOne1.png"));
		spriteMap.put("v2", loadImage("treeOne2.png"));
		spriteMap.put("v3", loadImage("treeOne3.png"));
		spriteMap.put("e0", loadImage("treeTwo0.png"));
		spriteMap.put("e1", loadImage("treeTwo1.png"));
		spriteMap.put("e2", loadImage("treeTwo2.png"));
		spriteMap.put("e3", loadImage("treeTwo3.png"));
		spriteMap.put("l0", loadImage("treeThree0.png"));
		spriteMap.put("l1", loadImage("treeThree1.png"));
		spriteMap.put("l2", loadImage("treeThree2.png"));
		spriteMap.put("l3", loadImage("treeThree3.png"));


		// table - big
		spriteMap.put("T0", loadImage("tableRound0.png"));
		spriteMap.put("T1", loadImage("tableRound1.png"));
		spriteMap.put("T2", loadImage("tableRound2.png"));
		spriteMap.put("T3", loadImage("tableRound3.png"));

		// table - small
		spriteMap.put("t0", loadImage("tableSmall0.png"));
		spriteMap.put("t1", loadImage("tableSmall1.png"));
		spriteMap.put("t2", loadImage("tableSmall2.png"));
		spriteMap.put("t3", loadImage("tableSmall3.png"));

		// sign
		spriteMap.put("s0", loadImage("sign0.png"));
		spriteMap.put("s1", loadImage("sign1.png"));
		spriteMap.put("s2", loadImage("sign2.png"));
		spriteMap.put("s3", loadImage("sign3.png"));

		// Building
		spriteMap.put("B0", loadImage("building0.png"));
		spriteMap.put("B1", loadImage("building1.png"));
		spriteMap.put("B2", loadImage("building2.png"));
		spriteMap.put("B3", loadImage("building3.png"));
		
		// Box
		spriteMap.put("b0", loadImage("box0.png"));
		spriteMap.put("b1", loadImage("box1.png"));
		spriteMap.put("b2", loadImage("box2.png"));
		spriteMap.put("b3", loadImage("box3.png"));

		// Building walls
		spriteMap.put("Q0", loadImage("buildingCorner0.png"));
		spriteMap.put("Q1", loadImage("buildingCorner1.png"));
		spriteMap.put("Q2", loadImage("buildingCorner2.png"));
		spriteMap.put("Q3", loadImage("buildingCorner3.png"));
		spriteMap.put("R0", loadImage("roof0.png"));
		spriteMap.put("R1", loadImage("roof1.png"));
		spriteMap.put("R2", loadImage("roof2.png"));
		spriteMap.put("R3", loadImage("roof3.png"));

		// Pondy
		spriteMap.put("P00", loadImage("pondy00.png"));
		//spriteMap.put("0p01", loadImage("0player01.png"));
		//spriteMap.put("0p02", loadImage("0player02.png"));
		//spriteMap.put("0p03", loadImage("0player03.png"));

		spriteMap.put("P10", loadImage("pondy10.png"));
		//spriteMap.put("0p11", loadImage("0player11.png"));
		//spriteMap.put("0p12", loadImage("0player12.png"));
		//spriteMap.put("0p13", loadImage("0player13.png"));

		spriteMap.put("P20", loadImage("pondy20.png"));
		//spriteMap.put("0p21", loadImage("0player21.png"));
		//spriteMap.put("0p22", loadImage("0player22.png"));
		//spriteMap.put("0p23", loadImage("0player23.png"));

		spriteMap.put("P30", loadImage("pondy30.png"));
		//spriteMap.put("0p31", loadImage("0player31.png"));
		//spriteMap.put("0p32", loadImage("0player32.png"));
		//spriteMap.put("0p33", loadImage("0player33.png"));

		// player 0 

		spriteMap.put("0p00", loadImage("0player00.png"));
		spriteMap.put("0p01", loadImage("0player01.png"));
		spriteMap.put("0p02", loadImage("0player02.png"));
		spriteMap.put("0p03", loadImage("0player03.png"));

		spriteMap.put("0p10", loadImage("0player10.png"));
		spriteMap.put("0p11", loadImage("0player11.png"));
		spriteMap.put("0p12", loadImage("0player12.png"));
		spriteMap.put("0p13", loadImage("0player13.png"));

		spriteMap.put("0p20", loadImage("0player20.png"));
		spriteMap.put("0p21", loadImage("0player21.png"));
		spriteMap.put("0p22", loadImage("0player22.png"));
		spriteMap.put("0p23", loadImage("0player23.png"));

		spriteMap.put("0p30", loadImage("0player30.png"));
		spriteMap.put("0p31", loadImage("0player31.png"));
		spriteMap.put("0p32", loadImage("0player32.png"));
		spriteMap.put("0p33", loadImage("0player33.png"));

		// player 1
		/*spriteMap.put("1p00",loadImage("1player00.png"));
		spriteMap.put("1p01",loadImage("1player01.png"));
		spriteMap.put("1p02",loadImage("1player02.png"));
		spriteMap.put("1p03",loadImage("1player03.png"));
		
		spriteMap.put("1p10",loadImage("1player10.png"));
		spriteMap.put("1p11",loadImage("1player11.png"));
		spriteMap.put("1p12",loadImage("1player12.png"));
		spriteMap.put("1p13",loadImage("1player13.png"));
		
		spriteMap.put("1p20",loadImage("1player20.png"));
		spriteMap.put("1p21",loadImage("1player21.png"));
		spriteMap.put("1p22",loadImage("1player22.png"));
		spriteMap.put("1p23",loadImage("1player23.png"));
		
		spriteMap.put("1p30",loadImage("1player30.png"));
		spriteMap.put("1p31",loadImage("1player31.png"));
		spriteMap.put("1p32",loadImage("1player32.png"));
		spriteMap.put("1p33",loadImage("1player33.png"));
		
		
		// player 2
		spriteMap.put("2p00",loadImage("2player00.png"));
		spriteMap.put("2p01",loadImage("2player01.png"));
		spriteMap.put("2p02",loadImage("2player02.png"));
		spriteMap.put("2p03",loadImage("2player03.png"));
		
		spriteMap.put("2p10",loadImage("2player10.png"));
		spriteMap.put("2p11",loadImage("2player11.png"));
		spriteMap.put("2p12",loadImage("2player12.png"));
		spriteMap.put("2p13",loadImage("2player13.png"));
		
		spriteMap.put("2p20",loadImage("2player20.png"));
		spriteMap.put("2p21",loadImage("2player21.png"));
		spriteMap.put("2p22",loadImage("2player22.png"));
		spriteMap.put("2p23",loadImage("2player23.png"));
		
		spriteMap.put("2p30",loadImage("2player30.png"));
		spriteMap.put("2p31",loadImage("2player31.png"));
		spriteMap.put("2p32",loadImage("2player32.png"));
		spriteMap.put("2p33",loadImage("2player33.png"));
		
		// player 2
		spriteMap.put("3p00",loadImage("3player00.png"));
		spriteMap.put("3p01",loadImage("3player01.png"));
		spriteMap.put("3p02",loadImage("3player02.png"));
		spriteMap.put("3p03",loadImage("3player03.png"));
		
		spriteMap.put("3p10",loadImage("3player10.png"));
		spriteMap.put("3p11",loadImage("3player11.png"));
		spriteMap.put("3p12",loadImage("3player12.png"));
		spriteMap.put("3p13",loadImage("3player13.png"));
		
		spriteMap.put("3p20",loadImage("3player20.png"));
		spriteMap.put("3p21",loadImage("3player21.png"));
		spriteMap.put("3p22",loadImage("3player22.png"));
		spriteMap.put("3p23",loadImage("3player23.png"));
		
		spriteMap.put("3p30",loadImage("3player30.png"));
		spriteMap.put("3p31",loadImage("3player31.png"));
		spriteMap.put("3p32",loadImage("3player32.png"));
		spriteMap.put("3p33",loadImage("3player33.png"));*/

		/*       Overlay Window      */

		spriteMap.put("H0", loadImage("header.png"));
		spriteMap.put("P0", loadImage("footer.png"));

		/* Rain */

		spriteMap.put("Rain0", loadImage("rain0.png"));
		spriteMap.put("Rain1", loadImage("rain1.png"));
		spriteMap.put("Rain2", loadImage("rain2.png"));
		spriteMap.put("Rain3", loadImage("rain3.png"));
		spriteMap.put("Rain4", loadImage("rain4.png"));
		spriteMap.put("Rain5", loadImage("rain5.png"));
		spriteMap.put("Rain6", loadImage("rain6.png"));
		spriteMap.put("Rain7", loadImage("rain7.png"));


		/* Night overlay */
		spriteMap.put("N0", loadImage("night0.png"));
		spriteMap.put("N1", loadImage("night1.png"));
		spriteMap.put("N2", loadImage("night2.png"));
		spriteMap.put("N3", loadImage("night3.png"));

	}


	/**
	 * Load an image from the file system, using a given filename.
	 * 
	 * @param filename
	 * @return
	 */
	public static Image loadImage(String filename) {
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
