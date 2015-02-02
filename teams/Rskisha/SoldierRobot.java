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
import Rskisha.Constants;
import Rskisha.Actions.*;
import Rskisha.Actions.Soldier.*;
import Rskisha.Conditions.*;
import Rskisha.HFSM.*;
import Rskisha.Radio.*;

public class SoldierRobot extends BaseRobot 
{	
    public EncampmentJob job;
    public int squad;
    public ChannelType squadChannel;
    public boolean atkBot;

    public boolean findJob()
    {
        atkBot = false;
        int numOfSquads = 0;
        ChannelType[] jobChannels = {ChannelType.ENC1,ChannelType.ENC2,ChannelType.ENC3,ChannelType.ENC4,ChannelType.ENC5};
        for(ChannelType chan : jobChannels)
        {
            Message m = BroadcastSystem.read(chan);
            if(m.isValid)
            {
                int c = m.body & 0x20;
                int t = m.body & 0x10;
                if(t == 0 && c == 0)//not taken and not completed
                {
                    if(t <= 0)//EncampmentJob.isJob(m))
                    {
                        job = new EncampmentJob(m);
                        job.taken = true;
                        //BroadcastSystem.write(job);
                        m.body = m.body | 0x10;
                        BroadcastSystem.write(chan, m.body);
                        System.out.println("Soldier took a job.");
                        return true;
                    }
                }
            }
        }


        // Soldiers whose focus is to clear a path to the enemy hq
        int demineSquadSize = BroadcastSystem.read(ChannelType.DEMINESQUAD).body;

        if (demineSquadSize < Constants.SQUAD_SIZE) {
            squad = Constants.DEMINE_SQUAD;
            BroadcastSystem.write(ChannelType.DEMINESQUAD, ++demineSquadSize);
            atkBot = true;
            return true;
        }

        // First few soldiers are placed into squads to have coordinated attacks
        ChannelType[] atkSquadChannels = {ChannelType.SQUAD1,ChannelType.SQUAD2,ChannelType.SQUAD3,ChannelType.SQUAD4,ChannelType.SQUAD5};

        if (avgMapSize > 65) numOfSquads = 3;
        else numOfSquads = 5;

        for (int x = 0; x < numOfSquads; x++) {

            int sizeOfSquad = BroadcastSystem.read(atkSquadChannels[x]).body;

            if (sizeOfSquad < Constants.SQUAD_SIZE) {
                squad = x + 1;
                BroadcastSystem.write(atkSquadChannels[x], ++sizeOfSquad);
                //                BroadcastSystem.write(ChannelType.ATTACKERS, ++(BroadcastSystem.read(ChannelType.ATTACKERS).body));
                //                System.out.println("Risen: " + BroadcastSystem.read(ChannelType.ATTACKERS).body);
                atkBot = true;
                return true;
            }
        }


        // Soldiers whose focus is to defend in case a bot tries to flank us while we're attacking
        int defenseSquadSize = BroadcastSystem.read(ChannelType.DEFENSESQUAD).body;

        if (defenseSquadSize < Constants.SQUAD_SIZE) {
            squad = Constants.DEFENSE_SQUAD;
            BroadcastSystem.write(ChannelType.DEFENSESQUAD, ++defenseSquadSize);
            return true;
        }
        //  System.out.println(BroadcastSystem.read(ChannelType.ATTACKERS).body);
        //        BroadcastSystem.write(ChannelType.ATTACKERS, ++(BroadcastSystem.read(ChannelType.ATTACKERS).body));
        //        System.out.println("Risen: " + BroadcastSystem.read(ChannelType.ATTACKERS).body);
        atkBot = true;
        return false;
    }

