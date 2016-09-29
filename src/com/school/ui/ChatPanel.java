package com.school.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatPanel extends JPanel{
	private JTextField typeArea;
	private JButton sendMessage;
	private JTextArea messageList;
	private Display home;
	
	public ChatPanel(Display display) {
		super(new BorderLayout());
		this.setOpaque(true);
		home = display;
        this.setSize(288, 488);
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.BLACK);
        southPanel.setLayout(new GridBagLayout());

        typeArea = new JTextField(30);
      
        sendMessage = new JButton("Send");
        sendMessage.addActionListener(new sendMessageButtonListener());

        messageList = new JTextArea();
        messageList.setEditable(false);
        messageList.setOpaque(true);
        try {
    		Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/com/school/ui/slkscr.ttf"));
    		typeArea.setFont(font.deriveFont(Font.TRUETYPE_FONT, 12f));
    		messageList.setFont(font.deriveFont(Font.TRUETYPE_FONT, 12f));
    		
    	} catch (Exception e) {}
        
        messageList.setLineWrap(true);
        JScrollPane area = new JScrollPane(messageList);
        
        this.add(area, BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        southPanel.add(typeArea, left);
        southPanel.add(sendMessage, right);

        this.add(BorderLayout.SOUTH, southPanel);

        this.setVisible(true);
    }

    class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (typeArea.getText().length() < 1) {
                // do nothing
            } else if (typeArea.getText().equals(".clear")) {
                messageList.setText("Cleared all messages\n");
                typeArea.setText("");
            } else {
                messageList.append("<"+ ">:  " + typeArea.getText()
                        + "\n");
                typeArea.setText("");
            }
            home.refocus();
        }
    }
}
