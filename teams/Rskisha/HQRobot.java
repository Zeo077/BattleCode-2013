package Rskisha;

import java.util.Collection;
import java.util.ArrayList;

import Rskisha.Actions.*;
import Rskisha.Actions.HQ.*;
import Rskisha.Conditions.*;
import Rskisha.HFSM.*;
import Rskisha.Radio.*;
import battlecode.common.*;

public class HQRobot extends BaseRobot 
{
    private HFSM radio_hfsm;
    private int num_sup;
    private int num_gen;
    private MapLocation[] encs;
    private int enc_index;
    private ChannelType[] jobChannels = {ChannelType.ENC1,ChannelType.ENC2,ChannelType.ENC3,ChannelType.ENC4,ChannelType.ENC5};
    private EncampmentJob[] jobQueue = {null, null, null, null, null};
    private boolean comp_flag = false;

    private void init_machine()
    {
        hfsm = new HFSM();

        IHState normal = new HState();
        IAction spawn = new SpawnAction(rc);
        normal.setAction(spawn);

        hfsm.setInitialState(normal);
    }

    private void init_rmachine()
    {
        radio_hfsm = new HFSM();

        IHState normal = new HState();
        IAction safe = new SafeCastAction(); try{safe.doAction();}catch(Exception e){}
        normal.setEntryAction(safe);

        IHState mayday = new HState();
        IAction danger = new DangerCastAction();
        mayday.setEntryAction(danger);

        ICondition enemy = new NearEnemyCountCond(rc,1);
        IHTransition toMayday = new HTransition();
        toMayday.setTargetState(mayday);
        toMayday.setCondition(enemy);
        normal.addTransition(toMayday);

        IHTransition toNorm = new HTransition();
        toNorm.setTargetState(normal);
        toNorm.setCondition(new InverterCond(enemy));
        mayday.addTransition(toNorm);

        radio_hfsm.setInitialState(normal);
    }

    private void init_machines()
    {
        init_machine();
        init_rmachine();
//        int numOfEncamp = 0;
//        if (avgMapSize > 65) numOfEncamp = 5;
//        else numOfEncamp = 2;
//
//        try{
//            // encs = rc.senseEncampmentSquares(rc.getLocation(), 10000, Team.NEUTRAL);
//            encs = rc.senseEncampmentSquares(rc.getLocation(), 250, Team.NEUTRAL);
//            enc_index = 0;
//            for(int i = 0; i < numOfEncamp; i++)
//            {
//                EncampmentJob e = new EncampmentJob(encs[enc_index++], RobotType.SUPPLIER, jobChannels[i]);
//                BroadcastSystem.write(e);
//            }
//        }
//        catch (Exception e){}
//        
//
//        try {
//            BroadcastSystem.write(ChannelType.SEARCH, 0);
//            BroadcastSystem.write(ChannelType.SQUAD1, 0);
//            BroadcastSystem.write(ChannelType.SQUAD2, 0);
//            BroadcastSystem.write(ChannelType.SQUAD3, 0);
//            BroadcastSystem.write(ChannelType.SQUAD4, 0);
//            BroadcastSystem.write(ChannelType.SQUAD5, 0);
//            BroadcastSystem.write(ChannelType.DEFENSESQUAD, 0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        		if (avgMapSize > 65) {
        		    try{
        		       // encs = rc.senseEncampmentSquares(rc.getLocation(), 10000, Team.NEUTRAL);
        		        encs = rc.senseEncampmentSquares(rc.getLocation(), 250, Team.NEUTRAL);
        		        enc_index = 0;
        		        for(int i = 0; i < 5; i++)
        		        {
        		            EncampmentJob e = new EncampmentJob(encs[enc_index++], RobotType.SUPPLIER, jobChannels[i]);
        		            BroadcastSystem.write(e);
        		        }
        		    }
        		    catch (Exception e){}
        		}
        		
