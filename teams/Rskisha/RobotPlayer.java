package Rskisha;

import battlecode.common.Clock;
import battlecode.common.RobotController;

import battlecode.common.Direction;
import battlecode.common.GameConstants;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Robot;
import battlecode.common.MapLocation;

import Rskisha.Radio.*;

/** The example funcs player is a player meant to demonstrate basic usage of the most common commands.
 * Robots will move around randomly, occasionally mining and writing useless messages.
 * The HQ will spawn soldiers continuously. 
 */
public class RobotPlayer {
	public static void example_run(RobotController rc) {
		while(true)
		{
			try//assumes encampment
			{
				if(rc.getType() == RobotType.ARTILLERY)
				{
					Robot[] targets = rc.senseNearbyGameObjects(Robot.class, rc.getLocation(),63,rc.getTeam().opponent());
					MapLocation target = rc.senseLocationOf(targets[0]);
					if(target != null && rc.canAttackSquare(target))
						rc.attackSquare(target);
				}
//				else
//				{
//					if(!EncampmentSystem.completeJob(rc.getLocation()))
//						//System.err.println("Encampment could not completeJob");
//				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return;
			}
		}
		/*while (true) {
			try {
				if (rc.getType() == RobotType.HQ) {
					if (rc.isActive()) {
						// Spawn a soldier
						Direction dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
						if (rc.canMove(dir))
							rc.spawn(dir);
					}
				} else if (rc.getType() == RobotType.SOLDIER) {
					if (rc.isActive()) {
						if (Math.random()<0.005) {
							// Lay a mine 
							if(rc.senseMine(rc.getLocation())==null)
								rc.layMine();
						} else { 
							// Choose a random direction, and move that way if possible
							Direction dir = Direction.values()[(int)(Math.random()*8)];
							if(rc.canMove(dir)) {
								rc.move(dir);
								rc.setIndicatorString(0, "Last direction moved: "+dir.toString());
							}
						}
					}
					
					if (Math.random()<0.01 && rc.getTeamPower()>5) {
						// Write the number 5 to a position on the message board corresponding to the robot's ID
						rc.broadcast(rc.getRobot().getID()%GameConstants.BROADCAST_MAX_CHANNELS, 5);
					}
				}

				// End turn
				rc.yield();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
	}
	
	public static void run(RobotController rc) {
		BaseRobot robot = null;
		
		//int rseed = rc.getRobot().getID();
		//Util.randInit(rseed, rseed * Clock.getRoundNum());
		
		try {
			switch(rc.getType()) {
			case HQ:
				robot = new HQRobot(rc);
				break;
			case SOLDIER:
				robot = new SoldierRobot(rc);
				break;
//			case ARTILLERY:
//				robot = new ArtilleryRobot(rc);
//				break;
			case GENERATOR:
				robot = new EncampmentRobot(rc);//GeneratorRobot(rc);
				break;
			case MEDBAY:
				robot = new EncampmentRobot(rc);//MedbayRobot(rc);
				break;
			case SHIELDS:
				robot = new EncampmentRobot(rc);//ShieldsRobot(rc);
				break;
			case SUPPLIER:
				robot = new EncampmentRobot(rc);//SupplierRobot(rc);
				break;
			default:
				example_run(rc);
				//break;
				return;
			}
			robot.loop();
		} catch (Exception e) {
			//System.err.println("Run exception");
			e.printStackTrace();
			System.exit(1);
		}
	}
}
