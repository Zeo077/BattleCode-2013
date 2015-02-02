package Rskisha.Conditions;

import battlecode.common.*;
import Rskisha.SoldierRobot;

public class HasJobCond implements ICondition
{
	private SoldierRobot r;
	
	public HasJobCond(SoldierRobot robot)
	{
		r = robot;
	}
	
	public boolean test()
	{
		return (r.job != null);
	}
}
