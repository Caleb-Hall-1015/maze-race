/*
 * Class: AI
 * 
 * Purpose: AI character movement and math model
 * 
 * Attributes: double xLocation
 * 			   double yLocation
 * 			   int difficultyLevel
 * 			   ArrayList<Cell> path
 * 			   ArrayList<Cell> neighbors
 * 			   int direction
 * 			   int numReverses
 * 			
 * 
 * Methods: move(boolean[] possibleMoves, Cell[] allNeighbors, Cell current): void
 * 			getRandomInt(int max): int
 * 			hasUnvisitedNeighbors(Cell[] allNeighbors): boolean
 * 			findClosest(boolean[] possibleMoves, Cell[] allNeighbors): int
 * 			randomWalking(boolean[] possibleMoves, Cell[] allNeighbors): void
 * 			depthFirstSearch(boolean[] possibleMoves, Cell[] allNeighbors, Cell current): void
 * 
 * Created 11/9/21
 * Updated 12/12/21
 */

import java.util.ArrayList;

public class AI extends Dot
{
	private int difficultyLevel;
	//private double xLocation, yLocation;
	private ArrayList<Cell> path = new ArrayList<Cell>();
	private ArrayList<Cell> neighbors = new ArrayList<Cell>();
	int direction = 0;
	int numReverses;
	
	
	public AI(int difficultyLevel, Cell start)
	{
		this.difficultyLevel = difficultyLevel;
		this.xLocation = 3.5;
		this.yLocation = 3.5;
	}
	
	//@Override
	public void move(boolean[] possibleMoves, Cell[] allNeighbors, Cell current)
	{
		if (difficultyLevel == 1)
		{
			randomWalking(possibleMoves, allNeighbors);
		} else
		{
			if (difficultyLevel == 2)
			{
				depthFirstSearch(possibleMoves, allNeighbors, current);
			}
		}
	}
	
	public int getRandomInt(int max)
	{
		return (int) (Math.random() * max);
	}

	public double getXLocation() {
		return xLocation;
	}

	public double getYLocation() {
		return yLocation;
	}

	//used to determine if a node has any neighbors that have not yet been visisted. works by calling the cells in each direction
	//and asking for their boolean variable. takes in an array of all neighbors, with 0 being north, 1 east, 2 south, and 3 west
	//will return true if there is at least one unvisited node, otherwise false
	public boolean hasUnvisitedNeighbors(Cell[] allNeighbors)
	{
		for (int i = 0; i < 4; i++)
		{
			if (allNeighbors[0].getyLocation() > 0)
			{
				if (!allNeighbors[0].isVisited())
				{
					return true;
				}
			}
				
			if (allNeighbors[1].getxLocation() < 38)
			{
				if (!allNeighbors[1].isVisited())
				{
					return true;
				}
			}
				
			if (allNeighbors[2].getyLocation() < 38)
			{
				if (!allNeighbors[2].isVisited())
				{
					return true;
				}
			}
				
			if (allNeighbors[3].getxLocation() > 0)
			{
				if (!allNeighbors[3].isVisited())
				{
					return true;
				}
			}
		}
		return false;
	}
	
	//uses Manhattan distance to find the closest unvisited neighbor to the current cell
	//returns the direction that cell is in from the current cell, and -1 if there is no 
	//unvisited neighbors
	public int findClosest(boolean[] possibleMoves, Cell[] allNeighbors)
	{
		Cell closest = null;
		double shortestDist = 100000000;
		
		
		for (int i = 0; i < 4; i++)
		{
			if (possibleMoves[i])
			{
				if (i == 0 && !(allNeighbors[0].isVisited()))
				{
					if ((Math.abs(allNeighbors[i].getxLocation() - 39) + Math.abs(allNeighbors[i].getyLocation() - 39)) < shortestDist)
					{
						closest = allNeighbors[i];
						shortestDist = (Math.abs(allNeighbors[i].getxLocation() - 39) + Math.abs(allNeighbors[i].getyLocation() - 39));
						direction = i;
					}
				}
				
				if (i == 1 && !(allNeighbors[1].isVisited()))
				{
					if ((Math.abs(allNeighbors[i].getxLocation() - 39) + Math.abs(allNeighbors[i].getyLocation() - 39)) <= shortestDist)
					{
						closest = allNeighbors[i];
						shortestDist = (Math.abs(allNeighbors[i].getxLocation() - 39) + Math.abs(allNeighbors[i].getyLocation() - 39));
						direction = i;
					}
				}
				
				if (i == 2 && !(allNeighbors[2].isVisited()))
				{
					if ((Math.abs(allNeighbors[i].getxLocation() - 39) + Math.abs(allNeighbors[i].getyLocation() - 39)) <= shortestDist)
					{
						closest = allNeighbors[i];
						shortestDist = (Math.abs(allNeighbors[i].getxLocation() - 39) + Math.abs(allNeighbors[i].getyLocation() - 39));
						direction = i;
					}
				}
				
				if (i == 3 && !(allNeighbors[3].isVisited()))
				{
					if ((Math.abs(allNeighbors[i].getxLocation() - 39) + Math.abs(allNeighbors[i].getyLocation() - 39)) < shortestDist)
					{
						closest = allNeighbors[i];
						shortestDist = (Math.abs(allNeighbors[i].getxLocation() - 39) + Math.abs(allNeighbors[i].getyLocation() - 39));
						direction = i;
					}
				}
			}
		}
		
		if (closest == null)
		{
			return -1;
		}
		
		return direction;
	}
	
	//uses the Java random number generator to pick a number, then checks to see if the direction that 
	//number correlates to is a possible move. if it is possible then move that direction, otherwise
	//do not move
	public void randomWalking(boolean[] possibleMoves, Cell[] allNeighbors)
	{
		int moveAttempt = getRandomInt(4);	
		
		if (possibleMoves[moveAttempt])
		{
			switch (moveAttempt)
			{
				//north
				case 0:
					yLocation -= 17;
					break;
				//east
				case 1:
					xLocation += 17;
					break;
				//south
				case 2:
					yLocation += 17;
					break;
				//west
				case 3:
					xLocation -= 17;
					break;
			}
		}
	}
	
	//uses the HDFS algorithm described in the SDD. if there are no unvisited neighbors, 
	//the algorithm moves backwards according to the path arraylist, which tracks where the 
	//AI has visited so far
	public void depthFirstSearch(boolean[] possibleMoves, Cell[] allNeighbors, Cell current)
	{ 
		current.setVisited(true);
		int next = findClosest(possibleMoves, allNeighbors);
			
		if (next == -1)
		{
			if (path.get(path.size() - numReverses).getyLocation() < ((yLocation - 3.5) / 17))
			{
				numReverses++;
				yLocation -= 17;
			} else
			{
				if (path.get(path.size() - numReverses).getxLocation() < ((xLocation - 3.5) / 17))
				{
					numReverses++;
					xLocation -= 17;
				} else
				{
					if (path.get(path.size() - numReverses).getyLocation() > ((yLocation - 3.5) / 17))
					{
						numReverses++;
						yLocation += 17;
					} else
					{
						if (path.get(path.size() - numReverses).getxLocation() > ((xLocation - 3.5) / 17))
						{
							numReverses++;
							xLocation += 17;
						}
					}
				}
			}
		} else
		{
			numReverses = 1;
			path.add(current);
			switch (direction)
			{
				//north
				case 0:
					yLocation -= 17;
					break;
				//east
				case 1:
					xLocation += 17;
					break;
				//south
				case 2:
					yLocation += 17;
					break;
				//west
				case 3:
					xLocation -= 17;
					break;
			}
		}
	}
}