package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import network.Server;

public class CreateServerPanel extends JPanel {

	private JPanel contentPane;
	private Integer port = 4444;

	private Server server;

	private JTextField serverStatusField;
	private JTextArea printTextArea;

	public CreateServerPanel(JPanel contentPane) {
		this.contentPane = contentPane;
		this.setLayout(null); //Use no layout manager in this panel.
		this.setBackground(Color.darkGray);

		this.setupPanel(); //Sets up this panel. Adds various buttons and input fields.
	}

	/**
	 * Sets up the this (createServerPanel) panel. Adds various buttons and input fields.
	 */
	private void setupPanel() {

		//Server Status field
		this.serverStatusField = new JTextField("Waiting for Server Creation...", 15);
		serverStatusField.setHorizontalAlignment(SwingConstants.CENTER);
		serverStatusField.setEditable(false);
		serverStatusField.setBounds(150, 150, 200, 30);
		this.add(serverStatusField);

		//Add print panel.
		this.printTextArea = new JTextArea("Waiting for Server Creation...", 11, 25);
		this.printTextArea.setEditable(false); // set textArea non-editable
		JScrollPane scroll = new JScrollPane(this.printTextArea);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel panel = new JPanel();
		panel.setBounds(100, 200, 300, 200);
		panel.add(scroll);
		panel.setOpaque(false);
		this.add(panel);

		//port label
		JLabel portLabel = new JLabel("Create on Port:");
		portLabel.setForeground(Color.WHITE);
		portLabel.setFont(new Font("Arial", 1, 15));
		portLabel.setBounds(100, 420, 200, 30);
		this.add(portLabel);


		//Add Port Field
		JTextField portField = new JTextField(this.port.toString(), 15);
		portField.setHorizontalAlignment(SwingConstants.CENTER);
		portField.setBounds(225, 420, 175, 30);
		this.add(portField);

		//Add Create Server Button.
		JButton createServerBtn = new JButton("Create Server");
		createServerBtn.setToolTipText("Click here to create a new server");
		createServerBtn.setBounds(100, 460, 300, 70);
		this.add(createServerBtn);

		createServerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {

				port = Integer.parseInt(portField.getText());

				server = new Server(port, CreateServerPanel.this);
				server.start(); //Start the server.

				createServerBtn.setEnabled(false); //Disable create server button
			}
		});
	}


	/**
	 * Used by the server to create updates.
	 * @param update the update to print in the text field.
	 */
	public void updateServerStatusField(String update) {
		this.serverStatusField.setText(update);
	}


	/**
	 * Print an update to the text print area on the create sever panel.
	 * @param update the update to print.
	 */
	public void printToTextPrintArea(String update) {
		String current = this.printTextArea.getText();
		this.printTextArea.setText(current + "\n " + update);

	}

}
