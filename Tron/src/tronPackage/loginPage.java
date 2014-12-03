package tronPackage;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.Arrays;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class loginPage extends JPanel {
	JPanel cards;
	JPanel loginPanel;
	JPanel newAccountPanel;
	
	JPasswordField passwordField;
	JTextField usernameField;
	
	JLabel passwordLabel;
	JLabel usernameLabel;
	
	JButton loginButton;
	JButton newAccountButton;
	
	
	ImageIcon backgroundImage;
	JPanel parentPanel;

	TronPlayer tp;
	
	private PrintWriter pw;
	
	
	public loginPage(PrintWriter pw, JPanel parent, TronPlayer tp) {
		super();
		this.setSize(800, 800);
		this.pw = pw;
		this.parentPanel = parent;
		this.tp = tp;
		
		passwordField = new JPasswordField(10);
		usernameField = new JTextField(10);
		passwordLabel = new JLabel("Password:");
		usernameLabel = new JLabel("User Name:");
		
		loginButton = new JButton("Login");
		newAccountButton = new JButton("Create New Account");
		
		loginButton.addActionListener(new loginPageButton(tp, parentPanel, usernameField, passwordField));
		
		newAccountButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				usernameField.setText("");
				passwordField.setText("");
				CardLayout c1 = (CardLayout) parentPanel.getLayout();
				c1.show(parentPanel, "newAccountPage");
				
			}
		});
		
		
		
	
		
		
		
		this.addComponents();

		backgroundImage = new ImageIcon("../loginPageBackground.jpg");
		
		JLabel image = new JLabel(backgroundImage);
		this.add(image);
		
	}
	
	void addComponents(){
		this.add(usernameLabel);
		this.add(usernameField);
		this.add(passwordLabel);
		this.add(passwordField);
		this.add(loginButton);
		this.add(newAccountButton);
	}

	static boolean checkIfNameIsValid(String name){
		try{
			FileReader fr = new FileReader("./log.txt");
			BufferedReader br = new BufferedReader(fr);
			boolean nameFound = false;
			String line = br.readLine();
			while(line != null){
				System.out.println(line);
				String array[] = line.split(":");
				if(name.equals(array[0])){
					nameFound = true;
					break;
				}
				line = br.readLine();
			}
			br.close();
			fr.close();
			return nameFound;
		}catch(IOException ioe){
			System.out.println("Error in checkIfNameIsValid: " + ioe.getMessage());
		}
		return false;
	}

	static boolean checkifCorrectPassword(String name, String password){
		try{
			FileReader fr = new FileReader("./log.txt");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			String array[] = null;
			while(line != null){
				array = line.split(":");
				if(array[0].equals(name)){
					break;
				}
				line = br.readLine();
			}
			br.close();
			fr.close();
			if(array[1].equals(password)){
				return true;
			}else{
				return false;
			}
		}catch(IOException ioe){

		}
		return false;
	}
	
	/*@Override
	public void paint(Graphics g){
		super.paintComponent(g);
	    g.drawImage(backgroundImage.getImage(), 0, 0, null);
		
	}*/
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(800, 800);
		frame.add(new loginPage(null, null, null));
		frame.setVisible(true);
	}

}



class loginPageButton implements ActionListener {

	TronPlayer tp;
	JPanel parent;
	JTextField usernameField;
	JPasswordField password;

	loginPageButton(TronPlayer tp, JPanel parent, JTextField username, JPasswordField password){
		super();
		this.tp = tp;
		this.parent = parent;
		this.usernameField = username;
		this.password = password;
	}

	public void actionPerformed(ActionEvent ae){
		CardLayout c1 = (CardLayout) parent.getLayout();
		System.out.println("TEst");
		if(loginPage.checkIfNameIsValid(usernameField.getText())){
			System.out.println("Made it past name check");
			if(loginPage.checkifCorrectPassword(usernameField.getText(), new String(password.getPassword()))){
				tp.setPlayerName(usernameField.getText());
				c1.show(parent, "homePage");
			}
		}
	}
}