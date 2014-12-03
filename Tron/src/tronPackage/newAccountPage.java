package tronPackage;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class newAccountPage extends JPanel {
	
	JPanel parentPanel;
	PrintWriter pw;
	
	JLabel newUsernameLabel;
	JLabel newPasswordLabel;
	JLabel confirmPasswordLabel;
	
	JTextField newUsernameField;
	JPasswordField newPasswordField;
	JPasswordField confirmPasswordField;
	
	JButton createAccountButton;
	JButton cancelCreateAccountButton;
	
	public newAccountPage(PrintWriter pw, JPanel parent) {
		super();
		this.setSize(800,800);
		this.pw = pw;
		this.parentPanel = parent;
		
		
		newUsernameLabel = new JLabel("Username");
		newPasswordLabel = new JLabel("Password");
		confirmPasswordLabel = new JLabel("Confirm Password");
		
		newUsernameField = new JTextField(10);
		newPasswordField = new JPasswordField(10);
		confirmPasswordField = new JPasswordField(10);
		
		createAccountButton = new JButton("Create");
		cancelCreateAccountButton = new JButton("Cancel");
				
		createAccountButton.addActionListener(new ActionListener() {
					
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: Check database for existing user with username
				boolean usernameExists = false;
				if(usernameExists) {
					
				} else if(Arrays.equals(newPasswordField.getPassword(), confirmPasswordField.getPassword()) && newPasswordField.getPassword().length > 8) {
					String username = newUsernameField.getText();
					char[] password = newPasswordField.getPassword();
					//TODO: create new User in database
					
					newUsernameField.setText("");
					newPasswordField.setText("");
					confirmPasswordField.setText("");
					CardLayout c1 = (CardLayout) parentPanel.getLayout();
					c1.show(parentPanel, "loginPage");
				} else {
					System.out.println("INVALID PASSWORD");
				}
			}
		});
		
		cancelCreateAccountButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				newUsernameField.setText("");
				newPasswordField.setText("");
				confirmPasswordField.setText("");
				CardLayout c1 = (CardLayout) parentPanel.getLayout();
				c1.show(parentPanel, "loginPage");
			}
			
		});
		
		this.addComponents();

		
		JLabel image = new JLabel(new ImageIcon("../loginPageBackground.jpg"));
		this.add(image);
	}
	
	void addComponents(){
		this.add(newUsernameLabel);
		this.add(newUsernameField);
		this.add(newPasswordLabel);
		this.add(newPasswordField);
		this.add(confirmPasswordLabel);
		this.add(confirmPasswordField);
		this.add(createAccountButton);
		this.add(cancelCreateAccountButton);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
