package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import network.Client;

/**
 * 
 * @author Pritesh R. Patel
 *
 */
public class JoinServerPanel extends JPanel {

	private String playerName;
	private String ipAddress = "localhost";
	private Integer port = 4444;

	private String[] defaultNames = { "Bob", "Tony", "Sam", "John", "Marcus", "Susan", "Henry", "Bob", "Jill",
			"Brandon", "Tom", "Adam", "Daniel", "Alan", "Josh", "Rob", "Alex", "Jim", "Jessica" };

	private Client client;

	private JTextField serverStatusField;
	private JButton joinServerBtn;
	private BufferedImage uiBackground;

	public JoinServerPanel(JPanel contentPane) {
		this.setLayout(null); //Use no layout manager in this panel.
		this.setBackground(Color.darkGray);

		//Assign a random name from the list of default names.
		int nameIndex = (int) (Math.random() * defaultNames.length);
		this.playerName = defaultNames[nameIndex];

		this.setupPanel(); //Sets up this panel. Adds various buttons and input fields.

		try {
			this.uiBackground = ImageIO.read(new File("src/ui/images/networkui_bg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Sets up the this (JoinServerPanel) panel. Adds various buttons and input fields.
	 */
	private void setupPanel() {

		//Server Status field
		this.serverStatusField = new JTextField("Waiting to join server...", 15);
		serverStatusField.setHorizontalAlignment(SwingConstants.CENTER);
		serverStatusField.setEditable(false);
		serverStatusField.setBounds(150, 200, 200, 30);
		this.add(serverStatusField);


		//PlayerName Label
		JLabel playerNameLabel = new JLabel("Player Name:");
		playerNameLabel.setForeground(Color.WHITE);
		playerNameLabel.setFont(new Font("Arial", 1, 15));
		playerNameLabel.setBounds(105, 280, 200, 30);
		this.add(playerNameLabel);

		//Add playerName text field.
		JTextField playerNameField = new JTextField(this.playerName, 15);
		playerNameField.setBounds(225, 280, 200, 30);
		this.add(playerNameField);


		//IP Address Label
		JLabel ipLabel = new JLabel("IP Address:");
		ipLabel.setForeground(Color.WHITE);
		ipLabel.setFont(new Font("Arial", 1, 15));
		ipLabel.setBounds(105, 340, 200, 30);
		this.add(ipLabel);


		//Add ip address field.
		JTextField ipAddressField = new JTextField(this.ipAddress, 15);
		ipAddressField.setBounds(225, 340, 200, 30);
		this.add(ipAddressField);


		//Port label
		JLabel portLabel = new JLabel("Join on Port:");
		portLabel.setForeground(Color.WHITE);
		portLabel.setFont(new Font("Arial", 1, 15));
		portLabel.setBounds(105, 400, 200, 30);
		this.add(portLabel);


		//Add Port Field
		JTextField portField = new JTextField(this.port.toString(), 15);
		portField.setBounds(225, 400, 200, 30);
		this.add(portField);

		//Add Join Server Button.
		joinServerBtn = new JButton("Join Server");
		joinServerBtn.setToolTipText("Click here to join server");
		joinServerBtn.setBounds(100, 460, 300, 70);
		this.add(joinServerBtn);

		joinServerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {

				playerName = playerNameField.getText();
				ipAddress = ipAddressField.getText();
				port = Integer.parseInt(portField.getText());

				//Player name is not allowed to be -1. Display dialog if player attempts to use "-1" as their player name.
				if (playerName.equals("-1")) {
					JOptionPane.showMessageDialog(null,
							" The player name '-1' is reserved by the game and is not allowed.", "Name not allowed",
							JOptionPane.PLAIN_MESSAGE);
					return;
				}

				try {
					//Create a client if we haven't already.
					if (client == null) {

						Socket s = new Socket(ipAddress, port);
						updateServerStatusField("Socket created.");

						//FIXME: DISPLAY THAT SERVER IS FULL ON CLIENT SIDE.

						client = new Client(playerName, s, JoinServerPanel.this);
						client.start();
					} else {
						client.setPlayerName(playerName);
						client.sendCommand("newPlayer " + playerName); //Send the new player name
					}

				} catch (IOException e) {
					updateServerStatusField("Failed to connect to server.");
				}
			}
		});
	}


	/**
	 * Used by the client to ask user for a new player name as the one given is already being used on the server.
	 */
	public void askForNewName() {
		JOptionPane.showMessageDialog(null,
				"Another player with this name already exists on the server. Please enter a new name",
				"Name already taken", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Hide the join server window. 
	 */
	public void hideJoinWindow() {
		SwingUtilities.windowForComponent(JoinServerPanel.this).setVisible(false);
	}


	/**
	 * Used by the server to create updates.
	 * @param update
	 */
	public void updateServerStatusField(String update) {
		this.serverStatusField.setText(update);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.uiBackground, 0, 0, null);
	}

}
