package testing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import game.Area;
import game.Player;
import game.Position;
import game.SpookySchool;


/**
 * JUnit tests for the game logic package.
 * @author Pritesh R. Patel
 *
 */
public class GameLogicTests {

	//Add a player with a unique name to the game.
	@Test
	public void addPlayerTest() {
		SpookySchool game = new SpookySchool();

		//Test that player is added to the bundle and the player list.
		assertTrue(game.addPlayer("player"));
		assertTrue(game.getPlayer("player") != null);
		assertTrue(game.getBundle("player") != null);

		//Test that player has a position.
		Player p = game.getPlayer("player");
		assertTrue(p.getPosition() != null);

		//Test that player is on the correct tile in the area.
		Area a = p.getCurrentArea();
		assertTrue(a.getTile(p.getCurrentPosition()).getOccupant().equals(p));

		//Test that the player is currently the owner of the spawn area.
		assertTrue(a.getOwner().getPlayerName().equals("player"));

	}

	//Add player with name that already exists in the game.
	@Test
	public void addPlayerTest2() {
		SpookySchool game = new SpookySchool();
		assertTrue(game.addPlayer("player"));
		assertFalse(game.addPlayer("player")); //Should not able able to add another player with same name.
	}


	//Remove a player that exists in the game.
	@Test
	public void removePlayerTest() {
		SpookySchool game = new SpookySchool();
		assertTrue(game.addPlayer("player"));

		//Information required to test player removal.
		Player p = game.getPlayer("player");
		Area a = p.getCurrentArea(); //Also the spawn area in this test.
		Position pos = p.getCurrentPosition();

		assertTrue(a.getTile(pos).getOccupant() != null);

		game.removePlayer("player");//Remove the player from the game.

		//Test player has has been removed from their tile.
		assertTrue(a.getTile(pos).getOccupant() == null);

		//Test player no longer has a bundle.
		assertTrue(game.getBundle("player") == null);

		//Test the spawn area no longer has an owner.


	}

	//Remove a player that does not exist in the game.
	@Test
	public void removePlayerTest2() {
		SpookySchool game = new SpookySchool();
		game.removePlayer("xyz"); //Should do nothing.
		assertTrue(game.getPlayer("xyz") == null);
	}

}
