package Rskisha;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameObject;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;
import Rskisha.Actions.*;
import Rskisha.Actions.Soldier.*;
import Rskisha.Conditions.*;
import Rskisha.HFSM.*;
import Rskisha.Radio.*;

public class EncampmentRobot extends BaseRobot 
{	
	public EncampmentRobot(RobotController rc) throws GameActionException 
	{
		super(rc);
		if(!EncampmentSystem.completeJob(rc.getLocation()))
			System.out.println("Encampment could not send complete message");
	}
	
	//@override
	public void run() {}
}
