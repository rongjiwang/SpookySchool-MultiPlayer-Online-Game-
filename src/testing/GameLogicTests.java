package testing;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import game.Area;
import game.Player;
import game.SpookySchool;

public class GameLogicTests {

	//Add a player with a unique name to the game.
	@Test
	public void addPlayerTest() {
		SpookySchool game = new SpookySchool();

		//Test that player is added to the bundle and the player list.
		assertTrue(game.addPlayer("test"));
		assertTrue(game.getPlayer("test") != null);
		assertTrue(game.getBundle("test") != null);

		//Test that player has a position.
		Player p = game.getPlayer("test");
		assertTrue(p.getPosition() != null);

		//Test that player is on the correct tile in the area.
		Area a = p.getCurrentArea();
		assertTrue(a.getTile(p.getCurrentPosition()).getOccupant().equals(p));
	}

}
