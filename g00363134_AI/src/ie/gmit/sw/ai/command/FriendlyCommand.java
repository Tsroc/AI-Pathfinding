
package ie.gmit.sw.ai.command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import ie.gmit.sw.ai.GameModel;
import ie.gmit.sw.ai.node.Node;
import ie.gmit.sw.ai.node.NodePos;
import ie.gmit.sw.ai.node.NodeTile;

public class FriendlyCommand implements Command{
	private static ThreadLocalRandom rand = ThreadLocalRandom.current();
	GameModel model = GameModel.getInstance();
	char enemyID;
	private Node pos = new NodePos();
	private Node tempPos = new NodePos();
	private List<NodeTile> neighbours; 
	private List<Character> activePredator = new ArrayList<Character>();

	public FriendlyCommand(char enemyID, int row, int col) {
		this.enemyID = enemyID;
		pos.setPos(row, col);
		activePredator.add('5');

		// Set the tile with the enemyyID
		model.get(row, col).setTile(enemyID);
	}
	
	public int execute(boolean alive) {
		if(alive) {
			// Friendly npc's are destroyed by aggressive npc's.
			// If it is a neighbour, it has attacked.
			neighbours = model.getNeighbours(model.get(pos.getRow(), pos.getCol()));
			for (int i = 0; i < neighbours.size(); i++) {
				if(activePredator.contains(neighbours.get(i).getTile())) {
					return triggerDeath();
				}
			}

			   synchronized (model) {
				tempPos.setPos(pos.getRow(), pos.getCol());
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
		}else {
			return triggerDeath();
		}
		return 1;
       	
	}

	private int triggerDeath() {
		model.set(pos.getRow(), pos.getCol(), '\u0020');
		return 0;
	}
	
}
