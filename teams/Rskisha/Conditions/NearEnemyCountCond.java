package Rskisha.Conditions;

import battlecode.common.*;

public class NearEnemyCountCond implements ICondition
{
	
	private int rsquared;
	private int threshold;
	private Team enemy;
	private RobotController rc;
	
	public NearEnemyCountCond(RobotController myRC, int count, int r)
	{
		super();
		rc = myRC;
		threshold = count;
		rsquared = r;
		enemy = rc.getTeam().opponent();
	}
	public NearEnemyCountCond(RobotController myRC, int count)
	{
		super();
		rc = myRC;
		threshold = count;
		rsquared = rc.getType().sensorRadiusSquared;
		enemy = rc.getTeam().opponent();
	}
	
	public boolean test()
	{
		try
		{
			int num = rc.senseNearbyGameObjects(Robot.class, rsquared, enemy).length;
			return threshold <= num;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
