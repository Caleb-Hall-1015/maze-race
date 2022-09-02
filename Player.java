/*
 * Class: Player
 * 
 * Purpose: User input and player movement
 * 
 * Attributes: double xLocation
 * 			   double yLocation
 * 
 * Methods: move
 * 
 * Created 11/9/21
 * Updated 11/19/21
 */

public class Player extends Dot
{
	public Player()
	{
		xLocation = 3.5;
		yLocation = 3.5;
	}
	
	public void move(boolean[] possibleMoves, int direction)
	{
		if (possibleMoves[direction])
		{
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
	
	public double getXLocation() {
		return xLocation;
	}

	public double getYLocation() {
		return yLocation;
	}
}
