package Rskisha.HFSM;

import Rskisha.Actions.IAction;

import java.util.Collection;

public interface IHFSM extends IHState {
	
	//public IResult update(Game game);
	public IResult update();
	
	//public Collection<IAction> updateDown(IHState state, int level, Game game);
	public Collection<IAction> updateDown(IHState state, int level);
	
	public void setInitialState(IHState initialState);
	
	public IHState getInitialState();
	
	public IHFSM getParent();
	
	public void setParent(IHFSM parent);

}
