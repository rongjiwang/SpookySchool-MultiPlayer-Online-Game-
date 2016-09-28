package com.school.control;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import com.school.game.SpookySchool;
import com.school.ui.MainFrame;

public final class Client extends Thread {
	private Socket sock;
	private DataOutputStream output;
	private DataInputStream input;
	private PacketParser parser;
	private int uid;
	private SpookySchool game;
	private DatagramSocket socket;
	private InetAddress ipAddress;
	private final int port = 5000;
	private Server server;
	private String name;

	// Testing commit to repository - ignore comment

	public Client(DatagramSocket socket,Server server,String name) throws IOException {
		this.socket = socket;
		this.server = server;
		this.name = name;
		this.ipAddress = InetAddress.getLocalHost();
	}


	public void run() {
		//create board
		MainFrame frame = new MainFrame(name,this);// change
		
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String msg = new String("Display receiving message" + " " + packet.getAddress() + " " + packet.getSocketAddress() + " "
					+ packet.getPort());
			String msg1 = new String(packet.getData());
			System.out.println(msg1+"PACKET FROM SERVER : " + msg);
			//action on frame when receive "pong"
			if (msg.trim().substring(0, 4).equalsIgnoreCase("pong")) {
				frame.closeWindow();
			}
			// this.parsePacket(packet.getData(),packet.getAddress(),packet.getPort());
		}

	}

	private void parsePacket(byte[] data, InetAddress address, int port) {
		String msg = new String(data).trim();
		// Player player = new Player("1");
	}

	public void sendData(byte[] data) {
		System.out.println("SETUP DATA TOWARDS SERVER");
		if(data == null){return;}
		DatagramPacket packet = new DatagramPacket(data, data.length, this.ipAddress, 5001);
		System.out.println(data.toString()+" "+this.ipAddress);

		try {
			System.out.println("BEFORE SEND DATA TO SERVER");

			socket.send(packet);
			System.out.println("AFTER SEND DATA TO SERVER");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DataOutputStream getOutput() {
		return output;
	}

	public String getPlayerName() {
		return name;
	}
	
	

}
