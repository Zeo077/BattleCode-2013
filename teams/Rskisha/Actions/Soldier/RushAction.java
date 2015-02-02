package Rskisha.Actions.Soldier;

import battlecode.common.*;
import Rskisha.*;
import Rskisha.Radio.*;
import Rskisha.Actions.IAction;

public class RushAction implements IAction
{
	public static RobotController rc;
	public static int[] directionOffsets = new int[]{0,1,-1,2,-2};;
	
	public RushAction(RobotController myRC)
	{
		super();
		rc = myRC;
	}
	
	private static boolean hasBadMine(MapLocation location) {
		Team bombTeam = rc.senseMine(location);
		return !(bombTeam == null || bombTeam == rc.getTeam());
	}
	
	
	public static void goDirectionAndDefuse(Direction dir) throws GameActionException {
		if (rc.isActive()) {
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
	}
	
	public static void goToLocation(MapLocation location) throws GameActionException {
		Direction dir = rc.getLocation().directionTo(location);
		if (dir != Direction.OMNI) {
			goDirectionAndDefuse(dir);
		}
	}
	
	public void doAction()
	{
		try 
		{
		    if (BroadcastSystem.read(ChannelType.SEARCH).body != 0) {
		        MapLocation target = new MapLocation(BroadcastSystem.read(ChannelType.TARGETX).body, BroadcastSystem.read(ChannelType.TARGETY).body);
		        if (rc.canSenseSquare(target)) {
		            if (rc.senseObjectAtLocation(target) != null) {
		                goToLocation(target);
		            } else {
		                BroadcastSystem.write(ChannelType.SEARCH, 0);
		                goToLocation(rc.senseEnemyHQLocation());
		            }
		        } else {
		            goToLocation(rc.senseEnemyHQLocation());
		        }
		    } else {
                goToLocation(rc.senseEnemyHQLocation());
		    }
		} 
		catch (Exception e) 
		{
//		    e.printStackTrace();
		}
	}
}
