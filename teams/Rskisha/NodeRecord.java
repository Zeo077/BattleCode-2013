package Rskisha;

import battlecode.common.*;

public class NodeRecord {
	MapLocation Location;
	Node node;
	Connection connectTo;
	//double cost; //BEWARE this is a temporary addition. The book might have a typo
	double costSoFar;
	double estimatedTotalCost;

	public NodeRecord(Node iniNode) {
		node = iniNode;
		Location = iniNode.Loc; //IDK about this yet...
		connectTo = null; //New Node has no Connection; same with StartNode
		//cost = 0;
		costSoFar = 0;
		estimatedTotalCost = 0; 
	}
	
	public MapLocation getLocation() {
		return this.Location;
	}
	
	public Node getNode() {
		return this.node;
	}
	
	public Connection getConnection() {
		return this.connectTo;
	}
	
	public double getCostSoFar() {
		return this.costSoFar;
	}
	public double getEstimatedTotalCost() {
		return this.estimatedTotalCost;
	}
}