package com.school.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client extends Thread implements KeyListener {
	private Socket sock;
	private DataOutputStream output;
	private DataInputStream input;
	private PacketParser parser;
	private int uid;

	// Testing commit to repository - ignore comment

	public Client(Socket sock) throws IOException {
		this.sock = sock;

		// output = new OutputStreamWriter(sock.getOutputStream());
		// input = new InputStreamReader(sock.getInputStream());
		// parser = new PacketParser(this);
	}

	public void run() {

		try {
			output = new DataOutputStream(sock.getOutputStream());
			input = new DataInputStream(sock.getInputStream());

			uid = input.readInt();
			boolean exit = false;
			long totalRec = 0;
			while (!exit) {
				// read event
				int amount = input.readInt();
				byte[] data = new byte[amount];
				input.readFully(data);
				// game decode data
				// repaint game
				totalRec += amount;
				// System.out.println("\rREC: " + (totalRec / 1024) + "KB ("
				// + (rate(amount) / 1024) + "KB/s) TX: " + totalSent
				// + " Bytes");

			}
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

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
