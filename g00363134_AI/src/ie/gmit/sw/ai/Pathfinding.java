package ie.gmit.sw.ai;

import java.util.ArrayList;

public class Pathfinding implements Command{
	char enemyID;
	GameModel model;
	Personality personality;
	int row;
	int col;
	
	private Node[][] maze;
	private ArrayList<Node> path;
	private boolean moving;
	private int player_row, player_col;
	private boolean playerFound = false;

	public Pathfinding(char enemyID, GameModel model, Personality personality, int row, int col) {
		// Need the map and the player
		this.enemyID = enemyID;
		this.model = model;
		this.row = row;
		this.col = col;
		
		createMaze();
	}
	
	
	public void createMaze() {
		this.maze = new Node[model.size()][model.size()];
		for(int r = 0; r < maze.length; r++)
			for(int c = 0; c < maze[0].length; c++) {
				char tp = model.get(r, c);
				boolean wall = tp == '\u0030' ? true: false;
				this.maze[r][c] = new Node(wall, r, c, tp);
				
				if(tp == '1') {
					System.out.println("Player Found!");
					playerFound = true;
					player_row = r;
					player_col = c;
				}
			}
	}
	
	private void searchForPlayer() {
		for(int r = 0; r < maze.length; r++)
			for(int c = 0; c < maze[0].length; c++) {
				char tp = model.get(r, c);
				if(tp == '1') {
					System.out.println("Player Found!");
					playerFound = true;
					player_row = r;
					player_col = c;
				}
			}
		
	}
	
	public void printMaze() {
		for(int r = 0; r < this.maze.length; r++) {
			for(int c = 0; c < this.maze[0].length; c++) {
				System.out.print(this.maze[r][c].tile);
			}
			System.out.println();
		}
	}
	
	@Override
	public void execute() {
		int temp_row = this.row, temp_col = this.col;
		
		if(moving && !path.isEmpty()) {
			model.set(path.get(0).row, path.get(0).col, enemyID);
			model.set(temp_row, temp_col, '\u0020');
			this.col = path.get(0).col;
			this.row = path.get(0).row;
			path.remove(0);
			
			if(path.isEmpty()) {
				moving = false;
				playerFound = false;
				player_row = 0;
				player_col = 0;
			}
				
		}
		if(!playerFound) {
			searchForPlayer();
		}
		
		requestPath(maze[player_row][player_col]);
		
	}
	
	
	
	
	
	
	private void requestPath(Node endNode) {
		path = findPath(maze[this.row][this.col], endNode);
		if(path.isEmpty())
			return;
		moving = true;
	}
	
	private ArrayList<Node> findPath(Node start, Node target){
		ArrayList<Node> open = new ArrayList<Node>();
		ArrayList<Node> closed = new ArrayList<Node>();
		
		open.add(start);
		
		while(!open.isEmpty()) {
			
			Node currentNode = open.get(0);
			
			for(int i = 1; i < open.size(); i++) {
				Node q = open.get(i);
				if(q.f_cost() < currentNode.f_cost() ||
						q.f_cost() == currentNode.f_cost() &&
						q.h_cost < currentNode.h_cost) {
					currentNode = q;
				}
			}
			
			open.remove(currentNode);
			closed.add(currentNode);
			
			if(currentNode.equals(target)) {
				return retracePath(start, target);
			}
			
			
			for(Node n: getNeighbours(currentNode)) {
				if(n.wall || closed.contains(n)) {
					continue;
				}
				
				int newCostToNeighbour  = currentNode.g_cost +
						getDistance(currentNode, n);
				if(newCostToNeighbour  < n.g_cost || !open.contains(n)) {
					n.g_cost = newCostToNeighbour ;
					n.h_cost = getDistance(n, target);
					n.parent = currentNode;
					
					if(!open.contains(n))
						open.add(n);
				}
				System.out.println(open.size());
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
			currentNode = currentNode.parent;
		}
		
		for(int i = 0; i < path.size()/2; i++) {
			
			Node n = path.get(i);
			path.set(i, path.get(path.size() - i - 1));
			path.set(path.size() - i - 1, n);
		}
		return path;
	}

	private int getDistance(Node a, Node b) {
		
		int distX = Math.abs(a.col - b.col);
		int distY = Math.abs(a.row - b.row);
		
		if(distX > distY)
			return 14 * distY + 10 * (distX - distY);
		
		return 14 * distX + 10 * (distY - distX);
	}
	
	private ArrayList<Node> getNeighbours(Node n){
		ArrayList<Node> neighbours = new ArrayList<Node>();
		
		neighbours.add(maze[n.row - 1][n.col]);
		neighbours.add(maze[n.row + 1][n.col]);
		neighbours.add(maze[n.row][n.col - 1]);
		neighbours.add(maze[n.row][n.col + 1]);
		
		return neighbours;
	}
	
}
