package Rskisha;

import battlecode.common.*;

public class Connection {
	private Node fromNode;
	private Node endNode;
	//private double cost;
	RobotController rc;
	Direction direct;

	
	public Connection(Node ini, Node end, RobotController myRc) {
		rc = myRc;
		fromNode = ini;
		endNode = end;
		//cost = 0;
	}

	
	//Second attempt
	/*
	public Connection(RobotController myRc, Direction d) {
		rc = myRc;
		direct = d;
		fromNode = makeFromNode(rc);
		endNode = makeEndNode(rc, direct);
	}
	*/
	public Node getFromNode() {
		return this.fromNode;
	}

	public Node getEndNode() {
		return this.endNode;
	}

	public double getCost() {
		//return this.cost;
		//Cost is 1 for any moveable space
		//Cost is 2 for a mine
		//Might not need a cost for impassable spaces
		Team t = rc.senseMine(endNode.Loc);
		if(t != rc.getTeam()) {
			//The mine is either the enemy's or neutral, aka it will damage. Try to avoid)
			return 2;
		}
		//Either there is no mine or the mine is your team's.
		return 1;
	}
	
	public Node makeFromNode(RobotController myRc) {
		Node n = new Node(myRc.getLocation());
		return n;
	}
	public Node makeEndNode(RobotController myRc, Direction d) {
		Node n = new Node(myRc.getLocation().add(d));
		return n;
	}
}
