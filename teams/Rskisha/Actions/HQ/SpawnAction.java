package Rskisha.Actions.HQ;

import battlecode.common.*;
import Rskisha.*;
import Rskisha.Actions.IAction;

public class SpawnAction implements IAction
{
	public static RobotController rc;
	public static int[] directionOffsets = new int[]{0,1,-1,2,-2};
	public Direction dir;
	
	public SpawnAction(RobotController myRC)
	{
		super();
		rc = myRC;
		try
		{
			dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
		}
		catch(Exception e){}
	}
	
	private Direction getSpawnDirection(Direction dir) 
	{
		Direction canMoveDirection = null;
		int desiredDirOffset = dir.ordinal();
		int[] dirOffsets = new int[]{4, -3, 3, -2, 2, -1, 1, 0};
		for (int i = dirOffsets.length; --i >= 0; ) {
			int dirOffset = dirOffsets[i];
			Direction currentDirection = Direction.values()[(desiredDirOffset + dirOffset + 8) % 8];
			if (rc.canMove(currentDirection)) {
				if (canMoveDirection == null) {
					canMoveDirection = currentDirection;
				}
				Team mineTeam = rc.senseMine(rc.getLocation().add(currentDirection));
				if (mineTeam == null || mineTeam == rc.getTeam()) {
					// If there's no mine here or the mine is an allied mine, we can spawn here
					return currentDirection;
				}
			}			
		}
		// Otherwise, let's just spawn in the desired direction, and make sure to clear out a path later
		return canMoveDirection;
	}
	private void find_dir() throws GameActionException
	{
		dir = getSpawnDirection(dir);
		doAction();
	}
	
	public void doAction() //throws GameActionException
	{
		//Direction dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
		if (rc.canMove(dir))
		{
			try
			{
				rc.spawn(dir);
			}
			catch(GameActionException e)
			{
				if(e.getType()==GameActionExceptionType.CANT_MOVE_THERE)
				{
					try
					{
						find_dir();
					}
					catch(Exception e2){}
				}
			}
		}
	}
}
