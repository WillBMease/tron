package tronPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

public class GameMenu extends JPanel {
	private TronPlayer tp;
	private BufferedReader br;
	private PrintWriter pw;
	
	JLabel chatMsgs;
	JButton sendChat;
	JTextField chatField;
	
	public GameMenu(PrintWriter pw, BufferedReader br, TronPlayer tp) {
		super();
		this.setPreferredSize(new Dimension(600,800));
		this.pw = pw;
		this.br = br;
		this.tp = tp;
		setBackground(Color.blue);
		JButton requestGame = new JButton("I want to play a game");
		add(requestGame);
		requestGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				gameRequest();
				
			}
			
		});
		
		chatMsgs = new JLabel("**No Chat Messages**");
		chatMsgs.setOpaque(true);
		chatMsgs.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
		chatField = new JTextField(20);
		sendChat = new JButton("Send Message");
		sendChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendingChat();
			}
		});
		add(chatMsgs);
		add(chatField);
		add(sendChat);
		
		JLabel image = new JLabel(new ImageIcon("../loginPageBackground.jpg"));
		this.add(image);
		
	}
	public void gameRequest()
	{
		this.pw.println("1 CONNECT");
		this.pw.flush();
		this.tp.setGameOn(true);
	}
	
	public void sendingChat()
	{
		String temp = chatMsgs.getText();
		if (temp.equals("**No Chat Messages**")){
			temp = chatField.getText();
			chatMsgs.setText("<html>" + tp.getPlayerName() + ": " + temp + "</html>");
			System.out.println(chatMsgs.getText());
		}
		else{
			temp = chatField.getText();
			System.out.println(chatMsgs.getText());
			chatMsgs.setText("<html>" + chatMsgs.getText().substring(6,chatMsgs.getText().length()-7) + "<br>" + tp.getPlayerName() + ": " + temp + "</html>");
		}
		temp = "3 CHAT " + chatMsgs.getText();
		this.pw.println(temp);
		this.pw.flush();
	}
	
//	public void actionPerformed(ActionEvent ae) {
//		if (ae.getSource() == sendChat){
//			System.out.println("worked");
//		}
//		
//	}
}
