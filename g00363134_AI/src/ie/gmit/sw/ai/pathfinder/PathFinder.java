package ie.gmit.sw.ai.pathfinder;

import java.util.ArrayList;

import ie.gmit.sw.ai.GameModel;
import ie.gmit.sw.ai.node.NodeTile;

public class PathFinder{
	
	private ArrayList<NodeTile> path;
	//private Graph graph = Graph.getInstance();
	GameModel graph = GameModel.getInstance();
	
	public ArrayList<NodeTile> requestPath(NodeTile startNode, NodeTile endNode) {
		path = findPath(startNode, endNode);
		if(!path.isEmpty())
			return path;
		return null;
	}

	public ArrayList<NodeTile> requestEscape(NodeTile startNode, NodeTile endNode) {
		path = findFurthest(startNode, endNode);
		if(!path.isEmpty())
			return path;
		return null;
	}
	
	private ArrayList<NodeTile> findFurthest(NodeTile start, NodeTile target) {
		ArrayList<NodeTile> open = new ArrayList<NodeTile>();
		ArrayList<NodeTile> closed = new ArrayList<NodeTile>();
		
		open.add(start);
		NodeTile currentNode = open.get(0);

		for(int i = 0; i < 10; i++) {
			for(NodeTile n: graph.getNeighbours(currentNode)) {
				if(n.isWall())  {
					continue;
				}
				
				int newCostToNeighbour  = currentNode.getG_cost() +
						getDistance(currentNode, n);
				if(newCostToNeighbour > n.getG_cost() || !closed.contains(n)) {

					n.setG_cost(newCostToNeighbour) ;
					n.setH_cost(getDistance(n, target));
					n.setParent(currentNode);
					
					if(!open.contains(n)) {
						open.add(n);
					}
				}
				
				for(int j = 1; j < open.size(); j++) {
					NodeTile q = open.get(j);

				if(q.f_cost() > currentNode.f_cost() ||
						q.f_cost() == currentNode.f_cost() &&
						q.getH_cost() > currentNode.getH_cost()) {
						currentNode = q;
					}
				}

				
			}
			closed.add(currentNode);
		}
		
		return closed; 
		
	}

	private ArrayList<NodeTile> findPath(NodeTile start, NodeTile target){
	
		ArrayList<NodeTile> open = new ArrayList<NodeTile>();
		ArrayList<NodeTile> closed = new ArrayList<NodeTile>();
		
		open.add(start);
		
		while(!open.isEmpty()) {
			
			NodeTile currentNode = open.get(0);
			
			for(int i = 1; i < open.size(); i++) {
				NodeTile q = open.get(i);
				if(q.f_cost() < currentNode.f_cost() ||
						q.f_cost() == currentNode.f_cost() &&
						q.getH_cost() < currentNode.getH_cost()) {
					currentNode = q;
				}
			}

			open.remove(currentNode);
			closed.add(currentNode);
			
			if(currentNode.equals(target)) {
				return retracePath(start, target);
			}
			
			for(NodeTile n: graph.getNeighbours(currentNode)) {
				if(n.isWall() || closed.contains(n)) {
					continue;
				}
				
				int newCostToNeighbour  = currentNode.getG_cost() +
						getDistance(currentNode, n);
				if(newCostToNeighbour  < n.getG_cost() || !open.contains(n)) {
					n.setG_cost(newCostToNeighbour) ;
					n.setH_cost(getDistance(n, target));
					n.setParent(currentNode);
					
					if(!open.contains(n))
						open.add(n);
				}
			}
		}
		
		System.out.println("Path not found	");
		return null;	
		
	}
	private ArrayList<NodeTile> retracePath(NodeTile start, NodeTile end){
		
		ArrayList<NodeTile> path= new ArrayList<NodeTile>();
		
		NodeTile currentNode = end;
		
		while(!currentNode.equals(start)) {
			path.add(currentNode);
			currentNode = currentNode.getParent();
		}
		
		for(int i = 0; i < path.size()/2; i++) {
			
			NodeTile n = path.get(i);
			path.set(i, path.get(path.size() - i - 1));
			path.set(path.size() - i - 1, n);
		}
		return path;
	}

	private int getDistance(NodeTile a, NodeTile b) {
		
		int distX = Math.abs(a.getCol() - b.getCol());
		int distY = Math.abs(a.getRow() - b.getRow());
		
		if(distX > distY)
			return 14 * distY + 10 * (distX - distY);
		
		return 14 * distX + 10 * (distY - distX);
	}
	
	/*
	private ArrayList<Node> getNeighbours(Node n, Graph g){
		ArrayList<Node> neighbours = new ArrayList<Node>();
		
		// Was causing errors when attempting to add nodes outside of graph.
		if(n.getRow() > 1)
			neighbours.add(g.get(n.getRow() - 1, n.getCol()));
		if(n.getRow() < g.size()-2)
			neighbours.add(g.get(n.getRow() + 1, n.getCol()));
		if(n.getCol() > 1)
			neighbours.add(g.get(n.getRow(), n.getCol() - 1));
		if(n.getCol() < g.size()-2)
			neighbours.add(g.get(n.getRow(), n.getCol() + 1));
		
		return neighbours;
	}
	*/
}