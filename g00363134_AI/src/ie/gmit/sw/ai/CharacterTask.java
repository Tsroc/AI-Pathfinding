package ie.gmit.sw.ai;

import java.util.concurrent.ThreadLocalRandom;

import ie.gmit.sw.ai.command.Command;
import ie.gmit.sw.ai.command.CommandFactory;
import ie.gmit.sw.ai.command.AggressiveCommand;
import ie.gmit.sw.ai.personality.Personality;
import ie.gmit.sw.ai.personality.PersonalityCreatorType;
import ie.gmit.sw.ai.personality.PersonalityFactory;
import javafx.concurrent.Task;

/*
 * CharacterTask represents a Runnable (Task is a JavaFX implementation
 * of Runnable) game character. The character wanders around the game
 * model randomly and can interact with other game characters using
 * implementations of the Command interface that it is composed with. 
 * 
 * You can change how this class behaves is a number of different ways:
 * 
 * 1) DON'T CHANGE THIS CLASS
 * 	  Configure the class constructor with an instance of Command. This can
 *    be a full implementation of a fuzzy controller or a neural network. You
 *    can also parameterise this class with a lambda expression for the 
 *    command object. 
 *    
 * 2) CHANGE THIS CLASS 
 * 	  You can extend this class and provide different implementations of the 
 * 	  call by overriding (not recommended). Alternatively, you can change the
 *    behaviour of the game character when it moves by altering the logic in
 *    the IF statement:  
 *    
 * 		if (model.isValidMove(row, col, temp_row, temp_col, enemyID)) {
 * 	    	//Implementation for moving to an occupied cell
 * 	    }else{
 *      	//Implementation for moving to an unoccupied cell
 *      } 
 */
 
public class CharacterTask extends Task<Void>{
	private static final int SLEEP_TIME = 600; //Sleep for 300 ms
	private static ThreadLocalRandom rand = ThreadLocalRandom.current();
	private boolean alive = true;
	private GameModel model;
	private char enemyID;
	private int row;
	private int col;
	

	/*
	 * Configure each character with its own action. Use this functional interface
	 * as a hook or template to connect to your fuzzy logic and neural network. The
	 * method execute() of Command will execute when the Character cannot move to
	 * a random adjacent cell.
	 */
	private PersonalityFactory pf = PersonalityFactory.getInstance();
	private CommandFactory cf = CommandFactory.getInstance();
	private Personality personality;
	private Command cmd;
	
	private int health;
	private int fear;
	private int hunger;
	
	
	public CharacterTask(GameModel model, char enemyID, int row, int col, int hunger, int fear, int health) {
		this.model = model;
		this.enemyID = enemyID;
		this.row = row;
		this.col = col;

		this.health = health;
		this.fear = fear;
		this.hunger = hunger;
		System.out.println("Hunger:"+hunger+", Fear:"+fear+", Health:"+health);
		
		this.personality = pf.getPersonality(PersonalityCreatorType.NN, hunger, fear, health);//health, fear, hunger);
		personality.determinePersonality();
		System.out.println(personality.getPersonality());

		this.cmd = cf.getCommand(personality.getPersonality(), this.enemyID, this.model, this.row, this.col);//health, fear, hunger);

		//System.out.println(personality.toString());
		
		//cmd = new Pathfinding(this.enemyID, this.model, personality, this.row, this.col);
		//pfc = new AggressiveCommand(this.enemyID, this.model, personality, this.row, this.col);
		//((Pathfinding)cmd).printMaze();
		
		
	}
	
	public int getSleepTime() {
		return SLEEP_TIME;
	}
	
	public boolean getAlive() {
		return alive;
	}
	
	
    @Override
    public Void call() throws Exception {
    	/*
    	 * This Task will remain alive until the call() method returns. This
    	 * cannot happen as long as the loop control variable "alive" is set
    	 * to true. You can set this value to false to "kill" the game 
    	 * character if necessary (or maybe unnecessary...).
    	 */
    	while (alive) {
        	Thread.sleep(SLEEP_TIME);
        	cmd.execute();

    	}
		return null;
    }
    
}