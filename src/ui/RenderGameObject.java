package ui;


public class RenderGameObject {

	private String id;
	private String name;
	private String token;
	private int xPos;
	private int yPos;
	private int viewXPos;
	private int viewYPos;
	private boolean isPlayer;
	
	
	public RenderGameObject(String id, String name, String token, int xPos, int yPos, boolean isPlayer){
		this.isPlayer = isPlayer;
		if(isPlayer)
			this.id = name;
		else
			this.id = id;
		this.name = name;
		this.token = token;
		this.xPos = xPos;
		this.yPos = yPos;
		
	}
	
	public void move(String direction){
	
		switch(direction){
	
			case "NORTH": yPos--; 
			break;
		
			case "EAST": xPos++; 
			break;
		
			case "SOUTH": yPos++;
			break;
		
			case "WEST": xPos--;
			break;	
		
		}
	}
	
	public void changeDirection(String direction){
		String sub = token.substring(0, token.length()-1);
	
		switch(direction){
	
			case "NORTH": token = sub + 2; 
			break;
		
			case "EAST": token = sub + 3; 
			break;
		
			case "SOUTH": token = sub + 0;
			break;
		
			case "WEST": token = sub + 1;
			break;	
		
		}
	}

// Getters and Setters
	
	public String getID(){
		return id;
	}
	
	public void setID(String id){
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getToken(){
		return token;
	}
	
	public void setToken(String token){
		this.token = token;
	}
	
	public int getXPos(){
		return xPos;
	}
	
	public void setXPos(int xPos){
		this.xPos = xPos;
	}
	
	public int getYPos(){
		return yPos;
	}
	
	public void setYPos(int yPos){
		this.yPos = yPos;
	}
	
	public int getViewXPos(){
		return viewXPos;
	}
	
	public void setViewXPos(int viewXPos){
		this.viewXPos = viewXPos;
	}
	
	public int getViewYPos(){
		return viewYPos;
	}
	
	public void getViewYPos(int viewYPos){
		this.viewYPos = viewYPos;
	}
	
	public boolean isPlayer(){
		return isPlayer;
	}
	
	
	
}
	