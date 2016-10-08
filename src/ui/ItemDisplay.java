package ui;

import game.ContainerGO;
import game.InventoryGO;

public class ItemDisplay {
	private InventoryGO item;
	private int x;
	private int y;
	private int tempX;
	private int tempY;
	private boolean dragging;
	private boolean display;
	boolean container;
	
	public ItemDisplay(InventoryGO item){
		this.item = item;
		container = (item instanceof ContainerGO);
			
		x = 0;
		y = 0;
		dragging = false;
		display = false;
	}
	
	public boolean isContainer(){
		return this.container;
	}
	
	public void setTemp(int x, int y){
		this.tempX = x;
		this.tempY = y;
	}
	
	public int getTempX(){
		return tempX;
	}
	
	public int getTempY(){
		return tempY;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}

	public String getName(){
		return item.getName();
	}
	
	public String getID(){
		return item.getId();
	}
	
	public String getToken(){
		return item.getToken();
	}
	
	public String getDescription(){
		return item.getDescription();
	}
	
	public void changeDragging(){
		this.dragging = !this.dragging;
	}
	
	public void removeDisplay(){
		this.display=false;
		setX(-100);
		setY(-100);
	}
	
	public void setDisplay(int x, int y){
		setX(x);
		setY(y);
		this.display=true;
	}
	
	public boolean equals(InventoryGO item){
		return item.getId().equals(this.getID());
	}
}
