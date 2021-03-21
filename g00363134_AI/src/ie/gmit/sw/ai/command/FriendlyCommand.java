
package ie.gmit.sw.ai.command;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import ie.gmit.sw.ai.GameModel;
import ie.gmit.sw.ai.pathfinder.Graph;
import ie.gmit.sw.ai.pathfinder.Node;
import ie.gmit.sw.ai.pathfinder.PathFinder;
import ie.gmit.sw.ai.personality.Personality;

public class FriendlyCommand implements Command{
	char enemyID;
	GameModel model;
	Personality personality;
	int row;
	int col;
	private int player_row, player_col;
	private static ThreadLocalRandom rand = ThreadLocalRandom.current();
	
	Graph graph;
	PathFinder pf;
	ArrayList<Node> path;
	boolean playerFound = false;

	public FriendlyCommand(char enemyID, GameModel model, int row, int col) {
		// Need the map and the player
		this.enemyID = enemyID;
		this.model = model;
		this.row = row;
		this.col = col;
		
		graph = new Graph(model.getModel());
		pf = new PathFinder();
		
	}
	
	public void printMaze() {
		for(int r = 0; r < this.model.size(); r++) {
			for(int c = 0; c < this.model.size(); c++) {
				System.out.print(this.model.get(r, c));
			}
			System.out.println();
		}
	}

	private void searchForPlayer() {
		for(int r = 0; r < model.size(); r++)
			for(int c = 0; c < model.size(); c++) {
				char tp = model.get(r, c);
				if(tp == '1') {
					player_row = r;
					player_col = c;
				}
			}
	}
	
	public void execute() {
		int temp_row = this.row, temp_col = this.col;
		
       	synchronized (model) {
    		//Randomly pick a direction up, down, left or right
    		if (rand.nextBoolean()) {
        		temp_row += rand.nextBoolean() ? 1 : -1;
        	}else {
        		temp_col += rand.nextBoolean() ? 1 : -1;
        	}
        	
        	if (model.isValidMove(row, col, temp_row, temp_col, enemyID)) {
        		/*
        		 * This fires if the character can move to a cell, i.e. if it is not
        		 * already occupied. You can add extra logic here to invoke
        		 * behaviour when the computer controlled character is in the proximity
        		 * of the player or another character...
        		 */
        		model.set(temp_row, temp_col, enemyID);
        		model.set(row, col, '\u0020');
        		row = temp_row;
        		col = temp_col;
        	}
       	}
	}
	
}
