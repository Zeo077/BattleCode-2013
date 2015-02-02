package Rskisha.Actions.HQ;

import battlecode.common.*;
import Rskisha.*;
import Rskisha.Actions.IAction;
import Rskisha.Radio.*;

public class DangerCastAction implements IAction
{
	public static RobotController rc;
	
	public void doAction() throws GameActionException
	{
		// BroadcastSystem.write(ChannelType.RETREAT_CHANNEL, 1);
		BroadcastSystem.write(ChannelType.EMERGENCY, 1);//rc.broadcast(911, 1);
	}
}
