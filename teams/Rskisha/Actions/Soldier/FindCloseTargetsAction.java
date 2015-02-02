package Rskisha.Actions.Soldier;

import battlecode.common.*;
import Rskisha.*;
import Rskisha.Radio.*;
import Rskisha.Actions.IAction;

public class FindCloseTargetsAction implements IAction
{
    public static SoldierRobot r;
    
    public FindCloseTargetsAction(SoldierRobot myR)
    {
        super();
        r = myR;
    }
    
    public void doAction()
    {
        try 
        {
            Robot[] units = r.rc.senseNearbyGameObjects(Robot.class, r.rc.getType().attackRadiusMaxSquared, r.rc.getTeam().opponent());
            if (units.length > 0) {
                int c = 0;
                for (c = 0; c < units.length; c++) {
                    RobotInfo rt = r.rc.senseRobotInfo(units[c]);
                    if (rt.type == RobotType.ARTILLERY || rt.type == RobotType.SOLDIER) {
                        MapLocation target = rt.location;
                        BroadcastSystem.write(ChannelType.TARGETX, target.x);
                        BroadcastSystem.write(ChannelType.TARGETY, target.y);
                        break;
                    }
                }
                if (c >= units.length) BroadcastSystem.write(ChannelType.SEARCH, 0);
            } else {
                BroadcastSystem.write(ChannelType.SEARCH, 0);
            }
        } 
        catch (Exception e) 
        {
          //  e.printStackTrace();
        }
    }
}
