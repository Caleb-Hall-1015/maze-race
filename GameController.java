/*
 * Class: GameController
 * 
 * Purpose: Run the program and be the main point of contact between GUI and math model
 * 
 * Attributes: MazeGenerator mazeGenerator
 * 			   AI AI
 * 			   Player player
 * 			   File pastGames
 * 
 * Methods: makeMaze(): void
 * 			makeMoves(): void
 * 			isCellWall(int x, int y): boolean
 * 			wherePlayer(): double[]
 * 			whereAI(): double[]
 * 			gameOver(): boolean
 * 			possibleMoves(double x, double y): boolean[]
 * 			isMoveOutOfBounds(double x, double y, int direction): boolean
 * 			resetMaze(): void
 * 			setDifficultyLevel(): void
 * 			parseFile(): void
 * 			getWins(): int
 * 			getLosses(): int
 * 			getRatio(): double
 * 
 * Created 11/3/21
 * Updated 12/3/21
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class GameController 
{	
	private MazeGenerator mazeGenerator = new MazeGenerator();
	private AI AI;
	private Player player = new Player();
	private File pastGames = new File("Past Games.txt");
	
	//constructor
	public GameController()
	{
	}
	
	public void setDifficultyLevel(int difficultyLevel)
	{
		AI = new AI(difficultyLevel, mazeGenerator.getCell(0, 0));
	}

	public int[] parseFile() {
		// this will be to for the requirements
		int[] contents =  new int[2];
		int wins = 0;
		int losses = 0;

		try {
			FileReader fr = new FileReader(pastGames);
			BufferedReader br = new BufferedReader(fr);
			// make a String called line
			String line;

			// read the header line in the file
			line = br.readLine();
			while ((line = br.readLine()) != null) 
			{
				if (line.equalsIgnoreCase("w"))
				{
					wins++;
				} else
				{
					losses++;
				}
			}
			br.close();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		contents[0] = wins;
		contents[1] = losses;
		return contents;
	}

	
	//creates the maze
	public void makeMaze()
	{
		mazeGenerator.initialize();
		mazeGenerator.setMaze();
	}
	
	public void makeMoves()
	{
		double x = (AI.getXLocation() - 3.5) / 17;
		double y = (AI.getYLocation() - 3.5) / 17;
		
		AI.move(possibleMoves(x, y), mazeGenerator.allSides(mazeGenerator.getCell((int) x, (int) y)), mazeGenerator.getCell((int) x, (int) y));
		//Player.move();
	}
	
	//allows passing between MazeGenerator and GUI so the Cells can be displayed as the correct color
	public boolean isCellWall(double x, double y)
	{
		if (x >= 0 && x <= 38 && y >= 0 && y <= 38)
		{
			return mazeGenerator.isWall((int) x, (int) y);
		}
		return true;
	}
	
	public double[] wherePlayer()
	{
		double[] location = {getPlayer().getXLocation(), getPlayer().getYLocation()};
		return location;
	}
	
	public double[] whereAI()
	{
		double[] location = {AI.getXLocation(), AI.getYLocation()};
		return location;
	}
	
	public boolean gameOver()
	{
		double[] playerLoc = wherePlayer();
		double[] AILoc = whereAI();

		if (((playerLoc[0] - 3.5) / 17) == 38 && ((playerLoc[1] - 3.5) / 17) == 38)
		{
			System.out.println("GAME OVER");
			try
			{
				FileWriter fileWriter = new FileWriter(pastGames, true);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				bufferedWriter.write("w");
				bufferedWriter.newLine();
				bufferedWriter.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			return true;
		}
		
		if (((AILoc[0] - 3.5) / 17) == 38 && ((AILoc[1] - 3.5) / 17) == 38)
		{
			try
			{
				//https://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java
				FileWriter fileWriter = new FileWriter(pastGames, true);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				bufferedWriter.write("l");
				bufferedWriter.newLine();
				bufferedWriter.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			return true;
		}
		
		return false;
	}
	
	public int getWins()
	{
		return parseFile()[0];
	}
	
	public int getLosses()
	{
		return parseFile()[1];
	}
	
	public double getRatio()
	{
		return ((double) getWins() / (getWins() + getLosses())) * 100;
	}
	
	public boolean[] possibleMoves(double x, double y)
	{
		boolean[] moves = {false, false, false, false};
		
		if (!isMoveOutOfBounds(x, y, 0))
		{
			if (!(isCellWall(x, y - 1)))
			{
				moves[0] = true;
			}
		}
		
		if (!isMoveOutOfBounds(x, y, 1))
		{
			if (!(isCellWall(x + 1, y)))
			{
				moves[1] = true;
			}
		}
		
		if (!isMoveOutOfBounds(x, y, 2))
		{
			if (!(isCellWall(x, y + 1)))
			{
				moves[2] = true;
			}
		}
			
		if (!isMoveOutOfBounds(x, y, 3))
		{
			if (!(isCellWall(x - 1, y)))
			{
				moves[3] = true;
			}
		}
		
		return moves;
	}
	
	public boolean isMoveOutOfBounds(double x, double y, int direction)
	{
		if (direction == 0 && y < 1)
		{
			return true;
		}
		
		if (direction == 1 && x > 38)
		{
			return true;
		}
		
		if (direction == 2 && y > 38)
		{
			return true;
		}
		
		if (direction == 3 && x < 1)
		{
			return true;
		}
		
		return false;
	}
	
	public void resetMaze()
	{
		for (int x = 0; x < 39; x++)
		{
			for (int y = 0; y < 39; y++)
			{
				if (!mazeGenerator.isWall(x, y))
				{
					mazeGenerator.getMaze()[y][x].setVisited(false);
				}
			}
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
