package ie.gmit.sw.ai.command;

import java.util.ArrayList;
import java.util.List;

import ie.gmit.sw.ai.GameModel;
import ie.gmit.sw.ai.node.NodeTile;
import ie.gmit.sw.ai.node.Node;
import ie.gmit.sw.ai.node.NodePos;
import ie.gmit.sw.ai.pathfinder.PathFinder;

public class AggressiveCommand implements Command{
	private GameModel model = GameModel.getInstance();
	private char enemyID;
	private Node pos = new NodePos();
	private Node tempPos = new NodePos();
	private Node prey = new NodePos();
	private PathFinder pf;
	private List<NodeTile> path;
	private List<Character> activePrey = new ArrayList<Character>();
	private boolean preyFound;
	private final int MAX_SEARCH = 20;
	private int currentSearch = 0;

	public AggressiveCommand(char enemyID, int row, int col) {
		this.enemyID = enemyID;
		pos.setPos(row, col);
		preyFound = false;
		
		pf = new PathFinder();
		// Add prey...
		activePrey.add('1'); 
		activePrey.add('2'); 
		activePrey.add('3'); 
		activePrey.add('4'); 
		activePrey.add('6'); 
		
		// Set the tile with the enemyyID
		model.get(row, col).setTile(enemyID);
	}
	
	// Need a different search pattern for this, this is slow
	// IterativeDeepeining has not been working, recursive methods causing issues.
	private void searchForPrey() {
		boolean found = false;
		NodeTile node;
		currentSearch++;
		
		System.out.println("Searching for pray...");
		for(int r = 0; r < model.size(); r++)
			for(int c = 0; c < model.size(); c++) {
				node = model.get(r, c);
				if(!found && activePrey.contains(node.getTile())) {
					found = true;
					prey.setPos(r, c);
					currentSearch = 0;
				}
			}
	}
	
	public int execute(boolean alive) {
		if(alive) {
			if(!preyFound) {
				searchForPrey();
				path = pf.requestPath(model.get(pos.getRow(), pos.getCol()), model.get(prey.getRow(), prey.getCol()));
				
				try {
					if(!path.isEmpty()) {
						preyFound = true;
						System.out.println("\tStalking prey.");
					}
				}catch(NullPointerException e) {
					//System.out.println(e.toString());
				}
			}
			if(preyFound) {
				stalkPrey();
			}
		} else {
			return triggerDeath();
		}

		if(currentSearch == MAX_SEARCH) {
			System.out.println("Reached MAX_SEARCH, killing this npc.");
			return triggerDeath();
		}
		
		return 1;
	}
	
	private void stalkPrey() {
		tempPos.setPos(pos.getRow(), pos.getCol());
		
		try {
			// This was causing a lot of issues.
			if(path.isEmpty()) {
				preyFound = false;
				path = null;
			}
		}catch(Exception e) {
			preyFound = false;
			//System.out.println(e.toString());
		}
		
		synchronized (model) {
			// Already determined that this will be a value move.
			model.set(path.get(0).getRow(), path.get(0).getCol(), enemyID);
			model.set(tempPos.getRow(), tempPos.getCol(), '\u0020');
			pos.setPos(path.get(0).getRow(), path.get(0).getCol());
			path.get(0).resetNode();
			path.remove(0);
		}
	}

	private int triggerDeath() {
		model.set(pos.getRow(), pos.getCol(), '\u0020');
		return 0;
	}

}
