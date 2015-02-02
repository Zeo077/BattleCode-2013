package Rskisha;

import battlecode.common.*;

import java.util.ArrayList;
import java.util.Collection;

public class Graph {
	Node initialNode;
	Collection<Connection> ConnectArray;
	RobotController rc;
	public int[] directionOffsets = new int[]{0,1,-1,2,-2};
	
	public Graph(RobotController myRc) {
		rc = myRc;
	}

	public Collection<Connection> getConnections(Node fromNode) {
		ConnectArray = new ArrayList<Connection>();
	
		for(Direction d : Direction.values()) {
			if(d.equals(Direction.OMNI) || d.equals(Direction.NONE)) continue;
			Node endNode = new Node(fromNode.Loc.add(d));
			Connection connect = new Connection(fromNode, endNode, rc);
			ConnectArray.add(connect);
		}
		
		return ConnectArray;
		//Work on this later...
		//Make use of RobotController's senseMineLocation with radius of 1; this method returns a Team enum
		//Cost should be an arbitrary value like 2
	}
}