package Rskisha.HFSM;

import Rskisha.Actions.IAction;

import java.util.Collection;
import java.util.ArrayList;

public class HState extends HFSMBase implements IHState {
	private IAction action;
	private IAction entryAction;
	private IAction exitAction;
	private Collection<IHTransition> transitions;
	private Collection<IHState> states;
	private IHFSM parent;
	private String tag;

	public HState() {
		action = null;
		entryAction = null;
		exitAction = null;
		transitions = null;
		states = null;
		parent = null;
	}
	
	public HState(String s)
	{
		tag = s;
		action = null;
		entryAction = null;
		exitAction = null;
		transitions = null;
		states = null;
		parent = null;
	}

	public HState(IAction a, IAction enA, IAction exA, Collection<IHTransition> ts, Collection<IHState> ss, IHFSM p)
	{
		action = a;
		entryAction = enA;
		exitAction = exA;
		transitions = ts;
		states = ss;
		parent = p;
	}	
	
	@Override
	public IAction getAction() {
		return action;
	}

	@Override
	public IAction getEntryAction() {
		return entryAction;
	}

	@Override
	public IAction getExitAction() {
		return exitAction;
	}

	private boolean checkTransitions()
	{
		if(transitions == null)
			transitions = new ArrayList<IHTransition>();
		return true;
	}
	
	@Override
	public Collection<IHTransition> getTransitions() {
		checkTransitions();
		return transitions;
	}

	@Override
	public Collection<IHState> getStates() {
		Collection<IHState> ret = new ArrayList<IHState>(1);
		ret.add(this);
		return ret;
	}

	@Override
	public void setStates(Collection<IHState> ss) {
		states = ss;
	}

	@Override
	public void setAction(IAction a) {
		action = a;
	}

	@Override
	public void setEntryAction(IAction enA) {
		entryAction = enA;
	}

	@Override
	public void setExitAction(IAction exA) {
		exitAction = exA;
	}

	@Override
	public void setTransitions(Collection<IHTransition> ts) {
		transitions = ts;
	}

	@Override
	public void addTransition(IHTransition t) {
		checkTransitions();
		transitions.add(t);
	}

	@Override
	public IHFSM getParent() {
		return parent;
	}

	@Override
	public void setParent(IHFSM p) {
		parent = p;
	}
	
	//public IResult update(Game g){
	public IResult update(){
		IResult result = new Result();
		result.addAction(getAction());
		for(IHTransition t: getTransitions())
		{
			//if(t.isTriggered(g))
			if(t.isTriggered())
			{
				result.setTransition(t);
				result.setLevel(t.getLevel());
				break;
			}
		}
		return result;
	}
}
