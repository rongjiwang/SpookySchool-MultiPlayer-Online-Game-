package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class InventoryPanel extends JPanel implements MouseListener, MouseMotionListener{
	//private List<InvButton> invButtons;
	private List<ItemDisplay> itemList;
	private List<ItemDisplay> itemsShown;
	final static Dimension boardSize = new Dimension(250,150);
	private boolean highlighted;
	private int highlightedX;
	private int highlightedY;
	private Image highLight;
	private int dragged = -1;

	//used when up/down buttons pressed
	private int level;


	public InventoryPanel(){
		super(new GridLayout(3,5));
		itemList = new ArrayList<ItemDisplay>();
		itemsShown = new ArrayList<ItemDisplay>();
		this.level = 0;
		this.setPreferredSize(boardSize);
		highLight = new ImageIcon(this.getClass().getResource("UIimages/highlight.png")).getImage();

		//	invButtons = new ArrayList<InvButton>();
		addMouseListener(this);
		addMouseMotionListener(this);
		//create buttons, which will then populate the gridlayout, passing the correct coordinates 
		/*for(int i = 0; i < 3; i ++){
			for(int j = 0; j < 5; j++){
				InvButton iButton = new InvButton(i, j, this);
				invButtons.add(iButton);
				add(iButton);
			}
		}*/

		setOpaque(false);
		setVisible(true);
		validate();
	}

	public void addItems(List<String> items){
		itemList.clear();
		for(String item : items){
			itemList.add(new ItemDisplay(item));
		}
		System.out.println("Items added");
		processItems();
	}

	/**
	 * place items onto correct x,y coordinates, in the 5x3 grid
	 */
	public void processItems(){
		//clear current items
		for(ItemDisplay item : itemsShown){
			item.removeDisplay();
		}
		itemsShown.clear();

		int i = 0;

		ItemDisplay toAdd = null;
		for(int j = 0; j < 3; j++){ //row
			for(int k = 0; k < 5; k++){ //column

				//ensure we can never try access a non-existant item
				if(i >= itemList.size()){
					return;
				}
				toAdd = itemList.get(i);
				toAdd.setDisplay(k*50, j*50);
				itemsShown.add(toAdd);
				i++;
			}
		}	
	}

	/**
	 * Highlight a particular square of the InventoryPanel
	 * 
	 * @param x
	 * @param y
	 */
	public void highlight(int x, int y){
		highlighted = true;
		highlightedX = x*50;
		highlightedY = y*50;
		repaint();
	}

	/**
	 * Remove any highlighting (when mouse moves off InventoryPanel
	 */
	public void unhighlight(){
		highlighted = false;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		if(highlighted){
			g.drawImage(highLight, highlightedX, highlightedY, highLight.getWidth(null), highLight.getHeight(null), null);
		}
		Image image = null;
		ItemDisplay item = null;
		for(int i = (level*5); i < (level*5)+15; i++){
			if(i >= itemList.size())
				break;
			item = itemList.get(i);
			image = new ImageIcon(this.getClass().getResource("itemimages/"+item.getName()+".png")).getImage();
			//if(dragged != i){
				g.drawImage(image, item.getX(), item.getY(), image.getWidth(null), image.getHeight(null), null);
			//} 
					
		}
		//if(dragged != -1){
		//	g.drawImage(image, item.getTempX(), item.getTempY(), image.getWidth(null), image.getHeight(null), null);
		//}
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		highlightedX=(e.getX()/50)*50;
		highlightedY=(e.getY()/50)*50;
	/**	if(dragged != -1){
			itemsShown.get(dragged).setTemp(e.getX()-25, e.getY()-25);
			
			System.out.println("Loc: "+itemsShown.get(dragged).getTempX()+","+itemsShown.get(dragged).getTempY());
		}**/
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		highlightedX=(e.getX()/50)*50;
		highlightedY=(e.getY()/50)*50;
		repaint();

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)){
			System.out.println("Right mouse is pressed");
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int index = e.getX()/50 + ((e.getY()/50)*5);
	//	System.out.println(index);
		if(itemsShown.size() > index && index >= 0){
			dragged = index;
			//itemsShown.get(dragged).changeDragging();
		//	System.out.println(itemsShown.get(dragged).getName());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//ensure if e.getX() or e.getY() out of bounds that it does something
		if(dragged == -1){
			System.out.print("Dragged nothing onto ");
		} else {
			System.out.print("Dragged "+itemsShown.get(dragged).getName()+" onto ");
		}
		
		int secondIndexs = e.getX()/50 + ((e.getY()/50)*5);
		if(itemsShown.size() > secondIndexs && secondIndexs >= 0){
			System.out.println(itemsShown.get(secondIndexs).getName());
		} else {
			System.out.println("nothing");
		}
		//	System.out.println(itemsShown.get(secondIndexs).getName());
		//System.out.println(e.getX()/50 + ((e.getY()/50)*5));
		
		//if we are dragging an item
		if(dragged != -1){
			//itemsShown.get(dragged).changeDragging();
			dragged = -1;
		} 
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		highlightedX=(e.getX()/50)*50;
		highlightedY=(e.getY()/50)*50;
		highlighted = true;
		repaint();

	}

	@Override
	public void mouseExited(MouseEvent e) {
		highlighted = false;
		if(dragged != -1){
			//itemsShown.get(dragged).changeDragging();
			dragged = -1;
		} 
		repaint();

	}


}
