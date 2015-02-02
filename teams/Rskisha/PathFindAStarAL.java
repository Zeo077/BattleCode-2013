package Rskisha;

import battlecode.common.*;

import java.util.Collection;
import java.util.ArrayList;

public class PathFindAStarAL {
	
	public PathFindAStarAL() {
		
	}

	public NodeRecord smallestElement(ArrayList<NodeRecord> list) {
		double smallest = Double.MAX_VALUE;
		NodeRecord smallestNodeValue = null;
		for(int i  = 0; i < list.size(); i++) {
			if(list.get(i).estimatedTotalCost <= smallest) {
				smallest = list.get(i).estimatedTotalCost;
				smallestNodeValue = list.get(i);
			}
		}
		return smallestNodeValue;
	}

	//Collection's contain function won't suffice here
	public boolean containsElement(ArrayList<NodeRecord> list, Node n) {
		for(NodeRecord m : list) {
			if(m.node == n ) {
				return true;
			}
		}
		return false;
	}

	public NodeRecord getNodeRecord(ArrayList<NodeRecord> list, Node n) {
		NodeRecord node = null;
		for(NodeRecord m : list) {
			if(m.node == n) {
				node = m;
				return node;
			}
		}
		return node;
	}
	
	public boolean isEqual(Node one, Node two) {
		return (one.getLocation().x == two.getLocation().x && one.getLocation().y == two.getLocation().y);
	
	}

	public ArrayList<Connection> pathFindAStar(Graph g, Node start, Node end, Heuristic h) {
		//Make sure Heuristic h is declared with the goal node before 
		//calling this function

		NodeRecord startRecord = new NodeRecord(start);
		//majority of startRecord declarations in NodeRecord Constructors
		startRecord.estimatedTotalCost = h.estimateCost(start);
		Collection<Connection> connectList;

		ArrayList<NodeRecord> openList = new ArrayList<NodeRecord>();
		openList.add(startRecord);
		ArrayList<NodeRecord> closedList = new ArrayList<NodeRecord>();

		Node tempStartNode = new Node(new MapLocation(0,0));
		NodeRecord current = new NodeRecord(tempStartNode);
		while( openList.size() > 0 ) {
			current = smallestElement(openList);
			
			if(isEqual(current.getNode(), end)) {
				
				break;
			}

			connectList = g.getConnections(current.getNode());

			Node endNode = null;
			NodeRecord endNodeRecord;
			double endNodeHeuristic;
			for(Connection c: connectList) {
				endNode = c.getEndNode();
				double endNodeCost = current.costSoFar + c.getCost();

				//if(closedList.contains(endNode))
				if(containsElement(closedList, endNode)) {
					endNodeRecord = getNodeRecord(closedList, endNode);

					if(endNodeRecord.costSoFar <= endNodeCost) {
						continue;
					}
					closedList.remove(endNodeRecord);

					endNodeHeuristic = endNodeCost - endNodeRecord.costSoFar;

				} else if(containsElement(openList, endNode)) {
					endNodeRecord = getNodeRecord(openList, endNode);

					if(endNodeRecord.costSoFar <= endNodeCost) {
						continue;
					}
					endNodeHeuristic = endNodeCost - endNodeRecord.costSoFar;

				} else  {
					endNodeRecord = new NodeRecord(endNode);
					endNodeHeuristic = h.estimateCost(endNode);
				}

				endNodeRecord.costSoFar = endNodeCost;
				endNodeRecord.connectTo = c;
				endNodeRecord.estimatedTotalCost = endNodeCost + endNodeHeuristic;

				if(!containsElement(openList,endNode)) {
					openList.add(endNodeRecord);
				}
			}
			openList.remove(current);
			//System.out.println("This current has been removed from the Open List");
			closedList.add(current);
			//System.out.println("This current has been added to the Closed List");

		}

		ArrayList<Connection> path = new ArrayList<Connection>();;
		if(!isEqual(current.getNode(), end)) {
			return null;
		} else {

			while(!isEqual(current.getNode(), start)) {
				path.add(current.connectTo);
				current.node = current.connectTo.getFromNode();
				
				for(NodeRecord nR: closedList) {
					Node toCompare = nR.getNode();
					if(isEqual(current.getNode(), toCompare)) {
						current = nR;
						
						break;
					}
				}
			}
		}
		
		ArrayList<Connection> reversedPath = new ArrayList<Connection>();
		for(int i = path.size() - 1; i >= 0; i--) {
			reversedPath.add(path.get(i));
			
		}
		return reversedPath;
	}

}