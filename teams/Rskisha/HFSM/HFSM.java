package Rskisha.HFSM;

import Rskisha.Actions.IAction;

import java.util.Collection;
import java.util.ArrayList;

public class HFSM extends HFSMBase implements IHFSM {
	private IHFSM parent;
	private Collection<IHState> states;
	private IHState initialState;
	private IHState currentState;
	
	private IAction sAction;
	private IAction sEntryAction;
	private IAction sExitAction;
	
	private Collection<IHTransition> sTransitions;
	
	public HFSM()
	{
		parent = null;
		states = null;
		initialState = null;
		currentState = initialState;
		sAction = null;
		sEntryAction = null;
		sExitAction = null;
		sTransitions = null;
	}
	public HFSM(Collection<IHState> ss, IHState iS)
	{
		parent = null;
		states = ss;
		iS = initialState;
		currentState = initialState;
		sAction = null;
		sEntryAction = null;
		sExitAction = null;
		sTransitions = null;
	}
	
	public Collection<IHState> getStates()
	{
		if(currentState != null)
			return currentState.getStates();
		return new ArrayList<IHState>();
	}
	
	@Override
	//public IResult update(Game g)
	public IResult update()
	{
		if(currentState == null)
		{
			currentState = initialState;
			IResult ret = new Result();
			ret.addAction(currentState.getEntryAction());
			return ret;
		}
		
		IHTransition triggeredTransition = null;
		for(IHTransition t : currentState.getTransitions())
		{
			//if (t.isTriggered(g))
			if(t.isTriggered())
			{
				triggeredTransition = t;
				break;
			}
		}
		
		IResult result = null;
		if(triggeredTransition != null)
		{
			result = new Result();
			result.setTransition(triggeredTransition);
			result.setLevel(triggeredTransition.getLevel());
		}
		else
			//result = currentState.update(g);
			result = currentState.update();

		if(result.getTransition() != null)
		{
			int lev = result.getLevel();
			if(lev == 0)
			{
				IHState targetState = result.getTransition().getTargetState();
				result.addAction(currentState.getExitAction());
				result.addAction(result.getTransition().getAction());
				result.addAction(targetState.getEntryAction());
				
				currentState = targetState;
				
				result.addAction(getAction());//
				
				result.setTransition(null);
			}
			else if(lev > 0)
			{
				result.addAction(currentState.getExitAction());
				currentState = null;
				
				result.setLevel(lev-1);
			}
			else
			{
				IHState targetState = result.getTransition().getTargetState();
				IHFSM targetMachine = targetState.getParent();
				result.addAction(result.getTransition().getAction());
				//result.addActions(targetMachine.updateDown(targetState, -lev, g));
				result.addActions(targetMachine.updateDown(targetState, -lev));
				
				result.setTransition(null);
			}
		}
		else
		{
			result.addAction(getAction());
		}
		return result;
	}
	
	//public Collection<IAction> updateDown(IHState state, int level, Game game)
	public Collection<IAction> updateDown(IHState state, int level)
	{
		Collection<IAction> ret = new ArrayList<IAction>();
		if(level > 0)
		{
			ret.addAll(getParent().updateDown(this,level-1));
		}
		
		if(currentState != null)
		{
			ret.add(currentState.getExitAction());
		}
		
		currentState = state;
		ret.add(state.getEntryAction());
		return ret;
	}
	@Override
	public void setStates(Collection<IHState> ss) {
		states = ss;
	}
	@Override
	public IAction getAction()
	{
		return sAction;
	}
	@Override
	public void setAction(IAction a) {
		sAction = a;
	}
	@Override
	public IAction getEntryAction() {
		return sEntryAction;
	}
	@Override
	public void setEntryAction(IAction enA) {
		sEntryAction = enA;
	}
	@Override
	public IAction getExitAction() {
		return sExitAction;
	}
	@Override
	public void setExitAction(IAction exA) {
		sExitAction = exA;
	}
	@Override
	public Collection<IHTransition> getTransitions() {
		if(sTransitions == null)
			sTransitions = new ArrayList<IHTransition>();
		return sTransitions;
	}
	@Override
	public void setTransitions(Collection<IHTransition> ts) {
		sTransitions = ts;
	}
	@Override
	public void addTransition(IHTransition t) {
		getTransitions().add(t);
	}
	@Override
	public void setInitialState(IHState iS) {
		initialState = iS;
		currentState = initialState;
	}
	@Override
	public IHState getInitialState() {
		return initialState;
	}
	@Override
	public IHFSM getParent() {
		return parent;
	}
	@Override
	public void setParent(IHFSM p) {
		parent = p;
	}
}
