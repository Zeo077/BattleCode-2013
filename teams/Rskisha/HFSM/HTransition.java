package Rskisha.HFSM;

import Rskisha.Actions.IAction;
import Rskisha.Conditions.ICondition;

public class HTransition implements IHTransition {

	private ICondition cond;
	private IHState targetState;
	private IAction action;
	private int level;
	
	public HTransition() {
		cond = null;
		targetState = null;
		action = null;
		level = 0;
	}
	public HTransition(IHState s, ICondition c)
	{
		targetState = s;
		cond = c;
		action = null;
		level = 0;
	}
	public HTransition(ICondition c, IHState s, IAction a, int l)
	{
		cond = c;
		targetState = s;
		action = a;
		level = l;
	}
	public HTransition(IHState s, IAction a)
	{
		targetState = s;
		action = a;
	}

	@Override
	public IHState getTargetState() {
		return targetState;
	}

	@Override
	public IAction getAction() {
		return action;
	}

	@Override
	public void setCondition(ICondition condition) 
	{
		cond = condition;
	}

	@Override
	//public boolean isTriggered(Game g) {
	public boolean isTriggered(){
		/*if(cond.test(g))
		{
			System.out.print(this);
			System.out.print(" triggered to ");
			//System.out.println(targetState);
			HState tS = (HState)targetState;
			System.out.println(tS.tag);
		}*/
		//return cond.test(g);
		return cond.test();
	}
	
	@Override
	public int getLevel() {
		return level;
	}
	@Override
	public void setTargetState(IHState tS) {
		targetState = tS;
	}
	@Override
	public void setAction(IAction a) {
		action = a;
	}
	@Override
	public void setLevel(int l) {
		level = l;
	}
}
