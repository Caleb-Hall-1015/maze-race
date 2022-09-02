/*
 * Class: Cell
 * 
 * Purpose: Be a representation of the nodes in the maze
 * 
 * Attributes: boolean isWall
 * 			   boolean isVisited
 * 			   int xLocation
 * 			   int yLocation
 * 
 * Methods: none
 * 
 * Created 11/3/21
 * Updated 11/5/21
 */

public class Cell 
{
	private boolean isWall, isVisited;
	private int xLocation, yLocation;
	
	public Cell(int x, int y)
	{
		xLocation = x;
		yLocation = y;
		isWall = true;
		isVisited = false;
	}
	
	//setters and getters
	public boolean isVisited() 
	{
		return isVisited;
	}
	
	public void setVisited(boolean isVisited) 
	{
		this.isVisited = isVisited;
	}

	public int getxLocation() 
	{
		return xLocation;
	}

	public int getyLocation() 
	{
		return yLocation;
	}
	
	public void setIsWall(boolean isWall)
	{
		this.isWall = isWall;
	}
	
	public boolean getIsWall()
	{
		return isWall;
	}
}
