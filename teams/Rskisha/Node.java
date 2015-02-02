package Rskisha;

import battlecode.common.*;

public class Node {
	MapLocation Loc;

	public Node(MapLocation data) {
		Loc = data;
	}
	
	public MapLocation getLocation() {
		return this.Loc;
	}
}