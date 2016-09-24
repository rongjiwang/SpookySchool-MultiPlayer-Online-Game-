package com.school.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
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

/**
 * setup a server, setup clients from user input data and button confirm
 * 
 * @author rongjiwang
 *
 */
public class ClientServerFrame extends JFrame {

	private JLabel ipMsg;
	private JTextField ipAddress;
	private JLabel ipMsg2;
	private JTextField ipAddress1;
	private JButton okButton;
	private JRadioButton serverButton;
	private JRadioButton clientButton;
	private ButtonGroup buttonGroup;
	private JTextField textField;
	private JPanel panel;
	private JButton btn;
	private JTextField textField_1;

	public ClientServerFrame() {
		super("Client-Server");
		panel = new JPanel();
		getContentPane().add(panel);
		setResizable(false);
		init();
		setMenu();
		this.setLocation(100, 100);
		this.setVisible(true);
		this.setSize(300, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * 
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
	 * 
	 */
	private void init() {
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
		ipMsg = new JLabel("IP Address: ");
		springLayout.putConstraint(SpringLayout.NORTH, ipMsg, 29, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.WEST, ipMsg, 10, SpringLayout.WEST, panel);
		panel.add(ipMsg);
		// set current IP address as default
		InetAddress defaultLocalIpAddress = null;
		try {
			defaultLocalIpAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		ipAddress = new JTextField(defaultLocalIpAddress.getHostAddress());
		springLayout.putConstraint(SpringLayout.NORTH, ipAddress, 0, SpringLayout.NORTH, ipMsg);
		springLayout.putConstraint(SpringLayout.WEST, ipAddress, 2, SpringLayout.EAST, ipMsg);
		ipAddress.setEditable(false);
		// hover feature
		ipAddress.setToolTipText("Current Local IP Address");
		panel.add(ipAddress);

		// OK button
		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// create server, join a server, single player version
			}
		});
		panel.add(okButton);

		btn = new JButton("Cancel");
		springLayout.putConstraint(SpringLayout.SOUTH, btn, 0, SpringLayout.SOUTH, panel);
		springLayout.putConstraint(SpringLayout.NORTH, okButton, 0, SpringLayout.NORTH, btn);
		springLayout.putConstraint(SpringLayout.EAST, okButton, -6, SpringLayout.WEST, btn);
		springLayout.putConstraint(SpringLayout.EAST, btn, 0, SpringLayout.EAST, panel);

		btn.addActionListener(new ActionListener() {
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

		JLabel lblNewLabel = new JLabel("Player Name: ");
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 147, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel, 10, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, clientButton, -6, SpringLayout.NORTH, lblNewLabel);
		panel.add(lblNewLabel);

		textField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField, -3, SpringLayout.NORTH, lblNewLabel);
		springLayout.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.EAST, lblNewLabel);
		panel.add(textField);
		textField.setColumns(8);
		// ConfirmHandler handler = new ConfirmHandler();
		// okButton.addMouseListener(handler);

		// join game or single player
		JRadioButton single = new JRadioButton("Single");
		springLayout.putConstraint(SpringLayout.WEST, single, 0, SpringLayout.WEST, ipMsg);
		JRadioButton join = new JRadioButton("Join");
		springLayout.putConstraint(SpringLayout.NORTH, single, 5, SpringLayout.SOUTH, join);
		springLayout.putConstraint(SpringLayout.NORTH, join, 4, SpringLayout.SOUTH, lblNewLabel);
		springLayout.putConstraint(SpringLayout.WEST, join, 0, SpringLayout.WEST, ipMsg);
		ButtonGroup bg2 = new ButtonGroup();
		bg2.add(single);
		bg2.add(join);
		panel.add(single);
		panel.add(join);

		textField_1 = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField_1, 6, SpringLayout.SOUTH, lblNewLabel);
		springLayout.putConstraint(SpringLayout.WEST, textField_1, 0, SpringLayout.WEST, ipAddress);
		panel.add(textField_1);
		textField_1.setColumns(10);

		// extra button constraint
		ButtonGroup bg3 = new ButtonGroup();
		bg3.add(single);
		bg3.add(join);
		bg3.add(serverButton);

		// output area
		JTextArea textArea = new JTextArea();
		springLayout.putConstraint(SpringLayout.WEST, textArea, 0, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.EAST, textArea, -40, SpringLayout.WEST, serverButton);
		textArea.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, textArea, 6, SpringLayout.SOUTH, ipAddress);
		springLayout.putConstraint(SpringLayout.SOUTH, textArea, 0, SpringLayout.NORTH, clientButton);
		panel.add(textArea);

		// add Scroll to text area
		JScrollPane scroll = new JScrollPane(textArea);
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

	}
}
