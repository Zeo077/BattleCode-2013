package Rskisha.Actions.Soldier;

import battlecode.common.*;
import Rskisha.*;
import Rskisha.Actions.IAction;
import Rskisha.Radio.*;

public class FindJobAction implements IAction
{
	public static SoldierRobot r;
	public static int[] directionOffsets = new int[]{0,1,-1,2,-2};;
	
	public FindJobAction(SoldierRobot myR)
	{
		super();
		r = myR;
	}
	
	public void doAction()
	{
		System.out.println("FindJobAction");
		try 
		{
			if(r.findJob()) System.out.println("Job found");
			r.hfsm.update();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
