package Rskisha.Conditions;

import battlecode.common.*;
import Rskisha.Radio.*;

public class TargetAquiredCond implements ICondition
{
    private RobotController rc;
    
    public TargetAquiredCond (RobotController myRC)
    {
        super();
        rc = myRC;
    }
    
    public boolean test()
    {
        try {
            if (BroadcastSystem.read(ChannelType.SEARCH).body != 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return false;
    }
}
