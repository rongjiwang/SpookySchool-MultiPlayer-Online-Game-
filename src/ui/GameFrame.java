package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import game.Bundle;
import network.Client;

public class GameFrame extends JFrame implements WindowListener {

	private JPanel leftPanel = null;
	private JPanel rightPanel = null;
	private InventoryPanel invPanel;
	private ChatPanel chatPanel;

	private boolean render3D = false; //FIXME **CHANGE TO FALSE TESTING 2D RENDERING**

	private AreaDisplayPanel areaDisplayPanel; //This pane displays all of the other panels
	private AreaDisplayPanel2D areaDisplayPanel2D; //This pane displays all of the other panels
	private DebugDisplay debugDisplay;

	private Client client;
	private String name;

	public GameFrame(String title, Client client, String name) {
		super(title); // Set window title.



		this.client = client;

		//creates inventory panel
		invPanel = new InventoryPanel();

		//creates chat panel
		chatPanel = new ChatPanel(this, name, client);

		//sets up layout
		this.setLayout(new BorderLayout());
		this.setResizable(false); //Do not allow window resizing.

		if (this.render3D) {
			this.areaDisplayPanel = new AreaDisplayPanel(this.client, this);
		} else {
			this.areaDisplayPanel2D = new AreaDisplayPanel2D(this.client);
		}

		//temp, delete after
		debugDisplay = new DebugDisplay(areaDisplayPanel);

		setPanels();

		this.addWindowListener(this); //This frame also implements window listener so "add it"
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //Don't close window when x button is pressed. Allows us to get user confirmation.

		// Center window in screen
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension scrnsize = toolkit.getScreenSize();
		setBounds((scrnsize.width - getWidth()) / 2, (scrnsize.height - getHeight()) / 2, getWidth(), getHeight());
		//debugDisplay.updateDisplay();

		this.setVisible(true); //Display the window
	}

	public void updateDebug(String name) {
		debugDisplay.updateDisplay();
	}

	//refocuses on game window (after sending a message)
	public void refocus() {
		if (this.render3D) {
			areaDisplayPanel.requestFocusInWindow();
		} else {
			areaDisplayPanel2D.requestFocusInWindow();
		}
	}

	/**
	 * This method is called to set the default main and side panels upon opening the game.
	 */
	public void setPanels() {
		//leftPanel
		if (this.render3D) {
			leftPanel = new MainPanel(areaDisplayPanel, invPanel);
		} else {
			leftPanel = new MainPanel(areaDisplayPanel2D, invPanel);
		}
		this.add(leftPanel, BorderLayout.WEST);
		//rightPanel (need to remove DebugDisplay)
		rightPanel = new SidePanel(chatPanel, debugDisplay);
		this.add(rightPanel, BorderLayout.EAST);
		this.pack();
	}

	/**
	 * Process the bundle by passing its contents to relevant panels.
	 */
	public void processBundle(Bundle bundle) {
		if (bundle.getLog() != null && !bundle.getLog().isEmpty()) {
			chatPanel.addChange(bundle.getLog());
		}

		if (this.render3D) {
			this.areaDisplayPanel.processBundle(bundle);//Temporarily only passing bundle to the renderer.
		} else {
			this.areaDisplayPanel2D.processBundle(bundle);
		}

	}

	public DebugDisplay getDebugDisplay() {
		return debugDisplay;
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
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}


}
