package Rskisha.Conditions;

import battlecode.common.*;
import Rskisha.Radio.*;

public class AttackedCond implements ICondition
{
    private int range;
    private double energon;
    private RobotController rc;
    
    public AttackedCond(RobotController myRC, int ran)
    {
        super();
        rc = myRC;
        range = ran;
        energon = rc.getEnergon();
    }
    public AttackedCond(RobotController myRC)
    {
        super();
        rc = myRC;
        range = rc.getType().attackRadiusMaxSquared;
        energon = rc.getEnergon();
    }
    
    public boolean test()
    {
        try {
            if (rc.getEnergon() < energon && BroadcastSystem.read(ChannelType.SEARCH).body == 0) {
                energon = rc.getEnergon();
                BroadcastSystem.write(ChannelType.SEARCH, 1);
                return true;
            }
            return false;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return false;
    }
}
