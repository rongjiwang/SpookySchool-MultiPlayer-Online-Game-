package com.school.control;

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
public class ClockThread extends Thread {

	private final int delay;
	private final SpookySchool game;
	private final MainFrame display;

	public ClockThread(int delay, SpookySchool game, MainFrame display) {
		this.delay = delay;
		this.game = game;
		this.display = display;
	}

	/**
	 * Server-> Main game thread
	 */
	public void run() {
		while (true) {
			try {
				Thread.sleep(delay);
				//game.tick();//???
				
				if (display != null) {
					display.repaint();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}
