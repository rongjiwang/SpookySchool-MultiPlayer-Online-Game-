package ui;


public class RenderGameObject {

	private String id;
	private String token;
	private int xPos;
	private int yPos;
	private int viewXPos;
	private int viewYPos;
	private int xBuff;
	private int yBuff;
	private String area;
	private boolean isPlayer;
	
	
	public RenderGameObject(String id, String token, int xPos, int yPos, boolean isPlayer, String area){
		this.id = id;
		this.area = area;
		this.token = token;
		this.xPos = xPos;
		this.yPos = yPos;
		this.isPlayer = isPlayer;
		
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
	
	//players only
	public void changeDirection(String direction){
		String sub = token.substring(0, token.length()-2);
		String i = "" + token.charAt(token.length()-1);
		switch(direction){
	
			case "NORTH": token = sub + 2 + i; 
			break;
		
			case "EAST": token = sub + 3 + i; 
			break;
		
			case "SOUTH": token = sub + 0 + i;
			break;
		
			case "WEST": token = sub + 1 + i;
			break;	
		
		}
	}
	
	public void appear(String area, String x, String y){
		xPos = Integer.valueOf(x);
		yPos = Integer.valueOf(y);
		this.area = area;
		
	}

// Getters and Setters
	
	public boolean isPlayer(){
		return isPlayer;
	}
	
	public String getID(){
		return id;
	}
	
	public void setID(String id){
		this.id = id;
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
	
	
	public int getXBuff(){
		return xBuff;
	}
	
	public void setXBuff(int xBuff){
		this.xBuff = xBuff;
	}
	
	public int getYBuff(){
		return yBuff;
	}
	
	public void setYBuff(int yBuff){
		this.yBuff = yBuff;
	}
	
	public String getArea(){
		return area;
	}
	
}
	