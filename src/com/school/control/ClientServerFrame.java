package com.school.control;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

import com.school.game.SpookySchool;

/**
 * setup a server, setup clients from user input data with button confirm
 * 
 * @author rongjiwang
 *
 */
public class ClientServerFrame extends JFrame {

	private JTextField ipAddress;
	private JButton okButton;
	private JRadioButton serverButton;
	private JRadioButton clientButton;
	private ButtonGroup buttonGroup;
	private JTextField playerName;
	private JPanel panel;
	private JButton btn;
	private JTextField inputIpAddress;
	private ButtonGroup bg2;
	private JRadioButton join;
	private JRadioButton single;
	private JTextArea textArea;
	private JScrollPane scroll;
	public static boolean serverOn;
	private static final int port = 31122;
	public static final int GAME_CLOCK = 20;
	public static final int BROADCAST_CLOCK = 5;

	public ClientServerFrame() {
		super("Client-Server");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ClientServerFrame.class.getResource("/com/school/ui/images/spooky_cs.png")));
		panel = new JPanel();
		// getContentPane().add(panel);
		getContentPane().add(panel);
		panel.setBackground(new Color(230, 230, 250));
		setResizable(false);
		init();
		setMenu();
		this.setLocation(100, 100);
		this.setVisible(true);
		this.setSize(300, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * setup menu bar
	 */
	private void setMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);

		JMenuItem mntmLoad = new JMenuItem("Load");
		mnMenu.add(mntmLoad);

		JMenuItem mntmEndServer = new JMenuItem("End Server");
		mnMenu.add(mntmEndServer);

		JMenu mnSupport = new JMenu("Support");
		menuBar.add(mnSupport);

		JMenuItem mntmGameHelp = new JMenuItem("Game Help");
		mnSupport.add(mntmGameHelp);
	}

	/**
	 * setup client server panel
	 */
	private void init() {
		// setup client server radio button
		serverButton = new JRadioButton("Server", true);
		clientButton = new JRadioButton("Client", false);
		buttonGroup = new ButtonGroup();
		buttonGroup.add(serverButton);
		buttonGroup.add(clientButton);
		SpringLayout springLayout = new SpringLayout();
		springLayout.putConstraint(SpringLayout.WEST, clientButton, 0, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.WEST, serverButton, 0, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.NORTH, serverButton, 0, SpringLayout.NORTH, panel);
		panel.setLayout(springLayout);
		panel.add(serverButton);
		panel.add(clientButton);

		// IP text
		JLabel ipLabel = new JLabel("IP Address: ");
		springLayout.putConstraint(SpringLayout.NORTH, ipLabel, 29, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.WEST, ipLabel, 10, SpringLayout.WEST, panel);
		panel.add(ipLabel);

		// set current IP address as default
		InetAddress defaultLocalIpAddress = null;
		try {
			defaultLocalIpAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		ipAddress = new JTextField(defaultLocalIpAddress.getHostAddress());
		springLayout.putConstraint(SpringLayout.NORTH, ipAddress, 0, SpringLayout.NORTH, ipLabel);
		springLayout.putConstraint(SpringLayout.WEST, ipAddress, 2, SpringLayout.EAST, ipLabel);
		ipAddress.setEditable(false);
		// hover feature
		ipAddress.setToolTipText("Current Local IP Address");
		panel.add(ipAddress);

		// player text
		JLabel nameLabel = new JLabel("Player Name: ");
		springLayout.putConstraint(SpringLayout.NORTH, nameLabel, 147, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.WEST, nameLabel, 10, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, clientButton, -6, SpringLayout.NORTH, nameLabel);
		panel.add(nameLabel);

		// user input name
		playerName = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, playerName, -3, SpringLayout.NORTH, nameLabel);
		springLayout.putConstraint(SpringLayout.WEST, playerName, 0, SpringLayout.EAST, nameLabel);
		panel.add(playerName);
		playerName.setColumns(8);

		// join game or single player game
		single = new JRadioButton("Single", false);
		springLayout.putConstraint(SpringLayout.WEST, single, 0, SpringLayout.WEST, ipLabel);
		join = new JRadioButton("Join", true);
		springLayout.putConstraint(SpringLayout.NORTH, single, 5, SpringLayout.SOUTH, join);
		springLayout.putConstraint(SpringLayout.NORTH, join, 4, SpringLayout.SOUTH, nameLabel);
		springLayout.putConstraint(SpringLayout.WEST, join, 0, SpringLayout.WEST, ipLabel);
		bg2 = new ButtonGroup();
		bg2.add(single);
		bg2.add(join);
		panel.add(single);
		panel.add(join);
		single.addActionListener(new ActionListener() {
			/**
			 * single player should within client option
			 */
			public void actionPerformed(ActionEvent e) {
				clientButton.setSelected(true);
			}
		});
		join.addActionListener(new ActionListener() {
			/**
			 * join server should within client option
			 */
			public void actionPerformed(ActionEvent e) {
				clientButton.setSelected(true);
			}
		});

		// port label
		JLabel lblPort = new JLabel("Port:");
		springLayout.putConstraint(SpringLayout.NORTH, lblPort, 4, SpringLayout.NORTH, join);
		springLayout.putConstraint(SpringLayout.WEST, lblPort, 6, SpringLayout.EAST, join);
		panel.add(lblPort);

		// user input ip address
		inputIpAddress = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, inputIpAddress, 1, SpringLayout.NORTH, join);
		springLayout.putConstraint(SpringLayout.WEST, inputIpAddress, 53, SpringLayout.EAST, join);
		panel.add(inputIpAddress);
		inputIpAddress.setColumns(5);

		// output area
		textArea = new JTextArea();
		springLayout.putConstraint(SpringLayout.WEST, textArea, 0, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.EAST, textArea, -40, SpringLayout.WEST, serverButton);
		textArea.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, textArea, 6, SpringLayout.SOUTH, ipAddress);
		springLayout.putConstraint(SpringLayout.SOUTH, textArea, 0, SpringLayout.NORTH, clientButton);
		panel.add(textArea);

		// add Scroll to text area
		scroll = new JScrollPane(textArea);
		springLayout.putConstraint(SpringLayout.NORTH, scroll, 55, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.WEST, scroll, 10, SpringLayout.WEST, textArea);
		springLayout.putConstraint(SpringLayout.SOUTH, scroll, -6, SpringLayout.NORTH, clientButton);
		springLayout.putConstraint(SpringLayout.EAST, scroll, -10, SpringLayout.EAST, panel);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scroll);

		// // setup comboBox for IP selection
		// String[] ips = new String[5]; // stores all the available IP address
		// JComboBox<?> comboBox = new JComboBox<Object>(ips);
		// springLayout.putConstraint(SpringLayout.NORTH, comboBox, 6,
		// SpringLayout.SOUTH, textField);
		// springLayout.putConstraint(SpringLayout.WEST, comboBox, 10,
		// SpringLayout.WEST, textField);
		// panel.add(comboBox);

		// confirm button
		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// create server, client
				String name = playerName.getText();
				SpookySchool game = new SpookySchool();
				if (serverButton.isSelected()) {
					runServer(port, GAME_CLOCK, BROADCAST_CLOCK,game);
				} else if (join.isSelected()) {
					String ip = ipAddress.getText();
					try {
						runClient(ip, port, name,game);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					runSingle(name,game);
				}
			}
		});
		panel.add(okButton);

		// exit application
		btn = new JButton("Cancel");
		springLayout.putConstraint(SpringLayout.SOUTH, btn, 0, SpringLayout.SOUTH, panel);
		springLayout.putConstraint(SpringLayout.NORTH, okButton, 0, SpringLayout.NORTH, btn);
		springLayout.putConstraint(SpringLayout.EAST, okButton, -6, SpringLayout.WEST, btn);
		springLayout.putConstraint(SpringLayout.EAST, btn, 0, SpringLayout.EAST, panel);

		btn.addActionListener(new ActionListener() {
			// indeed exit
			public void actionPerformed(ActionEvent e) {
				int choice = JOptionPane.showOptionDialog(null, "Close this application?", null,
						JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
				if (choice == 0) {
					System.exit(0);
				}

				// Window w = SwingUtilities.getWindowAncestor(btn);
				// if (w != null) {
				// w.setVisible(false);
				// }
			}
		});
		panel.add(btn);

	}

	protected void runSingle(String name, SpookySchool game) {
		//set input value back to null
		//playerName.setText(null);
		System.out.println("Single player:" + name);
	}

	protected void runClient(String ip, int port, String name, SpookySchool game) throws UnknownHostException, IOException {
//		System.out.println("Client: " + name + " " + ip);
//		Socket s = new Socket(ip,port);
//		new Client(s).run();
		serverOn = false;
		game.start();
	}

	protected void runServer(int port, int gameClock, int broadcastClock, SpookySchool game) {
		serverOn = true;
		System.out.println("Server: " + port);
		
		//start main thread , server game thread
		//ClockThread clk = new ClockThread(gameClock, game, null);
		//game.start(); // thread start
		//clk.start();
		System.out.println(" SERVER LISTENING ON PORT " + port);
		game.start();
//		try {
//			int index = 1;
//			Server[] connections = new Server[5];
//			
//			ServerSocket ss = new ServerSocket(port);System.out.println("here?...");
//			while(true){
//				//wait for a socket
//				Socket s = ss.accept();
//				System.out.println("ACCEPTED CONNECTION FROM: "+ s.getInetAddress());
//				connections[index++] = new Server(s,index-1,broadcastClock,game);
//				connections[index-1].start();
//				
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}
}
