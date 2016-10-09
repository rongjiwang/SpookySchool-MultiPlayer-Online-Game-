package parser;

import org.w3c.dom.*;

import game.Area;
import game.DoorGO;
import game.FixedContainerGO;
import game.FixedGO;
import game.FloorTile;
import game.GameObject;
import game.InventoryGO;
import game.MarkerGO;
import game.MovableGO;
import game.Player;
import game.SpookySchool;
import game.Tile;
import game.WallTile;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Parser {
	
	private Document load;
	private Document save;
	private Element root;
	
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
		Map<String, InventoryGO> inventObjects = game.getInventoryObjects();
		
		saveMap(areas);
		
		
		createXML();
		
		
	}
	
	public void load(){
		
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
		
		saveTiles(currentArea, roomNode);
		
		return roomNode;
	}
	
	public void saveTiles(Area currentArea, Element currentParent){
		Tile[][] tiles = currentArea.getArea();
		
		for(int i = 0; i < currentArea.getArea().length; i++){
			for (int j = 0; j < currentArea.getArea()[i].length; j++){
				Element tileNode = save.createElement("tile");
				Tile currentTile = currentArea.getArea()[i][j];
				
				if (currentTile == null){
					tileNode.appendChild(save.createTextNode("null"));
					currentParent.appendChild(tileNode);
				}else{
				
					if(currentTile instanceof WallTile){
						savePosition(currentTile);
						saveToken(currentTile);
						//set occupant to null
					}else if (currentTile instanceof FloorTile && currentTile.getOccupant() != null){
						GameObject occupant = currentTile.getOccupant();
						Element occupantNode = save.createElement("occupant");
						occupantNode.setAttribute("objectType", occupant.getClass().toString());
						tileNode.appendChild(occupantNode);
						
						
						if(occupant instanceof DoorGO){
							saveDoorGOdata(occupant, tileNode);					
							
						}else if(occupant instanceof FixedContainerGO){
							saveFixedContData(occupant, tileNode);
							
						}else if(occupant instanceof FixedGO){
							saveFixedData(occupant, tileNode);
							
						}else if(occupant instanceof InventoryGO){
							saveInventData(occupant, tileNode);
							
						}else if(occupant instanceof MarkerGO){
							saveMarkerData(occupant, tileNode);
							
						}else if(occupant instanceof MovableGO){
							saveMovableData(occupant, tileNode);
					}
				}
			}
		}
		}
		
	}
	
	public void saveMovableData(GameObject occupant, Element parent) {
		//saveID(occupant);
		parent.appendChild(saveToken(occupant));
		saveDescription(occupant);
		saveAreaName(occupant);
		savePosition(occupant);
		
	}

	public void saveMarkerData(GameObject occupant, Element parent) {
		//saveBase(occupant);
		savePosition(occupant);
		saveDescription(occupant);
		
	}

	public void saveInventData(GameObject occupant, Element parent) {
		//saveName(occupant);
		//saveID(occupant);
		saveToken(occupant);
		saveSize(occupant);
		saveAreaName(occupant);
		savePosition(occupant);
		saveDescription(occupant);
		
		
	}

	public void saveFixedData(GameObject occupant, Element parent) {
		//saveID(occupant);
		saveToken(occupant);
		savePosition(occupant);
		saveDescription(occupant);
		
	}

	public void saveFixedContData(GameObject occupant, Element parent) {
		//saveName(occupant);
		saveAreaName(occupant);
		//saveID(occupant);
		saveToken(occupant);
		saveOpen(occupant);
		saveLocked(occupant);
		//saveKeyID(occupant);
		savePosition(occupant);
		//saveContents(occupant);
		saveSize(occupant);
		//saveSizeRemaining(occupant);
		saveDescription(occupant);
		
	}

	public void saveDoorGOdata(GameObject occupant, Element parent) {
		//saveID(occupant);
		saveOpen(occupant);
		saveLocked(occupant);
		//saveKeyID(occupant);
		saveDescription(occupant);
		
		//saveSideA(occupant);
		//saveTokenA(occupant);
		//saveSideAPos(occupant);
		//saveSideAEntryPos(occupant);
		
		//saveSideB(occupant);
		//saveTokenB(occupant);
		//saveSideBPos(occupant);
		//saveSideBEntryPos(occupant);
		
		
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
		x.appendChild(save.createTextNode("" + occupant.getPosition().getPosX()));
		y.appendChild(save.createTextNode("" + occupant.getPosition().getPosY()));
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


	


