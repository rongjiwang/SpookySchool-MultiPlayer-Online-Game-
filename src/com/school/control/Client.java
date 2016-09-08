package com.school.control;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client extends Thread {
	private Socket sock;
	private OutputStreamWriter output;
	private InputStreamReader input;
	private PacketParser parser;

	public Client(Socket sock) throws IOException {
		this.sock = sock;

		output = new OutputStreamWriter(sock.getOutputStream());
		input = new InputStreamReader(sock.getInputStream());
		parser = new PacketParser(this);
	}

	public void run() {

		while (1 == 1) {
			// once get the message or action
			// call parser to execute
		}

	}
}
