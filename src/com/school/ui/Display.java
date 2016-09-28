package com.school.ui;
import java.awt.BorderLayout;
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

/**
 * Display is the Frame that all JPanel components are added to. They are dynamically changed
 * as the game progresses.
 * 
 * @author Andy
 *
 */
public class Display {
	private JFrame gameFrame;
	private JPanel leftPanel = null;
	private JPanel rightPanel = null;
	private AreaDisplayPanel gamePanel;
	private InventoryPanel invPanel;
	private JMenuItem help;
	private JMenuItem about;

	public Display() {
		gameFrame = new JFrame("Spooky School");
		
		//creates renderer 
		gamePanel = new AreaDisplayPanel();
		
		//creates inventory panel
		invPanel = new InventoryPanel();
		
		//adds menu bar
		final JMenuBar displayMenuBar = createMenuBar();
		gameFrame.setJMenuBar(displayMenuBar);
		
		//sets up layout
		gameFrame.setLayout(new BorderLayout());
		gameFrame.setResizable(false);
		setPanels();
		gameFrame.setVisible(true);
		gameFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//ensures if player tries to close frame, that a warning message comes up asking if they want to.
		gameFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we)
			{ 
				String ObjButtons[] = {"Yes","No"};
				int PromptResult = JOptionPane.showOptionDialog(null,"Are you sure you want to exit?","Quit Spooky School?",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
				if(PromptResult==JOptionPane.YES_OPTION)
				{
					System.exit(0);
				}
			}
		});
	}

	/**
	 * This method is called to set the default main and side panels upon opening the game.
	 */
	public void setPanels(){
		leftPanel = new GamePanel(gamePanel);
		gameFrame.add(leftPanel, BorderLayout.WEST);
		//rightPanel 
		//gameFrame.add(rightPanel, BorderLayout.EAST);
		gameFrame.pack();
	}

	/**
	 * This method creates the menu bar
	 * 
	 * @return JMenuBar to be added
	 */
	private JMenuBar createMenuBar(){
		final JMenuBar displayMenuBar = new JMenuBar();
		displayMenuBar.add(createHelpMenu());
		return displayMenuBar;

	}


	/**
	 * This menu adds all the components to the Help menu, in the menu bar
	 * 
	 * @return JMenu to be added to menu bar
	 */
	private JMenu createHelpMenu(){
		final JMenu helpMenu = new JMenu("Help");
		help = new JMenuItem("Information");
		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Help info here", "Help", JOptionPane.ERROR_MESSAGE);

			}
		});
		helpMenu.add(help);
		about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "SpookySchool", "About", JOptionPane.ERROR_MESSAGE);

			}
		});
		helpMenu.add(about);
		return helpMenu;
	}

}
