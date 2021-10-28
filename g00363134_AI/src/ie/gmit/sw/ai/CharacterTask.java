package ie.gmit.sw.ai;


import java.util.concurrent.ThreadLocalRandom;

import ie.gmit.sw.ai.command.Command;
import ie.gmit.sw.ai.command.CommandFactory;
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
	private static ThreadLocalRandom rand = ThreadLocalRandom.current();
	private static final int SLEEP_TIME = 300; 
	private PersonalityFactory pf = PersonalityFactory.getInstance();
	private CommandFactory cf = CommandFactory.getInstance();
	private Personality personality;
	private Command cmd;
	private int alive = 1;
	private char enemyID;
	private double health;
	private double healthDecay = 0.5;
	
	/*
	 * Configure each character with its own action. Use this functional interface
	 * as a hook or template to connect to your fuzzy logic and neural network. The
	 * method execute() of Command will execute when the Character cannot move to
	 * a random adjacent cell.
	 */
	
	public CharacterTask(int FLTrigger, int row, int col, int hunger, int fear, int health) {
		this.health = health;
		
		if(FLTrigger == 1)
			this.personality = pf.getPersonality(PersonalityCreatorType.FL, hunger, fear, health);//health, fear, hunger);
		else
			this.personality = pf.getPersonality(PersonalityCreatorType.EG, hunger, fear, health);//health, fear, hunger);
		
		personality.determinePersonality();
		
		// Decide on the enemyID - based on personality
		switch(personality.getPersonality()) {
			case AGGRESSIVE:
				this.enemyID = '\u0035';
				break;
			case SCARED:
				if(rand.nextBoolean())
					this.enemyID = '\u0034';
				else
					this.enemyID = '\u0036';
				break;
			case FRIENDLY:
				if(rand.nextBoolean())
					this.enemyID = '\u0032';
				else
					this.enemyID = '\u0033';
				break;
		}
		
		this.cmd = cf.getCommand(personality.getPersonality(), enemyID, row, col);//health, fear, hunger);
		System.out.printf("{Hunger: %d, Fear: %d, Health: %d}\n\t%s NPC created!\n", hunger, fear, health, personality.getPersonality() );
	}
	
	public int getSleepTime() {
		return SLEEP_TIME;
	}
	
	public int getAlive() {
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
    	
    	while (alive == 1) {
			
        	Thread.sleep(SLEEP_TIME);
        	try {
				alive = cmd.execute(true);
        	}catch(Exception e) {
        		//System.out.println(e.toString());
        	}

    		health -= healthDecay;
			if (health <= 0){
				alive = 0;
				cmd.execute(false);
				break;
			}
        	
    	}
    	System.out.println("Died!");
		return null;
    }
    
}