package Rskisha.Conditions;

import battlecode.common.*;
import Rskisha.Radio.*;

public class ReadRetreatCond implements ICondition
{	
    private RobotController rc;
    
    public ReadRetreatCond(RobotController myRC)
    {
        super();
        rc = myRC;
    }
    
	public boolean test()
	{
	    try {
	        if (BroadcastSystem.read(ChannelType.EMERGENCY).body == 1)
	            if (rc.getLocation().distanceSquaredTo(rc.senseHQLocation()) < rc.getLocation().distanceSquaredTo(rc.senseEnemyHQLocation()))
	                return true;
	        return false;
	            
	            
//	        return BroadcastSystem.read(ChannelType.EMERGENCY).body == 1;
	    } catch (Exception e) {
	        return false;
	    }
	}
}
