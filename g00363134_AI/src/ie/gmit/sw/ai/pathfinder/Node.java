package ie.gmit.sw.ai.pathfinder;

public class Node {
	private boolean wall;
	private int row, col;
	private int g_cost, h_cost;
	private char tile;
	private Node parent;
	
	public Node(boolean wall, int row, int col, char tile) {
		this.wall = wall;
		this.row = row;
		this.col = col;
		this.tile = tile;
		
		g_cost = 0;
		h_cost = 0;
		parent = null;
	}
	
	// setters & getters
	public boolean isWall() {
		return wall;
	}

	public void setWall(boolean wall) {
		this.wall = wall;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	public int getG_cost() {
		return g_cost;
	}

	public void setG_cost(int g_cost) {
		this.g_cost = g_cost;
	}

	public int getH_cost() {
		return h_cost;
	}

	public void setH_cost(int h_cost) {
		this.h_cost = h_cost;
	}


	public char getTile() {
		return tile;
	}


	public void setTile(char tile) {
		this.tile = tile;
	}


	public Node getParent() {
		return parent;
	}


	public void setParent(Node parent) {
		this.parent = parent;
	}


	// methods
	public int f_cost() {
		return h_cost + g_cost;
	}
	
	public void resetNode() {
		g_cost = 0;
		h_cost = 0;
		parent = null;
	}
}
