/*
 * Class: MazeGenerator
 * 
 * Purpose: To create the maze
 * 
 * Attributes: Cell[][] maze
 * 			   Cell start
 * 			   ArrayList<Cell> neighbors
 *  		   ArrayList<Cell> unvisitedNeighbors
 *  		   ArrayList<Cell> path
 *  
 * Methods: initialize(): void
 * 			setMaze(int difficultyLevel): void
 * 			getUnvisitedNeighbors(Cell current): ArrayList<Cell>
 * 			getRandomUnvisitedNeighbor(Cell current): Cell
 * 			findNeighbors(Cell current): void
 * 			recursiveBacktracking(Cell current): void
 * 			finalizeMaze(): void
 * 			allSides(Cell current): Cell[]
 * 			getRandomInt(int max): int
 * 
 * Created 11/3/21
 * Updated 11/18/21
 */

import java.util.*;

public class MazeGenerator 
{
	private Cell[][] maze = new Cell[39][39];
	private Cell start;
	private ArrayList<Cell> neighbors = new ArrayList<Cell>();
	private ArrayList<Cell> unvisitedNeighbors = new ArrayList<Cell>();
	private ArrayList<Cell> path = new ArrayList<Cell>();
	
	public MazeGenerator()
	{	
	}
	
	//fills up the maze with Cells, then sets the initial walls
	public void initialize()
	{
		for (int x = 0; x < 39; x++)
		{
			for (int y = 0; y < 39; y++)
			{
				getMaze()[y][x] = new Cell(x, y);
			}
		}
		
		for (int x = 0; x < 39; x++)
		{
			for (int y = 0; y < 39; y++)
			{
				if (x % 2 == 0 && y % 2 == 0)
				getMaze()[y][x].setIsWall(false);
			}
		}
	}
	
	//tells which algorithm to use to generate maze
	public void setMaze()
	{
		start = maze[0][0];
		recursiveBacktracking(start);
		finalizeMaze();
	}
	
	//returns an ArrayList of Cells that are neighbors to the parameter and have not been visited
	public ArrayList<Cell> getUnvisitedNeighbors(Cell current)
	{
		ArrayList<Cell> temp = new ArrayList<>();

		for (int i = 0; i < neighbors.size(); i++)
		{
			if (neighbors.get(i) != null)
			{
				if (!neighbors.get(i).isVisited())
				{
					temp.add(neighbors.get(i));
				}
			}
		}
		unvisitedNeighbors.addAll(temp);
		return temp;
	}
	
	//randomly gets an unvisited neighbor of the parameter cell
	public Cell getRandomUnvisitedNeighbor(Cell current)
	{
		ArrayList<Cell> temp = new ArrayList<Cell>(getUnvisitedNeighbors(current));
		
		if (temp.size() > 0)
		{
			return temp.get(getRandomInt(temp.size()));
		} else
		{
			return null;
		}
	}
	
	//returns all neighbors of the parameter regardless if they have been visited or not
	public void findNeighbors(Cell current)
	{
		neighbors.clear();
		if (current.getyLocation() > 1)
		{
			//north
			neighbors.add(getMaze()[current.getyLocation() - 2][current.getxLocation()]);
		}
		
		if (current.getxLocation() < 37)
		{
			//east
			neighbors.add(getMaze()[current.getyLocation()][current.getxLocation() + 2]);
		}
		
		if (current.getyLocation() < 37)
		{
			//south
			neighbors.add(getMaze()[current.getyLocation() + 2][current.getxLocation()]);
		}
		
		if (current.getxLocation() > 1)
		{
			//west
			neighbors.add(getMaze()[current.getyLocation()][current.getxLocation() - 2]);
		}
	}
	
	//creates a majority of the pathway, but leaves untouched areas so this method must be used in conjunction
	//with the finializeMaze() method
	public void recursiveBacktracking(Cell current) 
	{
		current.setVisited(true);
		findNeighbors(current);
		Cell next = getRandomUnvisitedNeighbor(current);
			
		if (next == null)
		{
			for (int i = path.size() - 1; i > 0; i--)
			{
				findNeighbors(path.get(i));
				if (getUnvisitedNeighbors(path.get(i)).size() > 1)
				{
					recursiveBacktracking(path.get(i));
				} 
			}
		} else
		{
			Cell between = getMaze()[(current.getxLocation() + next.getxLocation()) / 2][(current.getyLocation() + next.getyLocation()) / 2];
			between.setIsWall(false);
			unvisitedNeighbors.remove(current);
			path.add(current);
			recursiveBacktracking(next);
		}
	}
	
	//for use after the recursiveBacktracking method to ensure every cell gets visited
	public void finalizeMaze()
	{
		for (int x = 0; x < 39; x += 2)
		{
			for (int y = 0; y < 39; y += 2)
			{
				if (getMaze()[y][x].isVisited() == false)
				{
					Cell current = getMaze()[y][x];
					findNeighbors(getMaze()[y][x]);
					Cell next = neighbors.get(getRandomInt(neighbors.size()));
					Cell between = getMaze()[(current.getxLocation() + next.getxLocation()) / 2][(current.getyLocation() + next.getyLocation()) / 2];
					between.setIsWall(false);
				}
			}
		}
	}
	
	//passes neighbors to other classes, especially AI, so it knows where it can go and can access those Cells
	public Cell[] allSides(Cell current)
	{
		Cell[] allNeighbors = new Cell[4];
		
		if (current.getyLocation() > 0)
		{
			allNeighbors[0] = getCell(current.getxLocation(), current.getyLocation() - 1);
		}
			
		if (current.getxLocation() < 38)
		{
			allNeighbors[1] = getCell(current.getxLocation() + 1, current.getyLocation());
		}
			
		if (current.getyLocation() < 38)
		{
			allNeighbors[2] = getCell(current.getxLocation(), current.getyLocation() + 1);
		}
			
		if (current.getxLocation() > 0)
		{
			allNeighbors[3] = getCell(current.getxLocation() - 1, current.getyLocation());
		}
		
		return allNeighbors;
	}
	
	public int getRandomInt(int max)
	{
		return (int) (Math.random() * max);
	}
	
	//setters and getters
	
	public Cell getCell(int x, int y)
	{
		return getMaze()[y][x];
	}
	
	public boolean isWall(int x, int y)
	{
		return getMaze()[y][x].getIsWall();
	}

	public Cell[][] getMaze() {
		return maze;
	}
}