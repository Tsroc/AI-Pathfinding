package ie.gmit.sw.ai.pathfinder;

import java.util.List;

import ie.gmit.sw.ai.GameModel;
import ie.gmit.sw.ai.node.NodeTile;

public class DepthLimitedSearch {
	GameModel model = GameModel.getInstance();
	private int x, y;

	public boolean search(NodeTile node, int limit, int depth){
		boolean found = false;
		if (depth > limit){
			return false;
		}
		if ((node.getTile() == '5')||(node.getTile() == '1')){
			x = node.getRow();
			y = node.getCol();
			return true;
		}else{
			if (!node.isVisited()){
				node.setVisited(true);
				List<NodeTile> neighbours = model.getNeighbours(node);
				for (int i = 0; i < neighbours.size(); i++) {
					if(!neighbours.get(i).isWall()) {
						if (search(neighbours.get(i), limit, depth++) == true)
								found = true;
					}
				}
			}
		}
		node.setVisited(false);
		if(found)
			return true;
		else
			return false;
	}

	public int getRow() {
		return x;
	}
	
	public int getCol() {
		return y;
	}
	
}