    private void init_machine()
    {
        hfsm = new HFSM();

        IHState initState = new HState();
        findJob();
        // if (atkBot) BroadcastSystem.write(ChannelType.ATTACKERS, ++(BroadcastSystem.read(ChannelType.ATTACKERS).body));
        //initState.setAction(new FindJobAction(this));

        IHFSM workMachine = new HFSM();
        IHState onRoute = new HState();
        IAction commute = new MoveToJobAction(this);
        onRoute.setAction(commute);

        IHState atWork = new HState();
        IAction work = new BuildAction(this);
        atWork.setAction(work);

        IHFSM normalMachine = new HFSM();
        IHFSM rushMachine = new HFSM();

        IHTransition assigned = new HTransition();
        assigned.setAction(commute);
        assigned.setCondition(new HasJobCond(this));
        assigned.setTargetState(workMachine);
        initState.addTransition(assigned);

        IHTransition unemployed = new HTransition();
        unemployed.setCondition(new InverterCond(new HasJobCond(this)));
        unemployed.setTargetState(normalMachine);
        initState.addTransition(unemployed);

        IHTransition arrived = new HTransition();
        arrived.setCondition(new AtWorkCond(this));
        arrived.setTargetState(atWork);
        onRoute.addTransition(arrived);

        workMachine.setInitialState(onRoute);

        IHState retreatState = new HState();
        IAction defendA = new DefendAction(rc);
        IHTransition retreat = new HTransition();
//        retreat.setAction(new DefendAction(rc));
        retreat.setAction(defendA);
        retreat.setCondition(new ReadRetreatCond(rc));
        retreat.setTargetState(retreatState);
        normalMachine.addTransition(retreat);

        IHTransition normalize = new HTransition();
        normalize.setAction(new RMineAction(rc));
        normalize.setCondition(new InverterCond(new ReadRetreatCond(rc)));
        normalize.setTargetState(normalMachine);
        retreatState.addTransition(normalize);

        int s_count;
        if (avgMapSize > 65) s_count = 15;
        else s_count = 10;

        IHState mineS = new HState();
        IAction mineA = new RMineAction(rc);
        mineS.setAction(mineA);
        mineS.setEntryAction(mineA);

        IHState rushS = new HState();
        IAction rushA = new RushAction(rc);
        rushS.setAction(rushA);
        rushS.setEntryAction(rushA);

        ICondition sCount = new SoldierCountCond(rc, s_count);
        IHTransition charge = new HTransition();
        charge.setTargetState(rushMachine);
        charge.setCondition(sCount);
        mineS.addTransition(charge);






        /********Focus fire stuff**************************************/
        IHState findS = new HState();
        IAction findA = new FindCloseTargetsAction(this);
        findS.setAction(findA);
        findS.setEntryAction(findA);

        ICondition attacked = new AttackedCond(rc);
        IHTransition findTarget = new HTransition();
        findTarget.setTargetState(findS);
        findTarget.setCondition(attacked);
        rushS.addTransition(findTarget);
//        mineS.addTransition(findTarget);

        ICondition foundTarget = new TargetAquiredCond(rc);
        IHTransition attackTarget = new HTransition();
        attackTarget.setTargetState(rushS);
        attackTarget.setCondition(foundTarget);
        findS.addTransition(attackTarget);
        /*************************************************************/                               

//        normalize.setTargetState(rushS);

        /********New Stuff********************************************/
        //        IHState dyingS = new HState();
        //        IAction dyingA = new LastBreathAction(this);
        //        dyingS.setAction(dyingA);
        //        dyingS.setEntryAction(dyingA);
        //        
        //        ICondition nearDeath = new AlmostDeadCond(this, 30);
        //        IHTransition getdown = new HTransition();
        //        getdown.setTargetState(dyingS);
        //        getdown.setCondition(nearDeath);
        //        rushS.addTransition(getdown);
        //        findS.addTransition(getdown);
        //        mineS.addTransition(getdown);
        //        initState.addTransition(getdown);
        //        onRoute.addTransition(getdown);
        //        atWork.addTransition(getdown);
        /*************************************************************/   


        ICondition sLess = new SoldierCountLessCond(rc, s_count - 5);
        IHTransition goBack = new HTransition();
        goBack.setTargetState(retreatState);
        goBack.setCondition(sLess);
        rushMachine.addTransition(goBack);
//        IHTransition mine = new HTransition();
//        mine.setTargetState(mineS);
//        mine.setCondition(sLess);
//        rushMachine.addTransition(mine);
        
        rushMachine.setInitialState(rushS);

        normalMachine.setInitialState(mineS);
//        normalMachine.setInitialState(retreatState);
        hfsm.setInitialState(initState);
        //hfsm.setInitialState(normalMachine);
    }

    public SoldierRobot(RobotController rc) throws GameActionException 
    {
        super(rc);
        init_machine();
    }
}
