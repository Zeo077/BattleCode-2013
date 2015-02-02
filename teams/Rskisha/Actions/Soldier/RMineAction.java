package Rskisha.Actions.Soldier;

import battlecode.common.*;
import Rskisha.*;
import Rskisha.Actions.IAction;

public class RMineAction implements IAction
{
	public static RobotController rc;
	public static int[] directionOffsets = new int[]{0,1,-1,2,-2};;
	
	public RMineAction(RobotController myRC)
	{
		super();
		rc = myRC;
	}
	
	private static boolean hasBadMine(MapLocation location) {
		Team bombTeam = rc.senseMine(location);
		return !(bombTeam == null || bombTeam == rc.getTeam());
	}
	
	public static void goDirectionAndDefuse(Direction dir) throws GameActionException {
		Direction lookingAtCurrently = dir;
		lookAround: for (int d : directionOffsets) {
			lookingAtCurrently = Direction.values()[(dir.ordinal() + d + 8) % 8];
			if (rc.isActive() && rc.canMove(lookingAtCurrently)) {
				if (hasBadMine(rc.getLocation().add(lookingAtCurrently))) {
					rc.defuseMine(rc.getLocation().add(lookingAtCurrently));
				} else {
					rc.move(lookingAtCurrently);
				}
				break lookAround;
			}
		}
	}
	/*
	public static void goToLocation(MapLocation location) throws GameActionException {
		Direction dir = rc.getLocation().directionTo(location);
		if (dir != Direction.OMNI) {
			goDirectionAndDefuse(dir);
		}
	}*/
	
	public void doAction() throws GameActionException
	{
//	    System.out.println("Mine");
	    try {
	        if(rc.senseMine(rc.getLocation())==null) {
	            rc.layMine();
	        }
	        else
	        {
	            Direction dir = Direction.values()[(int)(Math.random()*8)];
//	            System.out.println(rc.canMove(dir));
	            if(rc.canMove(dir))
	                //rc.move(dir);
	                goDirectionAndDefuse(dir);
	        }
	    } catch (Exception e) {
//	        e.printStackTrace();
	    }
		/*try 
		{
			goToLocation(rc.senseEnemyHQLocation());
			/*Direction dir = Direction.values()[(int)(Math.random()*8)];
			if(rc.canMove(dir)) 
			{
				rc.move(dir);
			} 
		} 
		catch (Exception e) 
		{
				e.printStackTrace();
		}*/
	}
}
