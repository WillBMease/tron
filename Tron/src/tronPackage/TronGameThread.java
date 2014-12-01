package tronPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TronGameThread extends Thread {
	private Socket s;
	private TronServer ts;
	private PrintWriter pw;
	public TronGameThread(Socket s, TronServer ts) {
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
				//ts.sendMessage(line, this);
			}
		} catch (IOException ioe) {
			//ts.removeChatThread(this);
			System.out.println("Client disconnected from " + s.getInetAddress());
		}
	}
}
