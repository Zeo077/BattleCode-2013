package Rskisha.HFSM;

import Rskisha.Actions.IAction;

import java.util.Collection;
import java.util.ArrayList;

public class HFSMBase implements IHFSMBase {

	public HFSMBase() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IAction getAction() {
		return null;
	}

	@Override
	//public IResult update(Game g){
	public IResult update(){
		IAction rAction = getAction();
		Collection<IAction> rActions = new ArrayList<IAction>(1);
		rActions.add(rAction);
		IHTransition rTrans = null;
		int rLevel = 0;
		IResult result = new Result(rActions, rTrans, rLevel);
		return result;
	}
}
