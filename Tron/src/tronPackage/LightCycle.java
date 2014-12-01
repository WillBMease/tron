package tronPackage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;


public class LightCycle implements Runnable{
	int xCoor;
	int yCoor;
	int xWallEmitter;
	int yWallEmitter;
	Color color;
	LinkedList<LightWall> pastWalls = new LinkedList<LightWall> ();
	LightWall currWall;
	TheGrid grid;
	Thread myself = new Thread(this);

	private static final int shortSide = 10;
	private static final int longSide = 20;
	private int xDim = shortSide;
	private int yDim = longSide;
	int direction = STOPPED;
	private int speed = 3;
	private int startingDirection;
	private int cycleNumber;
	static final int NORTH = 0;
	static final int EAST = 1;
	static final int SOUTH = 2;
	static final int WEST = 3;
	static final int STOPPED = 5;
	static final int DEREZZED = 6;
	static final int AWAITING_WINNER = 7;
	static final int [] startLocations= {345,600,100,345,355,100,600,355};
	public LightCycle(Color color, int startingDirection,TheGrid grid)
	{
		this.color = color;
		this.startingDirection = startingDirection;
		cycleNumber = startingDirection;
		//int xStart;
		//int yStart;
		xCoor = startLocations[startingDirection*2];
		yCoor = startLocations[startingDirection*2+1];
		xWallEmitter = xCoor+shortSide/2;
		yWallEmitter = yCoor+longSide/2;
		currWall = new LightWall(this,xWallEmitter,yWallEmitter);
		this.grid = grid;
		if(startingDirection%2 == 1)
		{
			swapDim();
		}
		//myself.start();
	}
	public void start()
	{
		//grid.addKeyListener(grid.kl);
		System.out.println("started");
		direction = startingDirection;
		myself.start();
	}
	void derezz()
	{
		direction = DEREZZED;
	}
	void stopCycle()
	{
		direction = AWAITING_WINNER;
	}
	protected void paint(Graphics2D g)
	{
		g.setColor(color);
		g.fillRect(xCoor, yCoor, xDim, yDim);
		currWall.paint(g);
		g.setColor(Color.RED);
		for(LightWall lw : pastWalls)
		{
			lw.paint(g);
		}
	}
	public void turnLeft()
	{
		endWall();
		direction = (direction+3)%4;
		swapDim();
	}
	public void turnRight()
	{
		endWall();
		direction = (direction+1)%4;
		swapDim();
	}
	private void endWall()
	{
		currWall.endOfTheWall();
		pastWalls.add(currWall);
		currWall = new LightWall(this,xWallEmitter,yWallEmitter);
	}
	private void swapDim()
	{
		if(xDim == shortSide)
		{
			xDim = longSide;
			yDim = shortSide;
			yCoor = yWallEmitter - shortSide/2;
			xCoor = xWallEmitter - longSide/2;
		}
		else
		{
			xDim = shortSide;
			yDim = longSide;
			yCoor = yWallEmitter - longSide/2;
			xCoor = xWallEmitter - shortSide/2;
		}
	}
	private boolean inBounds()
	{
		return (direction == NORTH && yCoor > 0) || (direction == SOUTH && yCoor < TheGrid.GRID_DIM-longSide)
				|| (direction == EAST && xCoor < TheGrid.GRID_DIM-longSide) || (direction == WEST && xCoor > 0 
				|| direction == STOPPED);
	}
	private boolean hitWall()
	{
		switch(direction)
		{
		case NORTH:
			for(int j = 0; j < speed;j++)
			{
				if(TheGrid.checkBlocked(xCoor, yCoor+j) || TheGrid.checkBlocked(xCoor+xDim, yCoor+j))
				{
					return true;
				}
			}
			for(int i = 0 ; i < xDim;i++)
			{
				for(int j = 0; j < speed;j++)
				{
					if(TheGrid.checkBlocked(xCoor+i, yCoor))
					{
						return true;
					}
				}
			}
			break;
		case EAST:
			for(int j = 0; j < speed;j++)
			{
				if(TheGrid.checkBlocked(xCoor+xDim-j, yCoor) || TheGrid.checkBlocked(xCoor+xDim-j, yCoor+yDim))
				{
					return true;
				}
			}
			for(int i = 0 ; i < yDim;i++)
			{
				if(TheGrid.checkBlocked(xCoor+xDim, yCoor+i))
				{
					return true;
				}
			}
			break;
		case SOUTH:
			for(int j = 0; j < speed;j++)
			{
				if(TheGrid.checkBlocked(xCoor, yCoor+yDim-j) || TheGrid.checkBlocked(xCoor+xDim, yCoor+yDim-j))
				{
					return true;
				}
			}
			for(int i = 0 ; i < xDim;i++)
			{
				if(TheGrid.checkBlocked(xCoor+i, yCoor+yDim))
				{
					return true;
				}
			}
			break;
		case WEST:
			for(int j = 0; j < speed;j++)
			{
				if(TheGrid.checkBlocked(xCoor+j, yCoor) || TheGrid.checkBlocked(xCoor+j, yCoor+yDim))
				{
					return true;
				}
			}
			for(int i = 0 ; i < yDim;i++)
			{
				if(TheGrid.checkBlocked(xCoor, yCoor+i))
				{
					return true;
				}
			}

		}
		/*for(int i = 0 ; i < xDim;i++)
		{
			if(TheGrid.checkBlocked(xCoor+i, yCoor) && direction != SOUTH)
			{
				return true;
			}
			if(TheGrid.checkBlocked(xCoor+i, yCoor+yDim) && direction != NORTH)
			{
				return true;
			}
		}
		for(int i = 0 ; i < yDim;i++)
		{
			if(TheGrid.checkBlocked(xCoor, yCoor+i) && direction != EAST)
			{				
				return true;
			}
			if(TheGrid.checkBlocked(xCoor+xDim, yCoor+i)&& direction != WEST)
			{
				return true;
			}
		}*/
		return false;
	}
	public void run()
	{
		while(true)
		{
			//System.out.println(xWallEmitter + " " + yWallEmitter);
			switch (direction)
			{
			case NORTH:
				yCoor-=speed;
				for(int i = 0 ; i < speed;i++)
				{
					yWallEmitter --;
					TheGrid.markAsBlocked(xWallEmitter, yWallEmitter);
				}
				break;
			case EAST:
				xCoor+=speed;
				for(int i = 0 ; i < speed;i++)
				{
					xWallEmitter ++;
					TheGrid.markAsBlocked(xWallEmitter, yWallEmitter);
				}
				break;
			case SOUTH:
				yCoor+=speed;
				for(int i = 0 ; i < speed;i++)
				{
					yWallEmitter ++;
					TheGrid.markAsBlocked(xWallEmitter, yWallEmitter);
				}
				break;
			case WEST:
				xCoor-=speed;
				for(int i = 0 ; i < speed;i++)
				{
					xWallEmitter --;
					TheGrid.markAsBlocked(xWallEmitter, yWallEmitter);
				}
				break;
			case STOPPED:
				try {
					Thread.sleep(19);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case DEREZZED:
				xDim = 0;
				yDim = 0;
				if(cycleNumber == grid.myCycleNum)
				{
					grid.sendMessage("1 DEREZZ "+cycleNumber);
					System.out.println(cycleNumber +" was derezzed " + direction);
				}
				direction = AWAITING_WINNER;
				continue;
			case AWAITING_WINNER:
				return;
			}
			if(direction != STOPPED && hitWall() && direction != AWAITING_WINNER)
			{
				System.out.println("hit wall" + xCoor+":"+yCoor);
				if(cycleNumber == grid.myCycleNum)
				{
					System.out.println("complete stop");
					grid.removeKeyListener(grid.kl);
				}
				direction = DEREZZED;
				continue;
			}
			TheGrid.markAsBlocked(xWallEmitter, yWallEmitter);
			try {
				Thread.sleep(19);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
class LightWall
{
	int xStart;
	int yStart;
	int xFinish;
	int yFinish;
	LightCycle lc;
	boolean isFinished = false;
	public LightWall(LightCycle lc,int xStart, int yStart)
	{
		this.xStart = xStart;
		this.yStart = yStart;
		this.lc = lc;
	}
	public void endOfTheWall()
	{
		isFinished = true;
		xFinish = lc.xWallEmitter;
		yFinish = lc.yWallEmitter;
	}
	public void paint(Graphics2D g)
	{
		g.setColor(lc.color);
		if(!isFinished)
		{
			g.drawLine(xStart, yStart, lc.xWallEmitter, lc.yWallEmitter);
		}
		else if(isFinished)
		{
			g.drawLine(xStart, yStart, xFinish, yFinish);		
		}
	}
}