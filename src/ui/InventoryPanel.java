package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;


public class InventoryPanel extends JPanel implements MouseListener, MouseMotionListener{
	private List<ItemDisplay> itemList;
	private List<ItemDisplay> itemsShown;
	final static Dimension boardSize = new Dimension(250,150);
	private boolean highlighted;
	private int highlightedX;
	private int highlightedY;
	private Image highLight;
	private Image background;
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
		background = new ImageIcon(this.getClass().getResource("UIimages/invBackground.png")).getImage();

		addMouseListener(this);
		addMouseMotionListener(this);


		setOpaque(false);
		setVisible(true);
		validate();
	}

	public void addItems(List<String> items){
		itemList.clear();
		for(String item : items){
			if(item.equals("box"))
				itemList.add(new ItemDisplay(item, true));
			else
				itemList.add(new ItemDisplay(item, false));
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
		g.drawImage(background, 0, 0, background.getWidth(null), background.getHeight(null), null);
		if(highlighted){
			g.drawImage(highLight, highlightedX, highlightedY, highLight.getWidth(null), highLight.getHeight(null), null);
		}
		Image image = null;
		ItemDisplay item = null;
		for(int i = (level*5); i < (level*5)+15; i++){
			if(i >= itemsShown.size())
				break;
			item = itemsShown.get(i);
			image = new ImageIcon(this.getClass().getResource("itemimages/"+item.getName()+".png")).getImage();
			if(dragged != i){
				g.drawImage(image, item.getX(), item.getY(), image.getWidth(null), image.getHeight(null), null);
			} 

		}
		if(dragged != -1){
			Image dragImage = new ImageIcon(this.getClass().getResource("itemimages/"+itemsShown.get(dragged).getName()+".png")).getImage();
			g.drawImage(dragImage, itemsShown.get(dragged).getTempX(), itemsShown.get(dragged).getTempY(), image.getWidth(null), image.getHeight(null), null);
		}
	}

	/**
	 * Popup menu
	 * 
	 * @param e
	 */
	private void doPop(MouseEvent e, boolean container, String name){
		PopUpMenu menu = new PopUpMenu(container, name);
		menu.show(e.getComponent(), e.getX(), e.getY());
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		highlightedX=(e.getX()/50)*50;
		highlightedY=(e.getY()/50)*50;
		if(dragged != -1){
			itemsShown.get(dragged).setTemp(e.getX()-25, e.getY()-25);
		}
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
		if(SwingUtilities.isRightMouseButton(e)){ //if right mouse button
			
			int index = e.getX()/50 + ((e.getY()/50)*5);
			
			//if item is there
			if(itemsShown.size() > index && index >= 0){
				doPop(e, itemsShown.get(index).isContainer(), itemsShown.get(index).getName());
			}
				
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int index = e.getX()/50 + ((e.getY()/50)*5);
		//	System.out.println(index);
		if(itemsShown.size() > index && index >= 0 && !SwingUtilities.isRightMouseButton(e)){
			dragged = index;
			itemsShown.get(dragged).changeDragging();

		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//ensure if e.getX() or e.getY() out of bounds that it does something
		if(dragged != -1)
			System.out.print("Dragged "+itemsShown.get(dragged).getName()+" onto ");


		int secondIndexs = e.getX()/50 + ((e.getY()/50)*5);

		if(dragged != -1){
			if(itemsShown.size() > secondIndexs && secondIndexs >= 0){
				System.out.println(itemsShown.get(secondIndexs).getName());
			} else {
				System.out.println("nothing");
			}
		}

		if(dragged != -1){
			itemsShown.get(dragged).changeDragging();
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
			itemsShown.get(dragged).changeDragging();
			dragged = -1;
		} 
		repaint();

	}

	/**
	 * Popupmenu will be a menu when a player right clicks on an inventory item
	 * 
	 * @author Andy
	 *
	 */
	class PopUpMenu extends JPopupMenu {
		JMenuItem inspect;
		JMenuItem drop;
		JMenuItem open;

		public PopUpMenu(boolean value, String name){
			ActionListener popUpListener = new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					System.out.println("Popup menu item ["
							+ event.getActionCommand() + "] was pressed on item "+name+".");
				}
			};

			inspect = new JMenuItem("Inspect");
			inspect.addActionListener(popUpListener);
			add(inspect);
			drop = new JMenuItem("Drop");
			drop.addActionListener(popUpListener);
			add(drop);
			if(value){
				open = new JMenuItem("Open");
				open.addActionListener(popUpListener);
				add(open);
			}

		}
	}
}
