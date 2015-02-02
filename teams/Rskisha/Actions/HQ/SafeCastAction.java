package Rskisha.Actions.HQ;

import battlecode.common.*;
import Rskisha.*;
import Rskisha.Actions.IAction;
import Rskisha.Radio.*;

public class SafeCastAction implements IAction
{
	public static RobotController rc;
	
	public void doAction() throws GameActionException
	{
		//BroadcastSystem.write(ChannelType.RETREAT_CHANNEL, 0);
	    BroadcastSystem.write(ChannelType.EMERGENCY, 0); //rc.broadcast(911, 0);
	}
}
