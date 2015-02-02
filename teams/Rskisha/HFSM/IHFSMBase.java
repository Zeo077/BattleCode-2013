package Rskisha.HFSM;

import Rskisha.Actions.IAction;

public interface IHFSMBase {

	public IAction getAction();
	
	//public IResult update(Game game);
	public IResult update();
	
}
