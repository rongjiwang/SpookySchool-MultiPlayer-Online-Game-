package parser;

import org.w3c.dom.*;

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
	
	private Document load;
	private Document save;
	private Element root;
	
	private List<DoorGO> doors; 
	private Player saver;
	private List<MovableGO> movables;
	private List<NonHumanPlayer> nonHumans;
	private Map<String, InventoryGO> inventObjects;
	List<InventoryGO> saversInvent;
	List<InventoryGO> itemsInContainers;
	
	public Parser(){
		
	}
	
	public Document createDocument(){
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			
			return doc;
			
		}catch(ParserConfigurationException e){
			e.printStackTrace();			
		}
		return null;
	}
	
	public void save(SpookySchool game, String playerName){
		
		save = createDocument();
		root = save.createElement("game");
		
		Map<String, Area> areas = game.getAreas();
		List<Player> players = game.getPlayers();
		this.inventObjects = game.getInventoryObjects();
		this.doors = game.getDoorObjects();
		this.saver = determinePlayer(playerName, players);
		this.movables = game.getMovableObjects();
		this.nonHumans = game.getNonHumanPlayers();
		
		
		saveMap(areas);
		
		//when saving areas, only save the WallTiles, FloorTiles, and nulltiles
		// before closing each room, save the moveables, nonhumans, and inventory obejcts on the map
		//
		//
		
		
		createXML();
		
		
	}
	
	public void load(){
		
	}
	
	public Player determinePlayer(String playerName, List<Player> players){
		for (Player p : players){
			if (p.getPlayerName().equals(playerName)){
				return p;
			}
		}
		
		return null; //should not get here
	}
	
	public void saveMap(Map<String, Area> areas){
		for (String key : areas.keySet()){
			Area currentArea = areas.get(key);
			root.appendChild(saveArea(currentArea));
		}
		save.appendChild(root);
	}
	
	public Element saveArea(Area currentArea){
		Element roomNode = save.createElement("room");
		Element heightNode = save.createElement("height");
		Element widthNode = save.createElement("width");
		
		heightNode.appendChild(save.createTextNode("" + currentArea.getArea()[0].length));
		widthNode.appendChild(save.createTextNode("" + currentArea.getArea().length));
		
		Element areaNameNode = save.createElement("areaName");
		areaNameNode.appendChild(save.createTextNode(currentArea.getAreaName()));
		
		roomNode.appendChild(heightNode);
		roomNode.appendChild(widthNode);
		roomNode.appendChild(areaNameNode);
		
		Element areaNode  = save.createElement("area");
		roomNode.appendChild(areaNode);
		
		saveTiles(currentArea, areaNode);
		saveDoors(currentArea, roomNode);
		saveMovables(currentArea, roomNode);
		saveNonHumans(currentArea, roomNode);
		saveInventoryGameObjects(currentArea, roomNode);
		saveFixedContainers(currentArea, roomNode);
		//saveFillContainers(currentArea, areaNode);
			//gets the inventory items that 
		
		return roomNode;
	}
	
	public void saveTiles(Area currentArea, Element currentParent){
		Tile[][] tiles = currentArea.getArea();
		
		for(int i = 0; i < currentArea.getArea().length; i++){
			for (int j = 0; j < currentArea.getArea()[i].length; j++){
				Element tileNode = save.createElement("tile");
				Tile currentTile = currentArea.getArea()[i][j];
				
				if (currentTile == null){
					tileNode.setAttribute("tileType", "black");
					tileNode.appendChild(save.createTextNode("null"));
					currentParent.appendChild(tileNode);
				}else{
					if(currentTile instanceof WallTile){
						tileNode.setAttribute("tileType", "WallTile");
						
					}else if(currentTile instanceof FloorTile){
						tileNode.setAttribute("tileType", "FloorTile");
						GameObject occupant = currentTile.getOccupant();
						Element occupantNode = save.createElement("occupant");
						if(occupant != null){
							occupantNode.setAttribute("objectType", occupant.getClass().toString().substring(11));

						}
						
						if(occupant instanceof FixedGO){
							occupantNode.appendChild(saveID(occupant));
							occupantNode.appendChild(saveToken(occupant));
							occupantNode.appendChild(savePosition(occupant));
							occupantNode.appendChild(saveDescription(occupant));
							
							tileNode.appendChild(occupantNode);
							
							
						}else if(occupant instanceof MarkerGO){
							occupantNode.appendChild(savePosition(occupant));
							occupantNode.appendChild(saveBaseGameObject(occupant));
							
							tileNode.appendChild(occupantNode);						}
						
					}
					tileNode.appendChild(savePosition(currentTile));
					currentParent.appendChild(tileNode);	
				}
					//currentParent.appendChild(tileNode);
				
			}
		}	
	}
	
	public void saveFixedContainers(Area currentArea, Element roomNode){
		
	}
	
	public void saveInventoryGameObjects(Area currentArea, Element roomNode){
		
		saversInvent = saver.getInventory();
		itemsInContainers = new ArrayList<>();
		
		
		for (String key : inventObjects.keySet()){
			InventoryGO currentObject = inventObjects.get(key);
			
			if(currentObject.getPosition() != null){
				//inventory item is on the floor
				Element inventoryObject = save.createElement("inventoryObject");
				inventoryObject.setAttribute("id", currentObject.getId());
				
				inventoryObject.appendChild(saveName(currentObject));
				inventoryObject.appendChild(saveID(currentObject));
				inventoryObject.appendChild(saveToken(currentObject));
				inventoryObject.appendChild(saveAreaName(currentObject));
				inventoryObject.appendChild(saveSize(currentObject));
				inventoryObject.appendChild(savePosition(currentObject));
				inventoryObject.appendChild(saveDescription(currentObject));
				
				roomNode.appendChild(inventoryObject);
			}else{
				if(!saversInvent.contains(currentObject)){
					itemsInContainers.add(currentObject);
				}
			}//STILL NEED TO FILL CONTAINERS
		}
	}
	
	public void saveNonHumans(Area currentArea, Element roomNode){
		for (NonHumanPlayer nhp : nonHumans){
			if (nhp.getCurrentArea().getAreaName().equals(currentArea.getAreaName())){
				Element nonHumanNode = save.createElement("nonHumanPlayer");
				nonHumanNode.setAttribute("name", nhp.getPlayerName());
				
				nonHumanNode.appendChild(saveName(nhp));	
				nonHumanNode.appendChild(saveNonHumanSpawnName(nhp));
				nonHumanNode.appendChild(saveAreaName(nhp));
				//when loading, load the Area not the areaName.
				nonHumanNode.appendChild(savePosition(nhp));
				
				roomNode.appendChild(nonHumanNode);
				
			}
		}
	}
	
	public void saveMovables(Area currentArea, Element roomNode){
		for (MovableGO object : movables){
			if(object.getAreaName().equals(currentArea.getAreaName())){
				Element movableNode = save.createElement("movableGO");
				
				movableNode.appendChild(saveID(object));
				movableNode.appendChild(saveToken(object));
				movableNode.appendChild(saveAreaName(object));
				movableNode.appendChild(savePosition(object));
				movableNode.appendChild(saveDescription(object));
				
				roomNode.appendChild(movableNode);
			}
		}
	}
	
	public void saveDoors(Area currentArea, Element roomNode){
		
		for(DoorGO door : doors){
			if (door.getSideA().equals(currentArea.getAreaName())){
				Element doorNode = save.createElement("door");
				doorNode.setAttribute("id", door.getId());
				
				doorNode.appendChild(saveID(door));
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
				
				roomNode.appendChild(doorNode);
			}
		}
		
	}
	
	public Element saveNonHumanSpawnName(NonHumanPlayer nhp){
		Element spawnName = save.createElement("spawnName");
		Text value = save.createTextNode("null");
		spawnName.appendChild(value);
		return spawnName;
	}
	
	public Element saveSideA(DoorGO door){
		Element sideA = save.createElement("sideA");
		Text value = save.createTextNode(door.getSideA());
		sideA.appendChild(value);
		return sideA;
	}
	
	public Element saveSideB(DoorGO door){
		Element sideB = save.createElement("sideB");
		Text value = save.createTextNode(door.getSideB());
		sideB.appendChild(value);
		return sideB;
	}
	
	public Element saveTokenA(DoorGO door){
		Element tokenA = save.createElement("tokenA");
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
		}
		id.appendChild(value);
		return id;
	}
	
	public Element saveKeyID(GameObject occupant){
		Text value = save.createTextNode("");
		Element keyID = save.createElement("id");
		if(occupant instanceof FixedContainerGO){
			value = save.createTextNode(((FixedContainerGO) occupant).getKeyID());
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
		}
		Element areaName = save.createElement("areaName");
		areaName.appendChild(value);
		return areaName;

	}
	

	public void createXML(){
		try{
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "save.dtd");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(save);
			StreamResult result = new StreamResult(new File("saveNew.xml"));
			transformer.transform(source, result);
			// Output to console for testing
			StreamResult consoleResult =new StreamResult(System.out);
			transformer.transform(source, consoleResult);
		}catch(TransformerException e){
			e.printStackTrace();
		}
	}
	
}


	


