package tronPackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TheGrid extends JPanel implements ActionListener,Runnable {
	private static final long serialVersionUID = 1L;
	private static final Color gridBGColor = new Color(20,20,100);
	private static final Color gridColor = new Color(100,100,100);	
	static final int GRID_DIM = 700;
	private static final int NUM_CELL_ROW = 50;
	private static final int CELL_DIM =  GRID_DIM/NUM_CELL_ROW;
	static final int LEFT = 0;
	static final int RIGHT = 1;
	static boolean [] [] GRID_WALLS = new boolean [GRID_DIM] [GRID_DIM];
	Timer timer;
	private double angle = 0;
	private double targetAngle = 900;
	private static int startAngle = 18;
	private PrintWriter pw;
	int myCycleNum = LightCycle.NORTH;
	private int deltaAngle = startAngle;
	KeyListener kl = new MyKeyListener();
	Deque<Integer> keyBuffer = new ConcurrentLinkedDeque<Integer>();
	ArrayList <LightCycle> combatants = new ArrayList<LightCycle>(4);
	Color cycleColors [] = {new Color(255,255,50), new Color(255,255,255), 
			new Color(200,150,200), new Color(200,240,255)};
	//LightCycle User;
	//LightCycle User2;

	public TheGrid(PrintWriter pw) {
		requestFocusInWindow();
		addKeyListener(kl);
		for(int i = 0 ; i < GRID_DIM;i++)
		{
			for(int j = 0; j< GRID_DIM;j++)
			{
				GRID_WALLS [i][j] = false;
			}
		}
		setFocusable(true);
		this.pw = pw;
		timer = new Timer(20, this);
		setBackground(gridBGColor);
		setVisible(true);
		angle = -myCycleNum*90;
		for(int i = 0 ; i < 4;i++)
		{
			combatants.add(new LightCycle(cycleColors[i],i,this));
		}
	}
	public void movementStart(int assignedCycleNum)
	{
		Thread myself = new Thread(this);
		//CountDownThread cd = new CountDownThread(User);
		//CountDownThread cd2 = new CountDownThread(User2);
		for(LightCycle c: combatants)
		{
			CountDownThread cd = new CountDownThread(c);
			cd.start();
		}
		myCycleNum = assignedCycleNum;
		angle = -myCycleNum*90; 
		repaint();
		myself.start();
		//cd.start();
		//cd2.start();
	}
	public void derezz(int cycleNum)
	{
		combatants.get(cycleNum).derezz();
	}
	public void paint(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d = (Graphics2D)g.create();
		g2d.rotate(Math.toRadians(angle), GRID_DIM/2, GRID_DIM/2);
		//g2d.drawString("NORTH", 250, 100);
		g2d.setColor(gridColor);
		for(int i = 0; i <=GRID_DIM;i=i+CELL_DIM)
		{
			g2d.drawLine(i, 0, i, GRID_DIM);
			g2d.drawLine(0, i, GRID_DIM, i);
		}
		for(LightCycle c: combatants)
		{
			c.paint(g2d);
		}
		//User.paint(g2d);
		//User2.paint(g2d);
	}
	public void otherCycleTurn(int cycleNumber, int direction) //left right
	{
		if(cycleNumber != myCycleNum)
		{
			LightCycle action = combatants.get(cycleNumber);
			if(direction == LEFT)
			{
				action.turnLeft();
			}
			else if(direction == RIGHT)
			{
				action.turnRight();
			}
		}
	}
	/*public void sendMessage(String msg)
	{
		pw.print(msg);
		pw.flush();
	}*/
	static public void markAsBlocked(int x,int y)
	{
		if(x == 280 && y == 480)
		{
			System.out.println("Mark: "+x +" "+y);
		}
		GRID_WALLS[x][y] = true;
	}
	static public boolean checkBlocked(int x, int y)
	{
		if(x >= GRID_DIM || y >= GRID_DIM || x <= 0 || y <= 0)
		{
			return true;
		}
		return GRID_WALLS[x][y];
	}
	public static void main(String[] args) {

		JFrame frame = new JFrame("TRON");
		//frame.add(new TheGrid(new PrintWriter()));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 700+50);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	public void sendMessage(String line)
	{
		pw.println(line);
		pw.flush();
	}
	public void actionPerformed(ActionEvent e) {
		angle += deltaAngle;

		repaint();
		if(angle==targetAngle)
		{
			timer.stop();
		}
	}
	class MyKeyListener implements KeyListener {

		public void keyTyped(KeyEvent e) {
		}


		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode();
			if(code == KeyEvent.VK_LEFT)
			{
				sendMessage("1 LEFT "+ myCycleNum);
				combatants.get(myCycleNum).turnLeft();
				keyBuffer.add(startAngle);
			}
			else if(code == KeyEvent.VK_RIGHT)
			{
				sendMessage("1 RIGHT "+ myCycleNum);
				combatants.get(myCycleNum).turnRight();
				keyBuffer.add(-startAngle);
			}
		}


		public void keyReleased(KeyEvent e) {
		}

	}
	public void run() {
		while(true)
		{
			repaint();
			if(!keyBuffer.isEmpty())
			{
				if(!timer.isRunning())
				{
					deltaAngle = keyBuffer.remove();
					targetAngle = angle + 90*deltaAngle/startAngle;
					timer.start();
				}
				else if(keyBuffer.peek() == -deltaAngle)
				{
					timer.stop();
					deltaAngle = keyBuffer.remove();
					targetAngle = targetAngle + 90*deltaAngle/startAngle;
					timer.start();
				}
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
class CountDownThread extends Thread
{
	LightCycle User;
	public CountDownThread (LightCycle User)
	{
		this.User = User;
	}
	public void run()
	{
		for(int i = 3; i>0;i--)
		{
			System.out.println("count down " + i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		User.start();
	}
}