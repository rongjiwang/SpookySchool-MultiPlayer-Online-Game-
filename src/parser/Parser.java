package parser;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.List;

import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import com.sun.org.apache.xml.internal.serialize.*;


import game.Area;
import game.DoorGO;
import game.FixedContainerGO;
import game.FixedGO;
import game.FloorTile;
import game.GameObject;
import game.InventoryGO;
import game.MarkerGO;
import game.MovableGO;
import game.NonHumanPlayer;
import game.Player;
import game.Position;
import game.SpookySchool;
import game.Tile;
import game.WallTile;

/** This class allows the program to parse through and read a given XML file.
 * After being read, the program will be able to create a game state from which
 * the user can play from.
 * 
 * @author Chethana Wijesekera
 * 
 * 
 */
public class Parser {

	//Fields
	private Document load;
	private Tile[][] area;
	private Element root;
	private Document save;

	/** Constructor for the Parser, creates a Parser object to be used in the game
	 * 
	 */
	public Parser() {

	}


	/** Allows the game to read an XML file to load a game from a File. Used to 
	 * initiate a game from scratch by loading a room, and also to load a game
	 * from a saved state.
	 * 
	 * @param filename -- name for the file to be read from
	 */
	public void load(String filename) {

		load = getDoc("src/com/school/xml/" + filename + ".xml"); //Loads the XML file into the program

		int width = extractIntFromNode("width"); //Sets width and height
		int height = extractIntFromNode("height");
		area = new Tile[width][height]; //Initializes 2D array


		NodeList allNodes = load.getElementsByTagName("*"); //Creates a list of all XML Nodes
		System.out.println("Total Nodes: " + allNodes.getLength()); //Number of Nodes (for debugging)

		for (int i = 0; i < width; i++) { //Number of cells in the Array is equal
			for (int j = 0; j < height; j++) { //to number of Nodes, so iterate over them
													//to fill the array
				Node currentNode = allNodes.item(i + j); //Find the Node of interest
				String currentString = extractStringFromNode(currentNode); //Extract the string at this Node
				System.out.println("Current String: " + currentString);
				System.out.println("Node name: " + currentNode.getNodeName()); //For Debugging

				if (currentNode.getNodeName().equals("tile")) { //if Node is an empty tile, set corresponding cell 
					if (currentString.equals("null")) { //in the array to null
						area[i][j] = null;
					}
					if (currentString.equals("c0")) { //if it is the floor, create a FloorTile at that cell
						area[i][j] = new FloorTile(new Position(i, j), currentString);
					} else {
						area[i][j] = new WallTile(new Position(i, j), currentString); //Otherwise, create a WallTile
					}
				}
				if (currentNode.getNodeName().equals("door")) {
					//steps for creating a Door GameObject - see com/school/game/Area.java

				}
				if (currentNode.getNodeName().equals("fixed")) {
					//steps for creating a fixed GameObject - see com/school/game/Area.java
				}
			}
		}
	}
	
		
	/** Takes all the information of the current state of the game and saves it to
	 * an XML file. This can be read from later when needed to be loaded
	 * 
	 * @param game -- SpookySchool instance to be saved. Contains all information
	 * 				  about the game that needs to be saved.
	 * @param player -- name of the player that requested the save. This is the player
	 * 					whose inventory and character will be saved.
	 * @throws IOException 
	 */
	public void save(SpookySchool game, String player){
		
		/*
		 * for every area in the areas map,
		 * get the Tile[][] area and save whats at every tile
		 * 
		 * only save invent objects that are on the floor
		 * 
		 * 
		 */
		

		
		//System.err.println(game.getAreas().keySet());
		//System.err.println(game.getAreas().entrySet());
		Map<String, Area> areas = game.getAreas();
		Map<String, Player> players = null;
		Map<String, InventoryGO> inventObjects = game.getInventoryObjects();
			

	    save = createXMLDom();
	    root = save.createElement("game");
	    saveMap(areas);
	    
	    outputFile();
		
	}
	
	public void saveMap(Map<String, Area> areas){
		for(String key : areas.keySet()){
			Element tagName = save.createElement("area");
			Text contents = save.createTextNode(key);
			tagName.appendChild(contents);
			root.appendChild(tagName);
			saveTilesOfArea(areas.get(key), tagName);
		}
		save.appendChild(root);
	}
	
