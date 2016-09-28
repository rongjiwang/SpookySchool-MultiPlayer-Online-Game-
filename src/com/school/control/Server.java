package com.school.control;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.school.game.Bundle;
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
	private InetAddress ipAddress;
	private DatagramSocket socket;
	private int port;
	private List<Client> connections;


	public Server(SpookySchool game, int port) throws SocketException {
		this.game = game;
		this.port = port;
		connections = new ArrayList<>();
		try {
			this.socket = new DatagramSocket(port);
			this.ipAddress = socket.getInetAddress();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		System.out.println("hello");
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);

			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String msg = new String(packet.getData());
			System.out.println("PACKET FROM CLIENT: " + msg);
			if(msg.trim().equalsIgnoreCase("TEST NORTH")){
				String[] split = msg.split(" ");
				game.respond(split[0], split[1]);
				byte[] b =game.getBundleTest(split[0]);
				System.out.println(packet.getAddress()+"* *"+packet.getPort());
				DatagramPacket packet1 = new DatagramPacket(b, b.length,packet.getAddress(),packet.getPort());
				try {
					System.out.println("BEFORE SEND BACK TO CLIENT FROM SERVER");
					socket.send(packet1);
					System.out.println("AFTER SEND BACK TO CLIENT FROM SERVER");

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// this.parsePacket(packet.getData(), packet.getAddress(),
		}

	}


	private void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void add(Client c) {
		if (c == null) {
			return;
		}
		connections.add(c);
		game.addPlayer(c.getPlayerName());
	}

	public InetAddress getIpAddress() {
		return ipAddress;
	}

	public DatagramSocket getSocket() {
		return socket;
	}

	public int getPort() {
		return port;
	}
}
