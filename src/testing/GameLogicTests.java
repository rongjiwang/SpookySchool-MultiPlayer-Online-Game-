package testing;

/**
 * JUnit tests for the game logic package.
 * @author Pritesh R. Patel
 * @author Rongji Wang
 */
public class GameLogicTests {

	/*
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
	
	//Add item to player inventory
	@Test
	public void playerInventoryAddingTest(){
		SpookySchool game = new SpookySchool();
		game.addPlayer("abc");
		Player p = game.getPlayer("abc");
		//Test Inventory should be empty
		assertTrue(p.getInventory().isEmpty() == true);
		// Adding one item to Inventory
		p.addToInventory(new InventoryGO("key","id1","token1",1,"area1",new Position(1,1),"Master Key"));
		// Test Inventory should not be empty
		assertFalse(p.getInventory().isEmpty());
		// Adding second item for size checking
		p.addToInventory(new InventoryGO("Book","id2","token2",2,"area2",new Position(2,2),"JAVA BOOK"));
		// Test inventory size
		assertTrue(p.getInventory().size() == 2);
		// Test item description
		assertTrue(p.getInventory().get(0).getDescription().equals("Master Key"));
		// Test for duplication
		assertFalse(p.getInventory().get(1).getDescription().equals("Master Key"));
				
	}
	
	//Delete item from player inventory
	@Test
	public void playerInventoryDeletingTest(){
		SpookySchool game = new SpookySchool();
		game.addPlayer("abc");
		Player p = game.getPlayer("abc");		
		// Adding one item to Inventory
		p.addToInventory(new InventoryGO("key","id1","token1",1,"area1",new Position(1,1),"Master Key"));
		assertTrue(p.getInventory().size() == 1);
		// drop the same item
		game.processDrop("abc", "id1");
		// player inventory should be empty after item drop. 
		assertTrue(p.getInventory().isEmpty());		
		// Adding two items
		p.addToInventory(new InventoryGO("key","id1","token1",1,"area1",new Position(1,1),"Master Key"));
		p.addToInventory(new InventoryGO("Book","id2","token2",2,"area2",new Position(2,2),"JAVA BOOK"));
		assertTrue(p.getInventory().size()==2);
		// Drop wrong item id
		game.processDrop("abc", "WRONG ID");
		assertTrue(p.getInventory().size() == 2);
		// Drop item by using wrong player name
		game.processDrop("WRONG PLAYER NAME", "id2"); //FIXME
		assertTrue(p.getInventory().size() == 2);
	
	}
	
	// Player direction test
	@Test
	public void playerDirectionTest(){
		SpookySchool game = new SpookySchool();
		game.addPlayer("abc");
		Player p = game.getPlayer("abc");
		// Player turn to the current direction
		assertTrue(game.movePlayer(p, p.getDirection()));
		// Player turn to a different direction
		if(!p.getDirection().equalsIgnoreCase("EAST")){
			assertTrue(game.movePlayer(p, "EAST"));
		}
		else{
			assertTrue(game.movePlayer(p, "NORTH"));
		}
		// insert non-direction string
		assertFalse(game.movePlayer(p, "WRONG DIRECTION"));//FIXME
		
	}
	
	// Player to next room
	@Test
	public void playerGoToNextRoomTest(){ //FIXME
		//processDoorMovement
		SpookySchool game = new SpookySchool();
		game.addPlayer("abc");
		Player p = game.getPlayer("abc");
		System.out.println(game.getDoorObjects().size());
		// GET DOOR FROM CURRENT AREA
		// TRY TO GET THROUGH IT TO ANOTHER AREA
		// NEED HELP FROM Pritesh FIXME
		//game.getPotentialTile(p.getCurrentArea(), game.get, "NORTH", 1);
		//game.processDoorMovement((Tile)(game.getDoorObjects().get(0)), p);
		
	}
	
	
	// Player bundle
	@Test
	public void playerBundleTest(){
		SpookySchool game = new SpookySchool();
		game.addPlayer("abc");
		Player p = game.getPlayer("abc");
		// player bundle should not be null
		assertTrue(game.getBundle("abc") != null);
	}
	
	// Game chat
	@Test
	public void gameChatLogTest(){
		SpookySchool game = new SpookySchool();
		game.addPlayer("abc");
		Player p = game.getPlayer("abc");
		// distribute message from game side
		game.addChatLogItemToAllBundles("Hello");
		// distribute message from player side
		game.getBundle("abc").addToChatLog("World");
		// check for player receiving
		assertTrue(game.getBundle("abc").getLog().size() == 2);
		assertTrue(game.getBundle("abc").getLog().get(0).equals("Hello"));
		assertTrue(game.getBundle("abc").getLog().get(1).equals("World"));
		
	}
	
	// Game state
	@Test
	public void gameStateTest(){
		SpookySchool game = new SpookySchool();
		// area maps should not be empty
		assertFalse(game.getAreas().isEmpty());
		// Movable objects should not be empty
		assertFalse(game.getMovableObjects().isEmpty());//FIXME
		// Door objects should not be empty
		assertFalse(game.getDoorObjects().isEmpty());
		// Inventory objects should be empty
		assertTrue(game.getInventoryObjects().isEmpty());//FIXME
		// No player exists until join
		assertTrue(game.getPlayers().isEmpty());
	}
	
	// Room search
	@Test
	public void roomSearchTest(){
		SpookySchool game = new SpookySchool();
		game.addPlayer("abc");
		Player p = game.getPlayer("abc");
		// Should never get a empty room with player in it
		for(int i=0; i<10; i++){
			Area a = game.findEmptySpawnRoom();
			assertFalse(p.getCurrentArea().getAreaName().equals(a.getAreaName()));							
		}
	}
	*/

}
