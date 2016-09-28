package com.school.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.school.control.Client;

public class MainFrame extends JFrame implements WindowListener {

	private JPanel areaDisplayPanel; //This pane displays all of the other panels
	private String name;

	public MainFrame(String title, Client client) {
		super(title); // Set window title.
		//--------------CS
		this.name = title;
		//----------------
		setSize(1024, 768); // default size is 0,0
		this.setResizable(false); //Do not allow window resizing.
		this.addWindowListener(this); //This frame also implements window listener so "add it"
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //Don't close window when x button is pressed. Allows us to get user confirmation.

		// Center window in screen
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension scrnsize = toolkit.getScreenSize();
		setBounds((scrnsize.width - getWidth()) / 2, (scrnsize.height - getHeight()) / 2, getWidth(), getHeight());

		this.areaDisplayPanel = new AreaDisplayPanel(client);
		this.add(this.areaDisplayPanel);

		this.setVisible(true); //Display the window
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
		if (PromptResult == 0)
			System.exit(0); //User has confirmed to close. Close the program completely.
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

	public String getName() {
		return name;
	}
	
	

}
