package Rskisha.Conditions;

import battlecode.common.*;
import Rskisha.Radio.*;

public class SoldierCountLessCond implements ICondition
{
	private int threshold;
	private RobotController rc;
	
	public SoldierCountLessCond(RobotController myRC, int count)
	{
		super();
		rc = myRC;
		threshold = count;
	}
	public SoldierCountLessCond(RobotController myRC)
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
		    return numAlliedSoldiers < threshold;
//			return BroadcastSystem.read(ChannelType.ATTACKERS).body < threshold;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
