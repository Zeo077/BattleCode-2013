package Rskisha.Radio;

import battlecode.common.*;
import Rskisha.Radio.*;

public class EncampmentJob
{	
	public MapLocation goal;
	public RobotType encType;
	public int encInt;
	public ChannelType channel;
	
	public int roundCreated;
	public boolean taken;
	public boolean complete;
	
	public EncampmentJob(MapLocation loc, RobotType rob, ChannelType ch)
	{
		goal = loc;
		set_enc(rob);
		channel = ch;
		roundCreated = Clock.getRoundNum() & 0xF;
		taken = false;
	}
	public EncampmentJob(Message m)
	{
		readMessage(m);
		channel = null;
	}
	public EncampmentJob(Message m, ChannelType ch)
	{
		readMessage(m);
		channel = ch;
	}
	
	private void set_enc(RobotType r)
	{
		encType = r;
		encInt = encNum(r);
	}
	
	public static int encNum(RobotType r)
	{
		if(r == RobotType.GENERATOR) return 1;
		else if(r == RobotType.ARTILLERY) return 2;
		else if(r == RobotType.SHIELDS) return 3;
		else if(r == RobotType.SUPPLIER) return 0;
		else return 0;
	}
	public static RobotType robType(int e)
	{
		switch(e)
		{
			case 1:
				return RobotType.GENERATOR;
			case 2:
				return RobotType.ARTILLERY;
			case 3:
				return RobotType.SHIELDS;
			case 0:
			default:
				return RobotType.SUPPLIER;
		}
	}
	
	public int buildMessage()
	{
		int msg = encInt << 22;
		
		msg += (goal.x << 14);
		msg += (goal.y << 6);
		if (taken)
			msg += 0x10;
		if (complete)
			msg += 0x20;
		msg += Clock.getRoundNum() & 0xF;
		
		verifyBody(msg, encInt,goal.x,goal.y,taken,complete,Clock.getRoundNum()&0xF);
		return msg;
	}

	public void verifyBody(int b, int eI, int x, int y, boolean t, boolean c, int cl)
	{
		int clk = b & 0xF;
		if(clk != cl)
			System.err.println("body inconsistency\n"+b+"\ncl: "+cl+"\nclk: "+clk);
		boolean tak = false;
		if(((b>>4)&0x1)>0)
			tak = true;
		if(tak != t)
			System.err.println("body inconsistency\n"+b+"\nt: "+t+"\ntak: "+tak);
		boolean com = false;
		if(((b>>5)&0x1)>0)
			com = true;
		if(com != c)
			System.err.println("body inconsistency\n"+b+"\nc: "+c+"\ncom: "+com);
		int a = (b >> 14) & 0xFF;
		int bb = (b >> 6) & 0xFF;
		if((a != x) && (bb != y))
			System.err.println("body inconsistency\n"+b+"\n(x,y): ("+x+","+y+")\n(a,b): ("+a+","+bb+")");
		int enN = (b>>22);
		if(enN != eI)
			System.err.println("body inconsistency\n"+b+"\neI: "+eI+"\nenN: "+enN);
	}
	public void readMessage(Message m)
	{
		int b = m.body;
		roundCreated = b & 0xF;
		
		if(((b >> 4) & 0x1)>0)
			taken = true;
		else
			taken = false;
		if(((b >> 5) & 0x1)>0)
			complete = true;
		else
			complete = false;
		
		int y = (b >> 6) & 0xFF;
		int x = (b >> 14) & 0xFF;
		goal = new MapLocation(x,y);
		
		encInt = (b >> 22);
		encType = robType(encInt);
	}

	public static Boolean isJob(Message m)
	{
		if(!m.isValid)
			return false;
		return true;
		/*int a = EncampmentMessage.COMPLETION.ordinal();
		int b = EncampmentMessage.EMPTY.ordinal();
		int c = EncampmentMessage.FAILURE.ordinal();
		int d = EncampmentMessage.SUPPLY.ordinal();
		int e = EncampmentMessage.POWER.ordinal();
		int f = EncampmentMessage.ART.ordinal();
		int g = EncampmentMessage.SHIELD.ordinal();
		switch(m.body)
		{
			case a:
			case b:
			case c:
			case d:
			case e:
			case f:
			case g:
				return false;
			default:
				return true;
		}
		return false;*/
	}
}
