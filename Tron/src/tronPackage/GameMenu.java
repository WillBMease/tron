package tronPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
			chatMsgs.setText(temp);
		}
		else{
			temp += chatField.getText();
			chatMsgs.setText(temp);
		}
		temp = "3 CHAT " + chatField.getText();
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
