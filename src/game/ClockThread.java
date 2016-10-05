package game;

/**
 * Clock Thread is a thread that is run in conjunction with the game. It is used to periodically call the tick method in the game 
 * object which can make necessary changes such as movements of NPCs.
 * @author Pritesh R. Patel
 *
 */
public class ClockThread extends Thread {

	private final int delay = 300; //How often (in miliseconds) to .
	private final SpookySchool game;

	public ClockThread(SpookySchool game) {
		this.game = game;
	}

	@Override
	public void run() {

		//Loop for ever.
		while (true) {

			try {
				Thread.sleep(delay);
				game.tick();
			} catch (InterruptedException e) {
				//Should never happen.
			}


		}
	}

}
