package ie.gmit.sw.ai.node;

public class NodeTile implements Node{
	private boolean wall;
	private int row, col;
	private int g_cost, h_cost;
	private char tile;
	private NodeTile parent;
	private boolean visited = false;
	
	public NodeTile(boolean wall, int row, int col, char tile) {
		this.wall = wall;
		this.row = row;
		this.col = col;
		this.tile = tile;
		
		g_cost = 0;
		h_cost = 0;
		parent = null;
	}

	public void setPos(int row, int col) {
		this.setRow(row);
		this.setCol(col);
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


	public NodeTile getParent() {
		return parent;
	}


	public void setParent(NodeTile parent) {
		this.parent = parent;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
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
