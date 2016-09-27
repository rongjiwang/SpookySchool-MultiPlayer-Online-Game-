package com.school.xml;

import org.xml.sax.*;
import org.w3c.dom.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.*;


/** This class allows the program to parse through and read a given XML file.
 * After being read, the program will be able to create a game state from which
 * the user can play from.
 * 
 * @author Chethana Wijesekera
 *
 */
public class Parser {
	
		//Fields
		private int width;
		private int height;

		/** Constructor for the Parser, creates a Parser object to be used in the game
		 * 
		 */
		public Parser(){
			
		}
		
		
		/** Allows the game to read an XML file to load a game from a File. Used to 
		 * initiate a game from scratch by loading a room, and also to load a game
		 * from a saved state.
		 * 
		 * @param filename -- name for the file to be read from
		 */
		public void load(String filename){
			
			Document load = getDoc("src/com/areas/" + filename);
			
			//int width = load.getDocumentElement().
			
			
		}
		
		
		/** Retrieves a specified XML file for the parser to work with
		 * 
		 * @param path -- path to the specified file
		 * @return -- the specified Document file (XML)
		 */
		public Document getDoc(String path) {
			
			try{
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setIgnoringComments(true);
				factory.setIgnoringElementContentWhitespace(true);
				factory.setValidating(true);
				
				DocumentBuilder builder =  factory.newDocumentBuilder();
				
				return builder.parse(new InputSource(path));
				
			}catch(Exception e){
				System.err.println("Cannot open file of path " + path);
			}
			
			return null;
			
		}


		/** Takes all the information of the current state of the game and saves it to
		 * an XML file. This can be read from later when needed to be loaded
		 */
		public void save(){
			
		}
	

}
