package ie.gmit.sw.ai.pathfinder;

public class Graph {
	
	private Node graph[][];
	
	public Graph(char[][] model) {
		setGraph(model);
	}

	public Node[][] getGraph() {
		return graph;
	}
	
	public int size() {
		return graph.length;
	}
	
	public Node get(int r, int c) {
		return graph[r][c];
	}

	public int[] get(char search) {
		for(int r = 0; r < graph.length; r++)
			for(int c = 0; c < graph[0].length; c++) {
				if(this.get(r, c).getTile() == search){
					return new int[]{r, c};
				}
			}
		return null;
	}

	public void setGraph(char[][] model) {
		graph = new Node[model.length][model.length];
		for(int r = 0; r < model.length; r++)
			for(int c = 0; c < model[0].length; c++) {
				char tp = model[r][c];
				boolean wall = tp == '\u0030' ? true: false;
				this.graph[r][c] = new Node(wall, r, c, tp);
			}
	}	
	
}
// This should stored as a graph (tree)
// Unsure how to link up adjacent nodes atm