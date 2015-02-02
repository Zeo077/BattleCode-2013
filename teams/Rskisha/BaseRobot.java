package Rskisha;

import Rskisha.HFSM.*;
import Rskisha.Actions.IAction;
import Rskisha.Radio.*;

import battlecode.common.RobotController;
//import java.util.ArrayList;
import java.util.Collection;

public abstract class BaseRobot {
	
	public RobotController rc;
	public int id;
	public HFSM hfsm;
	public double avgMapSize;
	
	//public boolean enemyNukeHalfDone = false;
	
	private void init_machine()
	{
		hfsm = new HFSM();
	}
	
	// Default constructor
	public BaseRobot(RobotController myRC) {
		rc = myRC;
		id = rc.getRobot().getID();
		avgMapSize = (rc.getMapWidth() + rc.getMapHeight())/2;
		init_machine();
		
		BroadcastSystem.init(this);
		
		/*DataCache.init(this); // this must come first
		BroadcastSystem.init(this);
		EncampmentJobSystem.init(this);*/
	}
	
	// Actions for a specific robot
	//abstract public void run();
	public void run()
	{
		IResult r = hfsm.update();
		Collection<IAction> as = r.getActions();
		for(IAction a : as)
		{
			try{a.doAction();}
			catch (Exception e){}
		}
	}
	
	public void loop() {
		while (true) {
			try {
				run();
			} catch (Exception e) {
				// Deal with exception
//				e.printStackTrace();
			}
			rc.yield();
		}
	}
}