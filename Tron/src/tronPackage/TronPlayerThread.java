package tronPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class TronPlayerThread extends Thread {
	private Socket s;
	private TronServer ts;
	private PrintWriter pw;
	private String playerName;
	public TronPlayerThread(Socket s, TronServer ts) {
		this.s = s;
		this.ts = ts;
		try {
			this.pw = new PrintWriter(s.getOutputStream());
		} catch (IOException ioe) {
			System.out.println("ioe in ChatThread: " + ioe.getMessage());
		}
	}
	
	public void send(String message) {
		pw.println(message);
		pw.flush();
	}
	
	public void run() {
		// a client has connected to the server
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			while(true) {
				String line = br.readLine();
				parseRequest(line);
			}
		} catch (IOException ioe) {
			//ts.removeChatThread(this);
			System.out.println("Client disconnected from " + s.getInetAddress());
		}
	}
	public void parseRequest(String line) {
		/* calls appropriate functions on the server
		server deals with how to send appropriate replies 
		and to whom to send replies to */
		System.out.println(line);
		if(line.startsWith("NAME:")){
			System.out.println("In name!");
			this.playerName = line.substring(6);
		}
		else if (line.contains("1")) {
			ts.GameMessage(line,this);
		}
		else if (line.contains("2")) {
			ts.SQLMessage(line,this);
		}
		else if (line.contains("3")) {
			ts.ChatMessage(line,this);
		}
	}

	public String getPlayerName(){
		return this.playerName;
	}
}

