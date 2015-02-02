package Rskisha.Actions.Soldier;

import battlecode.common.*;
import Rskisha.*;
import Rskisha.Actions.IAction;

import java.lang.Math;

public class DefendAction implements IAction
{
	public static RobotController rc;
	public static int[] directionOffsets = new int[]{0,1,-1,2,-2};
	
	public DefendAction(RobotController myRC)
	{
		super();
		rc = myRC;
	}
	
	private static boolean hasBadMine(MapLocation location) {
		Team bombTeam = rc.senseMine(location);
//		System.out.println("BAD MINE");
		return !(bombTeam == null || bombTeam == rc.getTeam());
	}
	
	public static void goDirectionAndDefuse(Direction dir) throws GameActionException {
		if (rc.isActive()) {
			Direction lookingAtCurrently = dir;
			lookAround: for (int d : directionOffsets) {
				lookingAtCurrently = Direction.values()[(dir.ordinal() + d + 8) % 8];
				if (rc.isActive() && rc.canMove(lookingAtCurrently)) {
					if (hasBadMine(rc.getLocation().add(lookingAtCurrently))) {
//					    System.out.println("Demine1");
						rc.defuseMine(rc.getLocation().add(lookingAtCurrently));
					} else {
//					    System.out.println("Moveout: ");
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
	
	public void doAction() throws GameActionException
	{
       //Direction dir = Direction.values()[(int)(Math.random()*8)];
	    try {
	        Robot[] units = rc.senseNearbyGameObjects(Robot.class, rc.senseHQLocation(),RobotType.HQ.sensorRadiusSquared, rc.getTeam().opponent());
	        
	        if (units.length != 0) {
	            MapLocation target = rc.senseRobotInfo(units[0]).location;
	            if (rc.canSenseSquare(target)) {
	                goToLocation(target);
//	                System.out.println("Retreat1");
//	                goToLocation(rc.senseHQLocation());
	            } else {
//	                System.out.println("Retreat2");
	                goToLocation(rc.senseHQLocation());
	            }
	        } else {
//	            System.out.println("Retreat3");
	            goToLocation(rc.senseHQLocation());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
//		int r = (int)(Math.random()*2);
//		if(rc.senseMine(rc.getLocation())==null && r > 0)
//			rc.layMine();
//		else
//		{
//			//Direction dir = Direction.values()[(int)(Math.random()*8)];
//			Direction dir = rc.getLocation().directionTo(rc.senseHQLocation());
//			if(rc.canMove(dir))
//				//rc.move(dir);
//				goDirectionAndDefuse(dir);
//			else
//			{
//				dir = Direction.values()[(int)(Math.random()*8)];
//				if(rc.canMove(dir))
//					goDirectionAndDefuse(dir);
//			}
//		}
		
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
