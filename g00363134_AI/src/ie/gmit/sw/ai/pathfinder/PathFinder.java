package ie.gmit.sw.ai.pathfinder;

import java.util.ArrayList;

public class PathFinder{
	
	private ArrayList<Node> path;
	
	public ArrayList<Node> requestPath(Node startNode, Node endNode, Graph graph) {
		path = findPath(startNode, endNode, graph);
		if(!path.isEmpty())
			return path;
		return null;
	}

	public ArrayList<Node> requestEscape(Node startNode, Node endNode, Graph graph) {
		path = findFurthest(startNode, endNode, graph);
		if(!path.isEmpty())
			return path;
		return null;
	}
	
	private ArrayList<Node> findFurthest(Node start, Node target, Graph graph) {
		ArrayList<Node> open = new ArrayList<Node>();
		ArrayList<Node> closed = new ArrayList<Node>();
		
		open.add(start);
		Node currentNode = open.get(0);

		for(Node n: getNeighbours(currentNode, graph)) {
			if(n.isWall())  {
				continue;
			}
			
			int newCostToNeighbour  = currentNode.getG_cost() +
					getDistance(currentNode, n);
			if(newCostToNeighbour > n.getG_cost() || !open.contains(n)) {
				n.setG_cost(newCostToNeighbour) ;
				n.setH_cost(getDistance(n, target));
				n.setParent(currentNode);
				
				if(!open.contains(n)) {
					open.add(n);
				}
			}
			
			for(int i = 1; i < open.size(); i++) {
				Node q = open.get(i);
				if(q.f_cost() > currentNode.f_cost()) {
					currentNode = q;
				}
			}

			
		}
		closed.add(currentNode);
		
		return closed; 
		
	}

	private ArrayList<Node> findPath(Node start, Node target, Graph graph){
	
		ArrayList<Node> open = new ArrayList<Node>();
		ArrayList<Node> closed = new ArrayList<Node>();
		
		open.add(start);
		
		while(!open.isEmpty()) {
			
			Node currentNode = open.get(0);
			
			for(int i = 1; i < open.size(); i++) {
				Node q = open.get(i);
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
			
			for(Node n: getNeighbours(currentNode, graph)) {
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
	private ArrayList<Node> retracePath(Node start, Node end){
		
		ArrayList<Node> path= new ArrayList<Node>();
		
		Node currentNode = end;
		
		while(!currentNode.equals(start)) {
			path.add(currentNode);
			currentNode = currentNode.getParent();
		}
		
		for(int i = 0; i < path.size()/2; i++) {
			
			Node n = path.get(i);
			path.set(i, path.get(path.size() - i - 1));
			path.set(path.size() - i - 1, n);
		}
		return path;
	}

	private int getDistance(Node a, Node b) {
		
		int distX = Math.abs(a.getCol() - b.getCol());
		int distY = Math.abs(a.getRow() - b.getRow());
		
		if(distX > distY)
			return 14 * distY + 10 * (distX - distY);
		
		return 14 * distX + 10 * (distY - distX);
	}
	
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
}