        		try {
//        		    BroadcastSystem.write(ChannelType.HQSENSOR, rc.)
        		    BroadcastSystem.write(ChannelType.SEARCH, 0);
        		    BroadcastSystem.write(ChannelType.SQUAD1, 0);
        		    BroadcastSystem.write(ChannelType.SQUAD2, 0);
        		    BroadcastSystem.write(ChannelType.SQUAD3, 0);
        		    BroadcastSystem.write(ChannelType.SQUAD4, 0);
        		    BroadcastSystem.write(ChannelType.SQUAD5, 0);
        		    BroadcastSystem.write(ChannelType.DEFENSESQUAD, 0);
        		} catch (Exception e) {
        		    e.printStackTrace();
        		}
    }

    public HQRobot(RobotController rc) throws GameActionException 
    {
        super(rc);
        init_machines();
    }

    public void updateEncCount(RobotType r)
    {
        switch(EncampmentJob.encNum(r))
        {
        case 0:
            num_sup++;
            break;
        case 1:
            num_gen++;
            break;
        default:
            break;
        }
    }
    public void updateEncCount(int r)
    {
        switch(r)
        {
        case 0:
            num_sup++;
            break;
        case 1:
            num_gen++;
            break;
        default:
            break;
        }
    }

    public void updateJobs()//update jobs too costly for HQ to run properly. Consider making gen_count and sup_count channels, and having the encampments update the jobs list
    {
        Message m;
        if(!comp_flag)
        {
            m = BroadcastSystem.read(ChannelType.ENCCOMP);
            if(m.isValid && m.body == 1)
                comp_flag = true;
        }
        if(comp_flag)
        {
            //	System.out.println("updating jobs");
            for(int i  = 0; i < 5; i++)
            {
                if(jobQueue[i] == null)
                {
                    m = BroadcastSystem.read(jobChannels[i]);
                    if(m.isValid && ((m.body & 0x20) > 0))
                    {
                        jobQueue[i] = new EncampmentJob(m, jobChannels[i]);
                        updateEncCount(jobQueue[i].encInt);
                    }
                }					
            }
            if (enc_index < encs.length) return;
            for(int i = 0; i < jobQueue.length; i++)
            {
                System.out.println("\t1");
                EncampmentJob j = jobQueue[i];
                if(j != null)
                {
                    //updateEncCount(j.encInt);
                    System.out.println("\t\t1");
                    MapLocation new_loc = encs[enc_index];
                    System.out.println("\t\t2");
                    RobotType rob;
                    if(num_gen > 0 && num_sup/num_gen >= 2 || num_gen == 0)
                        rob = RobotType.GENERATOR;
                    else
                        rob = RobotType.SUPPLIER;
                    System.out.println("\t\t3");
                    EncampmentJob newJ = new EncampmentJob(new_loc,rob,j.channel);
                    System.out.println("\t\t4");
                    BroadcastSystem.write(newJ);
                    enc_index++;
                    System.out.println("\t\t5");
                    jobQueue[i] = null;
                }
                System.out.println("\t2");
            }
            BroadcastSystem.write(ChannelType.ENCCOMP, 0);
            comp_flag = false;
            System.out.println("jobs updated");
        }
    }
    public void checkJobs()
    {

    }
    public void verifyJobs()
    {
        int count = 0;
        for(int i = 0; i < 5; i++)
        {
            Message m = BroadcastSystem.read(jobChannels[i]);
            if(!m.isValid)
            {
                count++;
            }
        }
        if(count > 0) System.err.println("HQ read " + count + " bad job channels");
    }

    public void run()
    {	
        updateJobs();
        //verifyJobs();
        IResult r = radio_hfsm.update();
        Collection<IAction> as = r.getActions();
        for(IAction a : as)
        {
            try{a.doAction();}
            catch (Exception e){}
        }

        r = hfsm.update();
        as = r.getActions();
        for(IAction a : as)
        {
            try{a.doAction();}
            catch (Exception e){}
        }
        //updateJobs();
    }
}
