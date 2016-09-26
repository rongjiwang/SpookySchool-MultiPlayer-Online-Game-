package com.school.control;

import java.util.ArrayList;


/**
 * The following does the parsing job in the client when it receives a message from the server.
 * @author kevin
 *
 */
public class PacketParser {

	
		private Client client;
		//private GameFrame frame;

		/**
		 * The following constructs a ClientParser for the given Client.
		 * @param c The Client.
		 */
		public PacketParser(Client c){
			client = c;
		}

		/**
		 * a setter for the GameFrame
		 * @param frame
		 */
//		public void setFrame(GameFrame frame) {
//		}

		/**
		 * The following processes the message received from the server
		 * @param message The message read from the server over the socket
		 */
		public void processMessage(char[] message){
			switch(message[0]){
			
			case 'A'://ip address and id
				readIP(message);
				break;
			case 'M'://map
				readMap(message);
				break;
			case 'I'://inventory information
				readInventory(message);
				break;
			case 'S'://container information
				readContainer(message);
				break;
			case 'R':
				readPlayer(message);
				break;
			case 'm':
				readMessageToDisplay(message);
				break;
			default:
			}
		}

		/*
		 * The following reads a message from the server and then display it on the frame
		 */
		private void readMessageToDisplay(char[] message) {
			
			//frame.displayMessageFromGame(messageToDisplay);

		}

		/*
		 * The following parses the available player and passes it to the initialisation
		 */
		private void readPlayer(char[] message) {
			//ArrayList<Player> team = new ArrayList<Player>();	
		}



		/*
		 * The following parses the container information and passes it to the frame
		 */
		private void readContainer(char[] message) {
			ArrayList<String> container = new ArrayList<String>();
			loop: for(int i=1; i<message.length; i++){
				switch(message[i]){
				case 'k':
					container.add("key");
					break;
				case '\0':
					container.add("emptyslot");
					break;

				default:
					System.out.println("ClientParser 157: unknown item: "+message[i]);//debug
					return;
				}
			}
			//frame.setContainerContents(container);
		}

		/*
		 * The following parses the inventory information and passes it to the frame
		 */
		private void readInventory(char[] message) {
			ArrayList<String> inventory = new ArrayList<String>();
			loop: for(int i=1; i<message.length; i++){
				switch(message[i]){
				case 'k':
					inventory.add("key");
					break;
				case '\0':
					inventory.add("emptyslot");
					break;
				case 'X':
					char[] newMessage = separateMessage(message, i);
					if(newMessage != null) processMessage(newMessage);
					break loop;
				default:
					System.out.println(message[i]);
				}
			}
			//frame.setInventoryContents(inventory);
		}

		/*
		 * This will separate the messages if more than one messages are read together in the client
		 */
		private char[] separateMessage(char[] message, int i){
			char[] newMessage = new char[message.length];
			return newMessage;
		}

		/**
		 * The following reads ip address and uid from the server and gives it to the gui
		 * @param message
		 */
		public void readIP(char[] message){

		}

		/**
		 * The following reads the map data from the server and gives it to the gui
		 * @param message
		 */
		public void readMap(char[] message){
//	read 2d array map and items for rendering 
//			if(frame != null){
//				frame.updateRenderer(type, map, items);
//			}
		}


	
}
