package ui;

import java.awt.Dimension;
import java.awt.Font;
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

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import game.InventoryGO;
import network.Client;


public class InventoryPanel extends JPanel implements MouseListener, MouseMotionListener{
	private List<ItemDisplay> itemList;
	private List<ItemDisplay> itemsShown;
	final static Dimension boardSize = new Dimension(250,150);
	private boolean highlighted;
	private boolean processing;
	private int highlightedX;
	private int highlightedY;
	private Image highLight;
	private Image background;

	private List<InventoryGO> tempStore;
	private boolean tempStored;

	private UIImageMap imageMap;
	private ItemImageMap itemMap;

	private int dragged = -1;

	//used when up/down buttons pressed
	private int level;

	private Client client;
	private OverlayPanel overlayPanel;

	public InventoryPanel(UIImageMap imageMap, Client client, OverlayPanel overlayPanel){
		super(new GridLayout(3,5));

		this.client = client;
		this.overlayPanel = overlayPanel;
		this.imageMap = imageMap;
		this.itemMap = new ItemImageMap();
		this.tempStore = new ArrayList<InventoryGO>();
		this.tempStored = false;

		processing = false;

		itemList = new ArrayList<ItemDisplay>();
		itemsShown = new ArrayList<ItemDisplay>();
		this.level = 0;
		this.setPreferredSize(boardSize);
		highLight = imageMap.getImage("hi");
		background = imageMap.getImage("invBack");

		addMouseListener(this);
		addMouseMotionListener(this);

		setOpaque(false);
		setVisible(true);
		validate();
	}


	public void upOne(){
		if(level > 0){
			level-=5;
			processItems();
			repaint();
		}
	}
	
	public void downOne(){
		if(level < (itemList.size()+5)){
			level+=5;
			processItems();
			repaint();
		}
	}
	/**
	 * Checks if the user is currently right clicked or dragging. If so, stores the inventory to pass after the user is finished, otherwise processes inventory as normal
	 * 
	 * @param items
	 */
	public void updateInventory(List<InventoryGO> items){
		
		//if(!processing)
			addItems(items);
	//	else{
		//	tempStore = items;
		//	tempStored = true;
		//}

	}

	public boolean itemsChanged(List<InventoryGO> items){
		if(items.size() != itemList.size())
			return true;

		for(InventoryGO item: items){
			if(itemList.contains(item))
				return true;
		}

		return false;
	}

	
	public void addItems(List<InventoryGO> items){
		if(items != null){
			if(itemsChanged(items)){

				for(ItemDisplay item : itemsShown){
					item.removeDisplay();
				}
				itemsShown.clear();

				itemList.clear();

				for(InventoryGO item : items){
					itemList.add(new ItemDisplay(item));
				}

				processItems();
				repaint();
			}
		}
	}

	/**
	 * place items onto correct x,y coordinates, in the 5x3 grid
	 */
	public void processItems(){
		int i = level;

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

	
	//public void processTemp(){
	//	if(processing){
	//		processing = false;
	//		if(tempStored){
	//			addItems(tempStore);
	//			tempStore.clear();
	//			tempStored = false;
	//		}
	//	}
	//}
	
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
			image = itemMap.getImage(item.getToken());
			if(dragged != i){
				g.drawImage(image, item.getX(), item.getY(), image.getWidth(null), image.getHeight(null), null);
			} 

		}
		if(dragged != -1){
			Image dragImage = itemMap.getImage(itemsShown.get(dragged).getToken());
			g.drawImage(dragImage, itemsShown.get(dragged).getTempX(), itemsShown.get(dragged).getTempY(), image.getWidth(null), image.getHeight(null), null);
		}
	}

	/**
	 * Popup menu
	 * 
	 * @param e
	 */
	private void doPop(MouseEvent e, ItemDisplay item){
		PopUpMenu menu = new PopUpMenu(item);
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
				doPop(e, /**itemsShown.get(index).isContainer(), **/itemsShown.get(index));
			}

		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int index = e.getX()/50 + ((e.getY()/50)*5);
		//	System.out.println(index);
		if(itemsShown.size() > index && index >= 0 && !SwingUtilities.isRightMouseButton(e)){
			dragged = index;
			//processing = true;
			itemsShown.get(dragged).changeDragging();

		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//ensure if e.getX() or e.getY() out of bounds that it does something
		String firstID = "";
		String secondID = "";

		if(dragged != -1){
			firstID += itemsShown.get(dragged).getID();

			int secondIndexs = e.getX()/50 + ((e.getY()/50)*5);

			if(itemsShown.size() > secondIndexs && secondIndexs >= 0)
				secondID += itemsShown.get(secondIndexs).getID();

			itemsShown.get(dragged).changeDragging();
			
			dragged = -1;
			//processTemp();
		}
		if(!firstID.equals("") && !secondID.equals("")){
			client.sendCommand("PACK "+secondID+" "+firstID);
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
			//processTemp();
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
		JMenuItem unpack;
		JMenuItem pass;

		public PopUpMenu(ItemDisplay item){
			ActionListener popUpListener = new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if(event.getActionCommand().equals("Inspect")){
						overlayPanel.setFooterMessage(item.getDescription());
					} else if(event.getActionCommand().equals("Drop")){
						client.sendCommand("DROP "+item.getID());
					} else if (event.getActionCommand().equals("Open")){
						client.sendCommand("OPEN "+item.getID());
					} else if (event.getActionCommand().equals("Pass")){
						client.sendCommand("PASS "+item.getID());
					} else if (event.getActionCommand().equals("Unpack")){
						client.sendCommand("UNPACK "+item.getID());
					}
				}
				
			};
			
			try {
				inspect = new JMenuItem("Inspect");
				drop = new JMenuItem("Drop");
				pass = new JMenuItem("Pass Item");
				unpack = new JMenuItem("Unpack");
				Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("slkscr.ttf"));
				inspect.setFont(font.deriveFont(Font.TRUETYPE_FONT, 13f));
				drop.setFont(font.deriveFont(Font.TRUETYPE_FONT, 13f));
				pass.setFont(font.deriveFont(Font.TRUETYPE_FONT, 13f));
				unpack.setFont(font.deriveFont(Font.TRUETYPE_FONT, 13f));

			} catch (Exception e) {}
			
			
			inspect.addActionListener(popUpListener);
			add(inspect);
			
			drop.addActionListener(popUpListener);
			add(drop);
			
			pass.addActionListener(popUpListener);
			add(pass);
			
			if(item.isContainer()){
				unpack.addActionListener(popUpListener);
				add(unpack);
			}

		}
	}
}
