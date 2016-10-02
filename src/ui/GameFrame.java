package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import game.Bundle;
import network.Client;

public class GameFrame extends JFrame implements WindowListener {

	private JPanel leftPanel = null;
	private JPanel rightPanel = null;
	private InventoryPanel invPanel;
	private ChatPanel chatPanel;
	
	private boolean render3D = true;

	private AreaDisplayPanel areaDisplayPanel; //This pane displays all of the other panels
	private AreaDisplayPanel2D areaDisplayPanel2D; //This pane displays all of the other panels

	private Client client;

	public GameFrame(String title, Client client) {
		super(title); // Set window title.

		this.client = client;

		//creates inventory panel
		invPanel = new InventoryPanel();
				
		//creates chat panel
		chatPanel = new ChatPanel(this);
		
		//sets up layout
		this.setLayout(new BorderLayout());
		this.setResizable(false); //Do not allow window resizing.
		
		if (this.render3D) {
			this.areaDisplayPanel = new AreaDisplayPanel(this.client);
		} else {
			this.areaDisplayPanel2D = new AreaDisplayPanel2D(this.client);
		}
		
		setPanels();
		
		this.addWindowListener(this); //This frame also implements window listener so "add it"
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //Don't close window when x button is pressed. Allows us to get user confirmation.

		// Center window in screen
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension scrnsize = toolkit.getScreenSize();
		setBounds((scrnsize.width - getWidth()) / 2, (scrnsize.height - getHeight()) / 2, getWidth(), getHeight());

		
		this.setVisible(true); //Display the window
	}
	
	//refocuses on game window (after sending a message)
	public void refocus(){
		if(this.render3D){
			areaDisplayPanel.requestFocusInWindow();
		} else {
			areaDisplayPanel2D.requestFocusInWindow();
		}
	}
	
	/**
	 * This method is called to set the default main and side panels upon opening the game.
	 */
	public void setPanels(){
		//leftPanel
		if(this.render3D){
			leftPanel = new MainPanel(areaDisplayPanel, invPanel);
		} else {
			leftPanel = new MainPanel(areaDisplayPanel2D, invPanel);
		}
		this.add(leftPanel, BorderLayout.WEST);
		//rightPanel 
		rightPanel = new SidePanel(chatPanel);
		this.add(rightPanel, BorderLayout.EAST);
		this.pack();
	}

	/**
	 * Process the bundle by passing its contents to relevant panels.
	 */
	public void processBundle(Bundle bundle) {

		if (this.render3D) {
			this.areaDisplayPanel.processBundle(bundle);//Temporarily only passing bundle to the renderer.
		} else {
			this.areaDisplayPanel2D.processBundle(bundle);
		}

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		this.closeWindow();
	}

	/**
	 * Confirms with user before closing the game window.
	 */
	public void closeWindow() {
		String optionButtons[] = { "Yes", "No" };
		int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to close the game?",
				"Close Spooky School?", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, optionButtons,
				optionButtons[1]);
		if (PromptResult == 0) {
			this.client.closeSocket();
			System.exit(0); //User has confirmed to close. Close the program completely.
		}
	}

	/**
	 * Force close the game window due to disconnection.
	 */
	public void disconnected() {
		JOptionPane.showMessageDialog(this, "GAME DISCONNECTED FROM SERVER!");
		System.exit(0);

	}

	// UNUSED WINDOW LISTENER METHODS
	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}


}