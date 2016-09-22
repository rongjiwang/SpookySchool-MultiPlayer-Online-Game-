package com.school.control;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
/**
 * setup a server, setup clients from user input data and button confirm 
 * @author rongjiwang
 *
 */
public class ClientServerFrame extends JFrame{
	public ClientServerFrame() {
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("New menu item");
		menuBar.add(mntmNewMenuItem);
	}

}
