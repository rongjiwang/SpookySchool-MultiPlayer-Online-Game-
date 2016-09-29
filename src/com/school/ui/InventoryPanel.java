package com.school.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;


public class InventoryPanel extends JPanel{
	private List<InvButton> invButtons;
	//private List<Items> items;
	final static Dimension boardSize = new Dimension(250,150);
	
	public InventoryPanel(){
		super(new GridLayout(3,5));
		this.setPreferredSize(boardSize);
		invButtons = new ArrayList<InvButton>();

		//create buttons, which will then populate the gridlayout, passing the correct coordinates 
		for(int i = 0; i < 3; i ++){
			for(int j = 0; j < 5; j++){
				InvButton iButton = new InvButton(i, j, null);
				invButtons.add(iButton);
				add(iButton);
			}
		}
		setOpaque(false);
		setVisible(true);
		validate();
	}
}
