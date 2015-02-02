package Rskisha.Actions.Soldier;

import battlecode.common.*;
import Rskisha.*;
import Rskisha.Actions.IAction;
import Rskisha.Radio.*;

public class BuildAction implements IAction
{
	public static SoldierRobot r;
	public static int[] directionOffsets = new int[]{0,1,-1,2,-2};;
	
	public BuildAction(SoldierRobot myR)
	{
		super();
		r = myR;
	}
	
	public void doAction()
	{
		try 
		{
			//if (rc.senseEncampmentSquare(rc.getLocation()) && currentLocation.equals(job.goal)) 
			{
				if (r.rc.getTeamPower() > r.rc.senseCaptureCost()) 
				{
					r.rc.captureEncampment(r.job.encType);
				}
			}
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
		}
	}
}
