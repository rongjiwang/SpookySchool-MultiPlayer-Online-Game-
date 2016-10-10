package parser;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import game.Area;
import game.ContainerGO;
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

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Parser {
	
	//Load related Fields
	private Document load;
	private List <MovableGO> movablesToLoad;	//List of MovableGameObjects that need to be loaded
	private List <DoorGO> doorsToLoad;		//List of DoorGameObjects that need to be loaded
	private List <InventoryGO> inventsToLoad;		//List of InventoryGameObjects that need to be loaded
	private List <FixedContainerGO> fixedContsToLoad;	//List of FixedContainerGameObjects to be loaded
	
	//Save related Fields
	private Document save;		//DOM structure for saving
	private Element root;	//root Node for the save file
	private List<DoorGO> doors; 	//list of Door objects that are currently in the Game
	private Player saver;		//The Player that requested the Save operation
	private List<MovableGO> movables; //list of MovableGameObjects that are currently in the Game
	private List<NonHumanPlayer> nonHumans;		//List of NonHumanPlayer Objects currently in the Game
	private Map<String, InventoryGO> inventObjects;		//Map of all InventoryGameObjects currently in the Game. Map is name of the item to the Item itself
	private List<InventoryGO> saversInvent;		//List of InventoryItems that the Player that requested the save holds
	private List<InventoryGO> itemsInContainers;	//List of all the items held in Containers in the game
	private Map<String, FixedContainerGO> fixedContainers;	//Map of all fixedContainers in the Game. Map is the name of the Container to the Container.
	
	/**
	 * COnstructor for the XML Parser which will handle the saving and loading of save states for the Game.
	 * 
	 * @author Chethana Wijesekera
	 * 
	 */
	public Parser(){
		
	}
	/**
	 * Creates a DOM document structure in memory for the program. 
	 * This is for saving the game state to an XML file.
	 * 
	 * @return Document -- the DOM Document structure to be Saved
	 */
	public Document createDocument(){
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();	//create an instance of the DocumentBuilderFactory
			DocumentBuilder builder = factory.newDocumentBuilder();		//use the factory to make a builder to create a Document
			Document doc = builder.newDocument();	//create a DOM Document
			
			return doc;		//return the newly-made DOM Structure
			
		}catch(ParserConfigurationException e){		//Catch the exception thrown by the DocumentBuilderFactory
			e.printStackTrace();			//Print stack trace
		}
		return null;	//should never get here, just to the class compile
	}
	
	/**
	 * Main method for saving the game state of the SpookySchool game. From this method, relevant details about the Game are stored and then 
	 * recorded onto an XML file through extra modular methods.
	 * 
	 * Holds a public nature as this is the method called from the SpookySchool class to initiate the saving command.
	 * 
	 * @param game -- The current instance of the running SpookySchool game
	 * @param playerName -- Name of the player that requested the save command. This players details are what will be saved, along with 
	 * 						the rest of the Game Objects.
	 */
	public void save(SpookySchool game, String playerName){
		
		save = createDocument();	//creates a workable DOM Document where the XML Tree is saved
		root = save.createElement("game");		//create the root node for the XML file
		
		Map<String, Area> areas = game.getAreas();	//Get all the different rooms in the Game. Map is from areaName to Area
		List<Player> players = game.getPlayers();	//Get all the Players currently in the Game
		this.inventObjects = game.getInventoryObjects();	//Get all the InventoryGO currently in the game
		this.doors = game.getDoorObjects();		//Get all the DoorGOs currently in the Game
		this.saver = determinePlayer(playerName, players);	//Find the Player object for the player that saved the Game
		this.movables = game.getMovableObjects();	//Get all the MovableGOs currently in the Game
		this.nonHumans = game.getNonHumanPlayers();	//Get all the NonHumanPlayers currently in the Game
		this.fixedContainers = game.getFixedContainerObjects();		//Get all the FixedContainerObjects currently in the Game
		
		saveMap(areas);		//Save the current Game Map, i.e: all the different rooms in the Game
		
		createXML();	//Output the DOM structure to be transformed into an XML file
	}
	
	/**
	 * Main method for loading an instance of a game state into the program to be played with by the user. For this method, a new XML is
	 * loaded into memory as a workable DOM structure, and parsed with the help of modular methods to create a SpookySchool instance
	 * that can be played.
	 * 
	 */
	public void load(){
		load = loadXML();		//Load the Save file as an XML, and extract the workable DOM strcuture from it
		
		Node loadRoot = load.getDocumentElement();	//Determines the root Node of the saved state
		iterate();	//iterates through each of the children nodes of the root	
	}
	
	/**
	 * Iterates over the elements in the loaded DOM structure starting from the root node. If a Node is found of a particular variety, then
	 * its child nodes are recorded and used to create objects corresponding to the name of the head Node.
	 */
	public void iterate(){
		NodeList nodeList = load.getElementsByTagName("*");		//create a list of all Nodes in teh DOM structure
		String content = "";	//initialize text value for the Node
        String nodeName = "";	//initialize the name of the Node
	    for (int i = 0; i < nodeList.getLength(); i++) {	//iterate through every Node in the structure
	        Node node = nodeList.item(i);		//the current Node
	        if (node.getNodeType() == Node.ELEMENT_NODE) {
	            nodeName = node.getNodeName();		//get the name of the current Node
	            
	            if (nodeName.equals("movableGO")){		//if its for a MovableGO,  
		        	NodeList children = node.getChildNodes(); //then save the child nodes of this and create an instance
		        	createMovableGO(children);
		        	
	            }if (nodeName.equals("door")){ 		//if its for a DoorGO,
	            	NodeList children = node.getChildNodes(); //save the child nodes of this and create an instance
		        	createDoorGO(children);		
		        	
	            }if (nodeName.equals("inventoryObject")){ //if its for a InventoryGO,
	            	NodeList children = node.getChildNodes();	//save the child nodes of this and create an instance
		        	createInventoryGO(children);
		        	
	            }if (nodeName.equals("fixedContainer")){	//if its for a FixedContainerGO
	            	NodeList children = node.getChildNodes();	//save the child nodes of this and create an instance
		        	createFixedContainerGO(children);
	            }
	        } 
	    }
	}
	
	/**
	 * Creates an instance of a MovableGO using the List of child nodes which represent the field values of the saved instance
	 * 
	 * @param fields -- NodeList of child nodes representing the fields for a MovableGO Object
	 */
	public void createMovableGO(NodeList fields){
		System.out.println(fields.getLength());
//		String id = fields.item(1).getTextContent();
//		String token = fields.item(3).getTextContent();
//		String description = fields.item(9).getTextContent();
//		String areaName = fields.item(5).getTextContent();
//		fields.item(6).get().replaceAll(" ", "");
//		fields.item(7).getTextContent().replaceAll(" ", "");
		//Position pos = new Position(x, y);
		
		//movablesToLoad.add(new MovableGO(id, token, areaName, pos));
	
	}
	
	/**
	 * Creates an instance of a DoorGO using the List of child nodes which represent the field values of the saved instance
	 * 
	 * @param fields -- NodeList of child nodes representing the fields for a DoorGO Object
	 */
	public void createDoorGO(NodeList fields){
			
	}
	
	/**
	 * Creates an instance of a InventoryGO using the List of child nodes which represent the field values of the saved instance
	 * 
	 * @param fields -- NodeList of child nodes representing the fields for a InventoryGO Object
	 */
	public void createInventoryGO(NodeList fields){
		
	}
	
	/**
	 * Creates an instance of a FixedContainerGO using the List of child nodes which represent the field values of the saved instance
	 * 
	 * @param fields -- NodeList of child nodes representing the fields for a FixedContainerGO Object
	 */
	public void createFixedContainerGO(NodeList fields){
		
	}
	
	/**
	 * Loads each room stored in the XML file by saving all Tile nodes held in a parent Room node
	 */
	public void loadAreas(){
		
	}
	
	/**
	 * Extracts the Player object that corresponds to the playerName string passed into it.
	 * 
	 * @param playerName -- String, name of the player who saved the game, and whose Player object is to be returned
	 * @param players -- List of all the Player objects held in the current Game.
	 * @return p -- the Player object whose name matches the given playerName
	 */
	public Player determinePlayer(String playerName, List<Player> players){
		for (Player p : players){		//iterate through each Player in the list of players
			if (p.getPlayerName().equals(playerName)){	//if the Player's name matches the given playerName
				return p;		//return this player
			}
		}
		return null; 	//should not get here, just to make it compile
	}
	
	/**
	 * Saves the entire map of the current Game by iterating through each Area and saving the fields of that Area.
	 * 
	 * @param areas -- Map of all the rooms in the Game. Map is from areaName to the Area itself
	 */
	public void saveMap(Map<String, Area> areas){
		for (String key : areas.keySet()){		//iterate through each areaName in the Map
			Area currentArea = areas.get(key);		//the area to work with
			root.appendChild(saveArea(currentArea));	//get the grid of the Area as a Node with children, and append it to the root.
		}
		root.appendChild(savePlayer());	//after saving the whole game map, save the player details to the root
		save.appendChild(root);		//append the root to the DOM document structure to be transformed into an XML
	}
	
	/**
	 * Save each room of the Game, including the height and width of the grid "area", the name of the Area, and all GameObjects that are
	 * in the room.
	 * 
	 * @param currentArea -- the current Area that is to be saved 
	 * @return -- the Element roomNode with all relevant data for the room saved as its children
	 */
	public Element saveArea(Area currentArea){
		Element roomNode = save.createElement("room");		//create the node "room". Holds all subsequent data in this method as a child
		Element heightNode = save.createElement("height");		//create node "height"
		Element widthNode = save.createElement("width");		//create the node "width"
		
		heightNode.appendChild(save.createTextNode("" + currentArea.getArea()[0].length));	//record the height of the area
		widthNode.appendChild(save.createTextNode("" + currentArea.getArea().length)); //record the width of the area
		
		Element areaNameNode = save.createElement("areaName");	//create a node to represent the name of the room
		areaNameNode.appendChild(save.createTextNode(currentArea.getAreaName())); //append the name of the room to the areaName node
		
		roomNode.appendChild(heightNode);	//append the height to the room node
		roomNode.appendChild(widthNode);	//append the width to the room node
		roomNode.appendChild(areaNameNode);		//append the areaName to the room node
		
		Element areaNode  = save.createElement("area");	//create a node to represent the grid 
		roomNode.appendChild(areaNode);		//append the grids node to the roomNode so its a part of that room
		
		saveTiles(currentArea, areaNode);	//save all the tiles in the area grid and append it to the areaNode
		saveDoors(currentArea, roomNode);	//save all the doors in the room and append it to the roomNode
		saveMovables(currentArea, roomNode);	//save all the movableGOs in the room and append it to the roomNode
		saveNonHumans(currentArea, roomNode);	//save all the NonHumanPlayers in the room and append it to the roomNode
		saveInventoryGameObjects(currentArea, roomNode);	//save all the inventoryGOs in the room and append it to the roomNode
		saveFixedContainers(currentArea, roomNode);		//save all the fixedContainerGOs in the room and append it o the roomNode
		saveFillContainers(currentArea, roomNode);	//fill all the containers in the room
				
		return roomNode;	//return the roomNode with all its children to be appended to the root
	}
	
	/**
	 * Saves each Tile in the 2D-array of Tiles in an Area, including WallTiles and FloorTiles as well as null tiles (Tiles that do
	 * are not a Floor or Wall Tile). Also saves the fixed position occupant of the Tile, i.e: saves the FixedGO and MarkerGO that is 
	 * on the FloorTile.
	 * 
	 * @param currentArea -- The current Area object that is being saved.
	 * @param currentParent -- The parent Node to be attached.
	 */
	public void saveTiles(Area currentArea, Element currentParent){
		
		for(int i = 0; i < currentArea.getArea().length; i++){		//iterate through the 2D-array, so essentially iterate through every Tile
			for (int j = 0; j < currentArea.getArea()[i].length; j++){
				Element tileNode = save.createElement("tile");		//Create a Tile Node
				Tile currentTile = currentArea.getArea()[i][j];		//Take a record of the current Tile
				
				if (currentTile == null){		//if the currentTile is a null Tiles
					tileNode.setAttribute("tileType", "black");		//record that it is a "black tile"
					tileNode.appendChild(save.createTextNode("null"));	//say it contains nothing
					currentParent.appendChild(tileNode);	//append the Tile to the room
				}else{
					if(currentTile instanceof WallTile){		//if the Tile is a WallTile
						tileNode.setAttribute("tileType", "WallTile");	//record that it is a WallTile
						
					}else if(currentTile instanceof FloorTile){		//if the Tile is a FloorTile
						tileNode.setAttribute("tileType", "FloorTile");		//record that it is a a FloorTile
						GameObject occupant = currentTile.getOccupant();	//get the GameObjec that is on the Tile
						Element occupantNode = save.createElement("occupant");	//create a node for this Occupant
						if(occupant != null){		//as long as there IS an occupant
							occupantNode.setAttribute("objectType", occupant.getClass().toString().substring(11)); //record the objectType of the
																				//occupant and format the string to remove unwanted tokens
						}
						
						if(occupant instanceof FixedGO){		//if the occupant is a FixedGameObject, save its fields as nodes
							occupantNode.appendChild(saveID(occupant));		//save the ID and append to the occupantNode
							occupantNode.appendChild(saveToken(occupant));		//save the token and append to the occupantNode
							occupantNode.appendChild(savePosition(occupant));		//save the position and append to the occupantNode
							occupantNode.appendChild(saveDescription(occupant));		//save the description and append to the occupantNode
							
							tileNode.appendChild(occupantNode);		//append the occupantNode and its children to the current tileNode
							
						}else if(occupant instanceof MarkerGO){
							occupantNode.appendChild(savePosition(occupant));		//save the position and append to the occupantNode
							occupantNode.appendChild(saveBaseGameObject(occupant));		//save the BaseGameObject and append to the occupantNode
							
							tileNode.appendChild(occupantNode);			//append the occupantNode and its children to the current tileNode			
						}
					}
					tileNode.appendChild(savePosition(currentTile));		//as long as the occupant isn't null, append the currentTile to the tileNode
																			//NOTE: Tiles without an occupant have already been appended
					currentParent.appendChild(tileNode);		//append the tileNode to the roomNode (the parent)
				}
			}
		}	
	}
	
	/**
	 * Saves the fields held by the Player who saved the Game. These fields are saved as child nodes to a parent "player" node, which is then
	 * appended to the main game node in the XML
	 * 
	 * @return	player -- Element that represents the player, with child nodes representing the fields.
	 */
	public Element savePlayer(){
		Element player = save.createElement("player");		//create an element to represent the player who saved the Game
		player.appendChild(saveName(saver));		//save the name and append it to the playerNode
		player.appendChild(saveAreaName(saver));		//save the areaName and append it to the playerNode
		//player.appendChild(saveSpawnName(saver)); 	//save the spawnName and append it to the playerNode
		player.appendChild(savePosition(saver));		//save the Position and append it to the playerNode
		//player.appendChild(saveInventory(saver));		//save the inventory and append it to the playerNode
		//player.appendChild(saveDirection(saver));		//save the direction and append it to the playerNode
		player.appendChild(saveToken(saver));		//save the token and append it to the playerNode
		player.appendChild(saveDescription(saver));		//save the description and append it to the playerNode
	
		return player;		//return the player to be appended to the gameNode
	}
	
	/**
	 * Save the fields for each fixedContainerGO in the Game. These fields are saved as child nodes to a parent "fixedContainer" node, which is then
	 * appended to the room node in the XML.
	 * 
	 * @param currentArea -- the current Area that is being saved
	 * @param roomNode -- the parent Element that represents the room being saved.
	 */
	public void saveFixedContainers(Area currentArea, Element roomNode){
		
		for(String key : fixedContainers.keySet()){		//iterate over the keys for the Map of fixedContainers
			FixedContainerGO currentFixedContainer = fixedContainers.get(key);		//establish a currentContainter
			
			if(currentFixedContainer.getArea().equals(currentArea.getAreaName())){		//if the container is in the currentArea
				Element fixedContainerNode = save.createElement("fixedContainer");		//create an element to represent it
				fixedContainerNode.setAttribute("id", currentFixedContainer.getId());	//mark it with an id attribute based on the containerID
				
				fixedContainerNode.appendChild(saveName(currentFixedContainer));	//create nodes for each field of the container and append them
				fixedContainerNode.appendChild(saveAreaName(currentFixedContainer));		//to the fixedContainerNode
				fixedContainerNode.appendChild(saveID(currentFixedContainer));
				fixedContainerNode.appendChild(saveToken(currentFixedContainer));
				fixedContainerNode.appendChild(saveOpen(currentFixedContainer));
				fixedContainerNode.appendChild(saveLocked(currentFixedContainer));
				fixedContainerNode.appendChild(saveKeyID(currentFixedContainer));
				fixedContainerNode.appendChild(savePosition(currentFixedContainer));
				fixedContainerNode.appendChild(saveSize(currentFixedContainer));
				fixedContainerNode.appendChild(saveContents(currentFixedContainer));
				fixedContainerNode.appendChild(saveSizeRemaining(currentFixedContainer));
				
				roomNode.appendChild(fixedContainerNode);		//append the containerNode and its children to the roomNode
			}
		}
	}
	
	/**
	 * Saves all the InventoryGO in the current Area and all their field values. These values are saved as child nodes appended to a node to represent the
	 * current item.
	 * 
	 * @param currentArea -- the current Area where the items are checked to exist.
	 * @param roomNode -- the Node to which each item will be appended to.
	 */
	public void saveInventoryGameObjects(Area currentArea, Element roomNode){
		
		saversInvent = saver.getInventory();		//get the Player who saved the game's inventory
		itemsInContainers = new ArrayList<>();		//initialize the list to remember which items remain in containers
		
		for (String key : inventObjects.keySet()){		//iterate through the keys in the Map of InventoryGOs
			InventoryGO currentObject = inventObjects.get(key);	//establish a currentObject to work with
			
			if(currentObject.getPosition() != null){	//inventory item is on the floor, not being held
				
				Element inventoryObject = save.createElement("inventoryObject");	//create a node to represent the current item
				inventoryObject.setAttribute("id", currentObject.getId());	//mark the item with an id attribute 
				
				inventoryObject.appendChild(saveName(currentObject));		//save each of the fields for the currentItem, appending each of them
				inventoryObject.appendChild(saveID(currentObject));				//to the inventoryObject node
				inventoryObject.appendChild(saveToken(currentObject));
				inventoryObject.appendChild(saveAreaName(currentObject));
				inventoryObject.appendChild(saveSize(currentObject));
				inventoryObject.appendChild(savePosition(currentObject));
				inventoryObject.appendChild(saveDescription(currentObject));
				
				roomNode.appendChild(inventoryObject);		//append the object Node to the parentRoom
			}else{			//otherwise, if item is not on the floor,
				if(!saversInvent.contains(currentObject)){	//and not in the inventory, it must be in a container
					itemsInContainers.add(currentObject);	//so add it to the list of items in containers
				}
			}
		}
	}
	
	/**
	 * Saves all the NonHumanPlayers in the game and their field values to their respective Areas in the XML.
	 * 
	 * @param currentArea -- the current Area where the NonHumanPlayer is checked to exist in
	 * @param roomNode -- the Node to which each NonHumnPlayer will be appended to
	 */
	public void saveNonHumans(Area currentArea, Element roomNode){
		for (NonHumanPlayer nhp : nonHumans){		//iterate through the list of NonHumans
			if (nhp.getCurrentArea().getAreaName().equals(currentArea.getAreaName())){	//if their name matches the name of the Area,
																						//then it should be saved here
				Element nonHumanNode = save.createElement("nonHumanPlayer");	//create a node to represent it
				nonHumanNode.setAttribute("name", nhp.getPlayerName());		//mark it with the attribute name to identify it
				nonHumanNode.appendChild(saveName(nhp));					//save the nhp's respective fields and append them to the nonHumanNode
				nonHumanNode.appendChild(saveNonHumanSpawnName(nhp));
				nonHumanNode.appendChild(saveAreaName(nhp));
				nonHumanNode.appendChild(savePosition(nhp));
				
				roomNode.appendChild(nonHumanNode);		//append the nonHuman node and its children to the parent roomNode
				
			}
		}
	}
	
	/**
	 * Saves all the Movable Game Objects in the game and their field values to their respective Areas in the XML
	 * 
	 * @param currentArea -- the current Area where the NonHumanPlayer is checked to exist in
	 * @param roomNode -- the Node to which each NonHumnPlayer will be appended to
	 */
	public void saveMovables(Area currentArea, Element roomNode){
		for (MovableGO object : movables){		//iterate through the list of movableObjects
			if(object.getAreaName().equals(currentArea.getAreaName())){		//if the areaName of the object matches the name of the current Area
				Element movableNode = save.createElement("movableGO");	//create a node to represent it
				
				movableNode.appendChild(saveID(object));		//save the movable's respective fields and append them to the movableNode 
				movableNode.appendChild(saveToken(object));
				movableNode.appendChild(saveAreaName(object));
				movableNode.appendChild(savePosition(object));
				movableNode.appendChild(saveDescription(object));
				
				roomNode.appendChild(movableNode);	//append the movable node and its children to the parent roomNode
			}
		}
	}
	
	/**
	 * Saves all the Door Game Objects in the game and their field values to their respective Areas in the XML.
	 * 
	 * @param currentArea -- the current Area where the NonHumanPlayer is checked to exist in
	 * @param roomNode -- the Node to which each NonHumnPlayer will be appended to
	 */
	public void saveDoors(Area currentArea, Element roomNode){
		
		for(DoorGO door : doors){			//iterate through the list of Doors
			if (door.getSideA().equals(currentArea.getAreaName())){ 	//if the door is in the currentArea
				Element doorNode = save.createElement("door");		//create a node to represent it
				doorNode.setAttribute("id", door.getId());		//mark this doorNode with an id attribute
				
				doorNode.appendChild(saveID(door));				//save the door's respective fields and append them to the doorNode  
				doorNode.appendChild(saveOpen(door));
				doorNode.appendChild(saveLocked(door));
				doorNode.appendChild(saveKeyID(door));
				doorNode.appendChild(saveDescription(door));
				
				doorNode.appendChild(saveSideA(door));
				doorNode.appendChild(saveTokenA(door));
				doorNode.appendChild(saveSideAPos(door));
				doorNode.appendChild(saveSideAEntryPos(door));
				
				doorNode.appendChild(saveSideB(door));
				doorNode.appendChild(saveTokenB(door));
				doorNode.appendChild(saveSideBPos(door));
				doorNode.appendChild(saveSideBEntryPos(door));
				
				roomNode.appendChild(doorNode);			//append the door node and its children to the parent roomNode
			}
		}
	}
	
	/**
	 * Saves the spawn name of a NonHumanPlayer into a node
	 * 
	 * @param nhp -- the NonHumanPlayer whose name is required
	 * @return spawnName -- the Node with the NonHumanPlayers spawnName
	 */
	public Element saveNonHumanSpawnName(NonHumanPlayer nhp){
		Element spawnName = save.createElement("spawnName");	//create a node called spawnName
		Text value = save.createTextNode("null");	//NonHumans do not have a spawn name, so append the String "null" to avoid NullPointers
		spawnName.appendChild(value);	//append the two Nodes
		return spawnName; 	//return the final Node
	}
	
	/**
	 * Save the "A side" of a given door. DoorGOs have two sides, so the sideA refers to one of these sides.
	 * 
	 * @param door -- the DoorGO whose side is required
	 * @return sideA -- the Node containing the DoorGOs sideA
	 */
	public Element saveSideA(DoorGO door){
		Element sideA = save.createElement("sideA");	//create a node called sideA
		Text value = save.createTextNode(door.getSideA());		//create a node with the doors sideA value
		sideA.appendChild(value);	//append the two Nodes
		return sideA;		//return the final Node
	}
	
	/**
	 * Save the "B side" of a given door. DoorGOs have two sides, so the sideB refers to one of these sides.
	 * 
	 * @param door -- the DoorGO whose side is required
	 * @return sideB -- the Node containing the DoorGOs sideB
	 */
	public Element saveSideB(DoorGO door){
		Element sideB = save.createElement("sideB");	//create a node called sideB
		Text value = save.createTextNode(door.getSideB());		//create a node with the doors sideB value
		sideB.appendChild(value);	//append the two Nodes
		return sideB;		//return the final Node
	}
	
	/**
	 * Save the "A side" token of the door.
	 * 
     * @param door -- the DoorGO whose side is required
	 * @return sideB -- the Node containing the DoorGOs sideAToken
	 */
	public Element saveTokenA(DoorGO door){
		Element tokenA = save.createElement("tokenA");		//
		Text value = save.createTextNode(door.getTokenA());
		tokenA.appendChild(value);
		return tokenA;
	}
	
	public Element saveTokenB(DoorGO door){
		Element tokenB = save.createElement("tokenB");
		Text value = save.createTextNode(door.getTokenB());
		tokenB.appendChild(value);
		return tokenB;
	}
	
	public Element saveSideAPos(DoorGO door){
		Element sideAPos = save.createElement("sideAPos");
		Element x = save.createElement("x");
		Element y = save.createElement("y");
		x.appendChild(save.createTextNode("" + door.getSideAPos().getPosX()));
		y.appendChild(save.createTextNode("" + door.getSideAPos().getPosY()));
		sideAPos.appendChild(x);
		sideAPos.appendChild(y);
		return sideAPos;
	}
	
	public Element saveSideBPos(DoorGO door){
		Element sideBPos = save.createElement("sideBPos");
		Element x = save.createElement("x");
		Element y = save.createElement("y");
		x.appendChild(save.createTextNode("" + door.getSideBPos().getPosX()));
		y.appendChild(save.createTextNode("" + door.getSideBPos().getPosY()));
		sideBPos.appendChild(x);
		sideBPos.appendChild(y);
		return sideBPos;
	}
	
	public Element saveSideAEntryPos(DoorGO door){
		Element sideAEntryPos = save.createElement("sideAEntryPos");
		Element x = save.createElement("x");
		Element y = save.createElement("y");
		x.appendChild(save.createTextNode("" + door.getSideAEntryPos().getPosX()));
		y.appendChild(save.createTextNode("" + door.getSideAEntryPos().getPosY()));
		sideAEntryPos.appendChild(x);
		sideAEntryPos.appendChild(y);
		return sideAEntryPos;
	}
	
	public Element saveSideBEntryPos(DoorGO door){
		Element sideBEntryPos = save.createElement("sideBEntryPos");
		Element x = save.createElement("x");
		Element y = save.createElement("y");
		x.appendChild(save.createTextNode("" + door.getSideBEntryPos().getPosX()));
		y.appendChild(save.createTextNode("" + door.getSideBEntryPos().getPosY()));
		sideBEntryPos.appendChild(x);
		sideBEntryPos.appendChild(y);
		return sideBEntryPos;
	}
	
	
	public Element saveBaseGameObject(GameObject occupant){
		
		Element base = save.createElement("base");
		GameObject baseObject = ((MarkerGO)occupant).getBaseGO();
		base.setAttribute("objectType", baseObject.getClass().toString().substring(11));
		
		if(baseObject instanceof FixedGO){
			base.appendChild(saveID(baseObject));
			base.appendChild(saveToken(baseObject));
			base.appendChild(savePosition(baseObject));
			base.appendChild(saveDescription(baseObject));
		}
		
		return base;
		
	}
	
	public Element saveContents(GameObject occupant){
		Text value = save.createTextNode("");
		Element contents = save.createElement("contents");
		
		List<InventoryGO> items = ((FixedContainerGO) occupant).getAllItems();
		for (InventoryGO i : items){
			Element item = save.createElement("item");
			item.setAttribute("objectType", i.getClass().toString());
			if(i instanceof ContainerGO){
				contents.appendChild(saveSizeRemaining(i));
			}
			item.appendChild(saveName(i));
			item.appendChild(saveID(i));
			item.appendChild(saveToken(i));
			item.appendChild(saveSize(i));
			item.appendChild(saveAreaName(i));
			item.appendChild(savePosition(i));
			item.appendChild(saveDescription(i));
			
			contents.appendChild(item);			
		}
		
		return contents;
		
	}
	
	public Element saveSizeRemaining(GameObject container){
		Text value = save.createTextNode("");
		Element sizeRemaining = save.createElement("sizeRemaining");
		
		if(container instanceof ContainerGO){
			value = save.createTextNode("" + ((ContainerGO)container).getSizeRemaining());
			sizeRemaining.appendChild(value);
		}else if(container instanceof FixedContainerGO){
			value = save.createTextNode("" + ((FixedContainerGO)container).getSizeRemaining());
			sizeRemaining.appendChild(value);
		}
		
		return sizeRemaining;
	}
	
	public Element saveName(GameObject occupant){
		Text value = save.createTextNode("");
		Element name = save.createElement("name");
		if(occupant instanceof InventoryGO){
			value = save.createTextNode(((InventoryGO) occupant).getName());
		} else if(occupant instanceof NonHumanPlayer){
			value = save.createTextNode(((NonHumanPlayer) occupant).getPlayerName());
		} else if(occupant instanceof FixedContainerGO){
			value = save.createTextNode(((FixedContainerGO) occupant).getName());
		} else if(occupant instanceof Player){
			value = save.createTextNode(((Player) occupant).getPlayerName());
		}
		
		name.appendChild(value);
		return name;
	}
	
	public Element saveID(GameObject occupant){
		Text value = save.createTextNode("");
		Element id = save.createElement("id");
		if(occupant instanceof InventoryGO){
			value = save.createTextNode(((InventoryGO) occupant).getId());
		}else if(occupant instanceof FixedGO){
			value = save.createTextNode(((FixedGO) occupant).getId());
		}else if(occupant instanceof FixedContainerGO){
			value = save.createTextNode(((FixedContainerGO) occupant).getId());
		}else if(occupant instanceof MovableGO){
			value = save.createTextNode(((MovableGO) occupant).getId());
		}
		id.appendChild(value);
		return id;
	}
	
	public Element saveKeyID(GameObject occupant){
		Text value = save.createTextNode("");
		Element keyID = save.createElement("id");
		if(occupant instanceof FixedContainerGO){
			if(((FixedContainerGO) occupant).getKeyID() == null){
				value = save.createTextNode("null");
			}else{
				value = save.createTextNode(((FixedContainerGO) occupant).getKeyID());
			}
			
		}
		keyID.appendChild(value);
		return keyID;
	}
	
	public Element savePosition(Tile currentTile){
		Element pos = save.createElement("pos");
		Element x = save.createElement("x");
		x.appendChild(save.createTextNode("" + currentTile.getPosition().getPosX()));
		Element y = save.createElement("y");
		y.appendChild(save.createTextNode("" + currentTile.getPosition().getPosY()));
		
		pos.appendChild(x);
		pos.appendChild(y);
		
		return pos;
	}
	
	public Element savePosition(GameObject occupant){
		Element pos = save.createElement("pos");
		Element x = save.createElement("x");
		Element y = save.createElement("y");
		Text xVal = save.createTextNode("" + occupant.getPosition().getPosX());
		Text yVal = save.createTextNode("" + occupant.getPosition().getPosY());
		x.appendChild(xVal);
		y.appendChild(yVal);
		pos.appendChild(x);
		pos.appendChild(y);
		return pos;
	}
	
	public Element saveToken(GameObject occupant){
		Element token = save.createElement("token");
		token.appendChild(save.createTextNode(occupant.getToken()));
		return token;
	}
	
	public Element saveToken(Tile currentTile){
		Element token = save.createElement("token");
		token.appendChild(save.createTextNode(currentTile.getToken()));
		return token;
	}
	
	public Element saveOpen(GameObject occupant){
		Text value = save.createTextNode("");
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
		Text value = save.createTextNode("");
		if(occupant instanceof DoorGO){
			value = save.createTextNode("" + ((DoorGO) occupant).isLocked());
		}else if (occupant instanceof FixedContainerGO){
			value = save.createTextNode("" + ((FixedContainerGO) occupant).isLocked());
		}
		Element locked = save.createElement("locked");
		locked.appendChild(value);
		return locked;
		
	}
	
	public Element saveDescription(GameObject occupant){
		Text value = save.createTextNode("");
		if(occupant instanceof InventoryGO){
			value = save.createTextNode(((InventoryGO) occupant).getDescription());
		}else if (occupant instanceof DoorGO){
			value = save.createTextNode(((DoorGO) occupant).getDescription());
		}else if (occupant instanceof FixedGO){
			value = save.createTextNode(((FixedGO) occupant).getDescription());			
		}else if (occupant instanceof MarkerGO){
			value = save.createTextNode(((MarkerGO) occupant).getDescription());			
		}else if (occupant instanceof Player){
			value = save.createTextNode(((Player) occupant).getDescription());			
		}else if (occupant instanceof MovableGO){
			value = save.createTextNode(((MovableGO) occupant).getDescription());			
		}
		
		Element description = save.createElement("description");
		description.appendChild(value);
		return description;
	}
	
	public Element saveSize(GameObject occupant){
		Text value = save.createTextNode("");
		if(occupant instanceof InventoryGO){
			value = save.createTextNode("" + ((InventoryGO) occupant).getSize());
		}else if (occupant instanceof FixedContainerGO){
			value = save.createTextNode("" + ((FixedContainerGO) occupant).getSize());
		}
			Element size = save.createElement("size");
			size.appendChild(value);
			return size;
	}
	
	public Element saveAreaName(GameObject occupant){
		Text value = save.createTextNode("");
		if(occupant instanceof InventoryGO){
			value = save.createTextNode(((InventoryGO) occupant).getAreaName());
		}else if (occupant instanceof MovableGO){
			value = save.createTextNode(((MovableGO) occupant).getAreaName());
		}else if (occupant instanceof NonHumanPlayer){
			value = save.createTextNode((((NonHumanPlayer) occupant).getCurrentArea().getAreaName()));
		}else if (occupant instanceof FixedContainerGO){
			value = save.createTextNode((((FixedContainerGO) occupant).getArea()));
		}else if (occupant instanceof Player){
			value = save.createTextNode((((Player) occupant).getCurrentArea().getAreaName()));
		}
		Element areaName = save.createElement("areaName");
		areaName.appendChild(value);
		return areaName;

	}
	
	public Document loadXML(){
		try{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		    Document document = docBuilder.parse(new File("saveNew.xml"));
		    return document;
		}catch(SAXException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}catch(ParserConfigurationException ex){
			ex.printStackTrace();
		}
		return null;
	}
	

	public void createXML(){
		try{
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			//transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "save.dtd");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(save);
			StreamResult result = new StreamResult(new File("saveNew.xml"));
			transformer.transform(source, result);
			// Output to console for testing
			StreamResult consoleResult =new StreamResult(System.out);
			transformer.transform(source, consoleResult);
			
			load();
		}catch(TransformerException e){
			e.printStackTrace();
		}
	}
	
}


	