	public void outputFile(){
         
		try{
	        Transformer tr = TransformerFactory.newInstance().newTransformer();
	        tr.setOutputProperty(OutputKeys.INDENT, "yes");
	        tr.setOutputProperty(OutputKeys.METHOD, "xml");
	        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "save.dtd");
	        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	
	        // send DOM to file
			tr.transform(new DOMSource(save), new StreamResult(new FileOutputStream("Save.xml")));
		}catch (FileNotFoundException | TransformerException e) {
			e.printStackTrace();
		}	
		
	}
	
	public void saveTilesOfArea(Area area, Element currentParent){
		
		for(int i = 0; i < area.getArea().length; i++){
			for (int j = 0; j < area.getArea()[i].length; j++){
				
				Element tagName = save.createElement("tile");
				Text contents = null;
				Tile currentTile = area.getArea()[i][j];
				
				if (currentTile == null){
					contents = save.createTextNode("null");
					tagName.appendChild(contents);
					currentParent.appendChild(tagName);
				}
				else if (currentTile instanceof Tile){
					
						Element positions = savePosition(currentTile);
						tagName.appendChild(positions);
						currentParent.appendChild(tagName);
						
						Element token = saveToken(currentTile, "");
						tagName.appendChild(token);
						currentParent.appendChild(tagName);
						
						/*if (currentTile.getOccupant() != null){
							GameObject occupant = currentTile.getOccupant();
							
							if(occupant instanceof InventoryGO){
								tagName.appendChild(saveName(occupant));
								tagName.appendChild(saveAreaName(occupant));
								tagName.appendChild(saveSize(occupant));
								tagName.appendChild(saveDescription(occupant));
								
								currentParent.appendChild(tagName);
								
							}else if (occupant instanceof DoorGO){
								Element open = saveOpen(occupant);
								Element locked = saveLocked(occupant);
								Element keyID = saveKeyID(occupant);
								Element description = saveDescription(occupant);
								
								Element sideA = saveSide(occupant, "a");
								Element tokenA = saveToken(currentTile, "a");
								Element sideAPos = saveSidePos(occupant, "a");
								Element sideAEntryPos = saveSideEntryPos(occupant, "a");
								
								Element sideB = saveSide(occupant, "b");
								Element tokenB = saveToken(currentTile, "b");
								Element sideBPos = saveSidePos(occupant, "b");
								Element sideBEntryPos = saveSideEntryPos(occupant, "b");								
								
							}else if (occupant instanceof FixedContainerGO){
								Element open = saveOpen(occupant);
								Element locked = saveLocked(occupant);
								//Element keyID = saveKeyID();
								Element size = saveSize(occupant);
								
							}else if (occupant instanceof FixedGO){
								Element description = saveDescription(occupant);
														
							}else if (occupant instanceof MarkerGO){
								//FIXME: Base GameObject?? do i need to save a record of this 
								Element description = saveDescription(occupant);
								
							}else if (occupant instanceof MovableGO){
								Element areaName = saveAreaName(occupant);
																
							}else if (occupant instanceof Player){
								Element playerName = saveName(occupant);
								//FIXME: CurrentArea?? do i need to save a record of this
								Element spawnName = saveSpawnName(occupant);
								Element currentPosition = savePosition(currentTile);

						}
						/*Element occupant = saveOccupant(currentTile);
						tagName.appendChild(occupant);
						currentParent.appendChild(tagName);*/
				}
				
				else {
					try{
						contents = save.createTextNode(currentTile.toString());
						tagName.appendChild(contents);
						currentParent.appendChild(tagName);
					}catch(NullPointerException e){
						
					}
				}
			}
			}
		}
			
			
	
	
	public Element saveName(GameObject occupant){
		Text value = null;
		
		if(occupant instanceof InventoryGO){
			value = save.createTextNode(((InventoryGO) occupant).getName());
		}else if (occupant instanceof Player){
			value = save.createTextNode(((Player) occupant).getPlayerName());
		}
			Element name = save.createElement("name");
			name.appendChild(value);
			return name;
		
	}
	
	public Element saveSpawnName(GameObject occupant){
		if(occupant instanceof Player){
			Text value = save.createTextNode(((Player) occupant).getSpawnName());
			Element spawnName = save.createElement("spawnName");
			spawnName.appendChild(value);
			return spawnName;
		}
		return null;
	}
	
	public Element saveAreaName(GameObject occupant){
		Text value = null;
		
		if(occupant instanceof InventoryGO){
			value = save.createTextNode(((InventoryGO) occupant).getAreaName());
			
		}else if (occupant instanceof MovableGO){
			value = save.createTextNode(((MovableGO) occupant).getAreaName());
		}
		
		Element areaName = save.createElement("areaName");
		areaName.appendChild(value);
		return areaName;

	}
	
	public Element saveSize(GameObject occupant){
		Text value = null;
		
		if(occupant instanceof InventoryGO){
			value = save.createTextNode("" + ((InventoryGO) occupant).getSize());
		}else if (occupant instanceof FixedContainerGO){
			value = save.createTextNode("" + ((FixedContainerGO) occupant).getSize());
		}
			Element size = save.createElement("size");
			size.appendChild(value);
			return size;
			
	}
	
	public Element saveDescription(GameObject occupant){
		Text value = null;
		
		if(occupant instanceof InventoryGO){
			value = save.createTextNode(((InventoryGO) occupant).getDescription());
			
		}else if (occupant instanceof DoorGO){
			value = save.createTextNode(((DoorGO) occupant).getDescription());
		}else if (occupant instanceof FixedGO){
			value = save.createTextNode(((FixedGO) occupant).getDescription());			
		}else if (occupant instanceof MarkerGO){
			value = save.createTextNode(((MarkerGO) occupant).getDescription());			
		}
		
		Element description = save.createElement("description");
		description.appendChild(value);
		return description;
	}
	
	public Element saveOpen(GameObject occupant){
		Text value = null;
		
		if(occupant instanceof DoorGO){
			 value = save.createTextNode("" + ((DoorGO) occupant).isOpen());	
			 
		}else if(occupant instanceof FixedContainerGO){
			 value = save.createTextNode("" + ((FixedContainerGO) occupant).isOpen());	
		}
		Element open = save.createElement("open");
		open.appendChild(value);
		return open;
	
	}
	
	public Element saveLocked(GameObject occupant){
		Text value = null;
		
		if(occupant instanceof DoorGO){
			value = save.createTextNode("" + ((DoorGO) occupant).isLocked());
		}else if (occupant instanceof FixedContainerGO){
			value = save.createTextNode("" + ((FixedContainerGO) occupant).isLocked());
		}
		Element locked = save.createElement("locked");
		locked.appendChild(value);
		return locked;
		
	}
	
	public Element saveKeyID(GameObject occupant){
		Text value = null;
		
		if(occupant instanceof DoorGO){
			value = save.createTextNode(((DoorGO) occupant).getKeyID());
		}else if (occupant instanceof FixedContainerGO){
			//value = save.createTextNode(((FixedContainerGO) occupant).getKeyID());
			value = save.createTextNode("chet");
		}
		
		Element keyID = save.createElement("keyID");
		keyID.appendChild(value);
		return keyID; 
		
	}
	
	public Element saveSide(GameObject occupant, String side){
		if(occupant instanceof DoorGO){
			if(side.equals("a")){
				Text value = save.createTextNode(((DoorGO) occupant).getSideA());
				Element sideA = save.createElement("sideA");
				sideA.appendChild(value);
				return sideA;
			}else{
				Text value = save.createTextNode(((DoorGO) occupant).getSideB());
				Element sideB = save.createElement("sideB");
				sideB.appendChild(value);
				return sideB;
			}
		}
		
		return null;
	}
	
	public Element saveToken(Tile currentTile, String side){
		GameObject occupant = currentTile.getOccupant();
		Text value = null;
		
		if (occupant == null){
			if(currentTile instanceof FloorTile){
				value = save.createTextNode(((FloorTile) currentTile).getToken());
			}else if (currentTile instanceof WallTile){
				value = save.createTextNode(((WallTile) currentTile).getToken());
			}
			
			Element token = save.createElement("token");
			token.appendChild(value);
			return token;
			
		}
		
		if(occupant instanceof DoorGO){
			if(side.equals("a")){
				value = save.createTextNode(((DoorGO)occupant).getTokenA());
				Element tokenA = save.createElement("tokenA");
				tokenA.appendChild(value);
				return tokenA;
			}else if(side.equals("b")){
				value = save.createTextNode(((DoorGO)occupant).getTokenB());
				Element tokenB = save.createElement("tokenB");
				tokenB.appendChild(value);
				return tokenB;
			}
		}else{
			value = save.createTextNode(occupant.getToken());
			Element token = save.createElement("token");
			token.appendChild(value);
			return token;
		}	
		return null;
	}
	
	public Element saveSidePos(GameObject occupant, String side){
		if(occupant instanceof DoorGO){
			
			Text xVal = null;
			Text yVal = null;
			
			if(side.equals("a")){
				Position posA = ((DoorGO)occupant).getSideAPos();
				xVal = save.createTextNode("" + posA.getPosX());
				yVal = save.createTextNode("" + posA.getPosY());
				
			}else{
				Position posB = ((DoorGO)occupant).getSideBPos();
				xVal = save.createTextNode("" + posB.getPosX());
				yVal = save.createTextNode("" + posB.getPosY());
			
			}
			Element x = save.createElement("x");
			Element y = save.createElement("y");
			x.appendChild(xVal);
			y.appendChild(yVal);
			
			if(side.equals("a")){
				Element sideAPos = save.createElement("sideAPos");
				sideAPos.appendChild(x);
				sideAPos.appendChild(y);
				return sideAPos;
				
			}else{
				Element sideBPos = save.createElement("sideBPos");
				sideBPos.appendChild(x);
				sideBPos.appendChild(y);
				return sideBPos; 
				
			}
		} 
		return null;
	}
	
	public Element saveSideEntryPos(GameObject occupant, String side){
		if (occupant instanceof DoorGO){
			
			Text xVal = null;
			Text yVal = null;
			
			if(side.equals("a")){
				Position posA = ((DoorGO)occupant).getSideAEntryPos();
				xVal = save.createTextNode("" + posA.getPosX());
				yVal = save.createTextNode("" + posA.getPosY());
				
			}else{
				Position posB = ((DoorGO)occupant).getSideBEntryPos();
				xVal = save.createTextNode("" + posB.getPosX());
				yVal = save.createTextNode("" + posB.getPosY());
			}
			Element x = save.createElement("x");
			Element y = save.createElement("y");
			x.appendChild(xVal);
			y.appendChild(yVal);
			
			if(side.equals("a")){
				Element sideAEntryPos = save.createElement("sideAPos");
				sideAEntryPos.appendChild(x);
				sideAEntryPos.appendChild(y);
				return sideAEntryPos;
				
			}else{
				Element sideBEntryPos = save.createElement("sideBPos");
				sideBEntryPos.appendChild(x);
				sideBEntryPos.appendChild(y);
				return sideBEntryPos;
				
			}
		}
		return null;
	}
	
	
	public Element savePosition(Tile currentTile){
		Element pos = save.createElement("pos");
		Element x = save.createElement("x");
		Text xVal = save.createTextNode("" + currentTile.getPosition().getPosX());
		Element y = save.createElement("y");
		Text yVal = save.createTextNode("" + currentTile.getPosition().getPosY());
		
		x.appendChild(xVal);
		y.appendChild(yVal);
		pos.appendChild(x);
		pos.appendChild(y);
		
		return pos;
		
		
		
	}
	
	
	public Document createXMLDom(){
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();	//Implements the static class to properly manage a file
			//factory.setIgnoringComments(true);										//Ignore comments
			//factory.setIgnoringElementContentWhitespace(true);						//Ignore whitespace
			//factory.setValidating(true);
			DocumentBuilder builder =  factory.newDocumentBuilder();				//Build the document in memory for the program to use
			Document save = builder.newDocument();
			return save;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null; 	//to compile, should never reach this point
		
	}
	

	/**
	 * Allows for a workable integer to be extracted from an XML Node. Rather than extracting the value as a 
	 * String, the String is parsed and converted into an integer primitive type.
	 * 
	 * @param nodeName -- name of the Node in question
	 * @return int -- integer value of the Node in question
	 */
	public int extractIntFromNode(String nodeName) {
		int value = Integer.parseInt(extractStringFromNode(nodeName));	//Determines String value and converts to int
		return value;
	}
	
	
	
	/**
	 * Allows the string value of the Node in question to be extracted as a String. Additionally, the string 
	 * is formatted to remove any unwanted default characters.
	 * 
	 * E.g: if a Node is displayed in XML as <width>13</width>, then this method will extract the "13" from the
	 * Node as a String
	 * 
	 * @param node -- Node whose value is sought
	 * @return String -- value of the Node
	 */
	public String extractStringFromNode(Node node) {

		Node content = node.getChildNodes().item(0); //the child Node of the node in question. This is what holds the data
		String value = content.toString(); //converts it to a string
		value = value.substring(8, value.length() - 1); //formats to only the required characters
		return value;
	}

	
	/**Allows the string value of the Node in question to be extracted as a String. Additionally, the string 
	 * is formatted to remove any unwanted default characters. 
	 * 
	 * Takes the name of the Node required than the Node itself.
	 * 
	 * E.g: if a Node is displayed in XML as <width>13</width>, then this method will extract the "13" from the
	 * Node as a String
	 * 
	 * @param nodeName -- name of the Node in question
	 * @return String -- value of the Node
	 */
	public String extractStringFromNode(String nodeName) {
		Node node = load.getElementsByTagName(nodeName).item(0); //Determines the Node in question
		Node child = node.getFirstChild(); //Finds the value of the Node
		String strRep = child.toString(); //Converts it to a String	
		return strRep.substring(8, strRep.length() - 1); //Formats and returns

	}


	/** Retrieves a specified XML file for the parser to work with
	 * 
	 * @param path -- path to the specified file
	 * @return -- the specified Document file (XML)
	 */
	public Document getDoc(String path) {

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); //Implements the static class to properly manage a file
			factory.setIgnoringComments(true); //Ignore comments
			factory.setIgnoringElementContentWhitespace(true); //Ignore whitespace
			//factory.setValidating(true);
			DocumentBuilder builder = factory.newDocumentBuilder(); //Build the document in memory for the program to use
			return builder.parse(new InputSource(path));

		} catch (Exception e) {
			System.err.println("Cannot open file of path " + path); //If the file does not exist
		}

		return null;
	}

}
