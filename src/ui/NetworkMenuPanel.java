package ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class NetworkMenuPanel extends JPanel {

	private JPanel contentPane;

	//Buttons
	private JButton newServerBtn;
	private JButton joinServerBtn;
	private JButton localGameBtn;

	public NetworkMenuPanel(JPanel contentPane) {
		this.contentPane = contentPane;
		this.setLayout(null); //Use no layout manager in this panel.
		this.setBackground(Color.darkGray);

		this.addNetworkMenuButtons(); //Add buttons to this panel
	}


	/**
	 * Add the various menu buttons to this panel
	 */
	private void addNetworkMenuButtons() {

		//Create and add newServerButton
		this.newServerBtn = new JButton("New Server");
		this.newServerBtn.setToolTipText("Click here to create a new Spooky School Server");
		this.newServerBtn.setBounds(100, 200, 300, 70);
		this.newServerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				CardLayout cardLayout = (CardLayout) contentPane.getLayout();
				cardLayout.show(contentPane, "CreateServerScreen"); //Player pressed start. Move onto the next screen.
			}
		});
		this.add(this.newServerBtn);

		//Create and add joinServerButton
		this.joinServerBtn = new JButton("Join Server");
		this.joinServerBtn.setToolTipText("Click here to join an existing Spooky School Server");
		this.joinServerBtn.setBounds(100, 300, 300, 70);
		this.joinServerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				CardLayout cardLayout = (CardLayout) contentPane.getLayout();
				cardLayout.show(contentPane, "JoinServerScreen"); //Player pressed start. Move onto the next screen.
			}
		});
		this.add(this.joinServerBtn);

		//Create and add play local game button
		this.localGameBtn = new JButton("Begin Local Game");
		this.localGameBtn.setToolTipText("Click here to bein a new local game");
		this.localGameBtn.setBounds(100, 400, 300, 70);
		this.localGameBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				//TODO add ability to play local game here
			}
		});
		this.add(this.localGameBtn);

	}

}
