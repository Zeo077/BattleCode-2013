package Rskisha.Actions.Soldier;

import battlecode.common.*;
import Rskisha.*;
import Rskisha.Actions.IAction;

public class MoveToJobAction implements IAction
{
	public static SoldierRobot r;
	public static int[] directionOffsets = new int[]{0,1,-1,2,-2};;
	
	public MoveToJobAction(SoldierRobot myR)
	{
		super();
		r = myR;
	}
	
	private static boolean hasBadMine(MapLocation location) {
		Team bombTeam = r.rc.senseMine(location);
		return !(bombTeam == null || bombTeam == r.rc.getTeam());
	}
	
	
	public static void goDirectionAndDefuse(Direction dir) throws GameActionException {
		if (r.rc.isActive()) {
			Direction lookingAtCurrently = dir;
			lookAround: for (int d : directionOffsets) {
				lookingAtCurrently = Direction.values()[(dir.ordinal() + d + 8) % 8];
				if (r.rc.isActive() && r.rc.canMove(lookingAtCurrently)) {
					if (hasBadMine(r.rc.getLocation().add(lookingAtCurrently))) {
						r.rc.defuseMine(r.rc.getLocation().add(lookingAtCurrently));
					} else {
						r.rc.move(lookingAtCurrently);
					}
					break lookAround;
				}
			}
		}
	}
	
	public static void goToLocation(MapLocation location) throws GameActionException {
		Direction dir = r.rc.getLocation().directionTo(location);
		if (dir != Direction.OMNI) {
			goDirectionAndDefuse(dir);
		}
	}
	
	public void doAction()
	{
		try 
		{
			goToLocation(r.job.goal);
			/*Direction dir = Direction.values()[(int)(Math.random()*8)];
			if(rc.canMove(dir)) 
			{
				rc.move(dir);
			} */
		} 
		catch (Exception e) 
		{
				e.printStackTrace();
		}
	}
}
