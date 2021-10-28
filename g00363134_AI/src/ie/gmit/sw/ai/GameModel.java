package ie.gmit.sw.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import ie.gmit.sw.ai.node.NodeTile;
import javafx.concurrent.Task;

/*
 * [READ THIS CAREFULLY]
 * You will need to change the method addGameCharacter() below and configure each 
 * instance of CharacterTask with a Command object. The implementation below uses
 * a lambda expression ()-> System.out.println("Action executing!") as the default
 * logic for the execute() method.
 * 
 * [WARNING] Don't mess with anything else in this class unless you know exactly 
 * what you're at... If you break it, you own it.
 */
public class GameModel {
	private static final int MAX_CHARACTERS = 10;
	private ThreadLocalRandom rand = ThreadLocalRandom.current();
	// Size has been hard coded. This is not ideal but I wanted to make this class a singleton
	private NodeTile[][] model = new NodeTile[60][60];
	
	private final ExecutorService exec = Executors.newFixedThreadPool(MAX_CHARACTERS, e -> {
        Thread t = new Thread(e);
        t.setDaemon(true);
        return t ;
    });

	private static GameModel gm = new GameModel();

	private GameModel() {
		init();
		carve();
	}

	public static GameModel getInstance() {
		return gm;
	}
	
	public void tearDown() {
		exec.shutdownNow();
	}
	
	/*
	 * Initialises the game model by creating an n x m array filled with hedge  
	 */
	private void init(){
		for (int row = 0; row < model.length; row++){
			for (int col = 0; col < model[row].length; col++){
				model[row][col] = new NodeTile(true, row, col, '\u0030'); //\u0030 = 0x30 = 0 (base 10) = A hedge
			}
		}
	}
	
	/*
	 * Carve paths through the hedge to create passages.
	 */
	private void carve(){
		for (int row = 0; row < model.length; row++){
			for (int col = 0; col < model[row].length - 1; col++){
				if (row == 0) {
					this.get(row, col + 1).setTile('\u0020'); 
					this.get(row, col + 1).setWall(false); 
				}else if (col == model.length - 1) {
					this.get(row - 1, col).setTile('\u0020'); 
					this.get(row - 1, col).setWall(false); 
				}else if (rand.nextBoolean()) {
					this.get(row, col + 1).setTile('\u0020'); 
					this.get(row, col + 1).setWall(false); 
				}else {
					this.get(row - 1, col).setTile('\u0020'); 
					this.get(row - 1, col).setWall(false); 
				}
			}
		}
	}
	
	//private void addGameCharacters() {
	public void addGameCharacters() {
		Collection<Task<Void>> tasks = new ArrayList<>();
		
		// Will use NN to determine personality. 
		// Aggression is primarily determined by hunger, hunger will be kept above 60 for these.
		// Should create 2x Friendly NPCs
		addGameCharacter(tasks, 0, '0', MAX_CHARACTERS / 5, rand.nextInt(40) + 60, rand.nextInt(10), rand.nextInt(60) + 40); //2 is a Red Enemy, 0 is a hedge
		addGameCharacter(tasks, 0, '0', MAX_CHARACTERS / 5, rand.nextInt(40) + 60, rand.nextInt(10), rand.nextInt(60) + 40); //3 is a Pink Enemy, 0 is a hedge
		// Should create 2x Scared NPCs
		addGameCharacter(tasks, 0, '0', MAX_CHARACTERS / 5, rand.nextInt(40) + 60, rand.nextInt(10) + 90, rand.nextInt(30)+20); //4 is a Blue Enemy, 0 is a hedge
		addGameCharacter(tasks, 0, '0', MAX_CHARACTERS / 5, rand.nextInt(40) + 60, rand.nextInt(10) + 90, rand.nextInt(30)+20); //6 is a Orange Enemy, 0 is a hedge
		// Aggressive : '\u0035',
		// Using FuzzyLogic to ensure 2x Aggressive NPCs are created.
		addGameCharacter(tasks, 1, '0', 2, 15, 10, 90); //5 is a Red Green Enemy, 0 is a hedge

		/*
		for(int i = 0; i < 10; i++)
			addGameCharacter(tasks, 1, '0', 1, rand.nextInt(101), rand.nextInt(101), rand.nextInt(101)); //6 is a Orange Enemy, 0 is a hedge
			*/
		tasks.forEach(exec::execute);
	}
	
	private void addGameCharacter(Collection<Task<Void>> tasks, int FLTrigger, char replace, int number, int hunger, int fear, int health){
		int counter = 0;
		
		while (counter < number){
			int row = rand.nextInt(model.length-6)+3;
			int col = rand.nextInt(model[0].length-6)+3;
			
			if (model[row][col].getTile() == replace){

				/*
				 * IMPORTANT! Change the following to parameterise your CharacterTask with an instance of
				 * Command. The constructor call below is only parameterised with a lambda expression. 
				 */
				tasks.add(new CharacterTask(FLTrigger, row, col, hunger, fear, health)); 
				counter++;
			}
		}
	}
	
	// Changed this method to ensure that a valid move is considered within 3 tiles from the edge
	public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, char character){
		if ((toRow <= this.size() - 3 && toCol <= this.size() - 3 && 
				toRow > 3 && toCol > 3) && this.get(toRow, toCol).getTile() == ' '){
			this.set(fromRow, fromCol, '\u0020');
			this.set(toRow, toCol, character);
			return true;
		}else{
			return false; //Can't move
		}
	}
	
	public NodeTile get(int row, int col){
		return this.model[row][col];
	}
	
	public void set(int row, int col, char c){
		this.model[row][col].setTile(c);
	}
	
	public int size(){
		return this.model.length;
	}

	public List<NodeTile> getNeighbours(NodeTile n){
		List<NodeTile> neighbours = new ArrayList<NodeTile>();
		
		// Was causing errors when attempting to add nodes outside of graph.
		if(n.getRow() > 1)
			neighbours.add( model[n.getRow() - 1][n.getCol()] );
		if(n.getRow() < model.length-2)
			neighbours.add(model[n.getRow() + 1][n.getCol()]);
		if(n.getCol() > 1)
			neighbours.add(model[n.getRow()][n.getCol() - 1]);
		if(n.getCol() < model.length-2)
			neighbours.add(model[n.getRow()][n.getCol() + 1]);
		
		return neighbours;
	}
	
}