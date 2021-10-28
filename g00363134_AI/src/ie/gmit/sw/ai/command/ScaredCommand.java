package ie.gmit.sw.ai.command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import ie.gmit.sw.ai.GameModel;
import ie.gmit.sw.ai.node.Node;
import ie.gmit.sw.ai.node.NodePos;
import ie.gmit.sw.ai.node.NodeTile;
import ie.gmit.sw.ai.pathfinder.DepthLimitedSearch;
import ie.gmit.sw.ai.pathfinder.PathFinder;

public class ScaredCommand implements Command{
	private static ThreadLocalRandom rand = ThreadLocalRandom.current();
	GameModel model = GameModel.getInstance();
	PathFinder pf;
	DepthLimitedSearch dls;
	char enemyID;
	private Node pos = new NodePos();
	private Node tempPos = new NodePos();
	private Node predator = new NodePos();
	private List<NodeTile> path;
	private List<NodeTile> neighbours; 
	private List<Character> activePredator = new ArrayList<Character>();
	boolean predatorFound = false;

	public ScaredCommand(char enemyID, int row, int col) {
		this.enemyID = enemyID;
		pos.setPos(row, col);
		activePredator.add('5');
		pf = new PathFinder();
		dls = new DepthLimitedSearch();

		// Set the tile with the enemyyID
		model.get(row, col).setTile(enemyID);
	}
	
	// Uses a depth limited search
	private void searchForPlayer() {
		predatorFound = dls.search(model.get(pos.getRow(), pos.getCol()), 4, 0);
		if(predatorFound) {
			predator.setPos(dls.getRow(), dls.getCol());
			//System.out.println("Found" + predator.getRow() + " " + predator.getCol());
		}
	}
	
	public int execute(boolean alive) {
		if(alive) {
			tempPos.setPos(pos.getRow(), pos.getCol());
			
			// Checks if its been attacked.
			neighbours = model.getNeighbours(model.get(pos.getRow(), pos.getCol()));
			for (int i = 0; i < neighbours.size(); i++) {
				if(activePredator.contains(neighbours.get(i).getTile())) {
					return triggerDeath();
				}
				
			}
			
			if(!predatorFound) {
				searchForPlayer();
				if(predatorFound) {
					path = pf.requestEscape(model.get(pos.getRow(), pos.getCol()), model.get(predator.getRow(), predator.getCol()));
				}
				else {
				   synchronized (model) {
						//Randomly pick a direction up, down, left or right
						if (rand.nextBoolean()) {
							tempPos.setRow(tempPos.getRow()  + (rand.nextBoolean() ? 1 : -1));
						}else {
							tempPos.setCol(tempPos.getCol()  + (rand.nextBoolean() ? 1 : -1));
						}
						
						// Randomly wanders.
						if (model.isValidMove(pos.getRow(), pos.getCol(), tempPos.getRow(), tempPos.getCol(), enemyID)) {
							//model.get(tempPos.getRow(), tempPos.getCol()).setTile(this.enemyID);
							//model.get(pos.getRow(), pos.getCol()).setTile('\u0020');
							pos.setPos(tempPos.getRow(), tempPos.getCol());
						}
					}	
				}
			}
			
			if(predatorFound) {
				flee();
			}
		}else {
			return triggerDeath();
		}
		return 1;
	}
	
	private void flee() {
		synchronized (model) {
			// Already determined that this will be a value move.
			model.set(pos.getRow(), pos.getCol(), '\u0020');
			model.set(path.get(0).getRow(), path.get(0).getCol(), enemyID);
			pos.setPos(path.get(0).getRow(), path.get(0).getCol());
			path.get(0).resetNode();
			path.remove(0);
		}
		
		// exiting loop before path ends to avoid NullPointerException.
		if(path.size() == 1) {
			predatorFound = false;
			path = null;
		}
	}

	private int triggerDeath() {
		model.set(pos.getRow(), pos.getCol(), '\u0020');
		return 0;
	}
	
}
