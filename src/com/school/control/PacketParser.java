package com.school.control;

public class PacketParser {

	private Client client;

	public PacketParser(Client client) {
		this.client = client;
	}

	public void matchMessage(String message) {
		switch (message) {

		case "11":
			actionMove1(message);
			break;
		case "22":
			actionMove2(message);
			break;
		default:
			// throw game exception
		}
	}

	private void actionMove2(String message) {
		// TODO Auto-generated method stub

	}

	private void actionMove1(String message) {
		// TODO Auto-generated method stub

	}
}
