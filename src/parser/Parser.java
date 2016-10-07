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
		

		
		System.err.println(game.getAreas().keySet());
		System.err.println(game.getAreas().entrySet());
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
						
						if (currentTile.getOccupant() != null){
							GameObject occupant = currentTile.getOccupant();
							
							if(occupant instanceof InventoryGO){
								Element name = saveName();
								Element areaName = saveAreaName();
								Element size = saveSize();
								Element description = saveDescription();
								
							}else if (occupant instanceof DoorGO){
								Element open = saveOpen();
								Element locked = saveLocked();
								Element keyID = saveKeyID();
								Element description = saveDescription();
								
								Element sideA = saveSideA();
								Element tokenA = saveTokenA();
								Element sideAPos = saveSideAPos();
								Element sideAEntryPos = saveSideAEntryPos();
								
								Element sideB = saveSideB();
								Element tokenB = saveTokenB();
								Element sideBPos = saveSideBPos();
								Element sideBEntryPos = saveSideBEntryPos();								
								
							}else if (occupant instanceof FixedContainerGO){
								Element open = saveOpen();
								Element locked = saveLocked();
								Element keyID = saveKeyID();
								Element size = saveSize();
								
							}else if (occupant instanceof FixedGO){
								Element description = saveDescription();
														
							}else if (occupant instanceof MarkerGO){
								//FIXME: Base GameObject?? do i need to save a record of this 
								Element description = saveDescription();
								
							}else if (occupant instanceof MovableGO){
								Element areaName = saveAreaName();
																
							}else if (occupant instanceof Player){
								Element playerName = saveName();
								//FIXME: CurrentArea?? do i need to save a record of this
								Element spawnName = saveSpawnName();
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
	
	public Element saveOccupant(Tile currentTile){
		//Element occupant = save.createElement("occupant");
		//Element 
		return nukk;
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
