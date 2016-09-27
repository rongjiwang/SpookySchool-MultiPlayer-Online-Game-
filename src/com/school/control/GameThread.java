package com.school.control;

import java.net.SocketException;

import com.school.game.SpookySchool;
import com.school.ui.MainFrame;

/**
 * The Clock Thread is responsible for producing a consistent "pulse" which is
 * used to update the game state, and refresh the display. Setting the pulse
 * rate too high may cause problems, when the point is reached at which the work
 * done to service a given pulse exceeds the time between pulses.
 *
 * @author rongji wang
 *
 */
public class GameThread extends Thread {



	private SpookySchool game;
	private Server server;

	public Server getServer() {
		return server;
	}

	public SpookySchool getGame() {
		return game;
	}

	public GameThread() {
		 game = new SpookySchool();
		try {
			 server = new Server(game);
			server.start();
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Server-> Main game thread
	 */
	public void run() {
		while (true) {
			try {
				Thread.sleep(20000);
				System.out.println("**main game thread**");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}
