package Rskisha.Conditions;

import battlecode.common.*;

public class InverterCond implements ICondition
{
	private ICondition cond;
	
	public InverterCond(ICondition c)
	{
		cond = c;
	}
	
	public boolean test()
	{
		return !cond.test();
	}
}
