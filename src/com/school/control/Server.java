package com.school.control;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.school.game.SpookySchool;
/**
 * Should receive events from client via socket.
 * Event should change the game board, eg player position.
 * Distributes current state of board to clients.
 * @author rongjiwang
 *
 */
public final class Server extends Thread {

	private final Socket sock;
	private final int uid;
	private final int delay;
	private final SpookySchool game;

	public Server(Socket sock, int uid, int delay, SpookySchool game) {
		this.sock = sock;
		this.uid = uid;
		this.delay = delay;
		this.game = game;

	}
	
	public void run(){
		try {
			DataInputStream input = new DataInputStream(sock.getInputStream());
			DataOutputStream output = new DataOutputStream(sock.getOutputStream());
			//write the period to the stream
			output.writeInt(uid);
			//write board detail
			boolean exit = false;
			while(!exit){
				if(input.available() != 0){
					//read direction event from client
					int dir = input.readInt();
					switch(dir){
					case 1:
						//use the game to move player position
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * the following always accept sockets connected from clients. New callable
	 * tasks based on the connection will be submitted in the thread pool.
	 */
//	public void run() {
//		
//		ExecutorService pool = Executors.newFixedThreadPool(50);
//		while (!exit) {
//			if (server.isClosed())
//				break;
//			try {
//				Socket connection = server.accept();
//				Callable<Void> task = new Task(connection);
//				pool.submit(task);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//		}
//	}

	/**
	 * The following creates a thread that is always listening to the client
	 * that establishes the socket connection
	 * 
	 * @author kevin
	 *
	 */
//	private class Task implements Callable<Void> {
//		private Socket connection;
//
//		public Task(Socket connection) {
//			this.connection = connection;
//		}
//
//		@Override
//		public Void call() throws Exception {
//
//			InputStreamReader in = new InputStreamReader(connection.getInputStream());
//			// could be a message or object
//			int receive = in.read();
//			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
//			// player = out;
//			sockets[1] = connection;
//
//			out.write("something");
//			out.flush();
//
//			return null;
//		}
//	}
}
