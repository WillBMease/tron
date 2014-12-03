package tronPackage;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TronPlayer extends JFrame implements Runnable {
	private String hostname;
	private int port;
	private PrintWriter pw;
	private BufferedReader br;
	
	private TheGrid grid;
	private JPanel mainPanel;
	private JPanel loginPage;
	private JPanel newAccountPage;
	private HomePage hp;
	
	private Thread thisPlayer;
	private int playerNum;
	private String playerName;
	private boolean gameOn = false;
	//private Socket s;
	
	// Accessors
	public int getPlayerNumber() {
		return playerNum;
	}
	public void setPlayerNumber(int n) {
		playerNum = n;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setGameOn(boolean b) {
		gameOn = true;
	}
	public TronPlayer(String hostname, int port, String name) {
		super("Tron");
		
		setFocusable(true);
		// Connect to to TronServer
		this.hostname = hostname;
		this.port = port;
		this.playerName = name;
		
		try {
			Socket s = new Socket(hostname, port);
			this.pw = new PrintWriter(s.getOutputStream());
			this.br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			this.pw.println("NAME:" + getPlayerName());
			this.pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Set up GUI
		setSize(700,800);
		CardLayout cl = new CardLayout();
		
		// Main card of cardLayout
		mainPanel = new JPanel();
		mainPanel.setLayout(cl);
		
		// For test purposes only
		JButton test = new JButton("TEST");
		
		// The first page the player should see, note that visibility is set true
		loginPage = new loginPage(pw, mainPanel);
		loginPage.setVisible(true);
		loginPage.add(test);
		
		newAccountPage = new newAccountPage(pw, mainPanel);
		
		// HomePage panel
		hp = new HomePage(br, pw, this);
		
		
		test.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout tcl = (CardLayout) mainPanel.getLayout();
				tcl.show(mainPanel, "homePage");
			}
			
		});
		
		
		grid = new TheGrid(pw);
		
		mainPanel.add(loginPage, "loginPage");
		mainPanel.add(newAccountPage, "newAccountPage");
		mainPanel.add(hp, "homePage");
		mainPanel.add(grid, "grid");
		
		add(mainPanel);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//Thread Stuff
		thisPlayer = new Thread(this);
		thisPlayer.start();
		
	}

	public void run() {
		while (true) {
			String line;
			try {
				line = br.readLine();
				System.out.println("Recieved Request in run: " + line);
				// parse server returns
				if (line.contains("START")) {
					System.out.println("Recieved start message from server!");
					gameOn = true;
					((CardLayout) mainPanel.getLayout()).show(mainPanel, "grid");
					String parsedLine [] = line.split(" ");
					playerNum = Integer.parseInt(parsedLine[2]);
					grid.movementStart(Integer.parseInt(parsedLine[2]));
					addKeyListener(grid.kl);
				}
				else if(line.contains("LEFT"))
				{
					String parsedLine [] = line.split(" ");
					grid.otherCycleTurn(Integer.parseInt(parsedLine[2]), TheGrid.LEFT);
				}
				else if(line.contains("RIGHT"))
				{
					String parsedLine [] = line.split(" ");		
					grid.otherCycleTurn(Integer.parseInt(parsedLine[2]), TheGrid.RIGHT);
				}
				else if(line.contains("DEREZZ"))
				{
					System.out.println("got derezz message");
					String parsedLine [] = line.split(" ");
					if(Integer.parseInt(parsedLine[2]) == playerNum)
					{
						System.out.println("removing keyListener");
						removeKeyListener(grid.kl);
					}
					grid.derezz(Integer.parseInt(parsedLine[2]));
				}
				else if(line.contains("WINNER"))
				{
					System.out.println("there is a winner");
					String parsedLine [] = line.split(" ");
					if(Integer.parseInt(parsedLine[2]) == playerNum)
					{
						System.out.println("is me");
						setTitle("I won");
					}
					grid.combatants.get(Integer.parseInt(parsedLine[2])).stopCycle();
					
				}
				else if(line.contains("CHAT"))
				{
					String temp = line.substring(7);
					System.out.println("Request?: " +temp);
					hp.gm.chatMsgs.setText("<html>" + temp.substring(6,temp.length()-7) + "</html>");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String [] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter your name:");
		String name = scan.nextLine();
		System.out.print("What is the name/IP of the server? ");
		String hostname = scan.nextLine();
		System.out.print("What is the port? ");
		int port = scan.nextInt();
		TronPlayer tp = new TronPlayer(hostname, port, name);
	}

}
