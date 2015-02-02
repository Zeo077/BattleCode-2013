package Rskisha.Radio;

import battlecode.common.*;
import Rskisha.Radio.*;

public class EncampmentSystem 
{	
	public static ChannelType[] jobChannels = {ChannelType.ENC1,ChannelType.ENC2,ChannelType.ENC3,ChannelType.ENC4,ChannelType.ENC5};
	//public static ChannelType[] jobCompletes = {ChannelType.COMP1,ChannelType.COMP2,ChannelType.COMP3,ChannelType.COMP4,ChannelType.COMP5};
	
	public static void clearJobs()
	{
		/*for(int i = 0; i < jobChannels.length; i++)
		{
			Message message = BroadcastSystem.read(jobChannels[i]);
			if(message.isValid && message.body == EncampmentMessage.COMPLETION.ordinal())
				BroadcastSystem.write(jobChannels[i],EncampmentMessage.EMPTY.ordinal());
		}
		BroadcastSystem.write(ChannelType.ENCCOMP,0);*/
	}
	
	public static void postJob(MapLocation loc, RobotType rob)
	{
		for(ChannelType chan : jobChannels)
		{
			Message message = BroadcastSystem.read(chan);
			if(!message.isValid || message.body == EncampmentMessage.COMPLETION.ordinal())
			{
				EncampmentJob e= new EncampmentJob(loc, rob, chan);
				BroadcastSystem.write(e);
			}
		}
	}
	
	public static void completeJob(EncampmentJob e)
	{
		Message m = BroadcastSystem.read(e.channel);
		if(e.roundCreated == (m.body & 0xF))//TODO replace with better equality check later
		{
			//BroadcastSystem.write(e.channel, EncampmentMessage.COMPLETION.ordinal());
			//BroadcastSystem.clear(e.channel);
			e.complete = true;
			BroadcastSystem.write(e.channel, e.buildMessage());
			BroadcastSystem.write(ChannelType.ENCCOMP, 1);
		}
		else
			System.err.println("EncampmentJob " + e + " does not exist on channel " + e.channel);
	}
	public static void completeJob(ChannelType channel)
	{
		//BroadcastSystem.write(channel, EncampmentMessage.COMPLETION.ordinal());
		//BroadcastSystem.clear(channel);
		Message m = BroadcastSystem.read(channel);
		m.body = m.body | 0x20;
		BroadcastSystem.write(channel, m.body);
		BroadcastSystem.write(ChannelType.ENCCOMP, 1);
	}
	public static boolean completeJob(MapLocation loc)
	{
		Message m;
		//System.out.println("loc: " + loc.x + ", " + loc.y);
		ChannelType[] jChs = {ChannelType.ENC1,ChannelType.ENC2,ChannelType.ENC3,ChannelType.ENC4,ChannelType.ENC5};
		for(ChannelType channel : jChs)//jobChannels)
		{
			m = BroadcastSystem.read(channel);
			if(m.isValid)
			{
				EncampmentJob e = new EncampmentJob(m);
				//System.out.println("goal: " + e.goal.x + ", " + e.goal.y);
				if(e.goal.equals(loc))
				{
					completeJob(channel);
					return true;
				}
			}
			else
			{
				System.out.println("bad message");
			}
		}
		return false;
	}
	
//	public static int createMessage(MapLocation goalLoc, boolean isTaken, int robType)//boolean onOrOff, int robType) 
//	{
//		boolean onOrOff = true;
//		int msg = robType << 22;
//		
//		msg += (goalLoc.x << 14);
//		msg += (goalLoc.y << 6);
//		if (isTaken) {
//			msg += 0x20;
//		}
//		if (onOrOff) {
//			msg += 0x10;
//		}
//		msg += Clock.getRoundNum() & 0xF;
//		
//		return msg;
//	}
}
