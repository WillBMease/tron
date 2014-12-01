package tronPackage;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.JPanel;

public class HomePage extends JPanel {
	GameMenu gm;
	private EastMenu em; // refer to design document picture, these two panels split HomePage panel graphically
	public HomePage(BufferedReader br, PrintWriter pw, TronPlayer tp) {
		super();
		setSize(800,800);
		/* Keep BorderLayout, Center will be gameMenuPanel, East will be profile information and chat info
	    */
		gm = new GameMenu(pw, br, tp);
		add(gm);
		
		em = new EastMenu();
		add(em, BorderLayout.EAST);
		
	}
}
