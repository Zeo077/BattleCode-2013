package Rskisha.Conditions;

import battlecode.common.*;
import Rskisha.SoldierRobot;

public class AtWorkCond implements ICondition
{
	private SoldierRobot r;
	
	public AtWorkCond(SoldierRobot robot)
	{
		r = robot;
	}
	
	public boolean test()
	{
		return r.job.goal.equals(r.rc.getLocation());
	}
}
