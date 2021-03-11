package ie.gmit.sw.ai;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class FuzzyStuff implements Command {
	
	private static final String FILE = "./resources/fuzzy/enemy.fcl";
	private int hunger;
	private int fear;
	private int health;

	public FuzzyStuff(int hunger, int fear, int health) {
		this.hunger = hunger;
		this.fear = fear;
		this.health = health;
		
	}
	
	public double getPersonality() {
		FIS fis = FIS.load(FILE, true); //Load and parse the FCL
		
		FunctionBlock fb = fis.getFunctionBlock("EnemyLogic");
		
		
		//JFuzzyChart.get().chart(fb); //Display the linguistic variables and terms
		fis.setVariable("hunger", this.hunger); //Apply a value to a variable
		fis.setVariable("fear", this.fear); //Apply a value to a variable
		fis.setVariable("health", this.health); //Apply a value to a variable
		fis.evaluate(); //Execute the fuzzy inference engine
		
		Variable action = fb.getVariable("personality");
		//JFuzzyChart.get().chart(action, action.getDefuzzifier(), true);
		
		return action.getValue();
	}
	
	public String toString() {
		if(this.getPersonality() > 55)
			return "Aggressive";
		else if (this.getPersonality() > 35)
			return "Friendly";
		else if (this.getPersonality() >= 0)
			return "Scared";
		else
			return "Unknown";
	}

				
	public void execute() {
		
	}
	
}

