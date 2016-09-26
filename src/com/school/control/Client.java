package com.school.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.school.game.SpookySchool;

public final class Client extends Thread implements KeyListener {
	private Socket sock;
	private DataOutputStream output;
	private DataInputStream input;
	private PacketParser parser;
	private int uid;
	private SpookySchool game;
	private DatagramSocket socket;
	private InetAddress ipAddress;
	private final int port = 5000;

	// Testing commit to repository - ignore comment

	public Client(Socket sock) throws IOException {
		this.sock = sock;

		// output = new OutputStreamWriter(sock.getOutputStream());
		// input = new InputStreamReader(sock.getInputStream());
		// parser = new PacketParser(this);
	}

	public Client(SpookySchool game, String ipAddress) {
		this.game = game;
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("SERVER > " + new String(packet.getData()));
		}

	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// public void run() {
	//
	// try {
	// System.out.println("olleh");
	//
	// output = new DataOutputStream(sock.getOutputStream());
	// input = new DataInputStream(sock.getInputStream());
	//
	// uid = input.readInt();
	// boolean exit = false;
	// long totalRec = 0;
	// while (!exit) {
	// // read event
	// int amount = input.readInt();
	// byte[] data = new byte[amount];
	// input.readFully(data);
	// // game decode data
	// // repaint game
	// totalRec += amount;
	// // System.out.println("\rREC: " + (totalRec / 1024) + "KB ("
	// // + (rate(amount) / 1024) + "KB/s) TX: " + totalSent
	// // + " Bytes");
	//
	// }
	// sock.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
