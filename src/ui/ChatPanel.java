package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import network.Client;

public class ChatPanel extends JPanel{
	private JTextField typeArea;
	private JTextPane messageList;
	private GameFrame home;
	private String playerName;
	private Client client;
	private StyledDocument styled;
	private Style message;
	private Style systemMessage;

	private ImageIcon[] send;
	private JLabel sendButton;
	private ButtonListen listen;
	private KeyListen keyListen;

	private JButton tempSendButton;

	private UIImageMap imageMap;

	public ChatPanel(GameFrame display, String playerName, Client client, UIImageMap imageMap) {
		super(new BorderLayout());
		this.imageMap = imageMap;
		this.setOpaque(true);
		home = display;
		this.playerName = playerName;
		this.client = client;
		this.setSize(288, 488);
		JPanel southPanel = new JPanel();
		southPanel.setBackground(Color.BLACK);
		southPanel.setLayout(new GridBagLayout());

		setSend();

		listen = new ButtonListen();
		keyListen = new KeyListen();

		typeArea = new JTextField(30);
		typeArea.addKeyListener(keyListen);
		
		sendButton = new JLabel(send[0]);
		sendButton.addMouseListener(listen);


		messageList = new JTextPane();
		messageList.setEditable(false);
		messageList.setOpaque(true);
		DefaultCaret caret = (DefaultCaret)messageList.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		styled = messageList.getStyledDocument();

		message = messageList.addStyle("Message Style", null);
		StyleConstants.setForeground(message, Color.BLACK);
		systemMessage = messageList.addStyle("System Message", null);
		StyleConstants.setForeground(systemMessage, Color.RED);

		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("slkscr.ttf"));
			typeArea.setFont(font.deriveFont(Font.TRUETYPE_FONT, 12f));
			messageList.setFont(font.deriveFont(Font.TRUETYPE_FONT, 12f));

		} catch (Exception e) {}

		// messageList.setLineWrap(true);
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
		// change back to "send button"
		southPanel.add(sendButton, right);

		this.add(BorderLayout.SOUTH, southPanel);

		this.setVisible(true);
	}	

	public void addChange(List<String> changes){
		for(String change: changes){
			if(change != null){
				if(change.length() > 0){
					if(change.charAt(0) == '<'){
						try { styled.insertString(styled.getLength(), change+"\n" ,message); }
						catch (BadLocationException e){}
					} else {
						try { styled.insertString(styled.getLength(), change+"\n" ,systemMessage); }
						catch (BadLocationException e){}
					}
				}
			}
		}
	}

	public void setSend(){
		send = new ImageIcon[2];

		send[0] = new ImageIcon(imageMap.getImage("sb"));
		send[1] = new ImageIcon(imageMap.getImage("sbhi"));
	}

	public Client getClient(){
		return client;
	}

	private class KeyListen implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				if (typeArea.getText().length() < 1) {
					// do nothing
				} else {
					String chat = "CHAT :  " + typeArea.getText();
					typeArea.setText("");

					getClient().sendCommand(chat);
				}
				home.refocus();
			} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				home.refocus();
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {}

		@Override
		public void keyTyped(KeyEvent arg0) {}

	}

	private class ButtonListen implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			if (typeArea.getText().length() < 1) {
				// do nothing
			} else {
				String chat = "CHAT :  " + typeArea.getText();
				typeArea.setText("");

				getClient().sendCommand(chat);
			}
			home.refocus();

		}


		@Override
		public void mouseEntered(MouseEvent e) {
			sendButton.setIcon(send[1]);

		}

		@Override
		public void mouseExited(MouseEvent e) {
			sendButton.setIcon(send[0]);
		}

		//UNUSED
		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}


}


