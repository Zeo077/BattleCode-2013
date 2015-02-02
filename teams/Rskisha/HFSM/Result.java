package Rskisha.HFSM;

import Rskisha.Actions.IAction;

import java.util.Collection;
import java.util.ArrayList;


public class Result implements IResult {
	
	private Collection<IAction> actions;
	private IHTransition transition;
	private int level;
	
	public Result() {
		actions = null;
		transition = null;
		level = 0;
	}
	public Result(Collection<IAction> as, IHTransition t, int l)
	{
		actions = as;
		transition = t;
		level = l;
	}

	private boolean checkActions()
	{
		if(actions == null)
			actions = new ArrayList<IAction>();
		return true;
	}
	
	@Override
	public Collection<IAction> getActions() {
		checkActions();
		return actions;
	}

	@Override
	public IHTransition getTransition() {
		return transition;
	}

	@Override
	public int getLevel() {
		return level;
	}
	@Override
	public void setActions(Collection<IAction> as) {
		actions = as;
	}
	@Override
	public void addAction(IAction a) {
		if(a==null)
			return;
		checkActions();
		actions.add(a);
	}
	@Override
	public void addActions(Collection<IAction> as) {
		checkActions();
		actions.addAll(as);
	}
	@Override
	public void setTransition(IHTransition t) {
		transition = t;
	}
	@Override
	public void setLevel(int l) {
		level = l;
	}
}
