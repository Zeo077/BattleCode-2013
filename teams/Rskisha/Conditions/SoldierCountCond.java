package Rskisha.Conditions;

import battlecode.common.*;
import Rskisha.Radio.*;

public class SoldierCountCond implements ICondition
{
	private int threshold;
	private RobotController rc;
	
	public SoldierCountCond(RobotController myRC, int count)
	{
		super();
		rc = myRC;
		threshold = count;
	}
	public SoldierCountCond(RobotController myRC)
	{
		super();
		rc = myRC;
		threshold = 50;
	}
	
	public boolean test()
	{
		try
		{
			int numAlliedRobots = rc.senseNearbyGameObjects(Robot.class, 10000, rc.getTeam()).length;
			int numAlliedEncampments = rc.senseEncampmentSquares(rc.getLocation(), 10000, rc.getTeam()).length;
			int numAlliedSoldiers = numAlliedRobots - numAlliedEncampments - 1;
		    return threshold <= numAlliedSoldiers;
//			return threshold <= BroadcastSystem.read(ChannelType.ATTACKERS).body;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
