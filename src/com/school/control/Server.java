package com.school.control;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import com.school.game.SpookySchool;

/**
 * Should receive events from client via socket. Event should change the game
 * board, eg player position. Distributes current state of board to clients.
 * 
 * @author rongjiwang
 *
 */
public final class Server extends Thread {

	private Socket sock;
	private int uid;
	private int delay;
	private SpookySchool game;
	private String ipAddress;
	private DatagramSocket socket;
	private final int port = 5000;

	public Server(Socket sock, int uid, int delay, SpookySchool game) {
		this.sock = sock;
		this.uid = uid;
		this.delay = delay;
		this.game = game;

	}

	public Server(SpookySchool game) throws SocketException {
		this.game = game;
		try {
			this.socket = new DatagramSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void run(){
		System.out.println("hello");
		while(true){
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String msg = new String(packet.getData());
			if(msg.trim().equalsIgnoreCase("ping")){
				System.out.println("CLIENT **> " + msg);
				sendData("pong".getBytes(),packet.getAddress(), packet.getPort());
			}
		}

	}

	private void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data,data.length,ipAddress,port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public void run() {
//		try {			
//			System.out.println("hello");
//
//			DataInputStream input = new DataInputStream(sock.getInputStream());
//			DataOutputStream output = new DataOutputStream(sock.getOutputStream());
//			// write the period to the stream
//			output.writeInt(uid);
//			// write board detail
//			boolean exit = false;
//			while (!exit) {
//				if (input.available() != 0) {
//					// read direction event from client
//					int dir = input.readInt();
//					switch (dir) {
//					case 1:
//						// use the game to move player position
//						// game.getPlayer(uid).moveup;
//						break;
//					case 2:
//						break;
//					case 3:
//						break;
//					case 4:
//						break;
//					default:
//						System.err.println("Server->Game Execution Cmmand Error.");
//					}
//				}
//				// updated Board, Server -> Clients sending...
//				// byte[] state = board.toByteArray();
//				// output.writeInt(size of the array board);
//				// output.write(board);
//				output.flush();
//				Thread.sleep(delay);
//			}
//			sock.close();
//		} catch (IOException e) {
//			System.err.println("PLAYER " + uid + " DISCONNECTED");
//			// game.disconnectPlayer(uid);
//
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//	}

	/**
	 * the following always accept sockets connected from clients. New callable
	 * tasks based on the connection will be submitted in the thread pool.
	 */
	// public void run() {
	//
	// ExecutorService pool = Executors.newFixedThreadPool(50);
	// while (!exit) {
	// if (server.isClosed())
	// break;
	// try {
	// Socket connection = server.accept();
	// Callable<Void> task = new Task(connection);
	// pool.submit(task);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// }
	// }

	/**
	 * The following creates a thread that is always listening to the client
	 * that establishes the socket connection
	 * 
	 * @author kevin
	 *
	 */
	// private class Task implements Callable<Void> {
	// private Socket connection;
	//
	// public Task(Socket connection) {
	// this.connection = connection;
	// }
	//
	// @Override
	// public Void call() throws Exception {
	//
	// InputStreamReader in = new
	// InputStreamReader(connection.getInputStream());
	// // could be a message or object
	// int receive = in.read();
	// OutputStreamWriter out = new
	// OutputStreamWriter(connection.getOutputStream());
	// // player = out;
	// sockets[1] = connection;
	//
	// out.write("something");
	// out.flush();
	//
	// return null;
	// }
	// }
}
