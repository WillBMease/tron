package tronPackage;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
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
	
	private PrintWriter pw;
	
	
	public loginPage(PrintWriter pw, JPanel parent) {
		super();
		this.setSize(800, 800);
		this.pw = pw;
		this.parentPanel = parent;
		
		passwordField = new JPasswordField(10);
		usernameField = new JTextField(10);
		passwordLabel = new JLabel("Password:");
		usernameLabel = new JLabel("User Name:");
		
		loginButton = new JButton("Login");
		newAccountButton = new JButton("Create New Account");
		
		loginButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: check password and username against database
				//TODO: redirect to game page if login successful
				CardLayout c1 = (CardLayout) parentPanel.getLayout();
				c1.show(parentPanel, "homePage");
			}
		});
		
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
	
	/*@Override
	public void paint(Graphics g){
		super.paintComponent(g);
	    g.drawImage(backgroundImage.getImage(), 0, 0, null);
		
	}*/
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(800, 800);
		frame.add(new loginPage(null, null));
		frame.setVisible(true);
	}

}