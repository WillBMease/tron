package tronPackage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
// Import all SQL commands
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

public class TronServer {
	private Vector<TronPlayerThread> tptVector = new Vector<TronPlayerThread>();
	private Vector<TronGameThread> gtVector = new Vector<TronGameThread>();
	// Tron Game variables
	private int numPlayersForGame;
	private int numCurrPlayers = 0;
	private int eliminations = 0;
	private ArrayList<Boolean> isDead = new ArrayList<Boolean>();

	private int numDerezzes = 0;
	
	// SQL variables 
	private Connection conn;
	
	// Chat variables
	private String chatStorage = "3 CHAT ";
	
	public TronServer(int port) {
		// SQL Data Base Setup
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/tron", "root", "password");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Server Setup
		numPlayersForGame = 0;
		try {
			ServerSocket ss = new ServerSocket(port);
			while (true) {
				//System.out.println("Waiting for more Players...");
				Socket s = ss.accept();
				System.out.println("Connection from " + s.getInetAddress());
				TronPlayerThread ct = new TronPlayerThread(s, this);
				tptVector.add(ct);
				numPlayersForGame++;
				ct.start();
			}

		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}
	public void GameMessage(String line, TronPlayerThread tpt) {
		if (line.contains("CONNECT")) {
			numCurrPlayers++;
			isDead.add(false);
			if(numCurrPlayers == 2)
			{
				for(int i = 0; i < tptVector.size();i++)
					tptVector.get(i).send("1 START "+i);
			}
			System.out.println("Sent START message to players");
		}
		if(line.contains("DEREZZ"))
		{
			System.out.println("DEREZZ:" + line);
			for(int i = 0; i < tptVector.size();i++)
			{
				System.out.println("send drezz message");
				tptVector.get(i).send(line);
			}
			String parsedLine [] = line.split(" ");
			isDead.set(Integer.parseInt(parsedLine[2]),true);
			eliminations++;
			System.out.println("There have been " + eliminations + "and total is " + numCurrPlayers);
			if(eliminations == numCurrPlayers-1)
			{
				for(int i = 0 ; i < isDead.size();i++)
				{
					System.out.println("checking for winner");
					if(!isDead.get(i))
					{
						System.out.println("send win message");
						for(int j = 0; j < tptVector.size();j++)
						{
							tptVector.get(j).send("1 WINNER "+i);
						}
					}
				}
			}
		}
		else
		{
			System.out.println("Sending " + line);
			for(int i = 0; i < tptVector.size();i++)
			{
				if(tptVector.get(i) != tpt)
				{
					tptVector.get(i).send(line);
				}
			}
		}
	}
	public void SQLMessage(String line, TronPlayerThread tpt) {
		
	}
	public void ChatMessage(String line, TronPlayerThread tpt) {
		if (line.contains("CHAT")) {
			chatStorage = line;
			System.out.println(chatStorage);
			for(int i = 0; i < tptVector.size();i++)
			{
				if(tptVector.get(i) != tpt)
				{
					tptVector.get(i).send(chatStorage);
				}
			}
			
		}
	}
	
	public void sendMessage(String message, TronPlayerThread tpt) {
		for(TronPlayerThread c : tptVector) {
			/*
			if (!c.equals(tpt)) {
				c.send(message);
			} */
		}
	}
	public void removeChatThread(TronPlayerThread tpt) {
		tptVector.remove(tpt);
	}

	public static void main(String [] args) {
		Scanner scan = new Scanner(System.in);
		System.out.print("What port? ");
		int port = scan.nextInt();
		new TronServer(port);
	}
}
