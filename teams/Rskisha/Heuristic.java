package Rskisha;

import battlecode.common.*;

public class Heuristic {
	//We will use Euclidean Distance as a Heuristic for this project
	Node goalNode;
	public Heuristic(Node goal) {
		goalNode = goal;
	}

	public double estimateCost(Node fromNode) {
		double estimatedTotalCost = fromNode.Loc.distanceSquaredTo(goalNode.Loc);
		return estimatedTotalCost;
	}
}