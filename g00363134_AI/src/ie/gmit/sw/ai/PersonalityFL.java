package ie.gmit.sw.ai;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class PersonalityFL implements Personality{
	
	private static final String FILE = "./resources/fuzzy/enemy.fcl";
	private int hunger;
	private int fear;
	private int health;
	private int personality;

	public PersonalityFL(int hunger, int fear, int health) {
		this.hunger = hunger;
		this.fear = fear;
		this.health = health;
		
	}
	
	public int determinePersonality() {
		FIS fis = FIS.load(FILE, true); //Load and parse the FCL
		
		FunctionBlock fb = fis.getFunctionBlock("EnemyLogic");
		
		
		//JFuzzyChart.get().chart(fb); //Display the linguistic variables and terms
		fis.setVariable("hunger", this.hunger); //Apply a value to a variable
		fis.setVariable("fear", this.fear); //Apply a value to a variable
		fis.setVariable("health", this.health); //Apply a value to a variable
		fis.evaluate(); //Execute the fuzzy inference engine
		
		Variable action = fb.getVariable("personality");
		//JFuzzyChart.get().chart(action, action.getDefuzzifier(), true);
		
		this.personality = (int)action.getValue();
		return this.personality;
	}
	
	public String toString() {
		System.out.print(this.personality + ", ");
		if(this.personality > 55)
			return "Aggressive";
		else if (this.personality > 35)
			return "Friendly";
		else if (this.personality >= 0)
			return "Scared";
		else
			return "Unknown";
	}
	
}

